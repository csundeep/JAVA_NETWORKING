package com.program.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

class TCPClient {
	private final static int SERVER_PORT = 2008;

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		System.out.println("Enter the sentence ");
		Socket clientSocket = null;
		try {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			clientSocket = new Socket();
			SocketAddress address = new InetSocketAddress("localhost", SERVER_PORT);
			clientSocket.connect(address);

			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');

			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clientSocket.close();
		}
	}
}