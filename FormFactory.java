import javax.swing.JFrame;


public class FormFactory {

    public enum FormType {
        BOOK,
        MEMBER,
        BORROWING
    }

    
    public static JFrame createForm(FormType type) {
        switch (type) {
            case BOOK:
                return new BookForm();
            case MEMBER:
                return new MemberForm();
            case BORROWING:
                return new BorrowingForm();
            default:
                throw new IllegalArgumentException("Unknown form type: " + type);
        }
    }
}
