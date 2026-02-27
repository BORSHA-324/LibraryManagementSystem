
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
    private static final Color TEAL_DARK = new Color(17, 128, 106);

    private List<Member> memberList = new ArrayList<>();
    private final String FILE_PATH = "data/members.dat";

    private JTextField txtMemberId, txtName, txtEmail, txtPhone, txtAddress;
    private JComboBox<Member.MembershipType> cmbMembershipType;
    private JSpinner dateSpinner;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public MemberForm() 
    {
        loadFromFile();
        setTitle("Member Management");
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
            "Member Information",
            0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setFont(new Font("Arial", Font.BOLD, 13));
        lblMemberId.setForeground(Color.WHITE);
        panel.add(lblMemberId, gbc);
        gbc.gridx = 1;
        txtMemberId = createDarkTextField(15);
        panel.add(txtMemberId, gbc);

        gbc.gridx = 2;
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 13));
        lblName.setForeground(Color.WHITE);
        panel.add(lblName, gbc);
        gbc.gridx = 3;
        txtName = createDarkTextField(20);
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 13));
        lblEmail.setForeground(Color.WHITE);
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = createDarkTextField(15);
        panel.add(txtEmail, gbc);

        gbc.gridx = 2;
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Arial", Font.BOLD, 13));
        lblPhone.setForeground(Color.WHITE);
        panel.add(lblPhone, gbc);
        gbc.gridx = 3;
        txtPhone = createDarkTextField(20);
        panel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblMembershipType = new JLabel("Membership Type:");
        lblMembershipType.setFont(new Font("Arial", Font.BOLD, 13));
        lblMembershipType.setForeground(Color.WHITE);
        panel.add(lblMembershipType, gbc);
        gbc.gridx = 1;
        cmbMembershipType = createDarkComboBox(Member.MembershipType.values());
        panel.add(cmbMembershipType, gbc);

        gbc.gridx = 2;
        JLabel lblJoinDate = new JLabel("Join Date:");
        lblJoinDate.setFont(new Font("Arial", Font.BOLD, 13));
        lblJoinDate.setForeground(Color.WHITE);
        panel.add(lblJoinDate, gbc);
        gbc.gridx = 3;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.setFont(new Font("Arial", Font.PLAIN, 13));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) dateSpinner.getEditor();
        editor.getTextField().setBackground(new Color(50, 50, 50));
        editor.getTextField().setForeground(Color.WHITE);
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(new Font("Arial", Font.BOLD, 13));
        lblAddress.setForeground(Color.WHITE);
        panel.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtAddress = createDarkTextField(45);
        panel.add(txtAddress, gbc);

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

    private JComboBox<Member.MembershipType> createDarkComboBox(Member.MembershipType[] items) {
        JComboBox<Member.MembershipType> combo = new JComboBox<>(items);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        
        combo.setRenderer(new ListCellRenderer<Member.MembershipType>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            
            @Override
            public Component getListCellRendererComponent(JList<? extends Member.MembershipType> list,
                    Member.MembershipType value, int index, boolean isSelected, boolean cellHasFocus) {
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
            "Member List",
            0, 0, new Font("Arial", Font.BOLD, 14), TEAL_TEXT));

        String[] columns = {"Member ID", "Name", "Email", "Phone",
            "Membership Type", "Join Date", "Address"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(30);
        memberTable.setFont(new Font("Arial", Font.PLAIN, 14));
        memberTable.setForeground(Color.WHITE);
        memberTable.setBackground(new Color(40, 40, 40));
        memberTable.setGridColor(new Color(70, 70, 70));
        memberTable.setSelectionBackground(TEAL_PRIMARY);
        memberTable.setSelectionForeground(Color.WHITE);
        memberTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = memberTable.getTableHeader();
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
        
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        memberTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        memberTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        memberTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(6).setPreferredWidth(200);

        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = memberTable.getSelectedRow();
                if (selectedRow != -1) loadMemberToForm(selectedRow);
            }
        });

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setPreferredSize(new Dimension(1100, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(40, 40, 40));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.BLACK);

        JButton btnAdd = createStyledButton("Add Member", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update Member", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Delete Member", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear Form", new Color(149, 165, 166));

        btnAdd.addActionListener(e -> addMember());
        btnUpdate.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
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
            public void mouseEntered(MouseEvent evt) { btn.setBackground(color.brighter()); }
            @Override
            public void mouseExited(MouseEvent evt) { btn.setBackground(color); }
        });

        return btn;
    }

    private void addMember() {
        if (!validateFields()) return;

        for (Member m : memberList) {
            if (m.getMemberId().equals(txtMemberId.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Member ID already exists!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        memberList.add(getMemberFromForm());
        saveToFile();
        refreshTable();
        clearForm();

        JOptionPane.showMessageDialog(this, "Member added successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateMember() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to update!",
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateFields()) return;
        
        memberList.set(selectedRow, getMemberFromForm());
        saveToFile();
        refreshTable();
        clearForm();
        
        JOptionPane.showMessageDialog(this, "Member updated successfully!",
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteMember() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete!",
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this member?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            memberList.remove(selectedRow);
            saveToFile();
            refreshTable();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Member deleted successfully!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        txtMemberId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        cmbMembershipType.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
        selectedRow = -1;
        memberTable.clearSelection();
        txtMemberId.setEditable(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Member m : memberList) {
            tableModel.addRow(new Object[]{
                m.getMemberId(), m.getName(), m.getEmail(), m.getPhone(),
                m.getMembershipType(), sdf.format(m.getJoinDate()), m.getAddress()
            });
        }
    }

    private void loadMemberToForm(int row) {
        Member m = memberList.get(row);
        txtMemberId.setText(m.getMemberId());
        txtName.setText(m.getName());
        txtEmail.setText(m.getEmail());
        txtPhone.setText(m.getPhone());
        txtAddress.setText(m.getAddress());
        cmbMembershipType.setSelectedItem(m.getMembershipType());
        dateSpinner.setValue(m.getJoinDate());
        txtMemberId.setEditable(false);
    }

    private Member getMemberFromForm() {
        return new Member(
            txtMemberId.getText().trim(),
            txtName.getText().trim(),
            txtEmail.getText().trim(),
            txtPhone.getText().trim(),
            (Member.MembershipType) cmbMembershipType.getSelectedItem(),
            (Date) dateSpinner.getValue(),
            txtAddress.getText().trim()
        );
    }

    private boolean validateFields() {
        if (txtMemberId.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill all required fields (Member ID and Name)!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveToFile() {
        try {
            new File("data").mkdirs();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(memberList);
            oos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            memberList = new ArrayList<>();
            return;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            memberList = (List<Member>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            memberList = new ArrayList<>();
        }
    }
}
