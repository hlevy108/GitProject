import java.io.*;
import java.nio.file.Files;

public class GitRepoTester {
    public static void main(String[] args) throws IOException {
        GitRepoTester tester = new GitRepoTester();
        tester.testRepoInit();
        tester.testBlobInit();
        tester.testIndexUpdate();

        GitRepositoryInitializer.initGitRepo();
        File testDir1 = new File("testDir1");
        testDir1.mkdir();
        File testFile1 = new File("testDir1" + File.separator + "testFile1");
        testFile1.createNewFile();
        Files.write(testFile1.toPath(), "hiya".getBytes());
        File testDir2 = new File("testDir1" + File.separator + "testDir2");
        testDir2.mkdir();
        File testFile2 = new File("testDir1" + File.separator + "testDir2" + File.separator + "testFile2");
        testFile2.createNewFile();
        Files.write(testFile2.toPath(), "hiya but different".getBytes());
        TREE.createTREE(testDir1.getAbsolutePath());

        testDir1.delete();
        testDir2.delete();
        testFile1.delete();
        testFile2.delete();
        cleanup();
    }

    public boolean verifyRepoInitialization() {
        File git = new File("git");
        File objects = new File(git, "objects");
        File index = new File(git, "index");
        File HEAD = new File(git, "HEAD");

        boolean gitExists = git.exists() && git.isDirectory();
        boolean objectsExists = objects.exists() && objects.isDirectory();
        boolean indexExists = index.exists() && index.isFile();
        boolean headExists = HEAD.exists() && HEAD.isFile();

        if (gitExists && objectsExists && indexExists && headExists) {
            return true;
        } else {
            return false;
        }

    }

    public boolean verifyBlobInitialization(String blobPath) {
        File blob = new File(blobPath);
        if (!blob.exists()) {
            return false;
        }
        return true;

    }

    public boolean verifyIndexUpdate(File original, File blob, File index) {
        String contents = BLOB.getFileContents(index);
        try {
            if (!contents.contains(blob.getName() + " " + original.getCanonicalPath())) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static void cleanup() {
        File file = new File("git");
        rmrf(file);
    }

    public static void rmrf(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                rmrf(f);
            }
        }
        file.delete();
    }

    public void testRepoInit() {
        for (int i = 1; i <= 3; i++) {
            System.out.println("Starting cycle #" + i);
            GitRepositoryInitializer.initGitRepo();
            if (!verifyRepoInitialization()) {
                System.out.println("Test failed on cycle #" + i);
                return;
            } else {
                System.out.println("Repository initialization verified for cycle #" + i);
            }
            cleanup();
            System.out.println("Cleanup done for cycle # " + i);
        }
        System.out.println("All 3 cycles done!");

    }

    public void testBlobInit() {
        for (int i = 1; i <= 3; i++) {
            GitRepositoryInitializer.initGitRepo();
            File readMe = new File("README.md");
            String blobPath = BLOB.createBlob(readMe);
            System.out.println("Starting cycle #" + i);
            if (!verifyBlobInitialization(blobPath)) {
                System.out.println("Test failed on cycle #" + i);
                return;
            } else {
                System.out.println("Blob initialization verified for cycle #" + i);
            }
            cleanup();
            System.out.println("Cleanup done for cycle # " + i);
        }
        System.out.println("All 3 cycles done!");

    }

    public void testIndexUpdate() {

        GitRepositoryInitializer.initGitRepo();

        File f1 = new File("ex1");
        try {
            f1.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        File f2 = new File("ex2");
        try {
            f2.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("ex1"))) {
            bufferedWriter.write("hello world!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("ex2"))) {
            bufferedWriter.write("!dlrow olleh");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File index = new File("git/index");
        File blob1 = new File(BLOB.createBlob(f1));
        File blob2 = new File(BLOB.createBlob(f2));
        if (verifyIndexUpdate(f1, blob1, index)) {
            System.out.println("update 1 successful!");
        } else {
            System.out.println("update 1 unsuccessful");
        }
        if (verifyIndexUpdate(f2, blob2, index)) {
            System.out.println("update 2 successful!");
        } else {
            System.out.println("update 2 unsuccessful");
        }
        f1.delete();
        f2.delete();
        cleanup();

    }

    public static void resetRepo() {
        File git = new File("git");
        File objects = new File(git, "objects");
        if (objects.exists() && objects.isDirectory()) {
            for (File f : objects.listFiles()) {
                rmrf(f);
            }
        }
        File index = new File(git, "index");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(index, false))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
