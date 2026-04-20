import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BookForm extends JFrame {

    private static final Color TEAL_PRIMARY = new Color(32, 178, 170);
    private static final Color TEAL_TEXT = new Color(175, 238, 238);
    private static final Color TEAL_DARK = new Color(0, 128, 128);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private List<Book> bookList = new ArrayList<>();
    private final String FILE_PATH = "data/books.dat";

    // Strategy Pattern Variable
    private DataOperationStrategy<Book> operationStrategy;

    private JTextField txtBookId, txtTitle, txtAuthor, txtIsbn, txtCopies;
    private JComboBox<Book.BookCategory> cmbCategory;
    private JSpinner dateSpinner;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public BookForm() {
        loadFromFile();
        setTitle("Book Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.BLACK);

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2, true),
                "Book Information", 0, 0, TABLE_HEADER_FONT, TEAL_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        txtBookId = createDarkTextField(15);
        panel.add(txtBookId, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("Title:"), gbc);
        gbc.gridx = 3;
        txtTitle = createDarkTextField(20);
        panel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Author:"), gbc);
        gbc.gridx = 1;
        txtAuthor = createDarkTextField(15);
        panel.add(txtAuthor, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("ISBN:"), gbc);
        gbc.gridx = 3;
        txtIsbn = createDarkTextField(20);
        panel.add(txtIsbn, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createLabel("Category:"), gbc);
        gbc.gridx = 1;
        cmbCategory = createDarkComboBox(Book.BookCategory.values());
        panel.add(cmbCategory, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("Published Date:"), gbc);
        gbc.gridx = 3;
        dateSpinner = createDarkSpinner();
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(createLabel("Available Copies:"), gbc);
        gbc.gridx = 1;
        txtCopies = createDarkTextField(15);
        panel.add(txtCopies, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createDarkTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(MAIN_FONT);
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JComboBox<Book.BookCategory> createDarkComboBox(Book.BookCategory[] items) {
        JComboBox<Book.BookCategory> combo = new JComboBox<>(items);
        combo.setFont(MAIN_FONT);
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        combo.setRenderer(new ListCellRenderer<Book.BookCategory>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            @Override
            public Component getListCellRendererComponent(JList<? extends Book.BookCategory> list, Book.BookCategory value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBackground(isSelected ? TEAL_PRIMARY : new Color(60, 60, 60));
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                return label;
            }
        });
        return combo;
    }

    private JSpinner createDarkSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setFont(MAIN_FONT);
        JTextField editor = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        editor.setBackground(new Color(50, 50, 50));
        editor.setForeground(Color.WHITE);
        editor.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return spinner;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2, true), "Book List", 0, 0, TABLE_HEADER_FONT, TEAL_TEXT));

        String[] columns = { "Book ID", "Title", "Author", "ISBN", "Category", "Published Date", "Copies" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(35);
        bookTable.setBackground(new Color(40, 40, 40));
        bookTable.setForeground(Color.WHITE);
        bookTable.setSelectionBackground(TEAL_PRIMARY);

        JTableHeader header = bookTable.getTableHeader();
        header.setBackground(TEAL_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) loadBookToForm(selectedRow);
            }
        });

        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.BLACK);

        JButton btnAdd = createStyledButton("Add Book", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update Book", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Delete Book", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear Form", new Color(149, 165, 166));

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd); panel.add(btnUpdate); panel.add(btnDelete); panel.add(btnClear);
        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(BOLD_FONT);
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }

    // --- STRATEGY PATTERN IMPLEMENTATION ---

    private void addBook() {
        if (!validateFields()) return;

        for (Book book : bookList) {
            if (book.getBookId().equals(txtBookId.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Book ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        operationStrategy = new AddStrategy<>();
        operationStrategy.execute(getBookFromForm(), bookList, -1);
        finalizeOperation("Book added successfully!");
    }

    private void updateBook() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateFields()) return;

        operationStrategy = new UpdateStrategy<>();
        operationStrategy.execute(getBookFromForm(), bookList, selectedRow);
        finalizeOperation("Book updated successfully!");
    }

    private void deleteBook() {
        if (selectedRow == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bookList.remove(selectedRow);
            finalizeOperation("Book deleted successfully!");
        }
    }

    private void finalizeOperation(String message) {
        saveToFile();
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- HELPERS ---

    private void clearForm() {
        txtBookId.setText(""); txtTitle.setText(""); txtAuthor.setText("");
        txtIsbn.setText(""); txtCopies.setText("");
        cmbCategory.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
        selectedRow = -1;
        bookTable.clearSelection();
        txtBookId.setEditable(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Book b : bookList) {
            tableModel.addRow(new Object[]{
                b.getBookId(), b.getTitle(), b.getAuthor(), b.getIsbn(),
                b.getCategory(), sdf.format(b.getPublishedDate()), b.getAvailableCopies()
            });
        }
    }

    private void loadBookToForm(int row) {
        Book b = bookList.get(row);
        txtBookId.setText(b.getBookId());
        txtTitle.setText(b.getTitle());
        txtAuthor.setText(b.getAuthor());
        txtIsbn.setText(b.getIsbn());
        cmbCategory.setSelectedItem(b.getCategory());
        dateSpinner.setValue(b.getPublishedDate());
        txtCopies.setText(String.valueOf(b.getAvailableCopies()));
        txtBookId.setEditable(false);
    }

    private Book getBookFromForm() {
        return new Book(
            txtBookId.getText().trim(), txtTitle.getText().trim(),
            txtAuthor.getText().trim(), txtIsbn.getText().trim(),
            (Book.BookCategory) cmbCategory.getSelectedItem(),
            (Date) dateSpinner.getValue(),
            Integer.parseInt(txtCopies.getText().trim())
        );
    }

    private boolean validateFields() {
        if (txtBookId.getText().trim().isEmpty() || txtTitle.getText().trim().isEmpty() ||
            txtAuthor.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty() ||
            txtCopies.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtCopies.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Copies must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            new File("data").mkdirs();
            oos.writeObject(bookList);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                bookList = (List<Book>) ois.readObject();
            } catch (Exception e) { bookList = new ArrayList<>(); }
        }
    }
}