package View;

import Model.Matiere;
import Model.Media;
import Model.Student;
import service.LibrarySystem;
import service.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame implements Observer {

    private Student student;
    private JTable mediaTable;
    private DefaultTableModel tableModel;
    private JList<String> matiereList;
    private JLabel statusLabel;
    private JTextField searchField;

    private String currentMatiereCode = null;

    public StudentDashboard(Student student) {
        this.student = student;

        // Register as Observer
        LibrarySystem.getInstance().attach(this);

        setTitle("Espace Étudiant - " + student.getUsername());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- 1. Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitle = new JLabel("Tableau de Bord Étudiant", SwingConstants.LEFT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        String specName = (student.getSpecialite() != null) ? student.getSpecialite().getNom() : "Général";
        JLabel lblUser = new JLabel("👤 " + student.getUsername() + " (" + specName + ")", SwingConstants.RIGHT);
        lblUser.setForeground(new Color(236, 240, 241));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- 2. Sidebar ---
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(220, 0));
        sidePanel.setBackground(new Color(245, 245, 245));
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel lblListHeader = new JLabel("📚 Mes Matières", SwingConstants.CENTER);
        lblListHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblListHeader.setBorder(new EmptyBorder(10, 0, 10, 0));
        sidePanel.add(lblListHeader, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (student.getMatieresInscrites().isEmpty()) {
            listModel.addElement("Aucune matière");
        } else {
            for (Matiere m : student.getMatieresInscrites()) {
                listModel.addElement(m.getCode());
            }
        }

        matiereList = new JList<>(listModel);
        matiereList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        matiereList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matiereList.setFixedCellHeight(35);
        matiereList.setBackground(new Color(245, 245, 245));
        matiereList.setSelectionBackground(new Color(52, 152, 219));
        matiereList.setSelectionForeground(Color.WHITE);

        matiereList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCode = matiereList.getSelectedValue();
                if (selectedCode != null && !selectedCode.equals("Aucune matière")) {
                    currentMatiereCode = selectedCode;
                    refreshTable(selectedCode);
                    statusLabel.setText("Affichage des ressources pour : " + selectedCode);
                }
            }
        });

        sidePanel.add(new JScrollPane(matiereList), BorderLayout.CENTER);
        add(sidePanel, BorderLayout.WEST);

        // --- 3. Center ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("🔍 Rechercher: "));
        searchField = new JTextField(20);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable(searchField.getText());
            }
        });
        searchPanel.add(searchField);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Type", "Titre", "Auteur", "Année", "Détails"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        mediaTable = new JTable(tableModel);
        mediaTable.setRowHeight(30);
        mediaTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mediaTable.setFillsViewportHeight(true);
        mediaTable.setSelectionBackground(new Color(220, 230, 240));

        // Hide ID column
        mediaTable.getColumnModel().getColumn(0).setMinWidth(0);
        mediaTable.getColumnModel().getColumn(0).setMaxWidth(0);
        mediaTable.getColumnModel().getColumn(0).setWidth(0);

        JTableHeader tableHeader = mediaTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHeader.setBackground(new Color(230, 230, 230));

        centerPanel.add(new JScrollPane(mediaTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- 4. Footer ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        footerPanel.setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("Prêt.");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.DARK_GRAY);

        JButton btnLogout = new JButton("Déconnexion");
        styleButton(btnLogout, new Color(192, 57, 43));
        btnLogout.addActionListener(e -> {
            LibrarySystem.getInstance().detach(this);
            dispose();
            new LoginFrame().setVisible(true);
        });

        footerPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(btnLogout, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        mediaTable.setRowSorter(sorter);
        if (query.trim().length() == 0) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    private void refreshTable(String codeMatiere) {
        tableModel.setRowCount(0);
        List<Media> filteredList = LibrarySystem.getInstance().getMediaByMatiere(codeMatiere);

        if (filteredList.isEmpty()) {
            statusLabel.setText("Aucune ressource trouvée pour " + codeMatiere);
        } else {
            for (Media m : filteredList) {
                String type = m.getClass().getSimpleName();
                String typeDisplay = type;
                String specific = "N/A";

                if (m instanceof Model.Video) {
                    typeDisplay = "🎥 Vidéo";
                    specific = ((Model.Video) m).getDuree() + " min"; // الرقم مباشرة
                } else if (m instanceof Model.Document) {
                    typeDisplay = "📄 Document";
                    specific = ((Model.Document) m).getNombrePages() + " pages"; // الرقم مباشرة
                } else if (m instanceof Model.Quiz) {
                    typeDisplay = "❓ Quiz";
                    specific = ((Model.Quiz) m).getNiveau();
                }


                tableModel.addRow(new Object[]{
                        m.getId(), typeDisplay, m.getTitre(), m.getAuteur(), m.getAnnee(), specific
                });
            }
        }
    }

    // =========================================================================
    //  Correction : Vérification rigoureuse avant notification
    // =========================================================================
    @Override
    public void update(Media m) {
        boolean isRelevant = false;

        // On vérifie si l'un des codes matières du média correspond à l'une des matières de l'étudiant
        for (String mediaCode : m.getCodesMatieres()) {
            // Nettoyage des espaces pour éviter les erreurs de comparaison " NFP121" vs "NFP121"
            String cleanMediaCode = mediaCode.trim();

            for (Matiere studentMat : student.getMatieresInscrites()) {
                if (studentMat.getCode().trim().equalsIgnoreCase(cleanMediaCode)) {
                    isRelevant = true;
                    break;
                }
            }
            if (isRelevant) break;
        }

        // Si le média est pertinent pour cet étudiant (inscrit à la matière)
        if (isRelevant) {
            // Simulation de l'envoi d'email (Console)
            System.out.println("EMAIL TO " + student.getUsername() + ": Nouvelle ressource ajoutée -> " + m.getTitre());

            // Mise à jour de l'interface graphique UNIQUEMENT si l'étudiant regarde déjà cette matière
            if (currentMatiereCode != null) {
                for (String code : m.getCodesMatieres()) {
                    if (code.trim().equalsIgnoreCase(currentMatiereCode.trim())) {
                        refreshTable(currentMatiereCode);
                        break;
                    }
                }
            }

            // Notification visuelle dans l'interface (Status Bar)
            statusLabel.setText("🔔 Nouveau contenu pertinent ajouté : " + m.getTitre());
            statusLabel.setForeground(new Color(39, 174, 96));

            // Pop-up pour attirer l'attention
            JOptionPane.showMessageDialog(this,
                    "Une nouvelle ressource pertinente a été ajoutée :\n" + m.getTitre(),
                    "Notification", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Optionnel : Pour le débogage, voir qui N'EST PAS notifié
            // System.out.println("DEBUG: Pas de notification pour " + student.getUsername() + " (Matière non inscrite)");
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}