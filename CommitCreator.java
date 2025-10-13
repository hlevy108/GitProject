import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
* Follows Milestone GP-4.2 instructions
* 1. Ensure repo is initialized and files are staged
* 2. Call TREE.createROOT to snapshot the working directory into tree objects
* 3. Call CommitCreator.createCommit with author and message
*
* Commit file layout
* tree: <root tree sha1>
* parent: <previous commit sha1>  (omit this line for the first commit)
* author: <author name>
* date: <date string>
* message: <summary>
*/
public class CommitCreator {


   public static String createCommit(String author, String message) {
       ensureRepo();


       // Build a fresh root tree from the current working directory
       TREE.createROOT();


       // Find the root tree hash
       String rootHash = TraceTree.findRootTreeHash();
       if (rootHash == null || rootHash.isEmpty()) {
           System.out.println("No root tree found. Run staging and TREE.createROOT first");
           return null;
       }


       // Read parent from HEAD if present
       String parentHash = readHead();


       // Build commit text
       StringBuilder sb = new StringBuilder();
       sb.append("tree: ").append(rootHash).append("\n");
       if (parentHash != null && !parentHash.isEmpty()) {
           sb.append("parent: ").append(parentHash).append("\n");
       }
       sb.append("author: ").append(author).append("\n");
       sb.append("date: ").append(nowString()).append("\n");
       sb.append("message: ").append(message).append("\n");
       String commitText = sb.toString();


       // Hash of the whole commit content
       String commitHash = SHA1.encryptThisString(commitText);


       // Save commit object file
       File commitFile = new File("git/objects/" + commitHash);
       writeString(commitFile, commitText);


       // Update HEAD to point at this commit
       writeString(new File("git/HEAD"), commitHash);


       System.out.println("Commit created: " + commitHash);
       return commitHash;
   }


   private static void ensureRepo() {
       File git = new File("git");
       if (!git.exists()) {
           GitRepositoryInitializer.initGitRepo();
       }
       File objects = new File("git/objects");
       if (!objects.exists()) {
           objects.mkdir();
       }
       File index = new File("git/index");
       if (!index.exists()) {
           try {
               index.createNewFile();
           } catch (IOException e) {
               System.out.println(e);
           }
       }
       File head = new File("git/HEAD");
       if (!head.exists()) {
           try {
               head.createNewFile();
           }
           catch (IOException e) {
               System.out.println(e);
           }
       }
   }


   private static String readHead() {
       File head = new File("git/HEAD");
       if (!head.exists()) {
           return null;
       }
       String text = readString(head);
       if (text == null) {
           return null;
       }
       text = text.trim();
       if (text.isEmpty()) {
           return null;
       }
       return text;
   }


   private static String nowString() {
       LocalDateTime now = LocalDateTime.now();
       DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       return now.format(fmt);
   }


   private static String readString(File file) {
       StringBuilder sb = new StringBuilder();
       try (BufferedReader br = new BufferedReader(new FileReader(file))) {
           String line;
           while ((line = br.readLine()) != null) {
               sb.append(line).append("\n");
           }
       }
       catch (IOException e) {
           return null;
       }
       return sb.toString();
   }


   private static void writeString(File file, String text) {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
           bw.write(text);
       }
       catch (IOException e) {
           System.out.println(e);
       }
   }
}
