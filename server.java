import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect Format. Use: java server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Hello!");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                if (inputLine.equals("bye")) {
                    out.println("disconnected");
                    clientSocket.close();
                    System.exit(0); 
                    break;
                }

                if (inputLine.matches("^[a-zA-Z]+$")) {
                    out.println(inputLine.toUpperCase());
                } else {
                    out.println("Error: String contains non-alphabetical letters. Please try again.");
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}