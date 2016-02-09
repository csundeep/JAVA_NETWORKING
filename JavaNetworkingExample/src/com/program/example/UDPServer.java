package com.program.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
	private final static int SERVER_PORT = 9878;

	public static void main(String args[]) {
		DatagramSocket serverSocket = null;
		try {
			System.out.println( InetAddress.getLocalHost());
			serverSocket = new DatagramSocket(SERVER_PORT, InetAddress.getLocalHost());
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while (true) {

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String sentence = new String(receivePacket.getData()).trim();
				System.out.println("Recived from relay server: " + sentence);

				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();

				String capitalizedSentence = sentence.toUpperCase();
				sendData = capitalizedSentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				serverSocket.close();
		}

	}
}
