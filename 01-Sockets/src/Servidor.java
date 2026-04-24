import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket clientSocket;

    public void connecta() throws IOException {
        srvSocket = new ServerSocket(PORT);
        System.out.println("Servidor escoltant a " + HOST + ":" + PORT);
        clientSocket = srvSocket.accept();
        System.out.println("Client connectat");
    }

    public void repDades() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String linia;
        System.out.println("Dades rebudes del client:");
        while ((linia = reader.readLine()) != null) {
            System.out.println(linia);
        }
        reader.close();
    }

    public void tanca() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error tancant clientSocket: " + e.getMessage());
        }
        try {
            if (srvSocket != null && !srvSocket.isClosed()) {
                srvSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error tancant srvSocket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        try {
            servidor.connecta();
            servidor.repDades();
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        } finally {
            servidor.tanca();
        }
    }
}
