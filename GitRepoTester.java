import java.io.File;
import java.io.IOException;

public class GitRepoTester {
    public static void main(String[] args) {
        GitRepoTester tester = new GitRepoTester();
       tester.testThreeTimes();
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

    public  void cleanup() {
        File git = new File("git");
        rmrf(git);
    }

    public void rmrf(File file) {
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

    public void testThreeTimes() {
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
}
