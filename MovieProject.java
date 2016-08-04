package movieProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.*;

public class MovieProject{

	private int numMovies;
	private String[] userDefinedMovies;
	public static String[] movieJSONData;    // parallel
	private double[] movieRatings;  // arrays
	private String[] movieNames;
	private Scanner myScanner;
	private JFreeChart objChart;

	MovieProject(){
		numMovies = 0;
		myScanner = new Scanner(System.in);
	}


	public MovieProject(int numMovies){
		setNumMovies(numMovies);
		myScanner = new Scanner(System.in);
	}


	public void setNumMovies(int numMovies){
		if(numMovies >= 1 && numMovies <= 10)
			this.numMovies = numMovies;
		else
			System.out.println("Number of movies must be >= 1 and <= 10.");
	}


	public int getNumMovies(){
		return numMovies;
	}


	public void requestNumMovies(){
		System.out.println("Enter how many movies you'd like to compare (1-10): ");
		numMovies = myScanner.nextInt();
		userDefinedMovies = new String[numMovies];
		movieJSONData = new String[numMovies];
		movieRatings = new double[numMovies];
		movieNames = new String[numMovies];
	}


	public void requestMovieNames(){
		String movieName;
		
		System.out.println("Please enter the name of the movie: ");
		movieName = myScanner.nextLine();
		movieName = myScanner.nextLine();
		movieName = movieName.replace(" ", "+");
		userDefinedMovies[0] = movieName;
		
		for(int i = 1; i < numMovies; i++){
			System.out.println("Please enter the name of the movie: ");
			movieName = myScanner.nextLine();
			movieName = movieName.replaceAll(" ", "+");
			userDefinedMovies[i] = movieName;
		}
	}


	public void getUserMovieJSONData(){
		
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
			}
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		}
		
		
	}

	public void generateChart(){
		DefaultCategoryDataset objDataset = new DefaultCategoryDataset();
		for(int i = 0; i < userDefinedMovies.length; i++){
			objDataset.addValue(movieRatings[i], "IMDB Score", movieNames[i]);
		}
		
		objChart = ChartFactory.createBarChart(
			    "IMDB Movie Ratings Comparison",     //Chart title
			    "Movie Name",     //Domain axis label
			    "IMDB Score",         //Range axis label
			    objDataset,         //Chart Data 
			    PlotOrientation.VERTICAL, // orientation
			    false,             // include legend?
			    true,             // include tooltips?
			    false             // include URLs?
			);
		
		ChartFrame frame = new ChartFrame("IMDB Movie Ratings Comparison", objChart);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void parseData(){
		JSONObject movieInformation;
		for(int i = 0; i < movieJSONData.length; i++){
			try{
				movieInformation = new JSONObject(movieJSONData[i]);
				movieRatings[i] = movieInformation.getDouble("imdbRating");
				movieNames[i] = movieInformation.getString("Title");
			}
			catch(Exception e){
				System.out.println("Error parsing JSON data.");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		MovieProject newProject = new MovieProject();
		newProject.requestNumMovies();
		newProject.requestMovieNames();
		newProject.getUserMovieJSONData();
		newProject.parseData();
		newProject.generateChart();
		
	}
}