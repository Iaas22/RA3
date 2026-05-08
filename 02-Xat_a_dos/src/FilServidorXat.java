import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FilServidorXat implements Runnable {
    private final ObjectInputStream ois;
    private final String nom;
    private final ObjectOutputStream oos;

    public FilServidorXat(ObjectInputStream ois, String nom, ObjectOutputStream oos) {
        this.ois = ois;
        this.nom = nom;
        this.oos = oos;
        System.out.println("Fil de " + nom + " iniciat.");
    }

    @Override
    public void run() {
        try {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (!(obj instanceof String)) continue;
                String msg = (String) obj;
                System.out.println("Missatge ('" + ServidorXat.MSG_SORTIR + "' per tancar): Rebut: " + msg);
                if (ServidorXat.MSG_SORTIR.equals(msg)) {
                    System.out.println("Fil de xat finalitzat.");
                    break;
                }
                if ("Hola servidor!".equals(msg)) {
                    System.out.println("Hola " + nom + "!");
                } else if ("Adeu".equals(msg)) {
                    System.out.println("Adeu");
                }
            }
        } catch (Exception e) {
            // finalitza
        }
    }
}
