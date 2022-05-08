package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket server;
    private Socket client;
    private GameController gameController;

    public Server(GameController gameController) {
        this.gameController = gameController;
        try {
            server = new ServerSocket(4242);
            client = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String recive() {
        try {
            InputStream input = client.getInputStream();
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
            OutputStream output = client.getOutputStream();
            output.write(step.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
