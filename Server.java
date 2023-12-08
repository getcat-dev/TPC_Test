import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Server started");

            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                executorService.submit(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
