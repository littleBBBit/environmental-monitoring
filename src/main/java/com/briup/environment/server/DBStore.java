package com.briup.environment.server;

import java.util.Collection;
import com.briup.environment.bean.Environment;
import com.briup.environment.util.WossModule;
/**
 * Simple to Introduction
 * @ProjectName:  智能家居之环境监控系统
 * @Package: com.briup.environment.server
 * @InterfaceName:  DBStore
 * @Description:  DBStore提供了入库模块的规范。<br/>
 * 				 该接口的实现类将Environment集合持久化。
 * @CreateDate:   2018-1-25 14:28:30
 * @author briup
 * @Version: 1.0
 */
public interface DBStore extends WossModule{
	/**
	 * 将BIDR集合进行持久化 。
	 * @param coll 需要储存的Environment集合
	 * @throws Exception
	 */
	public void saveDb(Collection<Environment> coll)throws Exception;
}
