#!/bin/bash
OWNER=$1
NAME=$2

# clone the project
git clone https://github.com/${OWNER}/${NAME}.git codebase

# get the hash for each commit
cd codebase
HASHES=`git log --pretty=format:'%h'`

for hash in $HASHES; do
	git checkout $hash		# checkout each commit
	cd ..
	ant -Doutput=$hash		# run jcsc for each commit
	cd codebase
done
cd ..

# run the git commit info tool
javac -d bin -cp "lib/gson-2.3.jar;lib/json-20140107.jar;lib/org.eclipse.egit.github.core-2.1.5.jar;lib/org.eclipse.egit.github.core-2.1.5-javadoc (1).jar;lib/org.eclipse.egit.github.core-2.1.5-sources.jar" src/gitHubParser/Parser.java
java -cp "bin;lib/gson-2.3.jar;lib/json-20140107.jar;lib/org.eclipse.egit.github.core-2.1.5.jar;lib/org.eclipse.egit.github.core-2.1.5-javadoc (1).jar;lib/org.eclipse.egit.github.core-2.1.5-sources.jar" gitHubParser.Parser $OWNER $NAME

# run the fuser
javac -d bin -cp "lib/json-simple-1.1.1.jar" src/fuser/Fuser.java
java -cp "bin;lib/json-simple-1.1.1.jar" fuser.Fuser

# run the visualization tool
cd src/visualizer
blender treeVisualizer.blend --background --python TreeVisualizer.py

cd treeFrames
ffmpeg -framerate 25 -i tree%d.png -c:v libx264 -vf fps=25 -pix_fmt yuv420p TreeVideo.mp4
