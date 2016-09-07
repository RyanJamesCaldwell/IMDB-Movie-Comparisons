package movieProject;

public class MovieLinkedList {
	
	private int length;
	private MovieLLNode head;
	private MovieLLNode tail;
	
	public MovieLinkedList(){
		head = new MovieLLNode("head", -1);
		tail = new MovieLLNode("tail", -1);
		setHeadTail();
		length = 0;
	}
	
	public void setHeadTail(){
		this.head.setNext(tail);
		this.tail.setPrevious(head);
		this.head.setPrevious(null);
		this.tail.setNext(null);
	}
	
	public void addMovie(MovieLLNode movie){
		
		movie.setPrevious(head);
		movie.setNext(head.getNext());
		head.setNext(movie);
		movie.getNext().setPrevious(movie);
		
		length++;
	}
	
	
	public void printLinkedList(){
		MovieLLNode current = head;
		
		for(int i = 0; i < length+2; i++){
			System.out.println("Title: " + current.getMovieTitle() + ": " + current.getMovieRating());
			current = current.getNext();
		}
		System.out.println();
	}
	
	public static void main(String[] args){
		MovieLinkedList hi = new MovieLinkedList();
		MovieLLNode what = new MovieLLNode("test1", 10);
		hi.addMovie(what);
		MovieLLNode next = new MovieLLNode("test2", 8);
		hi.addMovie(next);
		hi.printLinkedList();
	}

	public MovieLLNode getFirst() {
		return this.head.getNext();
	}
	
}
