package com.program.udp;
//***** UDPServer.java *****

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
//       connect to UDP server (UDPServer.java) and send/receive a
//       message to/from the server
//
//Editor/Platform:  vi/Linux
//
//
//
//Input:      none
//
//Output:     The server will display the message sent by this client
//
//Compile:    javac UDPServer.java
//
//Command:    (After server is running)
//        java UDPServer  [<port#>  [<IP>]]
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

public class UDPServer {

	private static int SOCKET_02_PORT = 9878; // default port number, remember
	// to change yours

	private static String SOCKET_02_IP = "147.97.156.237";// default ip

	public static void main(String argv[]) {
		DatagramSocket serverSocket = null;
		if (argv.length > 0)
			try {
				SOCKET_02_PORT = Integer.parseInt(argv[0]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		if (argv.length > 1)
			SOCKET_02_IP = argv[1]; // get the IP
		try {
			// binds server socket 1 with server ip address 02 and port number
			// 02
			System.out.println("Server Started...............");
			serverSocket = new DatagramSocket(SOCKET_02_PORT, InetAddress.getByName(SOCKET_02_IP));

			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];

			while (true) {

				// datagram packet to receive data from client
				DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivedPacket);

				String message = new String(receivedPacket.getData()).trim();
				System.out.println("Recived from relay server: " + message);

				InetAddress IPAddress = receivedPacket.getAddress();
				int port = receivedPacket.getPort();

				// datagram packet to sent data to client
				String convertedMessage = message;
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
