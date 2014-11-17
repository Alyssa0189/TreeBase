package fuser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Fuser {
	private static final String commitListFilePath = "src/gitHubParser/output/jsonastxt.txt";
	static ArrayList<String> violationList = new ArrayList<String>();
	static ArrayList<String> authorNameList = new ArrayList<String>();
	static ArrayList<Integer> commitNumberList = new ArrayList<Integer>();
	static ArrayList<String> commits = new ArrayList<String>();
	static String previous;

	public static void main(String[] args) throws Exception {
		
		// create new folder name filter
		FilenameFilter folderFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (dir.isDirectory()) {
					return true;
				} return false;
			}
		};

		// create new filename filter
		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.equals("overview.xml")) {
					return true;
				} else return false;
			}
		};
		
		// get the folders with code quality output
		File codeQualityFolder = new File("jcsc-output");
		File[] listOfCodeQualityFolders = codeQualityFolder.listFiles(folderFilter);
		
		// sort the folders by date
		// reference: http://stackoverflow.com/questions/203030/best-way-to-list-files-in-java-sorted-by-date-modified
		Arrays.sort(listOfCodeQualityFolders, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			}
		});
		
		// get the files with code quality output
		ArrayList<File> listOfCodeQualityFiles = new ArrayList<File>();
		for (File folder : listOfCodeQualityFolders) {
			listOfCodeQualityFiles.add((folder.listFiles(fileNameFilter)[0]));
		}

		// read all code quality files and create violation number list
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
		
		// reverse list of violations so first commit is at the front of the list
		Collections.reverse(violationList);

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
				"src/fuser/output/codequalityoutput.txt");
		FileWriter writeCommitNumFile = new FileWriter(
				"src/fuser/output/numberofcommitsoutput.txt");

		BufferedWriter write1 = new BufferedWriter(writeCqFile);
		BufferedWriter write2 = new BufferedWriter(writeCommitNumFile);

		for (String s : listOfViolations) {
			write1.write(s);
			write1.newLine();
			write1.flush();
		}
		System.out.println("Finished writing number of violations.");

		for (String a : commitNumberList2) {
			write2.write(a);
			write2.newLine();
			write2.flush();
		}
		System.out.println("Finished writing number of commits.");

		write1.close();
		write2.close();
		return null;
	}

	// convert Integer list to String list
	public static ArrayList<String> convertToString(ArrayList<Integer> someIntList) {
		for (Integer i : someIntList) {
			commits.add(String.valueOf(i));
		}
		return commits;
	}

}
