import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<ChatClient> clients;
    private List<String> messageHistory;

    public ChatServerImpl() throws RemoteException {
        clients = new ArrayList<>();
        messageHistory = new ArrayList<>();
    }

    public synchronized void registerClient(ChatClient client) throws RemoteException {
        clients.add(client);
        System.out.println("New client joined: " + client.getName());
        client.receiveMessage("Welcome to the chat!");
    }

    public synchronized void sendMessage(String message, ChatClient sender) throws RemoteException {
        String formattedMessage = sender.getName() + ": " + message;
        messageHistory.add(formattedMessage);
        for (ChatClient client : clients) {
            client.receiveMessage(formattedMessage);
        }
    }

    public synchronized List<String> getMessageHistory() throws RemoteException {
        return messageHistory;
    }

    public static void main(String[] args) {
        try {
            ChatServerImpl server = new ChatServerImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("ChatService", server);
            System.out.println("Chat Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
