import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
    private String name;

    public ChatClientImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void joined(String name) {
        System.out.println(name + " has joined the chat.");
    }

    @Override
    public void left(String name) {
        System.out.println(name + " has left the chat.");
    }

    @Override
    public void showMessage(String from, String message) {
        System.out.println(from + ": " + message);
    }
}
