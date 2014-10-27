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

	public static void main(String[] args) throws Exception {
		Parser p = new Parser();

		File codeQualityFolder = new File("src/codequality");
		File numberOfCommitsFolder = new File("src/numberofcommits");
		File[] listOfCodeQualityFiles = codeQualityFolder.listFiles();
		File[] listOfCommitFiles = numberOfCommitsFolder.listFiles();
		
		//reads all code quality input files and creates violation number list
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
		
		//reads number of commit files, make list of number of commits
		for (File file : listOfCommitFiles) {
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String line = read.nextLine();
				if (line.contains("author")) {
					int left = line.indexOf(":");
					int right = line.indexOf(",");

					String authorName = line.substring(left + 2, right - 1);
					System.out.println(authorName);
					authorNameList.add(authorName); // add for each file
					
					}
				}
			}
		for (int i = 0; i<authorNameList.size(); i++){
			commitNumberList.add(i+1);
			System.out.println(commitNumberList);
			
		}
		
		ArrayList<Integer> writeToFile = writeToFile(violationList,
				commitNumberList);
		}
		
		
	
	//writes code quality number and number of commits into seperate output files
	public static ArrayList<Integer> writeToFile(
			ArrayList<String> listOfViolations,
			ArrayList<Integer> commitNumberList2) throws Exception {
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
		for (Integer a : commitNumberList2) {
			//System.out.println(commitNumberList);
			write2.write(a);
			write2.newLine();
			write2.flush();
		}

		return null;
	}
}
