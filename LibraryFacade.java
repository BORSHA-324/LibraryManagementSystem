package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryFacade {

    private List<Book> books;
    private List<Member> members;
    private List<Borrowing> borrowings;

    private final String BOOK_FILE = "data/books.dat";
    private final String MEMBER_FILE = "data/members.dat";
    private final String BORROW_FILE = "data/borrowings.dat";

    public LibraryFacade() {
        new File("data").mkdirs();
        books = loadListFromFile(BOOK_FILE);
        members = loadListFromFile(MEMBER_FILE);
        borrowings = loadListFromFile(BORROW_FILE);
    }

    public boolean addBorrowing(Borrowing borrowing) {
        for (Borrowing b : borrowings) {
            if (b.getBorrowingId().equals(borrowing.getBorrowingId())) {
                return false; 
            }
        }
        borrowings.add(borrowing);
        saveListToFile(borrowings, BORROW_FILE);
        return true;
    }

    public void updateBorrowing(int index, Borrowing borrowing) {
        borrowings.set(index, borrowing);
        saveListToFile(borrowings, BORROW_FILE);
    }

    public void removeBorrowing(int index) {
        borrowings.remove(index);
        saveListToFile(borrowings, BORROW_FILE);
    }

    public List<Borrowing> getAllBorrowings() {
        return borrowings;
    }

    public boolean addBook(Book book) {
        for (Book b : books) {
            if (b.getBookId().equals(book.getBookId())) return false;
        }
        books.add(book);
        saveListToFile(books, BOOK_FILE);
        return true;
    }

    public void updateBook(int index, Book book) {
        books.set(index, book);
        saveListToFile(books, BOOK_FILE);
    }

    public void removeBook(int index) {
        books.remove(index);
        saveListToFile(books, BOOK_FILE);
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public boolean addMember(Member member) {
        for (Member m : members) {
            if (m.getMemberId().equals(member.getMemberId())) return false;
        }
        members.add(member);
        saveListToFile(members, MEMBER_FILE);
        return true;
    }

    public void updateMember(int index, Member member) {
        members.set(index, member);
        saveListToFile(members, MEMBER_FILE);
    }

    public void removeMember(int index) {
        members.remove(index);
        saveListToFile(members, MEMBER_FILE);
    }

    public List<Member> getAllMembers() {
        return members;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadListFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private <T> void saveListToFile(List<T> list, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
