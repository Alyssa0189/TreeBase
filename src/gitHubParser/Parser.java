package gitHubParser;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


public class Parser {
	
static List<RepositoryCommit> commitList;
private static JSONObject mainJSON;
private static JSONArray finalarray;
private static JSONObject finalJSON;
private static String repoCode;
private static String owner;
private static String name;


public static void main(String[] args) {
	Parser p = new Parser();
}

public Parser(){

	Executor executor = Executors.newSingleThreadExecutor();
	executor.execute(new Runnable() { public void run() {
		GitHubClient client = new GitHubClient();
		
		//Still trying to figure out authentication
		client.setOAuth2Token("aae877bfdb473ffedc5010372138d29c82efba75");
		client.setCredentials("410project", "project410");
		
		userRepoInput();

		try {
			getRepo(client);
		} catch (IOException e) {
			e.printStackTrace();
		}

		jsonBuilder(commitList);

		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}});
	
}

private static void userRepoInput(){
	boolean x = false;
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
    System.out.print("Please select a Repository Code(1 for dagger, 2 for 310[Won't work right now]): ");
    
    repoCode = null;
    try {
        repoCode = reader.readLine();
    } catch (IOException e) {
        e.printStackTrace();
    } 
  
    if(repoCode != null){
    	if(repoCode.equals("1") || repoCode.equals("dagger")){
    		owner = "square";
    		name = "dagger";
    		x = true;
    	}
    	
    	// This won't work because this repository is private to me. 
    	if(repoCode.equals("2") || repoCode.equals("310")){
    		owner = "ubc-cs310";
    		name = "creativeteamname";
    		x = false;
    	}
    }
    
    if(x == false){
		System.out.println("Inappropriate repository code.");
		userRepoInput();
	}
}


private static void getRepo(GitHubClient client) throws IOException{
	RepositoryService service = new RepositoryService();
	CommitService serv1 = new CommitService();
	
    System.out.println("You Selected the " + name + " repository.");
    
	Repository repo = service.getRepository(owner, name);
	System.out.println("Repo Name: " + repo.getName());
	System.out.println("Repo Link: " + repo.getHtmlUrl());
	System.out.println("Logged in User: " + client.getUser());
	
	
	//List of Commits on a Repository, in reverse order that is commit at list.get(0) is the most recent
	commitList = serv1.getCommits(repo);
	
//Proof that the commits are held in the list, by displaying the most recent authors
	for (int i = 0; i < 10; i++){
		System.out.println("Author: " + commitList.get(i).getCommit().getAuthor().getName());
		System.out.println("Date of Commit: " + commitList.get(i).getCommit().getCommitter().getDate());
	}
	
}

private static JSONObject jsonBuilder(List<RepositoryCommit> list){
		mainJSON = new JSONObject();
		finalJSON = new JSONObject();
		finalarray = new JSONArray();
	
	//Set to only grab the 10 most recent commits for testing purposes
	for(int i = 0; i < 10; i++){
		JSONObject commitsingle = new JSONObject();
		
		commitsingle.put("commitNumber", Integer.toString(i));
		commitsingle.put("author", list.get(i).getCommit().getAuthor().getName());
		commitsingle.put("commitTime", list.get(i).getCommit().getCommitter().getDate());

		JSONArray commitarray = new JSONArray();
		commitarray.put(commitsingle);
		
		
		//sorted JSONarray version
		finalarray.put(commitarray);
		
		//Unsorted JSONobject version
		mainJSON.put("commitNumber" + Integer.toString(i), commitarray);
	}
	
	
	//If you wanted an XML file
	String xml = XML.toString(mainJSON);
	
	//Example of the JSON being produced
	//System.out.println(mainJSON.toString(1));
	//Unsortable JSONobject version ^
	
	
	//Sorted JSONArray(nested within one JSONobject) version
	finalJSON.put("JSONarray", finalarray);
	System.out.println(finalJSON.toString(1));
	
	
	return finalJSON;
}

private void writeToFile() throws IOException{
	
    FileWriter file = new FileWriter("src/gitHubParser/jsonastxt.txt");
    try {
        file.write(finalJSON.toString(1));
        System.out.println("Successfully turned JSON array into text file.");
        System.out.println("\nJSON Object: " + finalJSON.toString(1));

    } catch (IOException e) {
        e.printStackTrace();

    } finally {
        file.flush();
        file.close();
    }
 }

}

