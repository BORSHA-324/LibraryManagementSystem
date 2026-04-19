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

    private LibraryFacade facade;   

    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);
    private static final Color TEAL_TEXT = new Color(77, 215, 198);
    private static final Color TEAL_DARK = new Color(17, 128, 106);

    private JTextField txtBookId, txtTitle, txtAuthor, txtIsbn, txtCopies;
    private JComboBox<Book.BookCategory> cmbCategory;
    private JSpinner dateSpinner;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public BookForm(LibraryFacade facade) {
        this.facade = facade;

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
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
                "Book Information",
                0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        txtBookId = createTextField(15);
        panel.add(txtBookId, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("Title:"), gbc);
        gbc.gridx = 3;
        txtTitle = createTextField(20);
        panel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Author:"), gbc);
        gbc.gridx = 1;
        txtAuthor = createTextField(15);
        panel.add(txtAuthor, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("ISBN:"), gbc);
        gbc.gridx = 3;
        txtIsbn = createTextField(20);
        panel.add(txtIsbn, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createLabel("Category:"), gbc);
        gbc.gridx = 1;
        cmbCategory = new JComboBox<>(Book.BookCategory.values());
        panel.add(cmbCategory, gbc);

        gbc.gridx = 2;
        panel.add(createLabel("Published Date:"), gbc);
        gbc.gridx = 3;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(createLabel("Copies:"), gbc);
        gbc.gridx = 1;
        txtCopies = createTextField(10);
        panel.add(txtCopies, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        return lbl;
    }

    private JTextField createTextField(int size) {
        JTextField field = new JTextField(size);
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        return field;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
                "Book List",
                0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        String[] columns = {"ID", "Title", "Author", "ISBN", "Category", "Date", "Copies"};

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        bookTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) loadBookToForm(selectedRow);
            }
        });

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

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

    private void addBook() {
        if (!validateFields()) return;

        Book book = new Book(
                txtBookId.getText().trim(),
                txtTitle.getText().trim(),
                txtAuthor.getText().trim(),
                txtIsbn.getText().trim(),
                (Book.BookCategory) cmbCategory.getSelectedItem(),
                (Date) dateSpinner.getValue(),
                Integer.parseInt(txtCopies.getText().trim())
        );

        facade.addBook(book);   
        refreshTable();
        clearForm();
    }

    private void updateBook() {
        if (selectedRow == -1) return;

        Book book = new Book(
                txtBookId.getText().trim(),
                txtTitle.getText().trim(),
                txtAuthor.getText().trim(),
                txtIsbn.getText().trim(),
                (Book.BookCategory) cmbCategory.getSelectedItem(),
                (Date) dateSpinner.getValue(),
                Integer.parseInt(txtCopies.getText().trim())
        );

        facade.updateBook(selectedRow, book);  
        refreshTable();
        clearForm();
    }

    private void deleteBook() {
        if (selectedRow == -1) return;

        facade.removeBook(selectedRow);
        refreshTable();
        clearForm();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Book book : facade.getAllBooks()) {  
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getCategory(),
                    sdf.format(book.getPublishedDate()),
                    book.getAvailableCopies()
            });
        }
    }

    private void loadBookToForm(int row) {
        Book book = facade.getAllBooks().get(row);
        txtBookId.setText(book.getBookId());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtIsbn.setText(book.getIsbn());
        cmbCategory.setSelectedItem(book.getCategory());
        dateSpinner.setValue(book.getPublishedDate());
        txtCopies.setText(String.valueOf(book.getAvailableCopies()));
        txtBookId.setEditable(false);
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

    private boolean validateFields() {
        if (txtBookId.getText().isEmpty() ||
            txtTitle.getText().isEmpty() ||
            txtAuthor.getText().isEmpty() ||
            txtIsbn.getText().isEmpty() ||
            txtCopies.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return false;
        }

        try {
            Integer.parseInt(txtCopies.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Copies must be number!");
            return false;
        }

        return true;
    }
}
