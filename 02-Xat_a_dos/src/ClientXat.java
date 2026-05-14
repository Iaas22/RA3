import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void connecta() throws Exception {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public void enviarMissatge(String msg) throws Exception {
        oos.writeObject(msg);
        oos.flush();
    }

    public void tancarClient() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        try {
            client.connecta();
            System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);

            System.out.println("Flux d'entrada i sortida creat.");
            
            FilLectorCX filLector = new FilLectorCX(client.ois);
            Thread t = new Thread(filLector);
            t.start();

            Scanner sc = new Scanner(System.in);
            String lin;
            while (true) {
                lin = sc.nextLine();
                System.out.println("Enviament missatge: " + lin);
                client.enviarMissatge(lin);
                if (ServidorXat.MSG_SORTIR.equals(lin)) break;
            }

            try { t.join(); } catch (Exception e) {}
            sc.close();
            client.tancarClient();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
