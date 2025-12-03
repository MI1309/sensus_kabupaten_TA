package com.kabupaten.view;

import com.kabupaten.model.*;
import com.kabupaten.dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class guest_dashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable tabelKecamatan, tabelDesa, tabelRTRW;
    private DefaultTableModel modelKecamatan, modelDesa, modelRTRW;
    private JButton btnLogout;
    private JLabel lblUser;
    private JLabel lblWelcome;
    
    // DAO untuk masing-masing entitas
    private KecamatanDAO kecamatanDAO;
    private DesaDAO desaDAO;
    private RTRWDAO rtrwDAO;

    public guest_dashboard() {
        initDAO();
        initComponents();
        setupFrame();
        loadAllData();
    }

    private void initDAO() {
        kecamatanDAO = new KecamatanDAO();
        desaDAO = new DesaDAO();
        rtrwDAO = new RTRWDAO();
    }

    private void initComponents() {
        setTitle("Dashboard Guest - Data Wilayah Kabupaten Sidoarjo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background abu-abu terang
        Color bgColor = new Color(240, 242, 245);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // Header panel (MODERN DESIGN)
        JPanel headerPanel = createModernHeaderPanel();

        // Tabbed pane (MODERN DESIGN)
        tabbedPane = createModernTabbedPane();

        // Footer panel (MODERN DESIGN)
        JPanel footerPanel = createModernFooterPanel();

        // Rakit ke main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ============================================
    // MODERN HEADER PANEL
    // ============================================
    private JPanel createModernHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(39, 82, 139));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(74, 144, 226)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        

        // LEFT SECTION: Logo + Title
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftSection.setOpaque(false);

        // Logo (Circular)
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        BufferedImage circleImage = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 55, 55);
        g2.setClip(circle);
        g2.drawImage(scaledImage, 0, 0, null);
        
        // Add border to logo
        g2.setClip(null);
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Ellipse2D.Double(0, 0, 55, 55));
        g2.dispose();
        ImageIcon roundedIcon = new ImageIcon(circleImage);

        JLabel logoLabel = new JLabel(roundedIcon);
        leftSection.add(logoLabel);

        // Title + Subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SISTEM PENDATAAN KABUPATEN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Dashboard Administrator");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 250));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        leftSection.add(titlePanel);

        // RIGHT SECTION: User Info + Logout
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setOpaque(false);

        // User Card (NEW DESIGN)
        JPanel userCard = new JPanel();
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.Y_AXIS));
        userCard.setBackground(new Color(255, 255, 255, 25));
        userCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // PERBAIKAN: Tampilkan Full Name
        lblWelcome = new JLabel("Selamat Datang,");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblWelcome.setForeground(new Color(200, 220, 250));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblUser = new JLabel("Guest User");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel("● Administrator");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRole.setForeground(new Color(144, 238, 144));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        userCard.add(lblWelcome);
        userCard.add(Box.createVerticalStrut(2));
        userCard.add(lblUser);
        userCard.add(Box.createVerticalStrut(2));
        userCard.add(lblRole);

        rightSection.add(userCard);

        // Modern Logout Button
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(220, 53, 69));
            }
        });
        
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });
        rightSection.add(btnLogout);

        headerPanel.add(leftSection, BorderLayout.WEST);
        headerPanel.add(rightSection, BorderLayout.EAST);

        return headerPanel;
    }

    private JLabel createCircularLogo() {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);

            BufferedImage circleImage = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 55, 55);
            g2.setClip(circle);
            g2.drawImage(scaledImage, 0, 0, null);
            
            g2.setClip(null);
            g2.setColor(new Color(255, 255, 255, 150));
            g2.setStroke(new BasicStroke(2));
            g2.draw(new Ellipse2D.Double(0, 0, 55, 55));
            g2.dispose();
            
            return new JLabel(new ImageIcon(circleImage));
        } catch (Exception e) {
            // Fallback: Create colored circle with text
            BufferedImage placeholder = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(new Color(255, 255, 255, 100));
            g2.fillOval(0, 0, 55, 55);
            
            g2.setColor(new Color(41, 98, 255));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2.drawString("SDA", 8, 34);
            
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(0, 0, 54, 54);
            g2.dispose();
            
            return new JLabel(new ImageIcon(placeholder));
        }
    }

    // ============================================
    // MODERN TABBED PANE
    // ============================================
    private JTabbedPane createModernTabbedPane() {
        JTabbedPane tabPane = new JTabbedPane(JTabbedPane.LEFT);
        
        tabPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabPane.setBackground(new Color(240, 242, 245));
        tabPane.setForeground(new Color(33, 37, 41));
        
        // Custom UI
        tabPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                                            int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    GradientPaint gp = new GradientPaint(x, y, new Color(41, 98, 255), x, y + h, new Color(33, 78, 204));
                    g2.setPaint(gp);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 10, 10);
                
                // Border untuk unselected tabs
                if (!isSelected) {
                    g2.setColor(new Color(220, 224, 229));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(x + 2, y + 2, w - 4, h - 4, 10, 10);
                }
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                    FontMetrics metrics, int tabIndex,
                                    String title, Rectangle textRect,
                                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (isSelected) {
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    g2.setColor(new Color(73, 80, 87));
                }

                String icon = getTabIcon(tabIndex);
                String fullText = icon + " " + title;
                
                g2.drawString(fullText, textRect.x - 5, textRect.y + metrics.getAscent());
            }
            
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 160;
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 50;
            }
        });

        // Add tabs
        tabPane.addTab("Kecamatan", createKecamatanPanel());
        tabPane.addTab("Desa/Kelurahan", createDesaPanel());
        tabPane.addTab("RT/RW", createRTRWPanel());

        return tabPane;
    }

    private String getTabIcon(int index) {
        switch (index) {
            case 0: return "🏙️";
            case 1: return "🏡";
            case 2: return "🏘️";
            default: return "📋";
        }
    }

    // ============================================
    // DATA PANELS
    // ============================================
    private JPanel createKecamatanPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = createPanelHeader("Data Kecamatan", "🏙️");
        
        // Table dalam card putih
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 229), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columns = {"ID", "Nama Kecamatan", "Alamat Kantor", "Kepala", "No HP", "Penduduk", "Desa", "Kelurahan"};
        modelKecamatan = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelKecamatan = createStyledTable(modelKecamatan);
        JScrollPane scrollPane = new JScrollPane(tabelKecamatan);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel(() -> loadKecamatanData());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDesaPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createPanelHeader("Data Desa/Kelurahan", "🏡");
        
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 229), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columns = {"ID", "Nama", "Jenis", "Kecamatan", "Alamat Kantor", "Kepala", "No HP"};
        modelDesa = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelDesa = createStyledTable(modelDesa);
        JScrollPane scrollPane = new JScrollPane(tabelDesa);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel(() -> loadDesaData());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRTRWPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(240, 242, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createPanelHeader("Data RT/RW", "🏘️");
        
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 229), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        String[] columns = {"ID", "RT", "RW", "Format RT/RW", "Desa/Kelurahan", "Ketua", "Kontak", "Alamat"};
        modelRTRW = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelRTRW = createStyledTable(modelRTRW);
        JScrollPane scrollPane = new JScrollPane(tabelRTRW);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel(() -> loadRTRWData());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ============================================
    // HELPER METHODS FOR UI COMPONENTS
    // ============================================
    private JPanel createPanelHeader(String title, String icon) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel(icon + " " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 98, 255));

        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 98, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionBackground(new Color(41, 98, 255, 30));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 235, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        return table;
    }

    private JPanel createButtonPanel(Runnable refreshAction) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton btnRefresh = new JButton("🔄 Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(33, 136, 56));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(40, 167, 69));
            }
        });
        
        btnRefresh.addActionListener(e -> refreshAction.run());
        buttonPanel.add(btnRefresh);

        return buttonPanel;
    }

    // ============================================
    // MODERN FOOTER PANEL
    // ============================================
    private JPanel createModernFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(220, 224, 229)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel lblCopyright = new JLabel("© 2025 Kabupaten Management System");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(108, 117, 125));

        JLabel lblDeveloper = new JLabel("Developed by Imron");
        lblDeveloper.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDeveloper.setForeground(new Color(108, 117, 125));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        statusPanel.setOpaque(false);
        
        JLabel lblStatus = new JLabel("🟡 Guest Mode Active");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(255, 193, 7));
        
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(108, 117, 125));
        
        statusPanel.add(lblStatus);
        statusPanel.add(new JLabel("|"));
        statusPanel.add(lblVersion);

        footerPanel.add(lblCopyright, BorderLayout.WEST);
        footerPanel.add(statusPanel, BorderLayout.CENTER);
        footerPanel.add(lblDeveloper, BorderLayout.EAST);

        return footerPanel;
    }

    // ============================================
    // LOGOUT FUNCTION
    // ============================================
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar dari sistem?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    // ============================================
    // DATA LOADING METHODS
    // ============================================
    private void loadAllData() {
        loadKecamatanData();
        loadDesaData();
        loadRTRWData();
    }

    private void loadKecamatanData() {
        modelKecamatan.setRowCount(0);
        try {
            List<Kecamatan> list = kecamatanDAO.getAllKecamatan();
            for (Kecamatan kec : list) {
                modelKecamatan.addRow(new Object[]{
                    kec.getIdKecamatan(),
                    kec.getNamaKecamatan(),
                    kec.getAlamatKantor(),
                    kec.getNamaKepala(),
                    kec.getNoHp(),
                    String.format("%,d", kec.getJumlahPenduduk()),
                    kec.getJumlahDesa(),
                    kec.getJumlahKelurahan()
                });
            }
        } catch (Exception e) {
            showError("Error loading Kecamatan: " + e.getMessage());
        }
    }

    private void loadDesaData() {
        modelDesa.setRowCount(0);
        try {
            List<Desa> list = desaDAO.getAllDesa();
            for (Desa desa : list) {
                modelDesa.addRow(new Object[]{
                    desa.getIdDesa(),
                    desa.getNamaDesa(),
                    desa.getJenis(),
                    desa.getNamaKecamatan() != null ? desa.getNamaKecamatan() : "-",
                    desa.getAlamatKantor(),
                    desa.getNamaKepala(),
                    desa.getNoHp()
                });
            }
        } catch (Exception e) {
            showError("Error loading Desa: " + e.getMessage());
        }
    }

    private void loadRTRWData() {
        modelRTRW.setRowCount(0);
        try {
            List<RTRW> list = rtrwDAO.getAllRTRW();
            for (RTRW rtrw : list) {
                modelRTRW.addRow(new Object[]{
                    rtrw.getIdRtrw(),
                    rtrw.getRt(),
                    rtrw.getRw(),
                    rtrw.getRtRwFormat(),
                    rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "-",
                    rtrw.getNamaKetua(),
                    rtrw.getKontak(),
                    rtrw.getAlamat()
                });
            }
        } catch (Exception e) {
            showError("Error loading RT/RW: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ============================================
    // SETUP FRAME
    // ============================================
    private void setupFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Could not load window icon");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new guest_dashboard().setVisible(true));
    }
}