import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BLOB {
    public static String getFileContents(File file) {

        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void copyToBlob(String fileContents, File newFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
            writer.write(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String createBlob(File file) {
        String fileContents = getFileContents(file);
        String key = SHA1.encryptThisString(fileContents); 
        File BLOB = new File("git/objects/" + key);
        try {
            BLOB.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        copyToBlob(fileContents, BLOB);
        return "git/objects/" + key;
    }

}
