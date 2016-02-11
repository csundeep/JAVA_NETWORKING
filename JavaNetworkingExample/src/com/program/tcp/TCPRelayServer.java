package com.program.tcp;
//***** TCPRelayServer.java *****

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
//Description: This is a TCP socket program that illustrates how a client
//       connect to TCP relay sever and relay server connect to TCP server (TCPServer.java) and exchanges
//       messages between client and server
//
//Editor/Platform:  vi/Linux
//
//
//
//Input:      none
//
//Output:     The server will display the message sent by this client
//
//Compile:    javac TCPRelayServer.java
//
//Command:    (After server is running)
//            java TCPRelayServer  [<port1#>  [<IP1>]] [<port2#>  [<IP2>]]
//
//
//Note:       Remember to Change SERVER_PORT_ID to be same as server's
//
//*******************************************************************

//Java libraries
//First three libraries are to implement message buffer for both in and out messages
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;//This library is used to create the server socket
import java.net.Socket;//For Socket class to listen messages from clients

public class TCPRelayServer {
	private static int SOCKET_01_PORT = 2008;// default port number, remember to
												// change yours
	private static int SOCKET_02_PORT = 2009; // default port number, remember
												// to change yours
	@SuppressWarnings("unused")
	private static String SOCKET_01_IP = "147.97.156.237";// default ip
	private static String SOCKET_02_IP = "147.97.156.237";// default ip

	public static void main(String argv[]) {
		String sentenceFromClient;
		String sentenceFromServer;
		ServerSocket serverSocket = null;

		Socket socket01 = null;
		Socket socket02 = null;

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
			// Opened connection and binds port number of socket 1
			serverSocket = new ServerSocket(SOCKET_01_PORT);
			System.out.println("Relay server Started...........");
			while (true) {
				socket01 = serverSocket.accept();
				// Opened connection and binds ip address and port number of
				// socket 2
				socket02 = new Socket(SOCKET_02_IP, SOCKET_02_PORT);

				// To receive message from socket 01
				BufferedReader receive_Socket_01 = new BufferedReader(new InputStreamReader(socket01.getInputStream()));
				sentenceFromClient = receive_Socket_01.readLine();
				System.out.println("Received from client : " + sentenceFromClient);

				// To send message to socket 02
				DataOutputStream send_Socket_02 = new DataOutputStream(socket02.getOutputStream());
				send_Socket_02.writeBytes(sentenceFromClient + '\n');

				// To receive message from socket 02
				BufferedReader receive_Socket_02 = new BufferedReader(new InputStreamReader(socket02.getInputStream()));
				sentenceFromServer = receive_Socket_02.readLine();
				System.out.println("Received from Server : " + sentenceFromServer);

				// To send message to socket 01
				DataOutputStream send_Socket_01 = new DataOutputStream(socket01.getOutputStream());
				send_Socket_01.writeBytes(sentenceFromServer + '\n');

				receive_Socket_01.close();
				send_Socket_02.flush();
				send_Socket_02.close();
				receive_Socket_02.close();
				send_Socket_01.flush();
				send_Socket_01.close();
			}
		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
				socket01.close();
				socket02.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}
	}
}
