package models;

import java.io.Serializable;
import java.util.Date;
import models.memento.BorrowingMemento;

public class Borrowing implements Serializable {

   
    public enum BorrowingStatus {
        BORROWED("Borrowed"),
        RETURNED("Returned"),
        OVERDUE("Overdue");

        private final String displayName;

        BorrowingStatus(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    
    private String borrowingId;
    private String bookId;
    private String memberId;
    private Date borrowDate;
    private Date dueDate;
    private Date returnDate;
    private BorrowingStatus status;

    
    public Borrowing() {
    }
    public Borrowing(String borrowingId, String bookId, String memberId,
                     Date borrowDate, Date dueDate,
                     Date returnDate, BorrowingStatus status) {
        this.borrowingId = borrowingId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getBorrowingId() {
        return borrowingId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    public void setBorrowingId(String borrowingId) {
        this.borrowingId = borrowingId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }

 
    public BorrowingMemento save() {
        return new BorrowingMemento(
                borrowingId,
                bookId,
                memberId,
                borrowDate,
                dueDate,
                returnDate,
                status
        );
    }

    public void restore(BorrowingMemento memento) {
        this.borrowingId = memento.getBorrowingId();
        this.bookId = memento.getBookId();
        this.memberId = memento.getMemberId();
        this.borrowDate = memento.getBorrowDate();
        this.dueDate = memento.getDueDate();
        this.returnDate = memento.getReturnDate();
        this.status = memento.getStatus();
    }
}
