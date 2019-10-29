package com.briup.environment.client;

public class ClientImplTest {

	public static void main(String[] args) {
		GatherImpl ga = new GatherImpl();
		ClientImpl ci = new ClientImpl();
		try {
			ci.send(ga.gather());
		} catch (Exception e) {
			e.getStackTrace();
		}
		
	
	}
}
