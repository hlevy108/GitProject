import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GitRepositoryInitializer {
    public static void main(String[] args) {
        initGitRepo();

    }

    public static void initGitRepo() {
        boolean created = false;
        File git = new File("git");
        if (!git.exists()) {
            git.mkdir();
            created = true;
        } 
        File objects = new File(git,"objects");
        if (!objects.exists()) {
            objects.mkdir();
            created = true;
        } 
        File index = new File(git,"index");
        if (!index.exists()) {
            try {
                index.createNewFile();
                created = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        File HEAD = new File(git,"HEAD");
        if (!HEAD.exists()) {
            try {
                HEAD.createNewFile();
                created = true;
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        if (created) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }


    }
}
