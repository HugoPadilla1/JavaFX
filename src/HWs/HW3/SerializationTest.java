package HWs.HW3;
import java.io.*;
public class SerializationTest {
    public static void main(String[] args) {
        Book book = new Book("Test Title", "Test Author", "History", true, "Test Library", 10);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.dat"))) {
            oos.writeObject(book);
            System.out.println("Serialization successful");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
