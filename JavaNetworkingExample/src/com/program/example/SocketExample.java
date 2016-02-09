package com.program.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketExample {
	public static void main(String[] args) throws IOException {
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket();
			SocketAddress address = new InetSocketAddress("time.nist.gov", 13);
			socket.connect(address);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() != 0)
					System.out.println(line.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
