import java.io.File;
import java.io.IOException;

public class TREE {
    public static String createTREE(String dirPath) {
        File file = new File(dirPath);
        String fileContents = "";
        if (file.isFile()) { // for recursion, if its a file just end the loop here
            String key = SHA1.encryptThisString(BLOB.getFileContents(file));
            if (!verifyIndexUpdate(dirPath, new File(key), new File("git" + File.separator + "index"))) {
                return "dns";
            }
            return key;
        }
        for (File f : file.listFiles()) { // go through each file in the directory and make the tree file
            if (createTREE(f.getPath()).equals("dns")) {
                continue;
            }
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

    public static boolean verifyIndexUpdate(String path, File blob, File index) {
        String contents = BLOB.getFileContents(index);
        if (!contents.contains(blob.getName() + " " + path)) {
            return false;
        }
        return true;
    }
}
