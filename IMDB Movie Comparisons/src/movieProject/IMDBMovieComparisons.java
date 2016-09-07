package movieProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

public class IMDBMovieComparisons {
	
	private int numMovies;					//holds the number of movies to be compared 
	private String[] userDefinedMovies;		//holds the names of movies, given by user
	public static String[] movieJSONData;   //holds the JSON data associated with the movies the user has entered
	private MovieLinkedList movieLL;		//linked list holding MovieLLNode objects
	private Scanner myScanner;				//used for user input. Will eventually be replaced by JFrame
	private JFreeChart objChart;			//graph used to compare IMDB movie scores
	final int MAX_NUM_MOVIES = 10;			//maximum number of movies to be compared
	final int MIN_NUM_MOVIES = 1;			//minimum number of movies to be compared
	
	public IMDBMovieComparisons(){
		movieLL = new MovieLinkedList();
		myScanner = new Scanner(System.in);
		setNumMovies(1);
	}
	
	public void setNumMovies(int numMovies){
		if(numMovies >= MIN_NUM_MOVIES && numMovies <= MAX_NUM_MOVIES)
			this.numMovies = numMovies;
		else
			System.out.println("Number of movies must be >= 1 and <= 10.");
	}
	
	public int getNumMovies(){
		return numMovies;
	}
	
	public void requestNumMovies(){
		//request the number of movies to be compared from the user 
		
		System.out.println("Enter how many movies you'd like to compare (1-10): ");
		numMovies = myScanner.nextInt();
		
		if(numMovies > 10){
			System.out.println("Only 10 movies are comparable at one time. Enter 10 movies.");
			setNumMovies(10);
		}
		else if(numMovies <= 0){
			System.out.println("Number of movies must be 1-10. Enter 2 movies.");
			setNumMovies(2);
		}
		
		userDefinedMovies = new String[numMovies];
		movieJSONData = new String[numMovies];
	}
	
	public void requestMovieNames(){
		String movieName;
		
		System.out.println("Please enter the name of movie 1: ");
		movieName = myScanner.nextLine();
		movieName = myScanner.nextLine();		//java.util.Scanner has issues with reading; necessitated extra .nextLine() call
		movieName = movieName.replace(" ", "+");	//replacing spaces with "+" to add to the URL in HTTP GET request
		userDefinedMovies[0] = movieName;
		
		for(int i = 1; i < numMovies; i++){		//prompt user for movie names
			System.out.println("Please enter the name of movie " + (i+1) + ": ");
			movieName = myScanner.nextLine();
			movieName = movieName.replaceAll(" ", "+");
			userDefinedMovies[i] = movieName;
		}
	}
	
	public void getUserMovieJSONData(){
		//HTTP GET request from OMDB
		//receives JSON data, stores in movieJSONData array
		
		System.out.println("Retrieving movie data...");
		for(int i = 0; i < numMovies; i++){
			StringBuilder result = new StringBuilder();
			URL url;
			try {
			      url = new URL("http://www.omdbapi.com/?t=" + userDefinedMovies[i] + "&y=&plot=short&r=json");
			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			      conn.setRequestMethod("GET");
			      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			      String line;
			      while ((line = rd.readLine()) != null) {
			         result.append(line);
			      }
			      rd.close();
			      movieJSONData[i] = result.toString();
			      System.out.println(result.toString());
			}
			 catch (Exception e) {
				// Catch all exceptions when HTTP GET request fails
				System.out.println("Error during HTTP GET request to OMDB");
				e.printStackTrace();
			 }
		}
		System.out.println("Done.");
	}
	
	public void parseData(){
		//parses the JSON data and stores the movie ratings and names in each respective array
		JSONObject movieInformation;
		MovieLLNode newMovie;
		
		for(int i = 0; i < movieJSONData.length; i++){
			try{
				movieInformation = new JSONObject(movieJSONData[i]);
				newMovie = new MovieLLNode(movieInformation.getString("Title"), movieInformation.getDouble("imdbRating"));
				movieLL.addMovie(newMovie);
				//movieRatings[i] = movieInformation.getDouble("imdbRating");
				//movieNames[i] = movieInformation.getString("Title");
			}
			catch(Exception e){
				//catch all errors when parsing JSON data
				System.out.println("Error parsing JSON data.");
				e.printStackTrace();
			}
		}
	}
	
	public void generateChart(){
		//generates, populates, and displays the JFreeChart with movie data
		
		int width = 1000;				//Width of the image
	    int height = 600; 				//Height of the image
	    MovieLLNode current;			//current MovieLLNode
		current = movieLL.getFirst();	//points to first MovieLLNode
		
	    System.out.println("Generating bar chart...");
	    
		DefaultCategoryDataset objDataset = new DefaultCategoryDataset();
		for(int i = 0; i < userDefinedMovies.length; i++){			//add movie ratings and names to chart dataset
			objDataset.addValue(current.getMovieRating(), "IMDB Score", current.getMovieTitle());
			current = current.getNext();
		}
		
		objChart = ChartFactory.createBarChart(
			    "IMDB Movie Ratings Comparison",     //Chart title
			    "Movie Name",     //Domain axis label
			    "IMDB Score",         //Range axis label
			    objDataset,         //Chart Data 
			    PlotOrientation.VERTICAL, // orientation
			    false,             // include legend?
			    false,             // include tooltips?
			    false             // include URLs?
		);

		//save the bar chart .jpg in the current working directory
	    File barChart = new File(numMovies + "-Movie-Comparison"+ Long.toString(System.currentTimeMillis()) + ".jpg"); 
		try {
			ChartUtilities.saveChartAsJPEG( barChart , objChart , width , height );
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error saving chart as .jpg");
		}
		
		System.out.println("Bar chart saved in current working directory.");
	}
	
	public static void main(String[] args){
		IMDBMovieComparisons newProject = new IMDBMovieComparisons();
		newProject.requestNumMovies();
		newProject.requestMovieNames();
		newProject.getUserMovieJSONData();
		newProject.parseData();
		newProject.generateChart();
	}
}
