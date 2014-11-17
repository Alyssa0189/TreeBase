package fuser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Fuser {
	private static final String commitListFilePath = "src/gitHubParser/jsonastxt.txt";
	static ArrayList<String> violationList = new ArrayList<String>();
	static ArrayList<String> authorNameList = new ArrayList<String>();
	static ArrayList<Integer> commitNumberList = new ArrayList<Integer>();
	static ArrayList<String> commits = new ArrayList<String>();
	static String previous;

	public static void main(String[] args) throws Exception {

		File codeQualityFolder = new File("src/codequality");
		File[] listOfCodeQualityFiles = codeQualityFolder.listFiles();

		// read all code quality input files and creates violation number list
		for (File file : listOfCodeQualityFiles) {
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				String line = read.nextLine();
				if (line.contains("<violationscount>")) {
					// reference: http://codingbat.com/doc/java-string-indexof-parsing.html
					int left = line.indexOf(">");
					int right = line.indexOf("/");

					String violationNumber = line.substring(left + 1, right - 1);
					violationList.add(violationNumber); // add for each file
				}
			}
			read.close();
		}

		// parse commit file, get list of all authors
		FileReader reader = new FileReader(commitListFilePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		JSONArray jsonArray = (JSONArray) jsonObject.get("JSONarray");

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONArray commitArray = (JSONArray) jsonArray.get(i);
			JSONObject commitObj = (JSONObject) commitArray.get(0);
			String authorName = commitObj.get("author").toString();
			authorNameList.add(authorName);
		}

		// reverse list of authors so first commit is at the front of the list
		Collections.reverse(authorNameList);

		// get the number of commits up to each commit,
		// discounting those made by the person who committed previously
		int count = 1;
		commitNumberList.add(count);

		for (int i = 1; i < authorNameList.size(); i++) {
			String currentName = authorNameList.get(i);
			String previousName = authorNameList.get(i - 1);
			if (!currentName.equals(previousName)) {
				count++;
			}
			commitNumberList.add(count);
		}

		convertToString(commitNumberList);
		writeToFile(violationList, commits);
	}

	// write number of violations and number of commits into separate output files
	public static ArrayList<Integer> writeToFile(
			ArrayList<String> listOfViolations,
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

		write1.close();
		write2.close();
		return null;
	}

	// convert Integer list to String list
	public static ArrayList<String> convertToString(
			ArrayList<Integer> someIntList) {
		for (Integer i : someIntList) {
			commits.add(String.valueOf(i));
		}
		return commits;
	}

}
