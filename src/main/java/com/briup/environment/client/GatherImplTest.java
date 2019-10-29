package com.briup.environment.client;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.briup.environment.bean.Environment;

public class GatherImplTest {

	public static void main(String[] args) {
		GatherImpl ga = new GatherImpl();

		String path = "src/main/java/com/briup/environment/test.txt";
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path)));
			ArrayList<Environment> ar = (ArrayList<Environment>) ga.gather();
			for (Environment e : ar)
				pw.println(e);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
