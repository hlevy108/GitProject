import java.io.*;
import java.nio.file.Files;

public class CommitTester {
    public static void main(String[] args) throws Exception {
        System.out.println("Commit tester start");
        cleanup();
        GitRepositoryInitializer.initGitRepo();

        // Create a small working directory
        File work = new File("work");
        work.mkdir();
        File sub = new File("work/sub");
        sub.mkdir();
        File a = new File("work/a.txt");
        File b = new File("work/sub/b.txt");
        a.createNewFile();
        b.createNewFile();
        Files.write(a.toPath(), "alpha".getBytes());
        Files.write(b.toPath(), "beta".getBytes());

        // Stage and snapshot
        BLOB.addFile("work/a.txt");
        BLOB.addFile("work/sub/b.txt");
        TREE.createROOT();

        // Create first commit
        String c1 = CommitCreator.createCommit("user", "initial snapshot");
        System.out.println("\nFirst commit: " + c1);
        printCommitFile(c1);
        checkCommitFormat(c1, true);

        // Modify a file and create another commit
        Files.write(a.toPath(), "alpha v2".getBytes());
        BLOB.addFile("work/a.txt");
        TREE.createROOT();
        String c2 = CommitCreator.createCommit("user", "updated a.txt");
        System.out.println("\nSecond commit: " + c2);
        printCommitFile(c2);
        checkCommitFormat(c2, false);

        System.out.println("\nHEAD now points to: " + readHead());

        // Clean up working directory and git folder
        deleteRecursively(work);
        cleanup();

        System.out.println("Commit tester end");
    }

    private static void printCommitFile(String commitHash) {
        if (commitHash == null || commitHash.isEmpty()) {
            System.out.println("No commit hash to print.");
            return;
        }
        File commitFile = new File("git/objects/" + commitHash);
        if (!commitFile.exists()) {
            System.out.println("Commit file not found: " + commitHash);
            return;
        }

        System.out.println("----- Commit File Content -----");
        try (BufferedReader br = new BufferedReader(new FileReader(commitFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } 
        catch (IOException e) {
            System.out.println("Error reading commit file: " + e.getMessage());
        }
        System.out.println("----- End of Commit File -----\n");
    }

    private static void checkCommitFormat(String commitHash, boolean firstCommit) {
        if (commitHash == null || commitHash.isEmpty()) {
            System.out.println("No commit hash to verify.");
            return;
        }
        File commitFile = new File("git/objects/" + commitHash);
        if (!commitFile.exists()) {
            System.out.println("Commit file not found: " + commitHash);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(commitFile))) {
            String line;
            int lineNumber = 0;
            boolean hasTree = false;
            boolean hasParent = false;
            boolean hasAuthor = false;
            boolean hasDate = false;
            boolean hasMessage = false;
            boolean inOrder = true;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.startsWith("tree:")) {
                    hasTree = true;
                    if (lineNumber != 1) {
                        inOrder = false;
                    }
                } 
                else if (line.startsWith("parent:")) {
                    hasParent = true;
                    if (firstCommit) {
                        System.out.println("Warning: first commit should not have parent line.");
                    }
                } 
                else if (line.startsWith("author:")) {
                    hasAuthor = true;
                } 
                else if (line.startsWith("date:")) {
                    hasDate = true;
                } 
                else if (line.startsWith("message:")) {
                    hasMessage = true;
                }
            }

            if (!hasTree || !hasAuthor || !hasDate || !hasMessage) {
                System.out.println("Commit format incorrect for: " + commitHash);
                if (!hasTree) {
                    System.out.println("Missing 'tree:' line");
                }
                if (!firstCommit && !hasParent) {
                    System.out.println("Missing 'parent:' line for non-initial commit");
                }
                if (!hasAuthor) {
                    System.out.println("Missing 'author:' line");
                }
                if (!hasDate) {
                    System.out.println("Missing 'date:' line");
                }
                if (!hasMessage) {
                    System.out.println("Missing 'message:' line");
                }
            } 
            else if (!inOrder) {
                System.out.println("Commit has all lines but order may be incorrect.");
            } 
            else {
                System.out.println("Commit file format verified for: " + commitHash);
            }
        } 
        catch (IOException e) {
            System.out.println("Error reading commit file: " + e.getMessage());
        }
    }

    private static void cleanup() {
        File git = new File("git");
        if (git.exists()) {
            deleteRecursively(git);
        }
    }

    private static void deleteRecursively(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] kids = file.listFiles();
            if (kids != null) {
                for (File k : kids) {
                    deleteRecursively(k);
                }
            }
        }
        file.delete();
    }

    private static String readHead() {
        File head = new File("git/HEAD");
        if (!head.exists()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(head))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\\n");
            }
        } 
        catch (IOException e) {
            return "";
        }
        return sb.toString().trim();
    }
}
