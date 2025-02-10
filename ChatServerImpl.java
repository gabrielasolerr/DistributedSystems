import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private Map<String, ChatClient> clientMap = new HashMap<>();
    private static final String HISTORY_FILE = "chat_history.txt";
    private List<String> chatHistory = new ArrayList<>();

    public ChatServerImpl() throws RemoteException {
        loadChatHistory();
    }

    private void loadChatHistory() {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    chatHistory.add(line);
                }
                System.out.println("Chat history loaded from file.");
            } catch (IOException e) {
                System.err.println("Failed to load chat history: " + e.getMessage());
            }
        }
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
    saveMessage(formattedMessage); 

    System.out.println("Added to history: " + formattedMessage);
    
    for (String clientName : list()) {
        clientMap.get(clientName).showMessage(from, message);
    }
}

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

    private void saveMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to save chat message: " + e.getMessage());
        }
    }

}
