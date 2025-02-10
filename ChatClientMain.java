import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatClientMain {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java ChatClientMain <server-hostname> <chatRoom> <yourName>");
            return;
        }

        try {
            // Read arguments
            String serverHost = args[0];
            String chatRoom = args[1];
            String clientName = args[2];

            // Locate the registry
            Registry registry = LocateRegistry.getRegistry(serverHost, 1100);
            
            // Lookup the chat server
            ChatServer server = (ChatServer) registry.lookup(chatRoom);
            
            // Create and register the client
            ChatClient client = new ChatClientImpl(clientName);
            String[] users = server.login(client);

            System.out.println("Welcome to the chat, " + clientName + "!");
            System.out.println("Users online: " + String.join(", ", users));

            // Chat loop
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                
                if (message.equalsIgnoreCase("EXIT")) {
                    server.logout(clientName);
                    System.out.println("You left the chat.");
                    break;
                }

                server.sendMessage(clientName, message);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
