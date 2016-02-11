package com.program.tcp;

//***** TCPServer.java *****

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
//         connect to TCP server (TCPServer.java) and send/receive a
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
//Compile:    javac TCPServer.java
//
//Command:    (After server is running)
//          java TCPServer  [<port#>  [<IP>]]
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

public class TCPServer {
	private static int SOCKET_02_PORT = 2009; // default port number, remember
												// to change yours
	@SuppressWarnings("unused")
	private static String SOCKET_02_IP = "147.97.156.237";// default ip

	public static void main(String argv[]) {
		String sentence;
		String modifiedSentence;
		ServerSocket serverSocket = null;
		Socket socket02 = null;

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
			// Opened connection and binds port number
			serverSocket = new ServerSocket(SOCKET_02_PORT);
			System.out.println("Server Started...........");
			while (true) {
				// listens to the clients messages
				socket02 = serverSocket.accept();

				// To read clients messages
				BufferedReader receive_Socket_02 = new BufferedReader(new InputStreamReader(socket02.getInputStream()));
				sentence = receive_Socket_02.readLine();
				System.out.println("Received from relay sever: " + sentence);

				// To reply to clients messages
				DataOutputStream send_Socket_02 = new DataOutputStream(socket02.getOutputStream());
				modifiedSentence = sentence + '\n';
				send_Socket_02.writeBytes(modifiedSentence);

				receive_Socket_02.close();
				send_Socket_02.flush();
				send_Socket_02.close();
			}
		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
				socket02.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
				e.printStackTrace();
			}
		}
	}
}
