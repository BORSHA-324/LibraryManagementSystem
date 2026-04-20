import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MemberForm extends JFrame {

    private static final Color TEAL_PRIMARY = new Color(32, 178, 170);
    private static final Color TEAL_TEXT = new Color(175, 238, 238);
    private static final Color TEAL_DARK = new Color(0, 128, 128);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private List<Member> memberList = new ArrayList<>();
    private final String FILE_PATH = "data/members.dat";

    // Strategy Pattern Variable
    private DataOperationStrategy<Member> operationStrategy;

    private JTextField txtMemberId, txtName, txtEmail, txtPhone, txtAddress;
    private JComboBox<Member.MembershipType> cmbMembershipType;
    private JSpinner dateSpinner;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private int selectedRow = -1;

    public MemberForm() {
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
            BorderFactory.createLineBorder(TEAL_PRIMARY, 2, true),
            "Member Information",
            0, 0, TABLE_HEADER_FONT, TEAL_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblMemberId = new JLabel("Member ID:");
        lblMemberId.setFont(BOLD_FONT);
        lblMemberId.setForeground(Color.WHITE);
        panel.add(lblMemberId, gbc);
        gbc.gridx = 1;
        txtMemberId = createDarkTextField(15);
        panel.add(txtMemberId, gbc);

        gbc.gridx = 2;
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(BOLD_FONT);
        lblName.setForeground(Color.WHITE);
        panel.add(lblName, gbc);
        gbc.gridx = 3;
        txtName = createDarkTextField(20);
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(BOLD_FONT);
        lblEmail.setForeground(Color.WHITE);
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        txtEmail = createDarkTextField(15);
        panel.add(txtEmail, gbc);

        gbc.gridx = 2;
        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(BOLD_FONT);
        lblPhone.setForeground(Color.WHITE);
        panel.add(lblPhone, gbc);
        gbc.gridx = 3;
        txtPhone = createDarkTextField(20);
        panel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblMembershipType = new JLabel("Membership Type:");
        lblMembershipType.setFont(BOLD_FONT);
        lblMembershipType.setForeground(Color.WHITE);
        panel.add(lblMembershipType, gbc);
        gbc.gridx = 1;
        cmbMembershipType = createDarkComboBox(Member.MembershipType.values());
        panel.add(cmbMembershipType, gbc);

        gbc.gridx = 2;
        JLabel lblJoinDate = new JLabel("Join Date:");
        lblJoinDate.setFont(BOLD_FONT);
        lblJoinDate.setForeground(Color.WHITE);
        panel.add(lblJoinDate, gbc);
        gbc.gridx = 3;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.setFont(MAIN_FONT);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) dateSpinner.getEditor();
        editor.getTextField().setBackground(new Color(50, 50, 50));
        editor.getTextField().setForeground(Color.WHITE);
        editor.getTextField().setCaretColor(Color.WHITE);
        editor.getTextField().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(dateSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setFont(BOLD_FONT);
        lblAddress.setForeground(Color.WHITE);
        panel.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtAddress = createDarkTextField(45);
        panel.add(txtAddress, gbc);

        return panel;
    }

    private JTextField createDarkTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(MAIN_FONT);
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JComboBox<Member.MembershipType> createDarkComboBox(Member.MembershipType[] items) {
        JComboBox<Member.MembershipType> combo = new JComboBox<>(items);
        combo.setFont(MAIN_FONT);
        combo.setBackground(new Color(50, 50, 50));
        combo.setForeground(Color.WHITE);
        combo.setRenderer(new ListCellRenderer<Member.MembershipType>() {
            private final BasicComboBoxRenderer defaultRenderer = new BasicComboBoxRenderer();
            @Override
            public Component getListCellRendererComponent(JList<? extends Member.MembershipType> list, Member.MembershipType value, int index, boolean isSelected, boolean cellHasFocus) {
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

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(TEAL_PRIMARY, 2, true), "Member List", 0, 0, TABLE_HEADER_FONT, TEAL_TEXT));

        String[] columns = {"Member ID", "Name", "Email", "Phone", "Membership Type", "Join Date", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(35);
        memberTable.setBackground(new Color(40, 40, 40));
        memberTable.setForeground(Color.WHITE);
        memberTable.setSelectionBackground(TEAL_PRIMARY);

        JTableHeader header = memberTable.getTableHeader();
        header.setBackground(TEAL_PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(TABLE_HEADER_FONT);

        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = memberTable.getSelectedRow();
                if (selectedRow != -1) loadMemberToForm(selectedRow);
            }
        });

        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
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

    private void addMember() {
        if (!validateFields()) return;

        for (Member m : memberList) {
            if (m.getMemberId().equals(txtMemberId.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Member ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        operationStrategy = new AddStrategy<>();
        operationStrategy.execute(getMemberFromForm(), memberList, -1);
        finalizeOperation("Member added successfully!");
    }

    private void updateMember() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a member to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateFields()) return;

        operationStrategy = new UpdateStrategy<>();
        operationStrategy.execute(getMemberFromForm(), memberList, selectedRow);
        finalizeOperation("Member updated successfully!");
    }

    private void deleteMember() {
        if (selectedRow == -1) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this member?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            memberList.remove(selectedRow);
            finalizeOperation("Member deleted successfully!");
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
        txtMemberId.setText(""); txtName.setText(""); txtEmail.setText("");
        txtPhone.setText(""); txtAddress.setText("");
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
            txtMemberId.getText().trim(), txtName.getText().trim(),
            txtEmail.getText().trim(), txtPhone.getText().trim(),
            (Member.MembershipType) cmbMembershipType.getSelectedItem(),
            (Date) dateSpinner.getValue(), txtAddress.getText().trim()
        );
    }

    private boolean validateFields() {
        if (txtMemberId.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID and Name are required!", "Error", JOptionPane.ERROR_MESSAGE);
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
        } catch (Exception e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
                memberList = (List<Member>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) { memberList = new ArrayList<>(); }
    }
}