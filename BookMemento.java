package models.memento;

import java.util.Date;
import models.Book.BookCategory;

public class BookMemento {

    private final String bookId;
    private final String title;
    private final String author;
    private final String isbn;
    private final BookCategory category;
    private final Date publishedDate;
    private final int availableCopies;

    public BookMemento(String bookId, String title, String author,
                       String isbn, BookCategory category,
                       Date publishedDate, int availableCopies) {

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
}
