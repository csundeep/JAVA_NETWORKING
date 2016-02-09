package com.program.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	private final static int RELAY_PORT = 9866;

	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String sentence = br.readLine();
			DatagramPacket request = new DatagramPacket(sentence.getBytes(), sentence.length(),
					InetAddress.getLocalHost(), RELAY_PORT);
			DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

			socket.send(request);
			socket.receive(response);

			String result = new String(response.getData(), 0, response.getLength(), "US-ASCII");
			System.out.println("Result from relay server" + result);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			socket.close();
		}
	}
}
