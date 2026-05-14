import java.io.ObjectInputStream;

public class FilLectorCX implements Runnable {
    private final ObjectInputStream ois;

    public FilLectorCX(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        try {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                if (obj instanceof String) {
                    String msg = (String) obj;
                    System.out.println("Rebut: " + msg);
                }
            }
        } catch (Exception e) {
            // finalitza
        }
    }
}
