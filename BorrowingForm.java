
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSpinner;
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowingForm extends JFrame {
    
    // Corporate Teal Theme Colors
    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);
    private static final Color TEAL_TEXT = new Color(77, 215, 198);
    private static final Color TEAL_DARK = new Color(17, 128, 106);
    
    private List<Borrowing> borrowingList = new ArrayList<>();
    
    private final String FILE_PATH = LibraryConfig.getInstance().getBorrowingsFilePath();
    
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
            BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
            "Borrowing Information",
            0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblBorrowingId = new JLabel("Borrowing ID:");
        lblBorrowingId.setFont(new Font("Arial", Font.BOLD, 13));
        lblBorrowingId.setForeground(Color.WHITE);
        panel.add(lblBorrowingId, gbc);
        gbc.gridx = 1;
        txtBorrowingId = createDarkTextField(15);
        panel.add(txtBorrowingId, gbc);
        
        gbc.gridx = 2;
        JLabel lblBookId = new JLabel("Book ID:");
        lblBookId.setFont(new Font("Arial", Font.BOLD, 13));
        lblBookId.setForeground(Color.WHITE);
        panel.add(lblBookId, gbc);
        gbc.gridx = 3;
        txtBookId = createDarkTextField(15);
        panel.add(txtBookId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setFont(new Font("Arial", Font.BOLD, 13));
        lblMemberId.setForeground(Color.WHITE);
        panel.add(lblMemberId, gbc);
        gbc.gridx = 1;
        txtMemberId = createDarkTextField(15);
        panel.add(txtMemberId, gbc);
        
        gbc.gridx = 2;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 13));
        lblStatus.setForeground(Color.WHITE);
        panel.add(lblStatus, gbc);
        gbc.gridx = 3;
        cmbStatus = createDarkComboBox(Borrowing.BorrowingStatus.values());
        panel.add(cmbStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblBorrowDate = new JLabel("Borrow Date:");
        lblBorrowDate.setFont(new Font("Arial", Font.BOLD, 13));
        lblBorrowDate.setForeground(Color.WHITE);
        panel.add(lblBorrowDate, gbc);
        gbc.gridx = 1;
        borrowDateSpinner = createDarkSpinner();
        panel.add(borrowDateSpinner, gbc);
        
        gbc.gridx = 2;
        JLabel lblDueDate = new JLabel("Due Date:");
        lblDueDate.setFont(new Font("Arial", Font.BOLD, 13));
        lblDueDate.setForeground(Color.WHITE);
        panel.add(lblDueDate, gbc);
        gbc.gridx = 3;
        dueDateSpinner = createDarkSpinner();
        panel.add(dueDateSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblReturnDate = new JLabel("Return Date:");
        lblReturnDate.setFont(new Font("Arial", Font.BOLD, 13));
        lblReturnDate.setForeground(Color.WHITE);
        panel.add(lblReturnDate, gbc);
        gbc.gridx = 1;
        returnDateSpinner = createDarkSpinner();
        panel.add(returnDateSpinner, gbc);
        
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
    
    private JComboBox<Borrowing.BorrowingStatus> createDarkComboBox(Borrowing.BorrowingStatus[] items) {
        JComboBox<Borrowing.BorrowingStatus> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        
        combo.setRenderer(new ListCellRenderer<Borrowing.BorrowingStatus>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            
            @Override
            public Component getListCellRendererComponent(JList<? extends Borrowing.BorrowingStatus> list,
                    Borrowing.BorrowingStatus value, int index, boolean isSelected, boolean cellHasFocus) {
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
    
    private JSpinner createDarkSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JTextField spinnerField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        spinnerField.setBackground(new Color(50, 50, 50));
        spinnerField.setForeground(Color.WHITE);
        spinnerField.setCaretColor(Color.WHITE);
        spinnerField.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        
        return spinner;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
            "Borrowing List",
            0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));
        
        String[] columns = {"Borrowing ID", "Book ID", "Member ID", 
                           "Borrow Date", "Due Date", "Return Date", "Status"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        borrowingTable = new JTable(tableModel);
        borrowingTable.setRowHeight(30);
        borrowingTable.setFont(new Font("Arial", Font.PLAIN, 14));
        borrowingTable.setForeground(Color.WHITE);
        borrowingTable.setBackground(new Color(40, 40, 40));
        borrowingTable.setGridColor(new Color(70, 70, 70));
        borrowingTable.setSelectionBackground(TEAL_PRIMARY);
        borrowingTable.setSelectionForeground(Color.WHITE);
        borrowingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JTableHeader header = borrowingTable.getTableHeader();
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
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(40, 40, 40));
        centerRenderer.setForeground(Color.WHITE);
        
        for (int i = 0; i < 7; i++) {
            borrowingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        borrowingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = borrowingTable.getSelectedRow();
                if (selectedRow != -1) loadBorrowingToForm(selectedRow);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(borrowingTable);
        scrollPane.setPreferredSize(new Dimension(1100, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.BLACK);
        
        JButton btnAdd = createStyledButton("Add Borrowing", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update Borrowing", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Delete Borrowing", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear Form", new Color(149, 165, 166));
        
        btnAdd.addActionListener(e -> addBorrowing());
        btnUpdate.addActionListener(e -> updateBorrowing());
        btnDelete.addActionListener(e -> deleteBorrowing());
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
        btn.setPreferredSize(new Dimension(190, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) { btn.setBackground(color.brighter()); }
            @Override
            public void mouseExited(MouseEvent evt) { btn.setBackground(color); }
        });
        
        return btn;
    }
    
    private void addBorrowing() {
        if (!validateFields()) return;
        
        for (Borrowing borrowing : borrowingList) {
            if (borrowing.getBorrowingId().equals(txtBorrowingId.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Borrowing ID already exists!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        Borrowing borrowing = new Borrowing(
            txtBorrowingId.getText().trim(), txtBookId.getText().trim(),
            txtMemberId.getText().trim(), (Date) borrowDateSpinner.getValue(),
            (Date) dueDateSpinner.getValue(), (Date) returnDateSpinner.getValue(),
            (Borrowing.BorrowingStatus) cmbStatus.getSelectedItem()
        );
        
        borrowingList.add(borrowing);
        saveToFile();
        refreshTable();
        clearForm();
        
        JOptionPane.showMessageDialog(this, "Borrowing record added successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void updateBorrowing() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrowing record to update!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateFields()) return;
        
        Borrowing borrowing = borrowingList.get(selectedRow);
        borrowing.setBookId(txtBookId.getText().trim());
        borrowing.setMemberId(txtMemberId.getText().trim());
        borrowing.setBorrowDate((Date) borrowDateSpinner.getValue());
        borrowing.setDueDate((Date) dueDateSpinner.getValue());
        borrowing.setReturnDate((Date) returnDateSpinner.getValue());
        borrowing.setStatus((Borrowing.BorrowingStatus) cmbStatus.getSelectedItem());
        
        saveToFile();
        refreshTable();
        clearForm();
        
        JOptionPane.showMessageDialog(this, "Borrowing record updated successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteBorrowing() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrowing record to delete!", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this borrowing record?", 
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            borrowingList.remove(selectedRow);
            saveToFile();
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Borrowing record deleted successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtBorrowingId.setText("");
        txtBookId.setText("");
        txtMemberId.setText("");
        cmbStatus.setSelectedIndex(0);
        borrowDateSpinner.setValue(new Date());
        dueDateSpinner.setValue(new Date());
        returnDateSpinner.setValue(new Date());
        selectedRow = -1;
        borrowingTable.clearSelection();
        txtBorrowingId.setEditable(true);
    }
    
    private void loadBorrowingToForm(int row) {
        Borrowing borrowing = borrowingList.get(row);
        txtBorrowingId.setText(borrowing.getBorrowingId());
        txtBookId.setText(borrowing.getBookId());
        txtMemberId.setText(borrowing.getMemberId());
        borrowDateSpinner.setValue(borrowing.getBorrowDate());
        dueDateSpinner.setValue(borrowing.getDueDate());
        returnDateSpinner.setValue(borrowing.getReturnDate());
        cmbStatus.setSelectedItem(borrowing.getStatus());
        txtBorrowingId.setEditable(false);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Borrowing borrowing : borrowingList) {
            Object[] row = {
                borrowing.getBorrowingId(), borrowing.getBookId(),
                borrowing.getMemberId(), sdf.format(borrowing.getBorrowDate()),
                sdf.format(borrowing.getDueDate()),
                borrowing.getReturnDate() != null ? sdf.format(borrowing.getReturnDate()) : "N/A",
                borrowing.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private boolean validateFields() {
        if (txtBorrowingId.getText().trim().isEmpty() || 
            txtBookId.getText().trim().isEmpty() ||
            txtMemberId.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void saveToFile() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(borrowingList);
            oos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            borrowingList = new ArrayList<>();
            return;
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            borrowingList = (List<Borrowing>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            borrowingList = new ArrayList<>();
        }
    }

}
