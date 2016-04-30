package com.program.proxy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpProxy implements Runnable {
	private static int RELAY_PORT = 28712;// default port number, remember to
	// change yours
	private static int WEB_SERVER_PORT = 8080;// 18712; // default port number,
												// remember
	// to change yours
	private static String WEB_SERVER_IP = "localhost";// "147.97.156.237";//
														// default ip

	private static String BASE_DIRECTORY = "E://";

	private Map<String, String> requestHeaders;
	private Map<String, String> responseHeaders;
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
				Thread t = new Thread(new HttpProxy(serverSocket.accept(), new Socket(WEB_SERVER_IP, WEB_SERVER_PORT)));
				t.start();
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
		byte[] dataFromWebServer = new byte[500];
		FileOutputStream fileOutputStream = null;

		try {
			// To receive message from socket 01
			InputStream receive_Socket_01 = socket01.getInputStream();

			OutputStream send_Socket_02 = socket02.getOutputStream();

			StringBuffer sb = new StringBuffer();
			requestHeaders = parseRequestHeader(receive_Socket_01, sb);

			String fileName = extractFileName(requestHeaders.get("request"));

			if (fileName != null) {
				File file = new File(BASE_DIRECTORY + fileName);
				file.getParentFile().mkdirs();
				fileOutputStream = new FileOutputStream(file);

			}

			// To send message to socket 02
			send_Socket_02.write(sb.toString().getBytes());

			// To receive message from socket 02
			BufferedInputStream receive_Socket_02 = new BufferedInputStream(socket02.getInputStream());

			sb = new StringBuffer();
			responseHeaders = parseHeader(receive_Socket_02, sb);
			int contentLength = Integer.parseInt(
					responseHeaders.get("Content-Length") != null ? responseHeaders.get("Content-Length") : "0");

			// To send message to socket 01
			OutputStream send_Socket_01 = socket01.getOutputStream();
			send_Socket_01.write(sb.toString().getBytes());
			// fileOutputStream.write(sb.toString().getBytes());
			int length = 0;

			while (contentLength != 0 && length != contentLength) {
				length += receive_Socket_02.read(dataFromWebServer);
				send_Socket_01.write(dataFromWebServer);
				fileOutputStream.write(dataFromWebServer);
				dataFromWebServer = new byte[500];
			}
			if (fileOutputStream != null)
				fileOutputStream.close();
			receive_Socket_01.close();
			send_Socket_02.close();
			send_Socket_02.flush();
			receive_Socket_02.close();
			send_Socket_01.close();
			send_Socket_01.flush();
			dataFromWebServer = new byte[500];
		} catch (Exception e) {
			System.err.print(e);
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null)
					fileOutputStream.close();
				socket01.close();
				socket02.close();
			} catch (IOException e) {
				System.err.println("Could not close port.");
				System.exit(1);
			}
		}

	}

	private String extractFileName(String request) {
		String fileName = null;
		for (String part : request.split(" ")) {
			if (part.contains(".html")) {// Extract fileName for html files
				fileName = part.substring(part.lastIndexOf('/') + 1, part.length());
			} else if (part.contains(".jpg") || part.contains(".jpeg") || part.contains(".png")) {// Extract
																									// file
																									// name
																									// for
																									// image
																									// files
				fileName = part.substring(part.lastIndexOf('/') + 1, part.length());
				part = part.replace(fileName, "");
				part = part.substring(0, part.length() - 1);
				String path = part.substring(part.lastIndexOf('/') + 1, part.length());
				fileName = path + "/" + fileName;
			}
		}
		return fileName;
	}

	public static Map<String, String> parseRequestHeader(InputStream inputStream, StringBuffer sb) throws IOException {
		int c = 0;

		while (c != -1)// read -1 means inputstream is not giving anything
		{
			c = inputStream.read();
			sb.append((char) c);
			if (c == '\r') {
				sb.append((char) inputStream.read());
				c = inputStream.read();
				if (c == '\r') {
					sb.append((char) inputStream.read());
					break;
				} else {
					sb.append((char) c);
				}
			}
		}
		String[] headersArray = sb.toString().split("\r\n");

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("request", headersArray[0]);
		for (int i = 1; i < headersArray.length - 1; i++) {
			headers.put(headersArray[i].split(": ")[0], headersArray[i].split(": ")[1]);
		}

		return headers;
	}

	public static Map<String, String> parseHeader(InputStream inputStream, StringBuffer sb) throws IOException {
		int c = 0;

		while (c != -1)// read -1 means inputstream is not giving anything
		{
			c = inputStream.read();
			sb.append((char) c);
			if (c == '\r') {
				sb.append((char) inputStream.read());
				c = inputStream.read();
				if (c == '\r') {
					sb.append((char) inputStream.read());
					break;
				} else {
					sb.append((char) c);
				}
			}
		}
		String[] headersArray = sb.toString().split("\r\n");
		Map<String, String> headers = new HashMap<String, String>();
		for (int i = 1; i < headersArray.length - 1; i++) {
			headers.put(headersArray[i].split(": ")[0], headersArray[i].split(": ")[1]);
		}

		return headers;
	}
}
