package com.briup.environment.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackupImpl;
import com.briup.environment.util.LogImpl;

public class DBStoreImpl implements DBStore {

	LogImpl log = new LogImpl();
	
	@Override
	public void init(Properties properties) throws Exception {

	}

	/**
	 * 将BIDR集合进行持久化 。
	 * 
	 * @param coll
	 *            需要储存的Environment集合
	 * @throws Exception
	 */
	@Override
	public void saveDb(Collection<Environment> coll) {

		BackupImpl bkl = new BackupImpl();
		String backup = "DBStoreBackup.txt";
		int temp = 0;

		try {
			// 与数据库开启连接
			String JDBCpath = "src/main/java/com/briup/environment/server/jdbc.properties";
			Properties pro = new Properties();
			BufferedReader jdbcbr = new BufferedReader(new InputStreamReader(new FileInputStream(JDBCpath)));
			pro.load(jdbcbr);
			Class.forName(pro.getProperty("driverName"));
			Connection conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"),
					pro.getProperty("password"));
			log.info("数据库成功开启连接...");
			
			Statement st = conn.createStatement();
			// 手动提交事务
			conn.setAutoCommit(false);

			// 查询数据库中是否有这张表，无则创建表，有则直接插入数据
			String sql1 = "select * from user_objects where object_name = 'ENVIRONMENT'";
			if (st.executeQuery(sql1).next() == false) {
				String sql2 = "create table ENVIRONMENT(\r\n" + "		name varchar2(20),\r\n"
						+ "		srcId varchar2(5),\r\n" + "		dstId varchar2(5),\r\n" + "		devId varchar2(5),\r\n"
						+ "		sersorAddress varchar2(7),\r\n" + "		count number(2),\r\n"
						+ "		cmd varchar2(5),\r\n" + "		status number(2),\r\n" + "		data number(9,4),\r\n"
						+ "		gather_date date)";
				st.execute(sql2);
				log.info("数据库查无此表,且成功创建此表...");
			}

			// 每次传输之前，先检查备份文件是否有数据，有的话先传备份的部分，没有的话，直接传
			Object obj = bkl.load(backup);
			if (obj != null) {
				Collection<Environment> collb = (Collection<Environment>)obj;
				coll.addAll(collb);
				// 传完之后就删掉备份文件的内容
				bkl.deleteBackup(backup);
				System.out.println("--------------");
				log.warn("读取备份文件,发现有未传输成功的残余数据"+collb.size()+"条.本次一同传输.");
			}
			
			// 遍历集合，并将集合中的数据存入数据库表中
			String sql3 = "insert into ENVIRONMENT values(?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(sql3);

			int count = 0;
			for (Environment e : coll) {
				if (e != null) {
					Environment element = (Environment) e;

					pst.setString(1, element.getName());
					pst.setString(2, element.getSrcId());
					pst.setString(3, element.getDstId());
					pst.setString(4, element.getDevId());
					pst.setString(5, element.getSersorAddress());
					pst.setInt(6, element.getCount());
					pst.setString(7, element.getCmd());
					pst.setInt(8, element.getStatus());
					pst.setFloat(9, element.getData());
					pst.setDate(10, new Date(element.getGather_date().getTime()));

					pst.addBatch();
					++count;
					if (count % 500 == 0) {
						pst.executeBatch();
						conn.commit();
						++temp;
					}
				}
			}

			pst.executeBatch();
			conn.commit();
			log.info("数据成功入库存储！本次传输"+coll.size()+"条数据！");

			st.close();
			pst.close();
			conn.close();
			if(jdbcbr == null)
				jdbcbr.close();
			log.info("关闭资源.");
		} catch (Exception e1) {
			log.error("传输失败！异常信息："+e1.getMessage());
			// 服务器端入库模块在向数据库插入数据时出现异常，备份未插入的数据
			try {

				// 前面传输成功的数据不必要备份,计算一下之前存成功了多少数据
				int tergat = coll.size() - (temp * 500);
				log.error(tergat+"条数据未成功入库！"+(temp * 500)+"条数据已入库！");
				Collection<Environment> newcoll = new ArrayList<Environment>();
				Iterator e = coll.iterator();
				int i = 0;
				while (e.hasNext()) {
					e.next();
					++i;
					if (i > tergat)
						newcoll.add((Environment) e.next());
				}
				bkl.backup(backup, newcoll);
				log.error("未成功入库数据已备份！");

			} catch (Exception e) {
				log.error("备份失败！异常信息："+e.getMessage());
			}
		}
	}

}
