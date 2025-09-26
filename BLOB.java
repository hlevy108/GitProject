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
        File index = new File("git/index");
        updateIndexFile(file, index);
        return "git/objects/" + key;
    }

    public static void updateIndexFile(File file, File index) {
        String fileContents = getFileContents(file);
        String hash = SHA1.encryptThisString(fileContents); 
        try {
            if (index.length() == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(index))) {
                    writer.write(hash + " " + file.getCanonicalPath());
                }
            } else {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(index, true))) {
                    writer.newLine(); 
                    writer.write(hash + " " + file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

}
