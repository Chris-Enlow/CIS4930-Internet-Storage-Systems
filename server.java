import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        // The server should take just 1 argument, the port number to listen on. The  format for server: java server <port_number>.
        if (args.length != 1) {
            System.out.println("Incorrect Format. Use: java server <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // The server should display the listening message if its running smoothly.
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
            // Send the predetermined "Hello" message after connection is established
            out.println("Hello!");

            // get input from client
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);

                // end the connection if the client sends "bye"
                if (inputLine.equals("bye")) {
                    out.println("disconnected");
                    clientSocket.close();
                    System.exit(0); 
                    break;
                }

                // if the client didn't say bye, check if the string is valid (only contains letters). 
                // If valid, send back the capitalized version; if not, send back an error message.
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