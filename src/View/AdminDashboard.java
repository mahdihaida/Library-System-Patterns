package View;

import Model.*;
import service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {

    // حقول الإدخال
    JTextField tTitle, tAuthor, tYear, tDesc, tExtra, tIdToEdit;
    JComboBox<String> cbType;
    JComboBox<String> cbCodes;
    JLabel lblSpecific;
    private Admin currentAdmin;

    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;
        setTitle("CNAM Liban - Library Manager (Admin)");
        setSize(850, 750); // حجم مناسب
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 1. Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(158, 11, 51));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 110));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblLogoMain = new JLabel("le cnam", SwingConstants.CENTER);
        lblLogoMain.setForeground(Color.WHITE);
        lblLogoMain.setFont(new Font("Serif", Font.PLAIN, 32));
        lblLogoMain.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLogoSub = new JLabel("Liban", SwingConstants.CENTER);
        lblLogoSub.setForeground(Color.WHITE);
        lblLogoSub.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblLogoSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setForeground(new Color(255, 255, 255, 100));

        JLabel lblTitle = new JLabel("Library Resource Manager", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(255, 255, 255, 200));
        lblTitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogoMain);
        headerPanel.add(lblLogoSub);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(sep);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblTitle);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- حقل ID ---
        addFormField(formPanel, "ID (Select from View All):", tIdToEdit = new JTextField(), gbc, 0);
        tIdToEdit.setEditable(false); // جعله للقراءة فقط لأنه يأتي من الجدول
        tIdToEdit.setBackground(new Color(230, 230, 230));

        // -- النوع --
        cbType = new JComboBox<>(new String[]{"Video", "Document", "Quiz"});
        cbType.setBackground(Color.WHITE);
        cbType.addActionListener(e -> updateSpecificLabel());
        addFormField(formPanel, "Media Type:", cbType, gbc, 1);

        addFormField(formPanel, "Title:", tTitle = new JTextField(), gbc, 2);
        addFormField(formPanel, "Author:", tAuthor = new JTextField(), gbc, 3);
        addFormField(formPanel, "Year:", tYear = new JTextField(), gbc, 4);

        // -- الوصف (تأكدنا من وجوده) --
        addFormField(formPanel, "Description:", tDesc = new JTextField(), gbc, 5);

        // 1. جلب الأكواد من ملف XML
        String[] codes = LibrarySystem.getInstance().getMatiereCodesFromXML();

// 2. إذا لم يجد شيئاً في الملف (أو حدث خطأ)، نضع قيم افتراضية لتجنب القائمة الفارغة
        if (codes == null || codes.length == 0) {
            codes = new String[]{"Général", "NFP121", "NFA032"};
        }

// 3. إنشاء القائمة المنسدلة
        cbCodes = new JComboBox<>(codes);
        cbCodes.setBackground(Color.WHITE);
        cbCodes.setSelectedItem(null); // لجعله فارغاً في البداية

// 4. الإضافة للواجهة
        addFormField(formPanel, "Code (Select):", cbCodes, gbc, 6);

        lblSpecific = new JLabel("Details:");
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        formPanel.add(lblSpecific, gbc);

        tExtra = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formPanel.add(tExtra, gbc);

        add(formPanel, BorderLayout.CENTER);

        // 3. Footer Buttons
        JPanel footerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Row 1
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row1.setOpaque(false);
        JButton btnAdd = new JButton("Add New");
        styleButton(btnAdd, new Color(46, 204, 113));
        btnAdd.addActionListener(e -> saveAction(false));

        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(243, 156, 18));
        btnUpdate.addActionListener(e -> saveAction(true));

        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteAction());

        row1.add(btnAdd);
        row1.add(btnUpdate);
        row1.add(btnDelete);

        // Row 2
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row2.setOpaque(false);
        JButton btnViewAll = new JButton("View All & Refresh");
        styleButton(btnViewAll, new Color(52, 73, 94));
        btnViewAll.addActionListener(e -> viewAllAction());

        JButton btnClear = new JButton("Clear Fields");
        styleButton(btnClear, Color.GRAY);
        btnClear.addActionListener(e -> clearFields());

        row2.add(btnViewAll);
        row2.add(btnClear);

        // Row 3
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row3.setOpaque(false);
        JButton btnCSV = new JButton("Export CSV");
        styleButton(btnCSV, new Color(52, 152, 219));
        btnCSV.addActionListener(e -> exportCSV());

        JButton btnXML = new JButton("Export XML");
        styleButton(btnXML, new Color(155, 89, 182));
        btnXML.addActionListener(e -> exportXML());

        JButton btnStats = new JButton("Statistics");
        styleButton(btnStats, new Color(241, 196, 15));
        btnStats.setForeground(Color.BLACK);
        btnStats.addActionListener(e -> showStats());

        row3.add(btnCSV);
        row3.add(btnXML);
        row3.add(btnStats);

        // Row 4
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row4.setOpaque(false);
        JButton btnLogout = new JButton("Log Out");
        styleButton(btnLogout, new Color(44, 62, 80));
        btnLogout.addActionListener(e -> logoutAction());
        row4.add(btnLogout);

        footerPanel.add(row1);
        footerPanel.add(row2);
        footerPanel.add(row3);
        footerPanel.add(row4);
        add(footerPanel, BorderLayout.SOUTH);

        updateSpecificLabel();
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void updateSpecificLabel() {
        String type = (String) cbType.getSelectedItem();
        if ("Video".equals(type)) lblSpecific.setText("Duration (Minutes):");
        else if ("Document".equals(type)) lblSpecific.setText("Number of Pages:");
        else if ("Quiz".equals(type)) lblSpecific.setText("Level (Easy/Medium/Hard):");
    }

    private void saveToCatalogueFile() {
        try {
            LibrarySystem.getInstance().saveToCatalogueFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // دالة لتحديث الجدول بناءً على كلمة البحث
    private void saveAction(boolean isUpdate) {
        try {
            String type = (String) cbType.getSelectedItem();
            String title = tTitle.getText();
            String auth = tAuthor.getText();

            int year;
            try {
                year = Integer.parseInt(tYear.getText());
            } catch (NumberFormatException e) {
                throw new Exception("Year must be a valid number.");
            }

            String desc = tDesc.getText();
            String extraStr = tExtra.getText();
            String code = (String) cbCodes.getSelectedItem();

            if (title.isEmpty() || code == null) {
                throw new Exception("Title and Code are required.");
            }

            Object extraInfo;
            if ("Quiz".equals(type)) {
                extraInfo = extraStr;
            } else {
                try {
                    extraInfo = Integer.parseInt(extraStr);
                } catch (NumberFormatException e) {
                    throw new Exception("Specific info (Duration/Pages) must be a number.");
                }
            }

            Media m = MediaFactory.createMedia(type, 0, title, auth, year, desc, extraInfo);

            if (m != null) {
                m.ajouterCodeMatiere(code);

                if (isUpdate) {
                    if (tIdToEdit.getText().isEmpty()) throw new Exception("Select an item first.");
                    int id = Integer.parseInt(tIdToEdit.getText());
                    m.setId(id);

                    boolean success = LibrarySystem.getInstance().updateMedia(m);
                    if (success) {
                        saveToCatalogueFile();
                        JOptionPane.showMessageDialog(this, "Updated Successfully!");
                    }
                } else {
                    // هنا سيتم استدعاء notifyObservers في LibrarySystem
                    // بفضل تصحيحنا لكلاس Student، لن يحدث ClassCastException هنا
                    LibrarySystem.getInstance().addMedia(m);

                    saveToCatalogueFile();
                    JOptionPane.showMessageDialog(this, "Added Successfully! Notifications sent to students.");
                }
                clearFields();
            }
        } catch (Exception ex) {
            // تتبع الخطأ في الكونسول للتأكد
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- تصحيح دالة viewAllAction لعرض قائمة المواد بشكل نصي ---
    private void viewAllAction() {
        JFrame viewFrame = new JFrame("All Resources - Select to Edit/Delete");
        viewFrame.setSize(950, 500);
        viewFrame.setLocationRelativeTo(this);

        String[] columns = {"ID", "Type", "Title", "Author", "Year", "Description", "Codes (Matieres)", "Details"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Media m : LibrarySystem.getInstance().getCatalogue()) {
            // تحويل قائمة المواد ArrayList إلى نص مفصول بفاصلة لعرضه في الجدول
            String codesString = String.join(", ", m.getCodesMatieres());

            model.addRow(new Object[]{
                    m.getId(),
                    m.getType(),
                    m.getTitre(),
                    m.getAuteur(),
                    m.getAnnee(),
                    m.getDescription(),
                    codesString, // عرض النص المصحح هنا
                    getExtraInfo(m)
            });
        }

        JTable table = new JTable(model);

        // عند اختيار سطر، نقوم بتعبئة الحقول
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tIdToEdit.setText(model.getValueAt(row, 0).toString());
                cbType.setSelectedItem(model.getValueAt(row, 1).toString());
                tTitle.setText(model.getValueAt(row, 2).toString());
                tAuthor.setText(model.getValueAt(row, 3).toString());
                tYear.setText(model.getValueAt(row, 4).toString());
                tDesc.setText(model.getValueAt(row, 5).toString());

                // للحصول على الكود الأول في حال وجود قائمة
                String firstCode = model.getValueAt(row, 6).toString().split(",")[0].trim();
                cbCodes.setSelectedItem(firstCode);

                tExtra.setText(model.getValueAt(row, 7).toString());
                updateSpecificLabel();
            }
        });

        viewFrame.add(new JScrollPane(table));
        viewFrame.setVisible(true);
    }

    private void deleteAction() {
        try {
            if (tIdToEdit.getText().isEmpty()) throw new Exception("Select an item from 'View All' first.");
            int id = Integer.parseInt(tIdToEdit.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Delete ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = LibrarySystem.getInstance().deleteMedia(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Deleted Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // تم التعديل: إضافة عمودي Type و Description وتعبئة الحقول عند الاختيار
    // =========================================================================

    private void exportCSV() {
        new CSVExporter().export(LibrarySystem.getInstance().getCatalogue());
        showResultFrame("Export CSV", "Exported to CSV.");
    }

    private void exportXML() {
        new XMLExporter().export(LibrarySystem.getInstance().getCatalogue());
        showResultFrame("Export XML", "Exported to XML.");
    }



    private void showStats() {
        StatsVisitor visitor = new StatsVisitor();

        for (Media m : LibrarySystem.getInstance().getCatalogue()) {
            m.accept(visitor);
        }

        // عرض النتائج في نافذة
        String statsText = "=== Statistiques de la Bibliothèque ===\n" +
                "Nombre de Vidéos   : " + visitor.getCountVideo() + " (Total durée: " + visitor.getTotalDureeVideo() + " mins)\n" +
                "Nombre de Documents: " + visitor.getCountDocument() + "\n" +
                "Nombre de Quiz     : " + visitor.getCountQuiz() + "\n" +
                "=======================================";

        JOptionPane.showMessageDialog(null, statsText, "Stats Library", JOptionPane.INFORMATION_MESSAGE);
    }



    private void showResultFrame(String title, String content) {
        JFrame f = new JFrame(title); f.setSize(400,300); f.setLocationRelativeTo(this);
        JTextArea a = new JTextArea(content); a.setEditable(false); f.add(new JScrollPane(a)); f.setVisible(true);
    }

    private String getExtraInfo(Media m) {
        if (m instanceof Video) return String.valueOf(((Video) m).getDuree());
        if (m instanceof Document) return String.valueOf(((Document) m).getNombrePages());
        if (m instanceof Quiz) return ((Quiz) m).getNiveau();
        return "";
    }

    private void logoutAction() { this.dispose(); new LoginFrame().setVisible(true); }

    private void clearFields() {
        tIdToEdit.setText(""); tTitle.setText(""); tAuthor.setText(""); tYear.setText(""); tDesc.setText(""); tExtra.setText(""); cbCodes.setSelectedItem(null);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color); btn.setForeground(Color.WHITE); btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color.darker()), BorderFactory.createEmptyBorder(8, 15, 8, 15)));
    }
}