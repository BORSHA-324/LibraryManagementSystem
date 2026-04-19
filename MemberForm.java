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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberForm extends JFrame {

    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);
    private static final Color TEAL_TEXT = new Color(77, 215, 198);

    private LibraryFacade facade;

    private JTextField txtMemberId, txtName, txtEmail, txtPhone, txtAddress;
    private JComboBox<String> cmbMembershipType;
    private JSpinner joinDateSpinner;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public MemberForm(LibraryFacade facade) {
        this.facade = facade; 
        setTitle("Member Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
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
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY, 2),
                "Member Information",
                0,0, new Font("Arial", Font.BOLD,14), TEAL_TEXT
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0;
        panel.add(createLabel("Member ID:"), gbc);
        gbc.gridx=1;
        txtMemberId = createTextField(15);
        panel.add(txtMemberId, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Name:"), gbc);
        gbc.gridx=3;
        txtName = createTextField(20);
        panel.add(txtName, gbc);

        gbc.gridx=0; gbc.gridy=1;
        panel.add(createLabel("Email:"), gbc);
        gbc.gridx=1;
        txtEmail = createTextField(15);
        panel.add(txtEmail, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Phone:"), gbc);
        gbc.gridx=3;
        txtPhone = createTextField(15);
        panel.add(txtPhone, gbc);

        gbc.gridx=0; gbc.gridy=2;
        panel.add(createLabel("Membership Type:"), gbc);
        gbc.gridx=1;
        String[] types = {"Regular","Premium","VIP"};
        cmbMembershipType = new JComboBox<>(types);
        cmbMembershipType.setBackground(new Color(50,50,50));
        cmbMembershipType.setForeground(Color.WHITE);
        panel.add(cmbMembershipType, gbc);

        gbc.gridx=2;
        panel.add(createLabel("Join Date:"), gbc);
        gbc.gridx=3;
        joinDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(joinDateSpinner,"dd/MM/yyyy");
        joinDateSpinner.setEditor(editor);
        panel.add(joinDateSpinner, gbc);

        gbc.gridx=0; gbc.gridy=3;
        panel.add(createLabel("Address:"), gbc);
        gbc.gridx=1; gbc.gridwidth=3;
        txtAddress = createTextField(30);
        panel.add(txtAddress, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD,13));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JTextField createTextField(int cols) {
        JTextField field = new JTextField(cols);
        field.setBackground(new Color(50,50,50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        return field;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30,30,30));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEAL_PRIMARY,2),
                "Member List",0,0, new Font("Arial",Font.BOLD,14), TEAL_TEXT
        ));

        String[] cols = {"Member ID","Name","Email","Phone","Membership Type","Join Date","Address"};
        tableModel = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        memberTable = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(memberTable);
        panel.add(scroll, BorderLayout.CENTER);

        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                selectedRow = memberTable.getSelectedRow();
                if(selectedRow!=-1) loadMemberToForm(selectedRow);
            }
        });

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,15));
        panel.setBackground(Color.BLACK);

        JButton btnAdd = createButton("Add Member", new Color(46,204,113), e -> addMember());
        JButton btnUpdate = createButton("Update Member", new Color(52,152,219), e -> updateMember());
        JButton btnDelete = createButton("Delete Member", new Color(231,76,60), e -> deleteMember());
        JButton btnClear = createButton("Clear Form", new Color(149,165,166), e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        return panel;
    }

    private JButton createButton(String text, Color color, java.awt.event.ActionListener listener){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD,16));
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        return btn;
    }

    private void addMember(){
        if(!validateFields()) return;

        for(Member m: facade.getAllMembers()){
            if(m.getMemberId().equals(txtMemberId.getText().trim())){
                JOptionPane.showMessageDialog(this,"Member ID already exists!","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Member member = new Member(
            txtMemberId.getText().trim(),
            txtName.getText().trim(),
            txtEmail.getText().trim(),
            txtPhone.getText().trim(),
            (String)cmbMembershipType.getSelectedItem(),
            (Date)joinDateSpinner.getValue(),
            txtAddress.getText().trim()
        );

        facade.addMember(member);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this,"Member added successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateMember(){
        if(selectedRow==-1){
            JOptionPane.showMessageDialog(this,"Select a member to update!","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(!validateFields()) return;

        Member member = new Member(
            txtMemberId.getText().trim(),
            txtName.getText().trim(),
            txtEmail.getText().trim(),
            txtPhone.getText().trim(),
            (String)cmbMembershipType.getSelectedItem(),
            (Date)joinDateSpinner.getValue(),
            txtAddress.getText().trim()
        );

        facade.updateMember(selectedRow, member);
        refreshTable();
        clearForm();
        JOptionPane.showMessageDialog(this,"Member updated successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteMember(){
        if(selectedRow==-1){
            JOptionPane.showMessageDialog(this,"Select a member to delete!","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure?","Confirm Delete",JOptionPane.YES_NO_OPTION);
        if(confirm==JOptionPane.YES_OPTION){
            facade.removeMember(selectedRow);
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this,"Member deleted successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm(){
        txtMemberId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        cmbMembershipType.setSelectedIndex(0);
        joinDateSpinner.setValue(new Date());
        selectedRow=-1;
        memberTable.clearSelection();
        txtMemberId.setEditable(true);
    }

    private void loadMemberToForm(int row){
        Member member = facade.getAllMembers().get(row);
        txtMemberId.setText(member.getMemberId());
        txtName.setText(member.getName());
        txtEmail.setText(member.getEmail());
        txtPhone.setText(member.getPhone());
        cmbMembershipType.setSelectedItem(member.getMembershipType());
        joinDateSpinner.setValue(member.getJoinDate());
        txtAddress.setText(member.getAddress());
        txtMemberId.setEditable(false);
    }

    private void refreshTable(){
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(Member m: facade.getAllMembers()){
            tableModel.addRow(new Object[]{
                m.getMemberId(), m.getName(), m.getEmail(), m.getPhone(),
                m.getMembershipType(), sdf.format(m.getJoinDate()), m.getAddress()
            });
        }
    }

    private boolean validateFields(){
        if(txtMemberId.getText().trim().isEmpty() ||
           txtName.getText().trim().isEmpty() ||
           txtEmail.getText().trim().isEmpty() ||
           txtPhone.getText().trim().isEmpty() ||
           txtAddress.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this,"Fill all fields!","Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
