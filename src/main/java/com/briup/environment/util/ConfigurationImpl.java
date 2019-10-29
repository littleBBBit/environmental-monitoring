package com.briup.environment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;

public class ConfigurationImpl implements Configuration{
	
	//使用Map集合保存所有模块的对象
	Map<String, WossModule> map = new HashMap<>();
	
	
	
	public ConfigurationImpl() throws Exception {
		//dom4j解析过程，maven工程不需导包
		SAXReader reader = new SAXReader();
		Document doc = reader.read("src/main/java/com.briup/environment/util/config.xml");
		Element  root = doc.getRootElement();
		List<Element> elements = root.elements();
		for(Element element:elements) {
			//先读取class属性值，获取到每个模块的全限类名
			String className= element.attributeValue("class");
			//调用反射机制创建模块实例化对象
			WossModule woss = (WossModule) Class.forName(className).newInstance();
			
			//把创建好的对象保存到Map集合中
			map.put(element.getName(), woss);
			
			List<Element> childElements = element.elements();
			Properties pro = new Properties();
			for(Element child:childElements) {
				String key = child.getName();
				String value = child.getText();
				pro.setProperty(key, value);
			}
			woss.init(pro);
		
		}
		for(String key:map.keySet()) {
			WossModule woss = map.get(key);
			//调用创建好的模块对象的setConfiguration()方法
			//把自己传过去
			//先判断这个模块是否实现了ConfigurationAWare接口
			if(woss instanceof ConfigurationAWare)
				((ConfigurationAWare)woss).setConfiguration(this);
		}
		
		
	}
	
	

	@Override
	public Log getLogger() throws Exception {
		return (Log) map.get("logger");
	}

	@Override
	public Server getServer() throws Exception {
		return (Server) map.get("server");
	}

	@Override
	public Client getClient() throws Exception {
		return (Client) map.get("client");
	}

	@Override
	public DBStore getDbStore() throws Exception {
		return (DBStore) map.get("dbstore");
	}

	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get("gather");
	}

	@Override
	public Backup getBackup() throws Exception {
		return (Backup) map.get("backup");
	}

	
}
