package movieProject;

public class MovieLLNode {
	
	private String movieTitle;
	private double movieRating;
	private MovieLLNode nextMovie;
	private MovieLLNode previousMovie;
	
	public MovieLLNode(String movieTitle){
		this.movieTitle = movieTitle;
		this.movieRating = 0;
	}
	
	public MovieLLNode(String movieTitle, double movieRating){
		setMovieTitle(movieTitle);
		setMovieRating(movieRating);
		this.nextMovie = null;
		this.previousMovie = null;
	}
	
	public void setNext(MovieLLNode nextMovie){
		this.nextMovie = nextMovie;
	}
	
	public MovieLLNode getNext(){
		return this.nextMovie;
	}
	
	public void setPrevious(MovieLLNode previousMovie){
		this.previousMovie = previousMovie;
	}
	
	public MovieLLNode getPrevious(){
		return this.previousMovie;
	}
	
	public void setMovieTitle(String movieTitle){
		this.movieTitle = movieTitle;
	}
	
	public String getMovieTitle(){
		return movieTitle;
	}
	
	public void setMovieRating(double movieRating){
		this.movieRating = movieRating;
	}
	
	public double getMovieRating(){
		return this.movieRating;
	}
}
