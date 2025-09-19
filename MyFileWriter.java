
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class MyFileWriter { 
    public void generateHiddenFile(String pswrd) {
        File secret = new File(".WhatsThePassword");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(secret))) {
            bufferedWriter.write(pswrd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aboutHannah() {
        File secret = new File("Hannah.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(secret))) {
            bufferedWriter.write("I'm 17 years old.\nI have a dog.\nI like coffee ice cream.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateHiddenFolderFile(String teacher) {
        File diary = new File(".diary");
        diary.mkdir();
        File file = new File(diary, "leastFavTeacher");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(teacher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void createFolder(String newFolderPath) {
        File dir = new File(newFolderPath);
        if (!dir.exists())
            dir.mkdir();
    }

    public static void main(String[] args) {

        createFolder("newFolderTest");

        File secret = new File("Hannah.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(secret))) {
            bufferedWriter.write("I'm 17 years old.\nI have a dog.\nI like coffee ice cream.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String data = "Hello, World!";
        // String fileName1 = "example.txt";
        // String fileName2 = "example2.txt";
        // String fileName3 = "example3.txt";
        // String fileName4 = "example4.txt";
        // String fileName5 = "example5.txt";

        // // 1. Using FileWriter
        // try (FileWriter writer = new FileWriter(fileName1)) {
        //     writer.write(data);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // 2. Using BufferedWriter
        // try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName2))) {
        //     bufferedWriter.write(data);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // 3. Using FileOutputStream
        // try (FileOutputStream outputStream = new FileOutputStream(fileName3)) {
        //     outputStream.write(data.getBytes());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // 4. Using BufferedOutputStream
        // try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName4))) {
        //     bufferedOutputStream.write(data.getBytes());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // 5. Using Files (java.nio.file)
        // try {
        //     Files.write(Paths.get(fileName5), data.getBytes(StandardCharsets.UTF_8));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // } 
        
        
        
    }
}
