package com.program.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private final static int SOCKET_02_PORT = 2009;

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		ServerSocket serverSocket = null;
		Socket socket02 = null;
		try {
			serverSocket = new ServerSocket(SOCKET_02_PORT);
			System.out.println("Server Started...........");
			while (true) {
				socket02 = serverSocket.accept();

				BufferedReader receive_Socket_02 = new BufferedReader(new InputStreamReader(socket02.getInputStream()));
				sentence = receive_Socket_02.readLine();
				System.out.println("Received from relay sever: " + sentence);

				DataOutputStream send_Socket_02 = new DataOutputStream(socket02.getOutputStream());
				modifiedSentence = sentence.toUpperCase() + '\n';
				send_Socket_02.writeBytes(modifiedSentence);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
			socket02.close();
		}
	}
}
