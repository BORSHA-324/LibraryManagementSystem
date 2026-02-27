
package models;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
    
    
    public enum BookCategory {
        FICTION("Fiction"),
        SCIENCE("Science"),
        HISTORY("History"),
        TECHNOLOGY("Technology"),
        BIOGRAPHY("Biography");
        
        private final String displayName;
        
        BookCategory(String displayName) 
        {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() 
        {
            return displayName;
        }
    }
    
    // Book attributes
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private BookCategory category;
    private Date publishedDate;
    private int availableCopies;
    
    // Default constructor
    public Book() 
    {
        
    }
    
    // Parameterized constructor
    public Book(String bookId, String title, String author, String isbn, BookCategory category, Date publishedDate, int availableCopies) 
    {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.publishedDate = publishedDate;
        this.availableCopies = availableCopies;
    }
    
    // Getters
    public String getBookId() 
    { 
        return bookId; 
    }
    public String getTitle() 
    { 
        return title; 
    }
    public String getAuthor() 
    { 
        return author; 
    }
    public String getIsbn() 
    { 
        return isbn; 
    }
    public BookCategory getCategory() 
    { 
        return category; 
    }
    public Date getPublishedDate() 
    { 
        return publishedDate; 
    }
    public int getAvailableCopies() 
    { 
        return availableCopies; 
    }
    
    // Setters
    public void setBookId(String bookId) 
    { 
        this.bookId = bookId; 
    }
    public void setTitle(String title) 
    { 
        this.title = title; 
    }
    public void setAuthor(String author) 
    { 
        this.author = author; 
    }
    public void setIsbn(String isbn) 
    { 
        this.isbn = isbn; 
    }
    public void setCategory(BookCategory category) 
    { 
        this.category = category; 
    }
    public void setPublishedDate(Date publishedDate) 
    { 
        this.publishedDate = publishedDate; 
    }
    public void setAvailableCopies(int availableCopies) 
    { 
        this.availableCopies = availableCopies; 
    }
}