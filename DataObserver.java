package models;

public interface DataObserver {
    // This method is called whenever ANY data (Book, Member, or Borrowing) changes
    void onDataChanged();
}