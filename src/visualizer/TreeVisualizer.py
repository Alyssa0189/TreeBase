# Retrieves files containing commit info and code quality data and creates a sequence of 
# trees using Blender 2.71, stored as .png files.
#
# Author: Alyssa Lerner
# Date: 10/26/14
# Version: 1.0
#
import bpy
import os
import sys
import imp

commitDataFileName = "numberofcommitsoutput.txt"
qualityDataFileName = "codequalityoutput.txt"

# Update the path for loading modules, returning the path.
# (Otherwise older versions of the file might be used, causing hours of frustration).
# 
def updatePaths():
    currPath = os.getcwd() # https://docs.python.org/2/library/os.html#os-file-dir
    sys.path.append(currPath)

    if sys.path[-1] != currPath:
        sys.path.append(modulePath)
        
    return currPath

currentPath = updatePaths()
import TreeCreator
imp.reload(TreeCreator) # http://www.blender.org/documentation/blender_python_api_2_61_0/info_tips_and_tricks.html

dataPath = (os.sep).join(currentPath.split(os.sep)[0:-1]) + (os.sep) + "fuser"+(os.sep)+"output"+(os.sep)
imagePath = currentPath + (os.sep)+"treeFrames"+(os.sep)

# Retrieve the contribution and quality data from the data files.
# Return: A 2-tuple of contribution and quality information.
# 
def readData():
    contribs = parseFile(commitDataFileName)
    qualities = parseFile(qualityDataFileName)
    return (contribs, qualities)


# Parse a generic file into a list from the data folder.
# Param fileName: The name of the file to parse.
# Return: The list of file elements, with newlines from the file seperating elements.
# 
def parseFile(fileName):
    file = open(dataPath + (os.sep) + fileName, 'r')
    into = []
    
    for line in file:
        into.append(line)
        
    return into


# Render the current scene and save the image.
# 
def saveScene():
    
    # http://stackoverflow.com/questions/14982836/rendering-and-saving-images-through-blender-python
    bpy.data.scenes['Scene'].render.filepath = imagePath + "/tree" + str(i)
    bpy.ops.render.render( write_still=True ) 


# Undergo the entire creation and deletion of a single frame. 
# ie. Create a tree, render it, save the image and delete it.
# Param contribs: The contribution parameter to affect the tree's height.
# Param quality: The code quality to affect the tree's liveliness.
# 
def makeFrame(contribs, quality):
	TreeCreator.createTree(contribs, quality)
	saveScene()

	bpy.data.objects[2].select = True # Select the leaves
	bpy.data.objects[3].select = True # Select the tree

	bpy.ops.object.delete(use_global=False) # Delete tree


data = readData()
contribs = data[0]
qualities = data[1]

# Make each frame in turn.
for i in range(0, len(contribs)):
    makeFrame(contribs[i], qualities[i])


