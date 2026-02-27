
package models;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerDateModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookForm extends JFrame {

    
    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);   
    private static final Color TEAL_LIGHT = new Color(26, 188, 156);      
    private static final Color TEAL_TEXT = new Color(77, 215, 198);       
    private static final Color TEAL_DARK = new Color(17, 128, 106);       

    private List<Book> bookList = new ArrayList<>();
    private final String FILE_PATH = "data/books.dat";

    private JTextField txtBookId, txtTitle, txtAuthor, txtIsbn, txtCopies;
    
    private JComboBox<Book.BookCategory> cmbCategory;
    
    private JSpinner dateSpinner;
    
    private JTable bookTable;
    
    private DefaultTableModel tableModel;
    
    private int selectedRow = -1;

    public BookForm() 
    {
        loadFromFile();
        setTitle("Book Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        refreshTable();
    }

    private void initComponents() 
    {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.BLACK);

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
                "Book Information",
                0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book ID
        gbc.gridx = 0; gbc.gridy = 0;
        
        JLabel lblBookId = new JLabel("Book ID:");
        
        lblBookId.setFont(new Font("Arial", Font.BOLD, 13));
        
        lblBookId.setForeground(Color.WHITE);
        
        panel.add(lblBookId, gbc);
        
        gbc.gridx = 1;
        
        txtBookId = createDarkTextField(15);
        
        panel.add(txtBookId, gbc);

        // Title
        gbc.gridx = 2;
        
        JLabel lblTitle = new JLabel("Title:");
        
        lblTitle.setFont(new Font("Arial", Font.BOLD, 13));
        
        lblTitle.setForeground(Color.WHITE);
        
        panel.add(lblTitle, gbc);
        
        gbc.gridx = 3;
        
        txtTitle = createDarkTextField(20);
        
        panel.add(txtTitle, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setFont(new Font("Arial", Font.BOLD, 13));
        lblAuthor.setForeground(Color.WHITE);
        panel.add(lblAuthor, gbc);
        gbc.gridx = 1;
        txtAuthor = createDarkTextField(15);
        panel.add(txtAuthor, gbc);

        // ISBN
        gbc.gridx = 2;
        JLabel lblIsbn = new JLabel("ISBN:");
        lblIsbn.setFont(new Font("Arial", Font.BOLD, 13));
        lblIsbn.setForeground(Color.WHITE);
        panel.add(lblIsbn, gbc);
        gbc.gridx = 3;
        txtIsbn = createDarkTextField(20);
        panel.add(txtIsbn, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setFont(new Font("Arial", Font.BOLD, 13));
        lblCategory.setForeground(Color.WHITE);
        panel.add(lblCategory, gbc);
        gbc.gridx = 1;
        cmbCategory = createDarkComboBox(Book.BookCategory.values());
        panel.add(cmbCategory, gbc);

        // Published Date
        gbc.gridx = 2;
        JLabel lblDate = new JLabel("Published Date:");
        lblDate.setFont(new Font("Arial", Font.BOLD, 13));
        lblDate.setForeground(Color.WHITE);
        panel.add(lblDate, gbc);
        gbc.gridx = 3;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        dateSpinner.setFont(new Font("Arial", Font.PLAIN, 13));
        JTextField spinnerField = ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField();
        spinnerField.setBackground(new Color(50, 50, 50));
        spinnerField.setForeground(Color.WHITE);
        spinnerField.setCaretColor(Color.WHITE);
        spinnerField.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        panel.add(dateSpinner, gbc);

        // Available Copies
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblCopies = new JLabel("Available Copies:");
        lblCopies.setFont(new Font("Arial", Font.BOLD, 13));
        lblCopies.setForeground(Color.WHITE);
        panel.add(lblCopies, gbc);
        gbc.gridx = 1;
        txtCopies = createDarkTextField(15);
        panel.add(txtCopies, gbc);

        return panel;
    }

    private JTextField createDarkTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        return field;
    }

    private JComboBox<Book.BookCategory> createDarkComboBox(Book.BookCategory[] items) {
        JComboBox<Book.BookCategory> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        
        combo.setRenderer(new ListCellRenderer<Book.BookCategory>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            
            @Override
            public Component getListCellRendererComponent(JList<? extends Book.BookCategory> list,
                    Book.BookCategory value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                
                label.setFont(new Font("Arial", Font.PLAIN, 13));
                
                if (isSelected) {
                    label.setBackground(TEAL_PRIMARY);
                    label.setForeground(Color.WHITE);
                } else if (index == -1) {
                    label.setBackground(new Color(50, 50, 50));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(60, 60, 60));
                    label.setForeground(Color.WHITE);
                }
                
                label.setOpaque(true);
                return label;
            }
        });
        
        return combo;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
                "Book List",
                0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        String[] columns = {"Book ID", "Title", "Author", "ISBN",
                "Category", "Published Date", "Copies"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { 
                return false; 
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookTable.setForeground(Color.WHITE);
        bookTable.setBackground(new Color(40, 40, 40));
        bookTable.setGridColor(new Color(70, 70, 70));
        bookTable.setSelectionBackground(TEAL_PRIMARY);
        bookTable.setSelectionForeground(Color.WHITE);
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = bookTable.getTableHeader();
        header.setDefaultRenderer(new TableCellRenderer() {
            private final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) renderer.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                label.setFont(new Font("Arial", Font.BOLD, 16));
                label.setBackground(TEAL_PRIMARY);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 1, TEAL_DARK),
                    BorderFactory.createEmptyBorder(8, 5, 8, 5)
                ));
                
                return label;
            }
        });
        
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(40, 40, 40));
        centerRenderer.setForeground(Color.WHITE);
        
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        bookTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        bookTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) loadBookToForm(selectedRow);
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(1100, 350));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        panel.add(scrollPane, BorderLayout.CENTER);

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

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { 
                btn.setBackground(color.brighter()); 
            }
            @Override
            public void mouseExited(MouseEvent evt) { 
                btn.setBackground(color); 
            }
        });

        return btn;
    }

    private void addBook() {
        if (!validateFields()) return;

        for (Book book : bookList) {
            if (book.getBookId().equals(txtBookId.getText().trim())) {
                JOptionPane.showMessageDialog(this,
                        "Book ID already exists!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Book book = new Book(
                txtBookId.getText().trim(),
                txtTitle.getText().trim(),
                txtAuthor.getText().trim(),
                txtIsbn.getText().trim(),
                (Book.BookCategory) cmbCategory.getSelectedItem(),
                (Date) dateSpinner.getValue(),
                Integer.parseInt(txtCopies.getText().trim())
        );

        bookList.add(book);
        saveToFile();
        refreshTable();
        clearForm();

        JOptionPane.showMessageDialog(this,
                "Book added successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateBook() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book to update!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!validateFields()) return;

        Book book = bookList.get(selectedRow);
        book.setTitle(txtTitle.getText().trim());
        book.setAuthor(txtAuthor.getText().trim());
        book.setIsbn(txtIsbn.getText().trim());
        book.setCategory((Book.BookCategory) cmbCategory.getSelectedItem());
        book.setPublishedDate((Date) dateSpinner.getValue());
        book.setAvailableCopies(Integer.parseInt(txtCopies.getText().trim()));

        saveToFile();
        refreshTable();
        clearForm();
        
        JOptionPane.showMessageDialog(this,
                "Book updated successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBook() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book to delete!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this book?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            bookList.remove(selectedRow);
            saveToFile();
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this,
                    "Book deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        txtBookId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtIsbn.setText("");
        txtCopies.setText("");
        cmbCategory.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
        selectedRow = -1;
        bookTable.clearSelection();
        txtBookId.setEditable(true);
    }

    private void loadBookToForm(int row) {
        Book book = bookList.get(row);
        txtBookId.setText(book.getBookId());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtIsbn.setText(book.getIsbn());
        cmbCategory.setSelectedItem(book.getCategory());
        dateSpinner.setValue(book.getPublishedDate());
        txtCopies.setText(String.valueOf(book.getAvailableCopies()));
        txtBookId.setEditable(false);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Book book : bookList) {
            Object[] row = {
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    sdf.format(book.getPublishedDate()),
                    book.getAvailableCopies()
            };
            tableModel.addRow(row);
        }
    }

    private boolean validateFields() {
        if (txtBookId.getText().trim().isEmpty() ||
                txtTitle.getText().trim().isEmpty() ||
                txtAuthor.getText().trim().isEmpty() ||
                txtIsbn.getText().trim().isEmpty() ||
                txtCopies.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtCopies.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Copies must be a number!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveToFile() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(bookList);
            oos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            bookList = new ArrayList<>();
            return;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            bookList = (List<Book>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            bookList = new ArrayList<>();
        }
    }
}
