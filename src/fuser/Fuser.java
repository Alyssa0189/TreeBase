package fuser;

import gitHubParser.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Fuser {
	static ArrayList<String> violationList = new ArrayList<String>();
	static ArrayList<String> authorNameList = new ArrayList<String>();
	static ArrayList<Integer> commitNumberList = new ArrayList<Integer>();
	static ArrayList<String> commits = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		Parser p = new Parser();

		File codeQualityFolder = new File("src/codequality");
		File numberOfCommitsFolder = new File("src/numberofcommits");
		File[] listOfCodeQualityFiles = codeQualityFolder.listFiles();
		File[] listOfCommitFiles = numberOfCommitsFolder.listFiles();

		// reads all code quality input files and creates violation number list
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
		System.out.println(violationList); // test for comparing correct value
											// for violation is read from the
											// file

		// reads number of commit files, makes list of number of commits
		for (File file : listOfCommitFiles) {
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String line = read.nextLine();
				if (line.contains("author")) {
					int left = line.indexOf(":");
					int right = line.indexOf(",");

					String authorName = line.substring(left + 2, right - 1);
					authorNameList.add(authorName);
					System.out.println(authorName); // tests if the Author's
													// name is substringged
													// correctly from the file
				}
			}

		}

		for (int i = 0; i < authorNameList.size(); i++) {
			commitNumberList.add(i + 1);
		}
		System.out.println(commitNumberList); // tests if correct input from
												// file is read and added to
												// commit numberlist

		convertToString (commitNumberList);
		writeToFile(violationList, commits);
			
	}

	// writes code quality number and number of commits into seperate output
	// files
	public static ArrayList<Integer> writeToFile(ArrayList<String> listOfViolations, 
			ArrayList<String> commitNumberList2) throws Exception {
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
		
		for (String a : commitNumberList2) {
			write2.write(a);
			write2.newLine();
			write2.flush();
		}

		return null;
	}
	
	//Converts Integer List to String List
	public static ArrayList<String> convertToString(ArrayList<Integer> someIntList) {
		for (Integer i : someIntList) { 
				commits.add(String.valueOf(i)); 
				}
			return commits;
			
			
	}
}
