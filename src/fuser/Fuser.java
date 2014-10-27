package fuser;

import gitHubParser.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class fuser {
	static ArrayList<String> violationList = new ArrayList<String>();
	static ArrayList<String> numberOfCommitList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		Parser p = new Parser();
		
		File codeQualityFolder = new File("src/codequality");
		File numberOfCommitsFolder = new File("src/numberofcommits");
		File[] listOfCodeQualityFiles = codeQualityFolder.listFiles();
		File[] listOfCommitFiles = numberOfCommitsFolder.listFiles();
		for (File file : listOfCodeQualityFiles) {
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String line = read.nextLine();
				if (line.contains("<violationscount>")) {
					// reference:http://codingbat.com/doc/java-string-indexof-parsing.html
					int left = line.indexOf(">");
					int right = line.indexOf("/");

					String violationNumber = line
							.substring(left + 1, right - 1);
					violationList.add(violationNumber); // add for each file
				}

			}

		}

		for (File file : listOfCommitFiles) {
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String line = read.nextLine();
				if (line.contains("number of commits")) {
					int left = line.indexOf(":");
					int right = line.indexOf(",");

					String commitNumber = line.substring(left + 3, right - 1);
					numberOfCommitList.add(commitNumber); // add for each file
				}
			}

		}
		ArrayList<Integer> writeToFile = writeToFile(violationList,
				numberOfCommitList);
	}

	public static ArrayList<Integer> writeToFile(
			ArrayList<String> listOfViolations, ArrayList<String> commitList2)
			throws Exception {
		FileWriter writeCqFile = new FileWriter(
				"src/codequality/codequalityoutput.txt");
		FileWriter writeCommitNumFile = new FileWriter(
				"src/numberofcommits/numberofcommitsoutput.txt");

		BufferedWriter write1 = new BufferedWriter(writeCqFile);
		BufferedWriter write2 = new BufferedWriter(writeCommitNumFile);

		for (String s : listOfViolations) {
			write1.write(s, 0, listOfViolations.size() + 1);
			write1.newLine();
			write1.flush();

		}
		for (String a : commitList2) {
			write2.write(a, 0, commitList2.size());
			write2.newLine();
			write2.flush();
		}

		return null;
	}
}
