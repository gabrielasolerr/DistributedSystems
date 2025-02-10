import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private Map<String, ChatClient> clientMap = new HashMap<>();
    private List<String> chatHistory = new ArrayList<>();

    public ChatServerImpl() throws RemoteException {
        
    }
    public String[] login(ChatClient client) throws RemoteException {
        String name = client.getName();
        if (clientMap.containsKey(name)) {
            throw new RuntimeException("Name already exists");
        }
        String [] clientNames = list();
        clientMap.put(name, client);
        for (String clientName : clientNames) {
            clientMap.get(clientName).joined(name);
        }

        for (String message : chatHistory) {
            client.showMessage("History", message);
        }
        return clientNames; 
    };

    public String[] list() {
        return clientMap.keySet().toArray(new String[clientMap.size()]);
    }; 
    public void sendMessage(String from, String to, String message) throws RemoteException {
        String formattedMessage = from + ": " + message;
        chatHistory.add(formattedMessage);
        System.out.println("Added to history: " + formattedMessage);

        for (String clientName : list()) {
            clientMap.get(clientName).showMessage(from, message);
        }
    };
    public void sendMessage(String from, String message) throws RemoteException {
        String formattedMessage = from + ": " + message;
        chatHistory.add(formattedMessage);
        for (String clientName : list()) {
            clientMap.get(clientName).showMessage(from, message);
            }
    };

    public void logout(String name) throws RemoteException {
        clientMap.remove(name);
        String [] clientNames = list();
        for (String clientName : clientNames) {
            clientMap.get(clientName).left(name);
        }
    };

    public List<String> getChatHistory() throws RemoteException {
        System.out.println("Sending chat history: " + chatHistory); 
        return chatHistory;
    };

}
