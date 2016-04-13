package com.program.proxy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpProxy implements Runnable {
	private static int RELAY_PORT = 28712;// default port number, remember to
	// change yours
	private static int WEB_SERVER_PORT = 8080;// 18712; // default port number,
												// remember
	// to change yours
	private static String WEB_SERVER_IP = "localhost";// "147.97.156.237";//
														// default ip

	private Socket socket01 = null;
	private Socket socket02 = null;

	public HttpProxy(Socket socket01, Socket socket02) {
		this.socket01 = socket01;
		this.socket02 = socket02;
	}

	public static void main(String argv[]) {
		ServerSocket serverSocket = null;
		if (argv.length > 0)
			try {
				WEB_SERVER_PORT = Integer.parseInt(argv[0]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		if (argv.length > 1)
			WEB_SERVER_IP = argv[1]; // get the IP 1

		if (argv.length > 2)
			try {
				RELAY_PORT = Integer.parseInt(argv[2]);
			} catch (NumberFormatException e) {
				System.err.println("First argument must be an integer");
				System.exit(1);
			}

		try {
			// Opened connection and binds port number of socket 1
			serverSocket = new ServerSocket(RELAY_PORT);
			System.out.println("Relay server Started...........");
			while (true) {
				// Opened connection and binds ip address and port number of
				// socket 2
				new Thread(new HttpProxy(serverSocket.accept(), new Socket(WEB_SERVER_IP, WEB_SERVER_PORT))).start();
			}
		} catch (Exception e) {
			System.out.println("Exception ::");
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}
	}

	@Override
	public void run() {
		byte[] dataFromBrowser = new byte[500];
		byte[] dataFromWebServer = new byte[500];
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(new File("F://simsons.txt"), true);
			// To receive message from socket 01
			InputStream receive_Socket_01 = socket01.getInputStream();
			// To send message to socket 02
			OutputStream send_Socket_02 = socket02.getOutputStream();
			do {

				receive_Socket_01.read(dataFromBrowser);

				String line = null;
				for (byte b : dataFromBrowser) {
					line = line + (char) b;
				}
				if (line.contains("favicon"))
					continue;

				send_Socket_02.write(dataFromBrowser);
				dataFromWebServer = new byte[500];
			} while (receive_Socket_01.available() > 0);

			// To receive message from socket 02
			BufferedInputStream receive_Socket_02 = new BufferedInputStream(socket02.getInputStream());
			// To send message to socket 01
			OutputStream send_Socket_01 = socket01.getOutputStream();
			do {
				receive_Socket_02.read(dataFromWebServer);
				send_Socket_01.write(dataFromWebServer);
				for (byte b : dataFromWebServer) {
					System.out.print((char) b);
				}
				fileOutputStream.write(dataFromWebServer);
				dataFromWebServer = new byte[500];
			} while (receive_Socket_02.available() > 0);

			fileOutputStream.flush();
			receive_Socket_01.close();
			send_Socket_02.close();
			send_Socket_02.flush();
			receive_Socket_02.close();
			send_Socket_01.close();
			send_Socket_01.flush();
			dataFromBrowser = new byte[500];
			dataFromWebServer = new byte[500];
		} catch (Exception e) {

		} finally {
			try {
				socket01.close();
				socket02.close();
				fileOutputStream.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}

	}
}
