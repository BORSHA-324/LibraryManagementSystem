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

    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);
    private LibraryFacade facade;

    private JTextField txtBorrowingId, txtBookId, txtMemberId;
    private JComboBox<Borrowing.BorrowingStatus> cmbStatus;
    private JSpinner borrowDateSpinner, dueDateSpinner, returnDateSpinner;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public BorrowingForm(LibraryFacade facade) {
        this.facade = facade;
        setTitle("Borrowing Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        mainPanel.setBackground(Color.BLACK);

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30,30,30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0;
        panel.add(createLabel("Borrowing ID:"), gbc);
        gbc.gridx=1;
        txtBorrowingId = createTextField(15);
        panel.add(txtBorrowingId, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Book ID:"), gbc);
        gbc.gridx=3;
        txtBookId = createTextField(15);
        panel.add(txtBookId, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(createLabel("Member ID:"), gbc);
        gbc.gridx=1;
        txtMemberId = createTextField(15);
        panel.add(txtMemberId, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Status:"), gbc);
        gbc.gridx=3;
        cmbStatus = new JComboBox<>(Borrowing.BorrowingStatus.values());
        cmbStatus.setBackground(new Color(50,50,50));
        cmbStatus.setForeground(Color.WHITE);
        panel.add(cmbStatus, gbc);

        gbc.gridx=0; gbc.gridy=2;
        panel.add(createLabel("Borrow Date:"), gbc);
        gbc.gridx=1;
        borrowDateSpinner = createSpinner();
        panel.add(borrowDateSpinner, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Due Date:"), gbc);
        gbc.gridx=3;
        dueDateSpinner = createSpinner();
        panel.add(dueDateSpinner, gbc);

        gbc.gridx=0; gbc.gridy=3;
        panel.add(createLabel("Return Date:"), gbc);
        gbc.gridx=1;
        returnDateSpinner = createSpinner();
        panel.add(returnDateSpinner, gbc);

        return panel;
    }

    private JLabel createLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JTextField createTextField(int cols){
        JTextField field = new JTextField(cols);
        field.setBackground(new Color(50,50,50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JSpinner createSpinner(){
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        return spinner;
    }

    private JPanel createTablePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30,30,30));

        String[] cols = {"Borrowing ID","Book ID","Member ID","Borrow Date","Due Date","Return Date","Status"};
        tableModel = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;}};
        borrowingTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(borrowingTable);
        panel.add(scroll, BorderLayout.CENTER);

        borrowingTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                selectedRow = borrowingTable.getSelectedRow();
                if(selectedRow!=-1) loadBorrowingToForm(selectedRow);
            }
        });

        return panel;
    }

    private JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,15));
        panel.setBackground(Color.BLACK);

        JButton btnAdd = createButton("Add Borrowing", new Color(46,204,113), e->addBorrowing());
        JButton btnUpdate = createButton("Update Borrowing", new Color(52,152,219), e->updateBorrowing());
        JButton btnDelete = createButton("Delete Borrowing", new Color(231,76,60), e->deleteBorrowing());
        JButton btnClear = createButton("Clear Form", new Color(149,165,166), e->clearForm());

        panel.add(btnAdd); panel.add(btnUpdate);
        panel.add(btnDelete); panel.add(btnClear);

        return panel;
    }

    private JButton createButton(String text, Color color, java.awt.event.ActionListener listener){
        JButton btn = new JButton(text);
        btn.setBackground(color); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD,16));
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        return btn;
    }

    private void addBorrowing(){
        if(!validateFields()) return;
        for(Borrowing b: facade.getAllBorrowings()){
            if(b.getBorrowingId().equals(txtBorrowingId.getText().trim())){
                JOptionPane.showMessageDialog(this,"Borrowing ID exists!","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Borrowing borrowing = new Borrowing(
            txtBorrowingId.getText().trim(),
            txtBookId.getText().trim(),
            txtMemberId.getText().trim(),
            (Date)borrowDateSpinner.getValue(),
            (Date)dueDateSpinner.getValue(),
            (Date)returnDateSpinner.getValue(),
            (Borrowing.BorrowingStatus)cmbStatus.getSelectedItem()
        );

        facade.addBorrowing(borrowing);
        refreshTable(); clearForm();
        JOptionPane.showMessageDialog(this,"Borrowing added successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateBorrowing(){
        if(selectedRow==-1){
            JOptionPane.showMessageDialog(this,"Select a borrowing to update!","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!validateFields()) return;

        Borrowing borrowing = new Borrowing(
            txtBorrowingId.getText().trim(),
            txtBookId.getText().trim(),
            txtMemberId.getText().trim(),
            (Date)borrowDateSpinner.getValue(),
            (Date)dueDateSpinner.getValue(),
            (Date)returnDateSpinner.getValue(),
            (Borrowing.BorrowingStatus)cmbStatus.getSelectedItem()
        );

        facade.updateBorrowing(selectedRow, borrowing);
        refreshTable(); clearForm();
        JOptionPane.showMessageDialog(this,"Borrowing updated successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBorrowing(){
        if(selectedRow==-1){
            JOptionPane.showMessageDialog(this,"Select a borrowing to delete!","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure?","Confirm",JOptionPane.YES_NO_OPTION);
        if(confirm==JOptionPane.YES_OPTION){
            facade.removeBorrowing(selectedRow);
            refreshTable(); clearForm();
            JOptionPane.showMessageDialog(this,"Borrowing deleted successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm(){
        txtBorrowingId.setText(""); txtBookId.setText(""); txtMemberId.setText("");
        cmbStatus.setSelectedIndex(0);
        borrowDateSpinner.setValue(new Date()); dueDateSpinner.setValue(new Date());
        returnDateSpinner.setValue(new Date());
        selectedRow=-1; borrowingTable.clearSelection(); txtBorrowingId.setEditable(true);
    }

    private void loadBorrowingToForm(int row){
        Borrowing b = facade.getAllBorrowings().get(row);
        txtBorrowingId.setText(b.getBorrowingId());
        txtBookId.setText(b.getBookId());
        txtMemberId.setText(b.getMemberId());
        borrowDateSpinner.setValue(b.getBorrowDate());
        dueDateSpinner.setValue(b.getDueDate());
        returnDateSpinner.setValue(b.getReturnDate());
        cmbStatus.setSelectedItem(b.getStatus());
        txtBorrowingId.setEditable(false);
    }

    private void refreshTable(){
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(Borrowing b: facade.getAllBorrowings()){
            tableModel.addRow(new Object[]{
                b.getBorrowingId(), b.getBookId(), b.getMemberId(),
                sdf.format(b.getBorrowDate()), sdf.format(b.getDueDate()),
                b.getReturnDate()!=null ? sdf.format(b.getReturnDate()):"N/A",
                b.getStatus()
            });
        }
    }

    private boolean validateFields(){
        return !txtBorrowingId.getText().trim().isEmpty() &&
               !txtBookId.getText().trim().isEmpty() &&
               !txtMemberId.getText().trim().isEmpty();
    }
}
