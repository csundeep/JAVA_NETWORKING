package com.program.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient {
	private final static int SOCKET_01_PORT = 2008;

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		Socket socket01 = null;
		try {
			socket01 = new Socket("localhost", SOCKET_01_PORT);

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the sentence ");
			sentence = br.readLine();
			
			DataOutputStream outToRelayServer = new DataOutputStream(socket01.getOutputStream());
			outToRelayServer.writeBytes(sentence + '\n');
			
			BufferedReader inFromRelayServer = new BufferedReader(new InputStreamReader(socket01.getInputStream()));
			modifiedSentence = inFromRelayServer.readLine();
			System.out.println("FROM RELAY SERVER: " + modifiedSentence);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket01.close();
		}
	}
}