import java.io.Serializable;
public class Book implements Serializable {
	private String title;
	private String author;
	private double price ;
	private String isbn;
	private String genre ;
	private int year;
	
	public String getTitle() {
		return this.title;
	}
	public String getAuthor() {
		return this.author;
	}
	public double getPrice() {
		return this.price;
	}
	public String getISBN() {
		return this.isbn;
	}
	public String getGenre() {
		return this.genre;
	}
	public int getYear() {
		return this.year;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setISBN(String isbn) {
		this.isbn = isbn;
	}
	public void setGenre(String genre ) {
		this.genre = genre;
	}
	public void  setYear(int year) {
		this.year = year;
	}
	public Book(String title, String author , double price , String isbn , String genre , int year) {
		this.title = title ;
		this.author = author;
		this.price = price;
		this.isbn = isbn;
		this.genre = genre;
		this.year = year;
	}
	public Book(Book bk) {
		this.title = bk.getTitle();
		this.author = bk.getAuthor();
		this.price = bk.getPrice();
		this.isbn = bk.getISBN();
		this.genre = bk.getGenre();
		this.year = bk.getYear();
	}
	@Override 
	public String toString( ) {
		return "Book Details:\n" +
		           "Title: " + title + "\n" +
		           "Author: " + author + "\n" +
		           "Price: $" + price + "\n" +
		           "ISBN: " + isbn + "\n" +
		           "Genre: " + genre + "\n" +
		           "Year: " + year;
	}
	public boolean equals (Book bk) {
		return (this.title.equals(bk.getTitle())) && (this.author.equals(bk.getAuthor())) 
				&& (this.price == bk.getPrice()) && (this.isbn.equals(bk.getISBN())) && 
				(this.genre.equals(bk.getGenre())) && (this.year == bk.getYear()) ;
	}
	
}