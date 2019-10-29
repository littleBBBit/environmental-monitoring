package com.briup.environment.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

public class BackupImpl implements Backup {

	@Override
	public void init(Properties properties) {
		
	}

	/**
	 * 备份数据
	 * 
	 * @param fileName
	 *            备份文件
	 * @param data
	 *            备份数据
	 * @throws Exception
	 */
	// 声明备份目录路径
	String backup = "src/backup";

	@Override
	public void backup(String fileName, Object data) throws Exception {

		// 创建备份文件
		File file = new File(backup, fileName);
		// 判断备份文件是否存在
		if (file.exists() == false) {
			file.createNewFile(); // 创建新文件
		}

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName,true));
		oos.writeObject(data);
		oos.writeObject(null);
		
		if (oos != null) {
			oos.close();
		}
	}

	/**
	 * 加载备份
	 * 
	 * @param fileName
	 *            备份文件
	 * @return 备份数据
	 * @throws Exception
	 */
	@Override
	public Object load(String fileName) {

		try {
			File file = new File(backup, fileName);
			if (file.exists() == false) {
				return null;
			} else {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file,true));
				oos.writeObject(null);
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				Object obj = ois.readObject();
				if (ois != null) {
					ois.close();
				}
				return obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除备份
	 * 
	 * @param fileName
	 */
	@Override
	public void deleteBackup(String fileName) {
		File file = new File(backup, fileName);
		if (!file.exists()) {
			System.out.println("文件不存在！");
		} else {
			file.delete();
		}

	}

}
