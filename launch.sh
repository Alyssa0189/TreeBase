#!/bin/bash
CODEBASE=$1

# clone the project
git clone $CODEBASE codebase

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
java -cp "bin;lib/gson-2.3.jar;lib/json-20140107.jar;lib/org.eclipse.egit.github.core-2.1.5.jar;lib/org.eclipse.egit.github.core-2.1.5-javadoc (1).jar;lib/org.eclipse.egit.github.core-2.1.5-sources.jar" gitHubParser.Parser

# run the fuser
javac -d bin src/fuser/fuser.java
java -cp bin fuser.fuser

# run the visualization tool
blender treeVisualizer.blend --background --python TreeVisualizer.py
