Run with the following command:
-------------------------------
```
./launch.sh <git repository URL>
```

Links to code bases:
-----------------------
[https://github.com/square/dagger](https://github.com/square/dagger): a fast dependency injector for Android and Java with 629 commits and 28 contributors  
[https://github.com/ubc-cs310/CreativeTeamName](https://github.com/ubc-cs310/CreativeTeamName): a CPSC 310 project with 115 commits and 4 contributors  

Testing procedures:
----------------------
**launch script:**  
* unit test: cloning the code base
    * Input: a code base's git repository URL
    * Output: a copy of the repository in the 'codebase' folder
    * Test: compare repository files on disk against repository files on git
* unit test: getting the list of all commit hashes of a repo
    * Input: a code base from a git repository for which you know the commit hashes
    * Output: a list of hashes for each commit of a repo (latest commit hash at the top)
    * Test: diff against the expected hashes; check the size of the list to ensure that it matches the total number of commits
* unit test: checking out a particular commit of the code base
    * Input: a code base's commit hash
    * Output: a copy of the repository at that commit in the 'codebase' folder
    * Test: compare repository files on disk against repository files on git for that commit
* integration test: calling Ant to run JCSC (code quality tool) once
    * Input: a directory containing a code base
    * Output: a set of XML files containing the analysis results in the 'jcsc-output/(commit hash)' folder
    * Test: run JSCS on the code base manually and compare the XML files produced
* integration test: calling Ant to run JCSC on all commits
    * Input: a directory containing a code base
    * Output: same as above, except there is a 'jcsc-output/(commit hash)' folder for *each* commit
    * Test: count the number of folders in the 'jcsc-output' folder to ensure that it matches the total number of commits; run JSCS on some commits of the code base manually and compare the XML files produced
* integration test: running the commit info tool
    * Input: n/a [note: commit info tool analyzes a hardcoded code base for now, so can't specify another code base]
    * Output: 'jsonastxt.txt' in the 'src/gitHubParser' directory
    * Test: confirm that the text file exists; inspect the text file to confirm that it parses correctly given the hardcoded code base (see tests below for more detailed git commit tool tests)
* integration test: running the fuser
    * Input: n/a [note: fuser reads mock input for now, so can't input an actual code base yet]
    * Output: 'codequalityoutput.txt' in the 'src/codequality' directory; 'numberofcommitsoutput.txt' in the 'src/numberofcommits' directory
    * Test: confirm that the text files exist; inspect the text files to confirm that it parses correctly given the mock inputs (see tests below for more detailed fuser tests)
* integration test: running the visualization tool
    * Input: n/a [note: visualizer reads mock input for now, so can't input an actual code base yet]
    * Output: a set of tree png images in the 'src/visualizer/treeFrames' directory
    * Test: confirm that the trees in the images have the correct height and 'liveliness' given the mock inputs
	
**git commit tool:**  
* (visual tests, manual)
    * Input: a GitHub-hosted repository
    * Output: a text file containing JSON-formatted information for all commits in the 'src/gitHubParser/' directory
    * Tests:
        * confirm that each element in the file contains a commit number, author, and commit time
        * confirm that the number of commits in the file matches the number of commits in the repository
        * compare values for author and time in the file to those in the repository (e.g., look at the most recent commits on the repository - on GitHub itself for the hardcoded 'square/dagger' repo - and confirm that they have the same authors as those in the file)
        * confirm that commits in the file starting at commitNumber0 refer to the latest commit in the repository
	
**fuser:**  
* blah
    * Input:
    * Output:
    * Test:
* blah
    * Input:
    * Output:
    * Test:
	
**visualizer:**  
* blah
    * Input:
    * Output:
    * Test:
* blah
    * Input:
    * Output:
    * Test:
