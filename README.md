## Run with the following command:
```
./launch.sh <repository URL>
```  

## Links to code bases:
[https://github.com/square/dagger](https://github.com/square/dagger): a fast dependency injector for Android and Java with 629 commits and 28 contributors  
[https://github.com/ubc-cs310/CreativeTeamName](https://github.com/ubc-cs310/CreativeTeamName): a CPSC 310 project with 115 commits and 4 contributors  

## Testing procedures:

## Visualization test:
##
## (Integration test, manual):
##	Input: 	Dummy commit and code quality data files.
##	Output: A sequence of images.
##	Test A: 	Edit TreeVisualizer.py so that commitDataFileName = "/visualizer/tests/mockCommitData0.txt"
##			Edit TreeVisualizer.py so that qualityDataFileName = "/visualizer/tests/mockQualityData0.txt"
##			Enter "blender treeVisualizer.blend --background --python TreeVisualizer.py" in the terminal.
##			Check the folder /src/visualizer/treeFrames to ensure that:
##				- There are 6 ordered .png images of trees.
##				- Each tree is taller than the last.
##				- Each tree is less green than the one before it.
##
##	Test B:		Edit file as above except with "mockCommitData1.txt" and "mockQualityData1.txt".
##			Check the folder /src/visualizer/treeFrames to ensure that:
##				- There are 8 ordered. png images of trees.
##				- Each tree is shorter than the last.
##				- Each tree is greener than the last.
## 			Remove the images from /treeframes/
##
##	Test C:		Edit file as above except with "mockCommitData2.txt" and "mockQualityData1.txt".
##			Check the folder /src/visualizer/treeFrames to ensure that:
##				- There are 6 ordered .png images of trees.
##				- The second tree is greener than the first.
##				- Each tree is one of two colors, which alternate.
##

