package models;

public class LibraryConfig {

    private static LibraryConfig instance = null;

    private final String BOOKS_FILE_PATH      = "data/books.dat";
    private final String MEMBERS_FILE_PATH    = "data/members.dat";
    private final String BORROWINGS_FILE_PATH = "data/borrowings.dat";
    private final String DATA_FOLDER          = "data";

    private LibraryConfig() 
    {
        new java.io.File(DATA_FOLDER).mkdirs();
    }

    public static synchronized LibraryConfig getInstance() 
    {
        if (instance == null) 
        {
            instance = new LibraryConfig();
        }
        return instance;
    }

    public String getBooksFilePath() 
    {
        return BOOKS_FILE_PATH;
    }

    public String getMembersFilePath() 
    {
        return MEMBERS_FILE_PATH;
    }

    public String getBorrowingsFilePath() 
    {
        return BORROWINGS_FILE_PATH;
    }

    public String getDataFolder() 
    {
        return DATA_FOLDER;
    }
}