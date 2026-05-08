import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorXat {
    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    public static final String MSG_SORTIR = "sortir";

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void iniciarServidor() throws Exception {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }

    public void pararServidor() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
        } catch (Exception e) {}
    }

    public String getNom() throws Exception {
        Object obj = ois.readObject();
        if (obj instanceof String) return (String) obj;
        return null;
    }

    public static void main(String[] args) {
        ServidorXat srv = new ServidorXat();
        try {
            srv.iniciarServidor();
            srv.clientSocket = srv.serverSocket.accept();
            System.out.println("Client connectat: " + srv.clientSocket.getRemoteSocketAddress());

            srv.oos = new ObjectOutputStream(srv.clientSocket.getOutputStream());
            srv.ois = new ObjectInputStream(srv.clientSocket.getInputStream());

            String nom = srv.getNom();
            System.out.println("Nom rebut: " + nom);

            System.out.println("Fil de xat creat.");
            FilServidorXat fil = new FilServidorXat(srv.ois, nom, srv.oos);
            Thread t = new Thread(fil);
            t.start();

            Scanner sc = new Scanner(System.in);
            String lin;
            while (true) {
                lin = sc.nextLine();
                srv.oos.writeObject(lin);
                srv.oos.flush();
                if (MSG_SORTIR.equals(lin)) break;
            }

            try { t.join(); } catch (Exception e) {}

            try { srv.clientSocket.close(); } catch (Exception e) {}
            srv.pararServidor();
            System.out.println("Servidor aturat.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
