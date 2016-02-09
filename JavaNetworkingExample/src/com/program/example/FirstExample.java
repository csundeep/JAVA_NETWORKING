package com.program.example;

import java.net.InetAddress;

public class FirstExample {
	public static void main(String[] args)  {
		try {
			System.out.println(InetAddress.getByName("www.google.com"));
			InetAddress[] addresses=InetAddress.getAllByName("www.facebook.com");
			for (int i = 0; i < addresses.length; i++) {
				System.out.println(addresses[i]);
			}
			System.out.println(InetAddress.getLocalHost());
			System.out.println(InetAddress.getByName("208.201.239.100").getCanonicalHostName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
