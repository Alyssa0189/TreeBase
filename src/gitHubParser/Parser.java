package gitHubParser;


import java.io.FileWriter;
import java.io.IOException;
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

public Parser(){
	
	Executor executor = Executors.newSingleThreadExecutor();
	executor.execute(new Runnable() { public void run() {
		GitHubClient client = new GitHubClient();
		client.setCredentials("410project", "project410");
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


public static void getRepo(GitHubClient client) throws IOException{
	RepositoryService service = new RepositoryService();
	CommitService serv1 = new CommitService();
	
	Repository repo = service.getRepository("square", "dagger");
	System.out.println("Repo Name: " + repo.getName());
	System.out.println("Repo Link: " + repo.getHtmlUrl());
	System.out.println("Logged in User: " + client.getUser());
	
	
	//List of Commits on a Repository, in reverse order that is commit at list.get(0) is the most recent
	commitList = serv1.getCommits(repo);
	
//Proof that the commits are held in the list, by displaying the most recent authors
	for (int i = 0; i < 6; i++){
		System.out.println("Author: " + commitList.get(i).getCommit().getAuthor().getName());
		System.out.println("Date of Commit: " + commitList.get(i).getCommit().getCommitter().getDate());
	}
	
}

public static JSONObject jsonBuilder(List<RepositoryCommit> list){
	
		mainJSON = new JSONObject();
	
	//Set to only grab the 25 most recent commits for testing purposes
	for(int i = 0; i < 25; i++){
		JSONObject commitsingle = new JSONObject();
		
		//returning null pointer right now not sure why
		//commitsingle.put("totalChanges", list.get(i).getStats().getTotal());
		
		commitsingle.put("author", list.get(i).getCommit().getAuthor().getName());
		commitsingle.put("commitTime", list.get(i).getCommit().getCommitter().getDate());

		JSONArray commitarray = new JSONArray();
		commitarray.put(commitsingle);
	
	
		mainJSON.put("commitNumber" + Integer.toString(i), commitarray);
	}
	
	//If you wanted an XML file
	String xml = XML.toString(mainJSON);
	
	//Example of the JSON being produced
	System.out.println(mainJSON.toString(1));
	
	return mainJSON;
}

public void writeToFile() throws IOException{
	
    FileWriter file = new FileWriter("src/gitHubParser/jsonastxt.txt");
    try {
        file.write(mainJSON.toString(1));
        System.out.println("Successfully turned JSON into text file.");
        System.out.println("\nJSON Object: " + mainJSON.toString(1));

    } catch (IOException e) {
        e.printStackTrace();

    } finally {
        file.flush();
        file.close();
    }
    
	}

}

