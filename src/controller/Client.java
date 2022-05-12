package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	private Socket client;
	private InputStream input;
	private OutputStream output;

	public Client(String host) {
		try {
			client = new Socket(host, 4242);
			System.out.println("Connected");
			output = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receive() {
		try {
			input = client.getInputStream();
			byte[] b = new byte[1024];
			int length = input.read(b);
			String step = new String(b, 0, length);
			return step;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void send(String step) {
		try {
			output.write(step.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
