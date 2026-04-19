package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainMenu extends JFrame {

    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);
    private static final Color TEAL_LIGHT = new Color(26, 188, 156);

    private LibraryFacade facade; 

    public MainMenu() {
        facade = new LibraryFacade(); 
        setTitle("Library Management System");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(45, 52, 54), 0, h, new Color(20, 25, 30));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 80, 100));

        JButton btnBooks = createMenuButton("📚 Manage Books");
        JButton btnMembers = createMenuButton("👥 Manage Members");
        JButton btnBorrowing = createMenuButton("📖 Manage Borrowing");
        JButton btnExit = createExitButton("🚪 Exit");

        btnBooks.addActionListener(e -> new BookForm(facade).setVisible(true));
        btnMembers.addActionListener(e -> new MemberForm(facade).setVisible(true));
        btnBorrowing.addActionListener(e -> new BorrowingForm(facade).setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnBooks);
        buttonPanel.add(btnMembers);
        buttonPanel.add(btnBorrowing);
        buttonPanel.add(btnExit);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("© 2025 Library Management System");
        footerLabel.setForeground(new Color(200, 200, 200));
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(footerLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(TEAL_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(TEAL_LIGHT); }
            public void mouseExited(MouseEvent evt) { button.setBackground(TEAL_PRIMARY); }
        });

        return button;
    }

    private JButton createExitButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(231, 76, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(new Color(231, 76, 60).brighter()); }
            public void mouseExited(MouseEvent evt) { button.setBackground(new Color(231, 76, 60)); }
        });

        return button;
    }

    public static void main(String[] args) {
        new java.io.File("data").mkdirs();
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
