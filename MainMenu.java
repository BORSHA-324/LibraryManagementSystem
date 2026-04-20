
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainMenu extends JFrame {

    private static final Color TEAL_PRIMARY = new Color(32, 178, 170); // Light Sea Green
    private static final Color TEAL_LIGHT = new Color(72, 209, 204); // Medium Turquoise
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 36);
    private static final Font FOOTER_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public MainMenu() {
        setTitle("Library Management System");
        setSize(700, 500);
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

                Color color1 = new Color(45, 52, 54);
                Color color2 = new Color(20, 25, 30);

                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(240, 240, 240));
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 25));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 60, 150));

        JButton btnBooks = createMenuButton("Manage Books");
        JButton btnMembers = createMenuButton("Manage Members");
        JButton btnBorrowing = createMenuButton("Manage Borrowing");
        JButton btnExit = createExitButton("Exit");

        btnBooks.addActionListener(e -> openBookForm());
        btnMembers.addActionListener(e -> openMemberForm());
        btnBorrowing.addActionListener(e -> openBorrowingForm());
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnBooks);
        buttonPanel.add(btnMembers);
        buttonPanel.add(btnBorrowing);
        buttonPanel.add(btnExit);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("© 2025 Library Management System");
        footerLabel.setForeground(new Color(200, 200, 200));
        footerLabel.setFont(FOOTER_FONT);
        footerPanel.add(footerLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(MAIN_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(TEAL_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(TEAL_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(TEAL_PRIMARY);
            }
        });

        return button;
    }

    private JButton createExitButton(String text) {
        JButton button = new JButton(text);
        button.setFont(MAIN_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(231, 76, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(231, 76, 60).brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(231, 76, 60));
            }
        });

        return button;
    }

    private void openBookForm() {
        JFrame bookForm = FormFactory.createForm(FormFactory.FormType.BOOK);
        bookForm.setVisible(true);
    }

    private void openMemberForm() {
        JFrame memberForm = FormFactory.createForm(FormFactory.FormType.MEMBER);
        memberForm.setVisible(true);
    }

    private void openBorrowingForm() {
        JFrame borrowingForm = FormFactory.createForm(FormFactory.FormType.BORROWING);
        borrowingForm.setVisible(true);
    }

    public static void main(String[] args) {

        new java.io.File("data").mkdirs();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}
