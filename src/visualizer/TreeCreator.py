# Contains methods responsible for creating a single tree in the current scene,
# based on commit number and code quality parameters.
#
# Author: Alyssa Lerner
# Version 1.1
#

import bpy
import os

minTreeHeight = 1.0
maxTreeHeight = 12.0
contribCeiling = 350.0   # This number of contributions or higher will result in the tallest tree.

greenestTreeColor = (0.1529412, 0.6588235, 0.0)
deadestTreeColor = (0.5451, 0.270588, 0.0)
badQualityCeiling = 6000.0    # Code of quality this bad or greater will result in the deadest tree.

# Leaf sizes which change with the tree height (since they scale seperately)
minLeafSize = 0.06
maxLeafSize = 0.25

# Vertical attraction of tree dependent on quality
minVertAttraction = -1.2
maxVertAttraction = 0.5


# Set the command which will create a single tree.

commandBeforeHeight = "bpy.ops.curve.tree_add(do_update=True, chooseSet='3', bevel=True, prune=False, showLeaves=True,\
 useArm=False, seed=0, handleType='1', levels=3, length=(1, 0.32, 0.6, 0.45), lengthV=(0, 0, 0, 0), branches=(0, 70, 22, 9),\
 curveRes=(3, 5, 3, 1), curve=(0, -40, -40, 0), curveV=(20, 50, 75, 0), curveBack=(0, 0, 0, 0), baseSplits=0, segSplits=(0,\
 0, 0.03, 0), splitAngle=(0, -0.09, 0, 0), splitAngleV=(0, -0.03, 0, 0), scale="

commandBeforeVAttraction = ", scaleV=3, attractUp="

commandBeforeLeafSize = ", shape='2', baseSize=0.28, ratio=0.02, taper=(1, 1, 1, 1),\
 ratioPower=1.25, downAngle=(90, 59.85, 45, 45), downAngleV=(0, -50, 10, 10), rotate=(140, 140, 140, 77), rotateV=(0.03,\
 0.09, 0, 0), scale0=1, scaleV0=0.2, pruneWidth=0.4, pruneWidthPeak=0.6, prunePowerHigh=0.5, prunePowerLow=0.001,\
 pruneRatio=1, leaves=18, leafScale="

commandEnding = ", leafScaleX=1, leafDist='4', bend=0, bevelRes=1, resU=4, frameRate=1, windSpeed=2, windGust=0,\
 armAnim=False, startCurv=0)"

# Create a tree in the current scene according to the given parameters.
#
# Param contribs: The contribution info, where a lower number creates a shorter tree.
# Param quality: The code quality info, where a lower number creates a more lively tree.
# 
def createTree(contribs, quality):
	# Set tree height to maximum if many contribs
	if float(contribs) >= contribCeiling:
		treeHeight = maxTreeHeight
	
	else:
		# Set tree height based on contribs
		treeHeight = ((maxTreeHeight - minTreeHeight) / contribCeiling) 
		treeHeight *= float(contribs)
		treeHeight += minTreeHeight
	
	treeColor = getColor(quality)
	makeTree(treeHeight, treeColor)
    

# Make a tree in the current scene with a given height and color.
# 
# Param height: A float indicating the tree's height.
# Param color: A 3-tuple indicating the tree's rgb color.
# 
def makeTree(treeHeight, color):
	leafHeight = getLeafSize(treeHeight)
	vAttract = getVerticalAttraction(color)

	# Create the tree mesh.
	eval(	commandBeforeHeight + str(treeHeight) + \
		commandBeforeLeafSize + str(leafHeight) + \
		commandBeforeVAttraction + str(vAttract) + \
		commandEnding)
    
	leaves = bpy.data.objects[2]
	leaves.active_material = makeLeafMaterial(color)

	bark = bpy.data.objects[3]
	bark.active_material = makeBarkMaterial()

# Get the leaf size corresponding to a given tree height.
# 
# Param treeHeight: The height of the tree.
# Return: A rough size to scale the leaf to, larger for a larger tree.
# 
def getLeafSize(treeHeight):
	relativeTreeScale = (treeHeight - minTreeHeight) / (maxTreeHeight - minTreeHeight)
	leafSizeToAdd = relativeTreeScale * (maxLeafSize - minLeafSize)
	leafSize = minLeafSize + leafSizeToAdd
	return leafSize

# Get the vertical attraction corresponding to a given tree color.
# 
# Param color: The tree color.
# Return: The vertical attraction of the tree based on its color.
# 
def getVerticalAttraction(color):
	relativeQualityScale = (color[0] - greenestTreeColor[0]) / (deadestTreeColor[0] - greenestTreeColor[0])
	attractionToAdd = relativeQualityScale * (maxVertAttraction - minVertAttraction)
	vertAttraction = maxVertAttraction - attractionToAdd
	return vertAttraction

# Make the material for the tree leaves.
#
# Param color: A 3-tuple indicating the tree's rgb color values.
# Return: The material to use for the tree leaves.
# 
def makeLeafMaterial(color):
	# http://blenderartists.org/forum/showthread.php?198080-How-to-color-a-object-using-python
	leafMaterial = bpy.data.materials.new("Leaves")
    
	leafMaterial.diffuse_color = color
	leafMaterial.specular_color = color
    
	return leafMaterial

# Make the material for the tree bark.
# 
# Return: The material to use for the tree bark.
# 
def makeBarkMaterial():
	# http://science-o-matics.com/2013/04/how-to-python-scripting-in-blender-2-material-und-textur/?lang=en

	barkMaterial = bpy.data.materials.new("Bark")
	barkMaterialTexture = barkMaterial.texture_slots.add()

	# Get image for bark
	barkImagePath = os.getcwd() + (os.sep) + "barkTexture" + (os.sep) + "bark.jpg"
	barkImage = bpy.data.images.load(barkImagePath)

	# Add bark texture to material
	barkTexture = bpy.data.textures.new("barkTex", type='IMAGE')
	barkTexture.image = barkImage
	
	# Link texture to material
	barkMaterialTexture.texture = barkTexture
	barkMaterialTexture.texture_coords = 'OBJECT'
	barkMaterialTexture.mapping = 'FLAT'

	return barkMaterial

# Get the color of a tree corresponding to a particular code quality.
# 
# Param: quality - int indicating the code's quality
# Return: A tuple of the (r,g,b) values for the color.
# 
def getColor(quality):
	rgb = [0,1,2]

	if float(quality) >= badQualityCeiling:
		rgb = list(deadestTreeColor)
	else:
		# Set color to appropriate value based on quality.
		for i in rgb:
			colorIncrement = (deadestTreeColor[i] - greenestTreeColor[i]) / badQualityCeiling
			rgb[i] = (colorIncrement * float(quality)) + greenestTreeColor[i]

	return (rgb[0],rgb[1],rgb[2])



