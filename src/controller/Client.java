package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	private Socket client;
	private GameController gameController;
	private OutputStream output;

	public Client(String host,GameController gameController) {
		this.gameController = gameController;
		try {
			client = new Socket(host, 4242);
			System.out.println("Connected");
			output = client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recive() {
		try {
			InputStream input = client.getInputStream();
			byte[] b = new byte[1024];
			int length = input.read(b);
			String step = new String(b, 0, length);
			// gameController.move(step);
			System.out.println(step);

		} catch (IOException e) {
			e.printStackTrace();
		}
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
