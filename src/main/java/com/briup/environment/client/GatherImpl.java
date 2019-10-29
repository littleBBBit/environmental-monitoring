package com.briup.environment.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackupImpl;
import com.briup.environment.util.LogImpl;

//实现Gather接口
/**
 * 采集智能家居的环境信息，将数据封装为Environment集合返回。
 * 
 * @return 采集封装Environment数据的集合
 * @throws Exception
 */
public class GatherImpl implements Gather {

	LogImpl log = new LogImpl();

	@Override
	public void init(Properties properties) throws Exception {

	}

	// 返回采集封装Environment数据的集合
	@Override
	public Collection<Environment> gather() {

		// 创建返回容器
		Collection<Environment> coll = new ArrayList<>();
		// 创建备份对象
		BackupImpl bkl = new BackupImpl();
		String backup = "src/main/java/com/briup/environment/GatherBackup.txt";

		try {
			// 1.IO流读入radwtmp文件
			String path = "src/main/java/com/briup/environment/radwtmp";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			// 读备份文件
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(backup)));
			log.info("IO流就绪...");

			// 统计备份文件里已读取的字符数
			long backupNum = 0;
			String temp;
			while ((temp = br2.readLine()) != null) {
				if (temp.equals(""))
					continue;
				backupNum += Long.parseLong(temp);
			}
			log.info("读取备份文件数据...");

			// 跳过已读取的字符
			if (backupNum >= 0) {
				br.skip(backupNum);
			}
			log.info("开始采集数据...");

			// 创建Environment对象,并将读取到的数据存入容器中
			int size = 0; // 日志输出用
			String result;
			int thistime = 0; // 临时变量，统计本次读取字符数，存入备份文件中
			while ((result = br.readLine()) != null) {
				Environment env = new Environment();
				String[] results = result.split("\\|");

				env.setSrcId(results[0]);
				env.setDstId(results[1]);
				env.setDevId(results[2]);
				/*
				 * 16代表温度和湿度 256代表光照强度 1280代表二氧化碳强度
				 */
				env.setSersorAddress(results[3]);
				switch (results[3]) {
				case "16":
					// 设置数据时再具体修改
					break;
				case "256":
					env.setName("光照强度");
					break;
				case "1280":
					env.setName("二氧化碳强度");
					break;
				default:
					break;
				}

				env.setCount(Integer.parseInt(results[4]));
				env.setCmd(results[5]);
				env.setStatus(Integer.parseInt(results[7]));
				env.setGather_date(new Timestamp(Long.parseLong(results[8])));
				/*
				 * 温度和湿度是同时采集的，两者只有name和data是不同的 在此处需要新创建出一个对象
				 */
				// "16"在前面，就永远不会出现空指针异常
				if ("16".equals(env.getSersorAddress())) {
					Environment env2 = new Environment();// 湿度的对象
					env.setName("温度");
					env2.setName("湿度");
					// 前两个字节是温度，中间两个字节是湿度。
					float tem = (float) Integer.parseInt(results[6].substring(0, 4), 16);// 温度
					float hum = (float) Integer.parseInt(results[6].substring(4, 8), 16);// 湿度
					env.setData((float) ((tem * 0.00268127) - 46.85));
					env2.setData((float) ((tem * 0.00190735) - 6));

					// 将温度的其他部分属性copy给湿度
					env2.setSrcId(env.getSrcId());
					env2.setCmd(env.getCmd());
					env2.setCount(env.getCount());
					env2.setDevId(env.getDevId());
					env2.setSersorAddress(env.getSersorAddress());
					env2.setDstId(env.getDstId());
					env2.setStatus(env.getStatus());
					env2.setGather_date(env.getGather_date());

					coll.add(env2);
					++size;
				} else {
					// 光照强度和二氧化碳直接转换成10进制
					long dec = Long.parseLong(results[6].substring(0, 4), 16);
					env.setData((float) dec);

				}

				coll.add(env);
				++size;

				// 统计读取到的字符数
				thistime += (result.length() + 2);

			}
			log.info("本次成功采集" + size + "条数据," + "共" + thistime + "字符数.");

			// 写备份文件
			PrintWriter pw = new PrintWriter(new FileOutputStream(backup, true));
			// 将读取到的字符数写入备份文件
			pw.println(thistime);
			log.info("写入备份文件...");

			pw.flush();
			br.close();
			br2.close();
			pw.close();
			log.info("成功传输数据并关闭资源！");

		} catch (Exception e) {
			log.error("采集失败！异常信息："+e.getMessage());
		}
		return coll;
	}

}
