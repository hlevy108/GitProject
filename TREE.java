import java.io.File;
import java.io.IOException;

public class TREE {
    public static String createTREE(String dirPath) {
        File file = new File(dirPath);
        String fileContents = "";
        if (file.isFile()) { //for recursion, if its a file just end the loop here
            return SHA1.encryptThisString(BLOB.getFileContents(new File(BLOB.createBlob(dirPath))));
        }
        for (File f : file.listFiles()) { //go through each file in the directory and make the tree file
            if (!fileContents.isBlank()) {
                fileContents += "\n";
            }
            String type = "";
            if (f.isFile()) {
                type = "blob";
            } else {
                type = "tree";
            }
            fileContents += type + " " + createTREE(f.getPath()) + " " + f.getName();
        }
        String key = SHA1.encryptThisString(fileContents);
        File TREE = new File("git" + File.separator + "objects" + File.separator + key);
        try {
            TREE.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        BLOB.copyToBlob(fileContents, TREE);
        return key;
    }
}
