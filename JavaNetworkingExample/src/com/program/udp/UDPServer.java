package com.program.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
	private final static int SERVER_PORT = 9878;

	public static void main(String args[]) {
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(SERVER_PORT, InetAddress.getLocalHost());
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while (true) {

				DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivedPacket);
				String message = new String(receivedPacket.getData()).trim();
				System.out.println("Recived from relay server: " + message);

				InetAddress IPAddress = receivedPacket.getAddress();
				int port = receivedPacket.getPort();

				String convertedMessage = message.toUpperCase();
				sendData = convertedMessage.getBytes();
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
