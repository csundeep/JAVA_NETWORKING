package com.program.tcp;
//***** TCPClient.java *****

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
//           connect to TCP server (TCPServer.java) and send/receive a
//           message to/from the server
//
//Editor/Platform:  vi/Linux
//
//
//
//Input:      none
//
//Output:     The server will display the message sent by this client
//
//Compile:    javac TCPClient.java
//
//Command:    (After server is running)
//            java TCPClient  [<port#>  [<IP>]]
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
import java.net.Socket;//For Socket class to connect to server

class TCPClient{
	private static int SOCKET_01_PORT = 28712; // default port number, remember
												// to change yours
	private static String SOCKET_01_IP = "localhost";// default ip

	public static void main(String argv[]) {
		String sentence;
		String modifiedSentence;
		Socket socket01 = null;
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
			
			// Opened connection and binds ip address and port number
			socket01 = new Socket(SOCKET_01_IP, SOCKET_01_PORT);
			
			// Set up stream for keyboard entry...
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the message ");
			sentence = br.readLine();

			// To send data to server
			DataOutputStream outToRelayServer = new DataOutputStream(socket01.getOutputStream());
			outToRelayServer.writeBytes(sentence + '\n');

			// To recive data from server
			BufferedReader inFromRelayServer = new BufferedReader(new InputStreamReader(socket01.getInputStream()));
			modifiedSentence = inFromRelayServer.readLine();
			System.out.println("FROM RELAY SERVER: " + modifiedSentence);

			br.close();
			outToRelayServer.flush();
			outToRelayServer.close();
			inFromRelayServer.close();

		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			try {
				socket01.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}
	}
}