import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Client {
    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 6666);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            int N = getRandomNumber(1000, 10000);
            int M = getRandomNumber(1000, 10000);
            int L = getRandomNumber(1000, 10000);

            int[][] matrixA = generateRandomMatrix(N, M);
            int[][] matrixB = generateRandomMatrix(M, L);


            objectOutputStream.writeObject(N);
            objectOutputStream.writeObject(M);
            objectOutputStream.writeObject(L);
            objectOutputStream.writeObject(matrixA);
            objectOutputStream.writeObject(matrixB);

            int[][] resultMatrix = (int[][]) objectInputStream.readObject();
            System.out.println("Received result from server:");
            printMatrix(resultMatrix);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int[][] generateRandomMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }

        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private static int getRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
