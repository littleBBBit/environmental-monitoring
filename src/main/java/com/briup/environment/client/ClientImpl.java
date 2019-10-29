package com.briup.environment.client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.BackupImpl;
import com.briup.environment.util.LogImpl;

public class ClientImpl implements Client {

	LogImpl log = new LogImpl();
	
	
	@Override
	public void init(Properties properties) throws Exception {

	}

	/**
	 * send方法用于与服务器端进行交互，发送Environment集合至服务器端。
	 * 
	 * @param coll
	 * @throws Exception
	 */
	@Override
	public void send(Collection<Environment> coll) {

		BackupImpl bk = new BackupImpl();
		String backup = "ClientBackup.txt";
		ObjectOutputStream oos = null;
		
		try {
			// 连接服务器并且获取客户端输出流
			Socket client = new Socket("127.0.0.1", 8088);
			oos = new ObjectOutputStream(client.getOutputStream());
			log.info("成功连接服务器...");

			// 每次传输之前，先检查备份文件是否有文件
			// 有的话把备份文件里的数据放到coll里一起传，没有的话，直接传
			Object obj = bk.load(backup);
			if (obj != null) {
				Collection<Environment> collb = (Collection<Environment>)obj;
				coll.addAll(collb);
				// 传完之后就删掉备份文件
				bk.deleteBackup(backup);
				System.out.println("----------");
				log.warn("读取备份文件,发现有未传输成功的残余数据"+collb.size()+"条.本次一同传输.");
			}else {
				log.warn("客户端备份文件无内容...");
			}

			oos.writeObject(coll);
			log.info("成功传输数据！");
			
			oos.flush();
			if(oos != null)
				oos.close();
			log.info("关闭资源.");
			
		} catch (Exception e) {
			log.error("传输失败！异常信息："+e.getStackTrace());
			// 网络模块客户端部分在发送数据的时候出现异常，将数据进行备份
			try {
				bk.backup(backup, coll);
				log.info("未成功传输数据已备份！");
			} catch (Exception e1) {
				log.error("备份失败！异常信息："+e1.getMessage());
			}
		}

	}

}
