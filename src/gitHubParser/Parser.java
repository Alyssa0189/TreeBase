package gitHubParser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class Parser {

	static List<RepositoryCommit> commitList;
	private static JSONObject mainJSON;
	private static JSONArray finalarray;
	private static JSONObject finalJSON;
	private static String owner;
	private static String name;

	public static void main(String[] args) {
		new Parser();
		owner = args[0];
		name = args[1];
	}

	public Parser() {

		final ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Runnable() {
			public void run() {
				GitHubClient client = new GitHubClient();

				client.setOAuth2Token("aae877bfdb473ffedc5010372138d29c82efba75");
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

			}
		});
		
		executor.shutdown();

	}

	private static void getRepo(GitHubClient client) throws IOException {
		RepositoryService service = new RepositoryService();
		CommitService serv1 = new CommitService();

		System.out.println("You selected the " + name + " repository.");

		Repository repo = service.getRepository(owner, name);
		System.out.println("Repo Name: " + repo.getName());
		System.out.println("Repo Link: " + repo.getHtmlUrl());
		System.out.println("Logged in User: " + client.getUser());

		// List of commits on a repository, in reverse order that is commit at
		// list.get(0) is the most recent
		commitList = serv1.getCommits(repo);
	}

	private static JSONObject jsonBuilder(List<RepositoryCommit> list) {
		mainJSON = new JSONObject();
		finalJSON = new JSONObject();
		finalarray = new JSONArray();

		// grab all the commits
		for (int i = 0; i < list.size(); i++) {
			JSONObject commitsingle = new JSONObject();

			commitsingle.put("commitNumber", Integer.toString(i));
			commitsingle.put("author", list.get(i).getCommit().getAuthor().getName());
			commitsingle.put("commitTime", list.get(i).getCommit().getCommitter().getDate());

			JSONArray commitarray = new JSONArray();
			commitarray.put(commitsingle);

			// sorted JSONarray version
			finalarray.put(commitarray);

			// Unsorted JSONobject version
			mainJSON.put("commitNumber" + Integer.toString(i), commitarray);
		}

		// If you wanted an XML file
		String xml = XML.toString(mainJSON);

		// Example of the JSON being produced
		// System.out.println(mainJSON.toString(1));
		// Unsortable JSONobject version ^

		// Sorted JSONArray(nested within one JSONobject) version
		finalJSON.put("JSONarray", finalarray);

		return finalJSON;
	}

	private void writeToFile() throws IOException {
		FileWriter file = new FileWriter("src/gitHubParser/output/jsonastxt.txt");
		try {
			file.write(finalJSON.toString(1));
			System.out.println("Successfully turned JSON array into text file.");

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			file.flush();
			file.close();
		}
	}

}
