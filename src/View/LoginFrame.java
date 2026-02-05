package View;

import Model.Admin;
import Model.Student;
import Model.User;
import service.AuthService;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    // الألوان المستخدمة في التصميم
    private final Color PRIMARY_COLOR = new Color(41, 128, 185); // أزرق
    private final Color HOVER_COLOR = new Color(52, 152, 219);   // أزرق فاتح
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245); // رمادي فاتح للخلفية
    private final Color CARD_BACKGROUND = Color.WHITE;
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("ISSAE Library - Connexion");
        setSize(500, 550); // زيادة الحجم قليلاً للتصميم الجديد
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // تعيين خلفية النافذة الأساسية
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(backgroundPanel);

        // إنشاء بطاقة تسجيل الدخول (اللوحة البيضاء في المنتصف)
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(CARD_BACKGROUND);
        loginCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true), // حدود ناعمة
                new EmptyBorder(40, 40, 40, 40) // حشوة داخلية
        ));

        // --- 1. الجزء العلوي (الشعار والعنوان) ---
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("ISSAE Library");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Bienvenue ! Veuillez vous connecter");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 2. حقول الإدخال ---

        // تسميات الحقول
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        styleLabel(userLabel);

        JLabel passLabel = new JLabel("Mot de passe");
        styleLabel(passLabel);

        // مربعات النص
        userField = createStyledTextField();
        passField = createStyledPasswordField();

        // --- 3. زر الدخول ---
        loginButton = new JButton("SE CONNECTER");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // تخصيص حجم الزر ليمتد على العرض
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setPreferredSize(new Dimension(300, 45));

        // تأثير الماوس (Hover)
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                loginButton.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });

        loginButton.addActionListener(e -> performLogin());

        // --- إضافة العناصر إلى البطاقة ---
        loginCard.add(iconLabel);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(subtitleLabel);
        loginCard.add(Box.createVerticalStrut(30));

        // إضافة الحقول بمحاذاة اليسار (باستخدام حاويات فرعية إذا لزم الأمر، أو مباشرة)
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(CARD_BACKGROUND);
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        fieldsPanel.add(userLabel);
        fieldsPanel.add(Box.createVerticalStrut(5));
        fieldsPanel.add(userField);
        fieldsPanel.add(Box.createVerticalStrut(15));
        fieldsPanel.add(passLabel);
        fieldsPanel.add(Box.createVerticalStrut(5));
        fieldsPanel.add(passField);

        loginCard.add(fieldsPanel);
        loginCard.add(Box.createVerticalStrut(30));
        loginCard.add(loginButton);

        // إضافة البطاقة إلى خلفية النافذة
        backgroundPanel.add(loginCard);

        // زر Enter لتسجيل الدخول
        getRootPane().setDefaultButton(loginButton);
    }

    // دالة مساعدة لتنسيق العناوين الصغيرة
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // دالة مساعدة لإنشاء حقل نصي منسق
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    // دالة مساعدة لإنشاء حقل كلمة مرور منسق
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private void performLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // استدعاء خدمة التحقق
        User user = AuthService.login(username, password);

        if (user != null) {
            dispose();
            if (user instanceof Student) {
                new StudentDashboard((Student) user).setVisible(true);
            } else if (user instanceof Admin) {
                new AdminDashboard((Admin) user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Identifiants incorrects !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}