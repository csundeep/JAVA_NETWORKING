package com.program.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPRelayServer {
	private final static int SOCKET_01_PORT = 2008;
	private final static int SOCKET_02_PORT = 2009;

	public static void main(String argv[]) throws Exception {
		String sentenceFromClient;
		String sentenceFromServer;
		ServerSocket serverSocket = null;

		Socket socket01 = null;
		Socket socket02 = null;
		try {
			serverSocket = new ServerSocket(SOCKET_01_PORT);
			System.out.println("Relay server Started...........");
			while (true) {
				socket01 = serverSocket.accept();

				socket02 = new Socket("localhost", SOCKET_02_PORT);

				BufferedReader receive_Socket_01 = new BufferedReader(new InputStreamReader(socket01.getInputStream()));
				sentenceFromClient = receive_Socket_01.readLine();
				System.out.println("Received from client : " + sentenceFromClient);

				DataOutputStream send_Socket_02 = new DataOutputStream(socket02.getOutputStream());
				send_Socket_02.writeBytes(sentenceFromClient + '\n');

				BufferedReader receive_Socket_02 = new BufferedReader(new InputStreamReader(socket02.getInputStream()));
				sentenceFromServer = receive_Socket_02.readLine();
				System.out.println("Received from Server : " + sentenceFromServer);

				DataOutputStream send_Socket_01 = new DataOutputStream(socket01.getOutputStream());
				send_Socket_01.writeBytes(sentenceFromServer + '\n');
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
			socket01.close();
			socket02.close();
		}
	}
}
