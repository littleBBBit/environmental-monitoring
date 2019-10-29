package com.briup.environment.util;

import java.util.Properties;

/**
 * 备份类
 * @author Administrator
 */
public interface Backup extends WossModule{
	// 继承自WossModule接口的方法
	public void init(Properties properties);
	/**
	 * 备份数据
	 * @param fileName 备份文件
	 * @param data  备份数据
	 * @throws Exception 
	 */
	public void backup(String fileName,Object data) throws Exception;
	
	/**
	 * 加载备份
	 * @param fileName 备份文件
	 * @return  备份数据
	 * @throws Exception  
	 */
	public Object load(String fileName) throws Exception;
	
	/**
	 * 删除备份
	 * @param fileName
	 */
	public void deleteBackup(String fileName);
		
}
