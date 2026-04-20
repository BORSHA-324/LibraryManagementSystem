package models;

import java.awt.BorderLayout; // এটি দিলে strategy ফোল্ডারের ফাইলগুলো সে আজীবন চিনবে
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainMenu extends JFrame {
    
    private static final Color TEAL_PRIMARY = new Color(22, 160, 133);    
    private static final Color TEAL_LIGHT = new Color(26, 188, 156);      
    
    public MainMenu() {
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
                
                Color color1 = new Color(45, 52, 54);   
                Color color2 = new Color(20, 25, 30);  
                
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 255, 255));
        titlePanel.add(titleLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 80, 100));
        
        JButton btnBooks = createMenuButton("📚 Manage Books");
        JButton btnMembers = createMenuButton("👥 Manage Members");
        JButton btnBorrowing = createMenuButton("💰 Test Fine Strategy");
        JButton btnExit = createExitButton("🚪 Exit");
        
        // Button actions
        btnBooks.addActionListener(e -> openBookForm());
        btnMembers.addActionListener(e -> openMemberForm());
        btnBorrowing.addActionListener(e -> openBorrowingTest()); // Strategy Test Call
        btnExit.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(btnBooks);
        buttonPanel.add(btnMembers);
        buttonPanel.add(btnBorrowing);
        buttonPanel.add(btnExit);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JLabel footerLabel = new JLabel("© 2026 Library Management System");
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
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(231, 76, 60));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
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
        try {
            new BookForm().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "BookForm logic not connected yet.");
        }
    }
    
    private void openMemberForm() {
        try {
            new MemberForm().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "MemberForm logic not connected yet.");
        }
    }
    
    // Ekhane Strategy Design Pattern test korar GUI logic
    private void openBorrowingTest() {
        String input = JOptionPane.showInputDialog(this, "How many days is the book late?", "Fine Calculation", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.isEmpty()) {
            try {
                int days = Integer.parseInt(input);
                
                // Member create kora hochche fine test korar jonno
                Member student = new Member("M001", "Rahim", "rahim@mail.com", "01711", 
                                          Member.MembershipType.STUDENT, new Date(), "Dhaka");
                
                Member teacher = new Member("T001", "Prof. Karim", "karim@mail.com", "01822", 
                                          Member.MembershipType.TEACHER, new Date(), "Sylhet");

                String resultMsg = "--- Calculation for " + days + " Days ---\n\n" +
                                   "Student (5 TK/Day): " + student.calculateFine(days) + " TK\n" +
                                   "Teacher (No Fine): " + teacher.calculateFine(days) + " TK";

                JOptionPane.showMessageDialog(this, resultMsg, "Fine Strategy Result", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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