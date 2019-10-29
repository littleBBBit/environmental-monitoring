package com.briup.environment.server;

public class ServerImplTest {

	public static void main(String[] args) {

		ServerImpl si = new ServerImpl();

		try {
			si.reciver();
		} catch (Exception e) {
			e.getStackTrace();
		}

		
		
	}
}
