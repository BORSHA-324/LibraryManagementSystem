import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class BorrowingForm extends JFrame {
    
    private static final Color TEAL_PRIMARY = new Color(32, 178, 170);
    private static final Color TEAL_TEXT = new Color(175, 238, 238);
    private static final Color TEAL_DARK = new Color(0, 128, 128);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    private List<Borrowing> borrowingList = new ArrayList<>();
    private final String FILE_PATH = "data/borrowings.dat";
    
    private DataOperationStrategy<Borrowing> operationStrategy;
    
    private JTextField txtBorrowingId, txtBookId, txtMemberId;
    private JComboBox<Borrowing.BorrowingStatus> cmbStatus;
    private JSpinner borrowDateSpinner, dueDateSpinner, returnDateSpinner;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;
    
    public BorrowingForm() {
        loadFromFile();
        setTitle("Borrowing Management");
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
            "Borrowing Information", 0, 0, TABLE_HEADER_FONT, TEAL_TEXT));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Borrowing ID:"), gbc);
        gbc.gridx = 1;
        txtBorrowingId = createDarkTextField(15);
        panel.add(txtBorrowingId, gbc);
        
        gbc.gridx = 2;
        panel.add(createLabel("Book ID:"), gbc);
        gbc.gridx = 3;
        txtBookId = createDarkTextField(15);
        panel.add(txtBookId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Member ID:"), gbc);
        gbc.gridx = 1;
        txtMemberId = createDarkTextField(15);
        panel.add(txtMemberId, gbc);
        
        gbc.gridx = 2;
        panel.add(createLabel("Status:"), gbc);
        gbc.gridx = 3;
        cmbStatus = createDarkComboBox(Borrowing.BorrowingStatus.values());
        panel.add(cmbStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createLabel("Borrow Date:"), gbc);
        gbc.gridx = 1;
        borrowDateSpinner = createDarkSpinner();
        panel.add(borrowDateSpinner, gbc);
        
        gbc.gridx = 2;
        panel.add(createLabel("Due Date:"), gbc);
        gbc.gridx = 3;
        dueDateSpinner = createDarkSpinner();
        panel.add(dueDateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(createLabel("Return Date:"), gbc);
        gbc.gridx = 1;
        returnDateSpinner = createDarkSpinner();
        panel.add(returnDateSpinner, gbc);
        
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
    
    private JComboBox<Borrowing.BorrowingStatus> createDarkComboBox(Borrowing.BorrowingStatus[] items) {
        JComboBox<Borrowing.BorrowingStatus> combo = new JComboBox<>(items);
        combo.setFont(MAIN_FONT);
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        combo.setRenderer(new ListCellRenderer<Borrowing.BorrowingStatus>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            @Override
            public Component getListCellRendererComponent(JList<? extends Borrowing.BorrowingStatus> list, Borrowing.BorrowingStatus value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(MAIN_FONT);
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
            BorderFactory.createLineBorder(TEAL_PRIMARY, 2, true), "Borrowing List", 0, 0, TABLE_HEADER_FONT, TEAL_TEXT));
        
        String[] columns = {"Borrowing ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Return Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        borrowingTable = new JTable(tableModel);
        borrowingTable.setRowHeight(35);
        borrowingTable.setBackground(new Color(40, 40, 40));
        borrowingTable.setForeground(Color.WHITE);
        borrowingTable.setSelectionBackground(TEAL_PRIMARY);
        borrowingTable.setGridColor(new Color(70, 70, 70));
        
        JTableHeader header = borrowingTable.getTableHeader();
        header.setBackground(TEAL_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);
        header.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        borrowingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = borrowingTable.getSelectedRow();
                if (selectedRow != -1) loadBorrowingToForm(selectedRow);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(borrowingTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.BLACK);
        
        // কালার গুলো একটু গাঢ় করা হয়েছে টেক্সট স্পষ্ট করার জন্য
        JButton btnAdd = createStyledButton("Add Borrowing", new Color(39, 174, 96));
        JButton btnUpdate = createStyledButton("Update Borrowing", new Color(41, 128, 185));
        JButton btnDelete = createStyledButton("Delete Borrowing", new Color(192, 57, 43));
        JButton btnClear = createStyledButton("Clear Form", new Color(127, 140, 141));
        
        btnAdd.addActionListener(e -> addBorrowing());
        btnUpdate.addActionListener(e -> updateBorrowing());
        btnDelete.addActionListener(e -> deleteBorrowing());
        btnClear.addActionListener(e -> clearForm());
        
        panel.add(btnAdd); panel.add(btnUpdate); panel.add(btnDelete); panel.add(btnClear);
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(BOLD_FONT);
        btn.setPreferredSize(new Dimension(190, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
        return btn;
    }

    private void addBorrowing() {
        if (!validateFields()) return;
        
        for (Borrowing b : borrowingList) {
            if (b.getBorrowingId().equals(txtBorrowingId.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Borrowing ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        operationStrategy = new AddStrategy<>();
        operationStrategy.execute(getBorrowingFromForm(), borrowingList, -1);
        finalizeOperation("Borrowing record added successfully!");
    }
    
    private void updateBorrowing() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateFields()) return;
        
        operationStrategy = new UpdateStrategy<>();
        operationStrategy.execute(getBorrowingFromForm(), borrowingList, selectedRow);
        finalizeOperation("Borrowing record updated successfully!");
    }
    
    private void deleteBorrowing() {
        if (selectedRow == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            borrowingList.remove(selectedRow);
            finalizeOperation("Borrowing record deleted successfully!");
        }
    }

    private void finalizeOperation(String message) {
        saveToFile();
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearForm() {
        txtBorrowingId.setText(""); txtBookId.setText(""); txtMemberId.setText("");
        cmbStatus.setSelectedIndex(0);
        borrowDateSpinner.setValue(new Date());
        dueDateSpinner.setValue(new Date());
        returnDateSpinner.setValue(new Date());
        selectedRow = -1;
        borrowingTable.clearSelection();
        txtBorrowingId.setEditable(true);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Borrowing b : borrowingList) {
            tableModel.addRow(new Object[]{
                b.getBorrowingId(), b.getBookId(), b.getMemberId(),
                sdf.format(b.getBorrowDate()), sdf.format(b.getDueDate()),
                b.getReturnDate() != null ? sdf.format(b.getReturnDate()) : "N/A", b.getStatus()
            });
        }
    }
    
    private void loadBorrowingToForm(int row) {
        Borrowing b = borrowingList.get(row);
        txtBorrowingId.setText(b.getBorrowingId());
        txtBookId.setText(b.getBookId());
        txtMemberId.setText(b.getMemberId());
        borrowDateSpinner.setValue(b.getBorrowDate());
        dueDateSpinner.setValue(b.getDueDate());
        if (b.getReturnDate() != null) returnDateSpinner.setValue(b.getReturnDate());
        cmbStatus.setSelectedItem(b.getStatus());
        txtBorrowingId.setEditable(false);
    }
    
    private Borrowing getBorrowingFromForm() {
        return new Borrowing(
            txtBorrowingId.getText().trim(), txtBookId.getText().trim(),
            txtMemberId.getText().trim(), (Date) borrowDateSpinner.getValue(),
            (Date) dueDateSpinner.getValue(), (Date) returnDateSpinner.getValue(),
            (Borrowing.BorrowingStatus) cmbStatus.getSelectedItem()
        );
    }
    
    private boolean validateFields() {
        if (txtBorrowingId.getText().trim().isEmpty() || txtBookId.getText().trim().isEmpty() || txtMemberId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All ID fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            new File("data").mkdirs();
            oos.writeObject(borrowingList);
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                borrowingList = (List<Borrowing>) ois.readObject();
            } catch (Exception e) { borrowingList = new ArrayList<>(); }
        }
    }
}