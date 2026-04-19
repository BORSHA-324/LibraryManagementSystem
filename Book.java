package models;

import java.io.Serializable;
import java.util.Date;
import models.memento.BookMemento;

public class Book implements Serializable {

    public enum BookCategory {
        FICTION("Fiction"),
        SCIENCE("Science"),
        HISTORY("History"),
        TECHNOLOGY("Technology"),
        BIOGRAPHY("Biography");

        private final String displayName;

        BookCategory(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

   
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private BookCategory category;
    private Date publishedDate;
    private int availableCopies;

  
    public Book() {
    }

 
    public Book(String bookId, String title, String author, String isbn,
                BookCategory category, Date publishedDate, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.publishedDate = publishedDate;
        this.availableCopies = availableCopies;
    }

    
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookCategory getCategory() {
        return category;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

   
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

  
    public BookMemento save() {
        return new BookMemento(
                bookId,
                title,
                author,
                isbn,
                category,
                publishedDate,
                availableCopies
        );
    }

    
    public void restore(BookMemento memento) {
        this.bookId = memento.getBookId();
        this.title = memento.getTitle();
        this.author = memento.getAuthor();
        this.isbn = memento.getIsbn();
        this.category = memento.getCategory();
        this.publishedDate = memento.getPublishedDate();
        this.availableCopies = memento.getAvailableCopies();
    }
}