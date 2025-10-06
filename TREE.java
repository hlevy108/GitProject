import java.io.File;
import java.io.IOException;

public class TREE {
    public static String createTREE(String dirPath) {
        File file = new File(dirPath);
        String fileContents = "";
        if (file.isFile()) {
            return SHA1.encryptThisString(BLOB.getFileContents(new File(BLOB.createBlob(file))));
        }
        for (File f : file.listFiles()) {
            if (!fileContents.isBlank()) {
                fileContents += "\n";
            }
            String type = "";
            if (f.isFile()) {
                type = "blob";
            } else {
                type = "tree";
            }
            fileContents += type + " " + createTREE(f.getAbsolutePath()) + " " + f.getName();
        }
        String key = SHA1.encryptThisString(fileContents);
        File TREE = new File("git" + File.separator + "objects" + File.separator + key);
        try {
            TREE.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        BLOB.copyToBlob(fileContents, TREE);
        File index = new File("git" + File.separator + "index");
        BLOB.updateIndexFile(file, index, "tree");
        return key;
    }
}
