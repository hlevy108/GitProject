# Honors Topics
Git Project
    - initialize repository by running GitRepositoryInitializer.java
        - The method used to initialize is is called initGitRepo()
            1. It checks if each file/directory exists by using the .exists() method
            2. If the file/directory doesn't exist, it's initialized using createNewFile() or mkdir()
            3. I used a boolean called "created" and set it automatically as false. If something new is created, it flips to true. If nothing new is created, then it stays false and the method prints that the git repo already exists.
        - had to handle IO exceptions when making index & HEAD files.
        - to verify that it worked (before making tester) I ran the code twice. As expected, the first time it printed that the repo was created, and the second time it printed that the repo already exists
    - Test using GitRepoTester.java
        - method used: testThreeTimes()
            1. initializes git repo using GitRepositoryInitializer.initGitRepo()
            2. verifies initialization using verifyRepoInitialization()
            3. deletes repo using cleanup()
            4. repeats 2 more times
    - hash using SHA1.encryptThisString()
        - taken from (https://www.geeksforgeeks.org/java/sha-1-hash-in-java/)