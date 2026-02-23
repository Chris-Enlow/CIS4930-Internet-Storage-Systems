import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        // The client should take two command-line arguments: the server’s URL and the port number on which the server is listening.
        if (args.length != 2) {
            System.out.println("Usage: java client <serverURL> <port_number>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port);
            // The client should read the welcome message from the server and display it to the user.
            // autoflush is set to true to ensure that the output is sent immediately without being put in a buffer potentially adding delay.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)) {

            // Read the predetermined "Hello" message from server after connection is established
            String helloMessage = in.readLine();
            System.out.println("Server: " + helloMessage);

            // While client does not enter "bye", the client should repeatedly prompt the user to enter a string.
            while (true) {
                System.out.print("Enter a string (or 'bye' to quit): ");
                String userInput = scanner.nextLine();

                // Start a timer to measure RTT
                long startTime = System.currentTimeMillis();
                out.println(userInput);

                // Client sends message and waits for response
                // readline() is a blocking call, so the client will wait here until it receives a response from the server.
                String response = in.readLine();

                // End timer
                long endTime = System.currentTimeMillis();

                // (disconnected) Response from server to client if client inputs "bye"
                if (response != null && response.equals("disconnected")) {
                    System.out.println("exit");
                    break;
                }

                // display errors
                if (response != null && response.startsWith("ERROR")) {
                    System.out.println("Server: " + response);
                } else {
                    // capitalzr the response from the server and display it to the user.
                    System.out.println("Capitalized Response: " + response);
                    long rtt = endTime - startTime;
                    System.out.println("Round-trip time: " + rtt + " ms");
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
