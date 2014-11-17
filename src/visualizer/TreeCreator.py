# Contains methods responsible for creating a single tree in the current scene,
# based on commit number and code quality parameters.
#
# Author: Alyssa Lerner
# Date: 10/26/14
# Version 1.0
#

import bpy

minTreeHeight = 4.0
maxTreeHeight = 10.0
contribCeiling = 4.0   # This number of contributions or higher will result in the tallest tree.

greenestTreeColor = (0.0, 0.588, 0.0)
deadestTreeColor = (0.41176, 0.22353, 0.02353)
badQualityCeiling = 200.0    # Code of quality this bad or greater will result in the deadest tree.


# Set the command to evaluate tree creation.  The tree's 'height' should go between these commands.

commandStrBeforeHeight = "bpy.ops.curve.tree_add(do_update=True, chooseSet='0', bevel=True, prune=False, \
showLeaves=False, useArm=False, seed=0, handleType='1', levels=2, length=(1, 0.3, 0.6, 0.45), \
lengthV=(0, 0, 0, 0), branches=(0, 50, 30, 10), curveRes=(3, 5, 3, 1), curve=(0, -40, -40, 0), \
curveV=(20, 50, 75, 0), curveBack=(0, 0, 0, 0), baseSplits=0, segSplits=(0, 0, 0, 0), splitAngle=(0, 0, 0, 0), splitAngleV=(0, 0, 0, 0), \
scale="

commandStrAfterHeight = ", scaleV=3, attractUp=0.5, shape='7', baseSize=0.4, ratio=0.015, taper=(1, 1, 1, 1), ratioPower=1.2, downAngle=(90, 60, 45, 45), \
downAngleV=(0, -50, 10, 10), rotate=(140, 140, 140, 77), rotateV=(0, 0, 0, 0), scale0=1, scaleV0=0.2, pruneWidth=0.4, pruneWidthPeak=0.6, \
prunePowerHigh=0.5, prunePowerLow=0.001, pruneRatio=1, leaves=25, leafScale=0.17, leafScaleX=1, leafDist='4', bend=0, bevelRes=0, resU=4, \
frameRate=1, windSpeed=2, windGust=0, armAnim=False, startCurv=0)"


# Create a tree in the current scene according to the given parameters.
#
# Param contribs: The contribution info, where a lower number creates a shorter tree.
# Param quality: The code quality info, where a lower number creates a more lively tree.
# 
def createTree(contribs, quality):
	treeHeight = ((maxTreeHeight - minTreeHeight) / contribCeiling) 
	treeHeight *= float(contribs)
	treeHeight += minTreeHeight

	treeColor = __getColor__(quality)
	__makeTree__(treeHeight, treeColor)
    

# Make a tree in the current scene with a given height and color.
# 
# Param height: A float indicating the tree's height.
# Param color: A 3-tuple indicating the tree's rgb color.
# 
def __makeTree__(height, color):
    
    # Create the tree mesh.
    eval(commandStrBeforeHeight + str(height) + commandStrAfterHeight)
    
    tree = bpy.data.objects[2]
    tree.active_material = makeTreeMaterial(color)


# Make the tree's material.
#
# Param color: A 3-tuple indicating the tree's rgb color values.
# 
def makeTreeMaterial(color):
    # http://blenderartists.org/forum/showthread.php?198080-How-to-color-a-object-using-python
    treeMaterial = bpy.data.materials.new("PKHG")
    
    treeMaterial.diffuse_color = color
    treeMaterial.specular_color = color
    
    return treeMaterial

# Get the color of a tree corresponding to a particular code quality.
# 
# Param: quality - int indicating the code's quality
# Return: A tuple of the (r,g,b) values for the color.
# 
def __getColor__(quality):
	rgb = [0,1,2]

	if float(quality) >= badQualityCeiling:
		rgb = list(deadestTreeColor)
	else:
		# Set color to appropriate value based on quality.
		for i in rgb:
			colorIncrement = (deadestTreeColor[i] - greenestTreeColor[i]) / badQualityCeiling
			rgb[i] = (colorIncrement * float(quality)) + greenestTreeColor[i]

	return (rgb[0],rgb[1],rgb[2])




