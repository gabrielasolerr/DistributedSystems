import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
    private String name;
    private ChatServer server;

    public ChatClientImpl(String name, ChatServer server) throws RemoteException {
        this.name = name;
        this.server = server;
        server.registerClient(this);
    }

    public void receiveMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public String getName() throws RemoteException {
        return name;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatServer server = (ChatServer) registry.lookup("ChatService");

            ChatClientImpl client = new ChatClientImpl(name, server);
            
            while (true) {
                String message = scanner.nextLine();
                server.sendMessage(message, client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
