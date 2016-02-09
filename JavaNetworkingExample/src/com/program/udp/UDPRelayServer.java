package com.program.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPRelayServer {
	private final static int SERVER_PORT = 9878;
	private final static int RELAY_PORT = 9866;

	public static void main(String args[]) {
		DatagramSocket serverSocket = null;
		try {
			serverSocket = new DatagramSocket(RELAY_PORT, InetAddress.getLocalHost());   
			byte[] receiveData = new byte[1024];
			while (true) {

				DatagramPacket receivePacket_Socket_01 = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket_Socket_01);

				String sentence_socket_01 = new String(receivePacket_Socket_01.getData()).trim();
				System.out.println("Recived from socket 01: " + sentence_socket_01);

				DatagramPacket sendPacket_Socket_02 = new DatagramPacket(sentence_socket_01.getBytes(),
						sentence_socket_01.getBytes().length, InetAddress.getLocalHost(), SERVER_PORT);
				serverSocket.send(sendPacket_Socket_02);

				DatagramPacket receivePacket_Socket_02 = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket_Socket_02);

				String sentence_socket_02 = new String(receivePacket_Socket_02.getData()).trim();
				System.out.println("Recived from socket 02: " + sentence_socket_02);

				DatagramPacket sendPacket_Socket_01 = new DatagramPacket(sentence_socket_02.getBytes(),
						sentence_socket_02.getBytes().length, InetAddress.getLocalHost(),
						receivePacket_Socket_01.getPort());
				serverSocket.send(sendPacket_Socket_01);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				serverSocket.close();
		}
	}
}
