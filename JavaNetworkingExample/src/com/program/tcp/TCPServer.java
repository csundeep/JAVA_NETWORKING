package com.program.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private final static int SERVER_PORT = 2008;

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(SERVER_PORT);
			System.out.println("Server Started...........");
			while (true) {
				Socket connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

				sentence = inFromClient.readLine();
				System.out.println("Received: " + sentence);

				modifiedSentence = sentence.toUpperCase() + '\n';
				outToClient.writeBytes(modifiedSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			welcomeSocket.close();
		}
	}
}
