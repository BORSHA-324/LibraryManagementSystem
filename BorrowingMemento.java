package models.memento;

import java.util.Date;
import models.Borrowing.BorrowingStatus;

public class BorrowingMemento {

    private final String borrowingId;
    private final String bookId;
    private final String memberId;
    private final Date borrowDate;
    private final Date dueDate;
    private final Date returnDate;
    private final BorrowingStatus status;

    public BorrowingMemento(String borrowingId, String bookId, String memberId,
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
}

