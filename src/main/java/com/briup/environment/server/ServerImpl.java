package com.briup.environment.server;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.LogImpl;

public class ServerImpl implements Server {

	boolean flag = true;
	LogImpl log = new LogImpl();
	
	@Override
	public void init(Properties properties) throws Exception {

	}

	/**
	 * 该方法用于启动这个Server服务，开始接收客户端传递的信息，<br/>
	 * 并且调用DBStore将接收的数据持久化
	 * 
	 * @return 接受的Environment数据的集合
	 * @throws Exception
	 */

	@Override
	public Collection<Environment> reciver() throws Exception {
		// 启动Server服务，开始接收客户端传递的信息
		ServerSocket ss = new ServerSocket(8088);
		log.info("服务器已启动,处于监听状态...");
		Socket client = ss.accept();

		// 接收客户端发送过来的数据
		ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
		log.info("客户端输入流已就绪...");
		Collection<Environment> coll = (Collection<Environment>) ois.readObject();
		log.info("成功接收客户端数据...");
		

		// 调用DBStore将接收的数据持久化
		new DBStoreImpl().saveDb(coll);
		log.info("服务器调用入库模块将数据持久化储存...");
		
		ois.close();
		ss.close();
		log.info("服务器成功接收数据并存储...");
		
		return coll;
	}

	/**
	 * 该方法用于使Server安全的停止运行。
	 */
	@Override
	public void shutdown() {

		flag = false;
		
	}

}
