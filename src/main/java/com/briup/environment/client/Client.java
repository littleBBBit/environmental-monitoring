package com.briup.environment.client;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.WossModule;
/**
 * Simple to Introduction
 * @ProjectName:  智能家居之环境监控系统
 * @Package: com.briup.environment.client
 * @InterfaceName:  Client
 * @Description:  Client接口是采集系统网络模块客户端的规范.<br/>
 * 				Client的作用就是与服务器端进行通信,传递数据
 * @CreateDate:   2018-1-25 14:28:30
 * @author briup
 * @Version: 1.0
 */
public interface Client extends WossModule{
	/**
	 * send方法用于与服务器端进行交互，发送Environment集合至服务器端。
	 * @param coll
	 * @throws Exception
	 */
	public void send(Collection<Environment> coll)throws Exception;
}
