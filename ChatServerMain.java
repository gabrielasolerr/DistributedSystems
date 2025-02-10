import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatServerMain {
    public static void main(String[] args) throws RemoteException {
        String chatRoom = args[0];
        Registry r = LocateRegistry.createRegistry(1100);
        ChatServer server = new ChatServerImpl();
        r.rebind(chatRoom, server);
    }
}
