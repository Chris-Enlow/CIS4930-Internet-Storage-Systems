import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java client <serverURL> <port_number>");
            return;
        }

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            String helloMessage = in.readLine();
            System.out.println("Server: " + helloMessage);

            while (true) {
                System.out.print("Enter a string (or 'bye' to quit): ");
                String userInput = scanner.nextLine();

                // For the normal actions, the client will (1) start the timer; (2) [cite_start]send the string to the server[cite: 34].
                long startTime = System.currentTimeMillis();
                out.println(userInput);

                String response = in.readLine();

                long endTime = System.currentTimeMillis();

                if (response != null && response.equals("disconnected")) {
                    System.out.println("exit");
                    break;
                }

                if (response != null && response.startsWith("ERROR")) {
                    System.out.println("Server: " + response);
                } else {
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