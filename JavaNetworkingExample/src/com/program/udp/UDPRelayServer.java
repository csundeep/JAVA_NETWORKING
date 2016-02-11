package com.program.udp;
//***** UDPRelayServer.java *****

//*******************************************************************
//
//Computer Science 4/5313 Computer Networks 
//
//Spring  2016
//
//Instructor:  Hung-Chi Su
//
//Assignment # n
//
//Programmer:  Chilukuri Sundeep
//
//Due Date:    4, 02 11, 2016
//
//Description: This is a UDP socket program that illustrates how a client
//     connect to UDP relay sever and relay server connect to UDP server (UDPServer.java) and exchanges
//     messages between client and server
//
//Editor/Platform:  vi/Linux
//
//
//
//Input:      none
//
//Output:     The server will display the message sent by this client
//
//Compile:    javac UDPRelayServer.java
//
//Command:    (After server is running)
//            java UDPRelayServer  [<port1#>  [<IP1>]] [<port2#>  [<IP2>]]
//
//
//Note:       Remember to Change SERVER_PORT_ID to be same as server's
//
//*******************************************************************

//Java libraries
//First three libraries are to implement message buffer for both in and out messages

import java.net.DatagramPacket;//To construct the datagram packets to carry messages
import java.net.DatagramSocket;//For Socket class to connect to UDP server
import java.net.InetAddress;

public class UDPRelayServer {
	private static int SOCKET_01_PORT = 9866;// default port number, remember to
	// change yours
	private static int SOCKET_02_PORT = 9878; // default port number, remember
	// to change yours
	private static String SOCKET_01_IP = "147.97.156.237";// default ip
	private static String SOCKET_02_IP = "147.97.156.237";// default ip

	public static void main(String argv[]) {
		DatagramSocket serverSocket = null;
		if (argv.length > 0)
			try {
				SOCKET_01_PORT = Integer.parseInt(argv[0]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		if (argv.length > 1)
			SOCKET_02_IP = argv[1]; // get the IP 1

		if (argv.length > 2)
			try {
				SOCKET_02_PORT = Integer.parseInt(argv[2]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		if (argv.length > 3)
			SOCKET_02_IP = argv[3]; // get the IP 2
		try {
			System.out.println("Relay Server Started...............");
			// binds server socket 1 with server ip address 01 and port number
			// 01
			serverSocket = new DatagramSocket(SOCKET_01_PORT, InetAddress.getByName(SOCKET_01_IP));
			byte[] receiveData = new byte[1024];
			while (true) {

				// datagram packet to receive data from socket 01
				DatagramPacket receivePacket_Socket_01 = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket_Socket_01);

				String sentence_socket_01 = new String(receivePacket_Socket_01.getData()).trim();
				System.out.println("Recived from socket 01: " + sentence_socket_01);

				// datagram packet to send data to socket 02
				DatagramPacket sendPacket_Socket_02 = new DatagramPacket(sentence_socket_01.getBytes(),
						sentence_socket_01.getBytes().length, InetAddress.getByName(SOCKET_02_IP), SOCKET_02_PORT);
				serverSocket.send(sendPacket_Socket_02);

				// datagram packet to receive data from socket 02
				DatagramPacket receivePacket_Socket_02 = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket_Socket_02);

				String sentence_socket_02 = new String(receivePacket_Socket_02.getData()).trim();
				System.out.println("Recived from socket 02: " + sentence_socket_02);

				// datagram packet to send data to socket 01
				DatagramPacket sendPacket_Socket_01 = new DatagramPacket(sentence_socket_02.getBytes(),
						sentence_socket_02.getBytes().length, InetAddress.getByName(SOCKET_01_IP),
						receivePacket_Socket_01.getPort());
				serverSocket.send(sendPacket_Socket_01);

			}
		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				serverSocket.close();
		}
	}
}
