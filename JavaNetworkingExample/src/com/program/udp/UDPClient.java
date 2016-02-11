package com.program.udp;
//***** UDPClient.java *****

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
//         connect to UDP server (UDPServer.java) and send/receive a
//         message to/from the server
//
//Editor/Platform:  vi/Linux
//
//
//
//Input:      none
//
//Output:     The server will display the message sent by this client
//
//Compile:    javac UDPClient.java
//
//Command:    (After server is running)
//             java UDPClient  [<port#>  [<IP>]]
//
//
//Note:       Remember to Change SERVER_PORT_ID to be same as server's
//
//*******************************************************************

//Java libraries
//First three libraries are to implement message buffer for both in and out messages

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;//To construct the datagram packets to carry messages
import java.net.DatagramSocket;//For Socket class to connect to UDP server
import java.net.InetAddress;

public class UDPClient {
	private static int SOCKET_01_PORT = 9866; // default port number, remember
	// to change yours
	private static String SOCKET_01_IP = "147.97.156.237";// default ip

	public static void main(String[] argv) {
		DatagramSocket socket = null;
		if (argv.length > 0)
			try {
				SOCKET_01_PORT = Integer.parseInt(argv[0]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		if (argv.length > 1)
			SOCKET_01_IP = argv[1]; // get the IP
		try {
			// To bind client socket
			socket = new DatagramSocket();

			// Set up stream for keyboard entry...
			System.out.println("Enter the message");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String sentence = br.readLine();

			// datagram packet to send data
			DatagramPacket request = new DatagramPacket(sentence.getBytes(), sentence.length(),
					InetAddress.getByName(SOCKET_01_IP), SOCKET_01_PORT);
			// send packet to server
			socket.send(request);

			// datagram packet to receive data
			DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
			// receive packet from server
			socket.receive(response);

			String result = new String(response.getData(), 0, response.getLength(), "US-ASCII");
			System.out.println("Result from relay server : " + result);
		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}
}
