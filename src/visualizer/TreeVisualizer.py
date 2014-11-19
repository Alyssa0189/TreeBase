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

# Files and folders should be located in the same directory as the script.
commitDataFileName = os.sep + "fuser" + os.sep + "output" + os.sep + "numberofcommitsoutput.txt"
qualityDataFileName = os.sep + "fuser" + os.sep + "output" + os.sep + "codequalityoutput.txt"
imagePath = ""
dataPath = ""
modulePath = ""

# Update the path for loading modules, returning the path.
# 
def updatePaths():
    currentPath = os.path.abspath(__file__) # http://stackoverflow.com/questions/3430372/how-to-get-full-path-of-current-directory-in-python
    sys.path.append(currentPath)

    # Add the current path's parent.
    modulePath = os.sep.join(currentPath.split(os.sep)[0:-1])
    if sys.path[-1] != modulePath:
        sys.path.append(modulePath)
        
    return modulePath

modulePath = updatePaths()
import TreeCreator
imp.reload(TreeCreator) # http://www.blender.org/documentation/blender_python_api_2_61_0/info_tips_and_tricks.html

dataPath = os.sep.join(modulePath.split(os.sep)[0:-1])
imagePath = modulePath + os.sep + "treeFrames"

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
    file = open(dataPath + os.sep + fileName, 'r')
    into = []
    
    for line in file:
        into.append(line)
        
    return into


# Render the current scene and save the image.
# 
def saveScene():
    
    # http://stackoverflow.com/questions/14982836/rendering-and-saving-images-through-blender-python
    bpy.data.scenes['Scene'].render.filepath = imagePath + os.sep + "tree" + str(i)
    bpy.ops.render.render( write_still=True ) 


# Make a single frame. 
# ie. Create a tree, render it, save the image and delete it.
# Param contribs: The contribution parameter to affect the tree's height.
# Param quality: The code quality to affect the tree's liveliness.
# 
def makeFrame(contribs, quality):
    TreeCreator.createTree(contribs, quality)
    bpy.data.objects[2].select = True 

    saveScene()

    bpy.ops.object.delete(use_global=False) # Delete tree


data = readData()
contribs = data[0]
qualities = data[1]

# Make each frame in turn.
for i in range(0, len(contribs)):
    makeFrame(contribs[i], qualities[i])


