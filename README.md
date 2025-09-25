# Honors Topics
Git Project
    - initialize repository using GitRepositoryInitializer.java
        - The method used to initialize is is called GitRepositoryInitializer.initGitRepo()
            1. It checks if each file/directory exists by using the .exists() method
            2. If the file/directory doesn't exist, it's initialized using createNewFile() or mkdir()
            3. I used a boolean called "created" and set it automatically as false. If something new is created, it flips to true. If nothing new is created, then it stays false and the method prints that the git repo already exists.
        - had to handle IO exceptions when making index & HEAD files.
        - to verify that it worked (before making tester) I ran the code twice. As expected, the first time it printed that the repo was created, and the second time it printed that the repo already exists
    - create blob file using BLOB.createBlob(File file)
        1. get file contents using BLOB.getFileContents()
            - uses buffered reader to read contents and returns them as a String
        2. hash using SHA1.encryptThisString(String input)
            - taken from (https://www.geeksforgeeks.org/java/sha-1-hash-in-java/)
        3. initialize blob file in git objects folder
        4. copy file contents into blob using BLOB.copyToBlob(String fileContents, File newFile)
            - uses bufferedwriter
        5. returns pathname of blob   
    - index file is updated every time blob is created using BLOB.updateIndexFile(File file, File index)
        - writes hash of blob and its old file name into the index folder
    - all tests in GitRepoTester.java
        - testRepoInit()
            1. initializes git repo using GitRepositoryInitializer.initGitRepo()
            2. verifies initialization using verifyRepoInitialization()
            3. deletes repo using cleanup("git")
            4. repeats 2 more times
         - testBlobInit()
            1. initializes git repo
            2. creates blob file
            3. verifies blob creation using verifyBlobInitialization()
            4. repeats 2 more times
        - testIndexUpdate()
            1. make test files
            2. make blobs of the test files
            3. make sure that the index entry is correct (hash + " " + fileName)