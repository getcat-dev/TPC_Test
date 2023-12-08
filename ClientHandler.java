import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable<Void> {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public Void call() throws IOException {
        try (
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            int N = (int) objectInputStream.readObject();
            int M = (int) objectInputStream.readObject();
            int L = (int) objectInputStream.readObject();

            int[][] matrixA = (int[][]) objectInputStream.readObject();
            int[][] matrixB = (int[][]) objectInputStream.readObject();

            if (matrixA[0].length != matrixB.length) {
                objectOutputStream.writeObject(new InvalidMatrixSizeException("Invalid matrix sizes for multiplication"));
            } else {
                int[][] resultMatrix = multiplyMatrices(matrixA, matrixB);
                objectOutputStream.writeObject(resultMatrix);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;

        int[][] resultMatrix = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }

        return resultMatrix;
    }
}
