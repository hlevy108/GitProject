import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
* Milestone GP-4.1 Identify and Use the Root Tree
* 1) Find the root tree object in git/objects
* 2) Recursively trace referenced trees and blobs
* 3) Verify every referenced hash exists in git/objects
* 4) For blobs, verify the index contains "blob <hash> <path>"
* 5) Print clear messages and return overall success or failure
*/
public class TraceTree {


   public static String findRootTreeHash() {
       File objects = new File("git/objects");
       if (!objects.exists()) {
           return null;
       }
  
       List<String> topNames = listTopLevelNames();
       File[] candidates = objects.listFiles();
       if (candidates == null) {
           return null;
       }
  
       int i = 0;
       while (i < candidates.length) {
           File file = candidates[i];
           if (file.isFile()) {
               String content = BLOB.getFileContents(file);
               if (content == null) {
                   content = "";
               }
               content = content.trim();
               if (!content.isEmpty()) {
                   List<String> namesInTree = parseNames(content);
                   if (namesInTree.size() > 0) {
                       boolean looksLikeRoot = matchesTopLevel(namesInTree, topNames);
                       if (looksLikeRoot) {
                           String hash = file.getName();
                           boolean verified = verifyTreeRecursive(hash, "");
                           if (verified) {
                               return hash;
                           }
                       }
                   }
               }
           }
           i = i + 1;
       }
       return null;
   }
  


   /**
    * Recursively verifies a tree entries and confirms consistency with the index
    * The hash of a tree object stored in git/objects
    * The path prefix relative to the repo root, empty string for the root
    * True if all entries verify correctly, false otherwise
    */
   public static boolean verifyTreeRecursive(String treeHash, String basePath) {
       File treeFile = objectPath(treeHash);
       if (!treeFile.exists()) {
           System.out.println("Missing TREE object: " + treeHash);
           return false;
       }


       String content = BLOB.getFileContents(treeFile);
       if (content == null) {
           content = "";
       }
       content = content.trim();
       if (content.isEmpty()) {
           System.out.println("Empty TREE object: " + treeHash);
           return false;
       }


       boolean allGood = true;
       String[] lines = content.split("\n");
       int i = 0;
       while (i < lines.length) {
           String line = lines[i];
           String[] parts = line.split(" ", 3);
           if (parts.length < 3) {
               System.out.println("Malformed tree entry in " + treeHash + ": '" + line + "'");
               allGood = false;
           }
           else {
               String type = parts[0];
               String hash = parts[1];
               String name = parts[2];


               String childRel;
               if (basePath.isEmpty()) {
                   childRel = name;
               }
               else {
                   childRel = basePath + "/" + name;
               }


               File obj = objectPath(hash);
               if (!obj.exists()) {
                   System.out.println("Missing object '" + hash + "' for entry: " + type + " " + name + " under " + treeHash);
                   allGood = false;
               }
               else {
                   if (type.equals("blob")) {
                       File index = new File("git/index");
                       boolean inIndex = TREE.verifyIndexUpdate(childRel, new File(hash), index);
                       if (!inIndex) {
                           System.out.println("INDEX mismatch. Expected line for blob '" + hash + " " + childRel + "'");
                           allGood = false;
                       }
                   }
                   else if (type.equals("tree")) {
                       boolean subtreeOK = verifyTreeRecursive(hash, childRel);
                       if (!subtreeOK) {
                           allGood = false;
                       }
                   }
                   else {
                       System.out.println("Unknown entry type in tree: '" + type + "' for name '" + name + "'");
                       allGood = false;
                   }
               }
           }
           i = i + 1;
       }
       return allGood;
   }




   // This method creates a list of all file and folder names that are located in the top level
   // of the current working directory (the same place where the program is running).
   // It looks through every item in the folder, skipping the "git" folder since that is part of the repository system.
   // For each remaining item, it checks if the name is already in the list.
   // If the name is not already included, it adds it to the list.
   // When finished, it returns a list containing the unique names of all top-level files and folders.
   private static List<String> listTopLevelNames() {
       List<String> names = new ArrayList<String>();
       File[] list = new File(".").listFiles();
       if (list == null) {
           return names;
       }
       int i = 0;
       while (i < list.length) {
           File f = list[i];
           String n = f.getName();
           if (!n.equals("git")) {
               if (!containsInList(names, n)) {
                   names.add(n);
               }
           }
           i = i + 1;
       }
       return names;
   }


   // This method reads the text of a tree file and collects all of the file and folder names listed inside it.
   // Each line in the tree file describes one entry in the format: "type hash name".
   // The method splits the tree text into lines and then splits each line into its parts.
   // It takes the third part (the name) and adds it to a list, but only if that name is not already in the list.
   // When it finishes reading all lines, it returns the list of unique names found in the tree file.
   private static List<String> parseNames(String treeContent) {
       List<String> names = new ArrayList<String>();
       String[] lines = treeContent.split("\n");
       int i = 0;
       while (i < lines.length) {
           String[] parts = lines[i].split(" ", 3);
           if (parts.length >= 3) {
               String name = parts[2];
               if (!containsInList(names, name)) {
                   names.add(name);
               }
           }
           i = i + 1;
       }
       return names;
   }


   // This method checks if every name listed in treeNames also appears in topNames.
   // It goes through each name in the treeNames list one by one.
   // For each name, it calls containsInList to see if that name exists in the topNames list.
   // If any name from treeNames is missing in topNames, the method returns false right away.
   // If all names are found, it returns true at the end.
   private static boolean matchesTopLevel(List<String> treeNames, List<String> topNames) {
       int i = 0;
       while (i < treeNames.size()) {
           String name = treeNames.get(i);
           boolean found = containsInList(topNames, name);
           if (!found) {
               return false;
           }
           i = i + 1;
       }
       return true;
   }


   // This method checks if a given value is already inside a list of strings.
   // It goes through each item in the list one by one.
   // If it finds an item that is the same as the given value, it returns true.
   // If it finishes checking every item and does not find a match, it returns false.
   private static boolean containsInList(List<String> list, String value) {
       int i = 0;
       while (i < list.size()) {
           if (list.get(i).equals(value)) {
               return true;
           }
           i = i + 1;
       }
       return false;
   }


   private static File objectPath(String hash) {
       return new File("git/objects/" + hash);
   }
}
