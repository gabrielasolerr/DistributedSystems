import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServer extends Remote {
    String[] login(ChatClient client) throws RemoteException;
    String[] list() throws RemoteException; 
    void sendMessage(String from, String to, String message) throws RemoteException;
    void sendMessage(String from, String message) throws RemoteException;
    void logout(String name) throws RemoteException;

    List<String> getChatHistory() throws RemoteException;   
}
