Run with the following command:
-------------------------------
```
./launch.sh <git repository URL>
```

Links to code bases:
--------------------
https://github.com/square/dagger - a fast dependency injector for Android and Java with 629 commits and 28 contributors  
https://github.com/ubc-cs310/CreativeTeamName - a CPSC 310 project with 115 commits and 4 contributors  

Testing procedures:
-------------------
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
* (Visual test, Manual)
	*Input: overview1.xml, overview2.xml, overview3.xml (files in codeQualityFolder)
	*Output: violation number from each file in a list format. For example: [2008, 3000, 5788]
	*Test: This will test if correct data is obtained from input file. 
	Compare against the expected value (in the input file) to output printed in the console by outputSystem.out.println(violationList) command.

* (Visual test, Manual)
	*Input: mockCommitDataForFuser1.txt, mockCommitDataForFuser2.txt (files in numberofcommits folder)
	*Output: list of total number of commits from all files. For example: [1, 2, 3, 4, 5, 6]
	*Test: This will test if correct data is obtained from input file. 
	Compare number of commits in mock input file to the output printed in console by System.out.println(commitNumberList) command.
	Note: the code does not yet take consecutive commits by the same author into consideration. This will be added later. 
 
* (Visual test, Manual)
	*Input: Array list of violations and Array list of number of commits
	*Output: “codequalityoutput.txt" and “numberofcommitsoutput.txt" files in “codequality” and “numberofcommits” folder respectively
	*Test: This will test writeToFile method. 
	Compare number of commits and number of violations in the mock input files (overview1.xml, overview2.xml, overview3.xml, mockCommitDataForFuser1.txt and mockCommitDataForFuser2.txt) to the result obtained in the two output files. 
			 
		
**visualizer:**
*  (manual test):
	Input: 	Dummy commit and code quality data files.
	Output: A sequence of images.
	Test A: 	Edit TreeVisualizer.py so that commitDataFileName = "/visualizer/tests/mockCommitData0.txt"
			Edit TreeVisualizer.py so that qualityDataFileName = "/visualizer/tests/mockQualityData0.txt"
			Enter "blender treeVisualizer.blend --background --python TreeVisualizer.py" in the terminal.
			Check the folder /src/visualizer/treeFrames to ensure that:
				- There are 6 ordered .png images of trees.
				- Each tree is taller than the last.
				- Each tree is less green than the one before it.

	Test B:		Edit file as above except with "mockCommitData1.txt" and "mockQualityData1.txt".
			Check the folder /src/visualizer/treeFrames to ensure that:
				- There are 8 ordered. png images of trees.
				- Each tree is shorter than the last.
				- Each tree is greener than the last.
 			Remove the images from /treeframes/

	Test C:		Edit file as above except with "mockCommitData2.txt" and "mockQualityData1.txt".
			Check the folder /src/visualizer/treeFrames to ensure that:
				- There are 6 ordered .png images of trees.
				- The second tree is greener than the first.
				- Each tree is one of two colors, which alternate.
