package com.briup.environment.server;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.WossModule;
/**
 * Simple to Introduction
 * @ProjectName:  智能家居之环境监控系统
 * @Package: com.briup.environment.server
 * @InterfaceName:  Server
 * @Description:  Server接口提供了采集系统网络模块服务器端的标准。 Server的实现类<br/>
 * 		需要实现与采集发送客户端进行交互，并调用DBStore将接收的数据持久化。<br/>
 *     当Server的实现类执行revicer()方法时，Server开始监听端口。
 * @CreateDate:   2018-1-25 14:28:30
 * @author briup
 * @Version: 1.0
 */
public interface Server extends WossModule{
	/**
	 * 该方法用于启动这个Server服务，开始接收客户端传递的信息，<br/>
	 * 并且调用DBStore将接收的数据持久化
	 * @return 接受的Environment数据的集合
	 * @throws Exception
	 */
	public Collection<Environment> reciver()throws Exception;
	/**
	 * 该方法用于使Server安全的停止运行。
	 */
	public void shutdown();
}
