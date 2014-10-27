package gitHubParser;


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

import com.google.gson.JsonArray;


public class Parser {
	
static List<RepositoryCommit> commitList;


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
		}});
	
}


public static void getRepo(GitHubClient client) throws IOException{
	RepositoryService service = new RepositoryService();
	CommitService serv1 = new CommitService();
	
	Repository repo = service.getRepository("square", "dagger");
	System.out.println(repo.getName());
	System.out.println(repo.getHtmlUrl());
	System.out.println(client.getUser());
	
	
	//List of Commits on a Repository, in reverse order that is commit at list.get(0) is the most recent
	commitList = serv1.getCommits(repo);
	
	//Proof that the commits are held in the list, by displaying the most recent authors
//	for (int i = 0; i < 6; i++){
//		System.out.println(x.get(i).getCommit().getAuthor().getName());
//		System.out.println(x.get(i).getCommit().getCommitter().getDate());
//	}
	
}

public static JSONObject jsonBuilder(List<RepositoryCommit> list){
	
	JSONObject mainJSON = new JSONObject();
	
	for(int i = 0; i < 5; i++){
		JSONObject commitsingle = new JSONObject();
		
		//returning null pointer right now not sure why
		//commitsingle.put("totalChanges", list.get(i).getStats().getTotal());
		
		commitsingle.put("author", list.get(i).getCommit().getAuthor().getName());
		commitsingle.put("commitTime", list.get(i).getCommit().getCommitter().getDate());

		JSONArray commitarray = new JSONArray();
		commitarray.put(commitsingle);
	
	
		mainJSON.put("commitNumber" + Integer.toString(i), commitarray);
	}
	
	//Example of the JSON being produced
	System.out.println(mainJSON.toString());
	return mainJSON;
}

}
