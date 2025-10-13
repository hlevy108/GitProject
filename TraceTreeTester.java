import java.io.*;
import java.nio.file.Files;

public class TraceTreeTester {

    public static void main(String[] args) throws IOException {
        System.out.println("TraceTree test start");
        cleanup();

        // Initialize repo
        GitRepositoryInitializer.initGitRepo();

        // Build a small working directory tree
        File dirA = new File("work");
        dirA.mkdir();
        File sub = new File("work/sub");
        sub.mkdir();
        File f1 = new File("work/a.txt");
        File f2 = new File("work/sub/b.txt");
        f1.createNewFile();
        f2.createNewFile();
        Files.write(f1.toPath(), "alpha".getBytes());
        Files.write(f2.toPath(), "beta".getBytes());

        // Stage files into index and create blobs
        BLOB.addFile("work/a.txt");
        BLOB.addFile("work/sub/b.txt");

        // Build root tree from current folder
        TREE.createROOT();

        // Find and verify the root tree
        String root = TraceTree.findRootTreeHash();
        if (root == null) {
            System.out.println("No root tree found");
            finish(dirA);
            return;
        }

        System.out.println("Root tree hash: " + root);

        boolean ok = TraceTree.verifyTreeRecursive(root, "");
        if (ok) {
            System.out.println("Trace verification passed");
        } 
        else {
            System.out.println("Trace verification found problems");
        }

        // Cleanup
        finish(dirA);
        System.out.println("TraceTree test end");
    }

    private static void finish(File workRoot) {
        deleteRecursively(workRoot);
        cleanup();
    }

    private static void cleanup() {
        File git = new File("git");
        deleteRecursively(git);
    }

    private static void deleteRecursively(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }
}
