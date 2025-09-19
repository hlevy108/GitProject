import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Git {
    public static void main(String[] args) {
        

    }

    public void initGit() {
        File git = new File("git");
        if (!git.exists()) {
            git.mkdir();
        } else {
            System.out.println("Git Repository Already Exists");
        }
        // File git = new File("git");
        // if (!git.exists()) {
        //     git.mkdir();
        // } else {
        //     System.out.println("Git Repository Already Exists");
        // }
        // File git = new File("git");
        // if (!git.exists()) {
        //     git.mkdir();
        // } else {
        //     System.out.println("Git Repository Already Exists");
        // }

        //index and objects
    }
}
