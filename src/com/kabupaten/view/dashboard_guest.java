package com.kabupaten.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.Kecamatan;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.File;

public class dashboard_guest extends JFrame {

    private JPanel cardGridPanel;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private JLabel lblTotalKecamatan;
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();

    public dashboard_guest() {
        initComponents();
        setupFrame();
        loadKecamatanCards("");
    }

    private void initComponents() {
        setTitle("Portal Informasi Publik - Kabupaten Sidoarjo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 114));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(79, 172, 254)),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        left.setOpaque(false);

        try {
            ImageIcon ico = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
            Image scaled = ico.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            BufferedImage ci = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ci.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new Ellipse2D.Double(0, 0, 48, 48));
            g2.drawImage(scaled, 0, 0, null);
            g2.setClip(null);
            g2.setColor(new Color(255, 255, 255, 130));
            g2.setStroke(new BasicStroke(2));
            g2.draw(new Ellipse2D.Double(1, 1, 46, 46));
            g2.dispose();
            left.add(new JLabel(new ImageIcon(ci)));
        } catch (Exception ignored) {}

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);
        JLabel t1 = new JLabel("PEMKAB SIDOARJO");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 19));
        t1.setForeground(Color.WHITE);
        JLabel t2 = new JLabel("Portal Informasi Publik");
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t2.setForeground(new Color(190, 215, 255));
        titleBlock.add(t1);
        titleBlock.add(Box.createVerticalStrut(2));
        titleBlock.add(t2);
        left.add(titleBlock);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private JPanel createContentPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(245, 247, 250));

        JPanel topBar = new JPanel(new BorderLayout(16, 0));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 225, 238)),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));

        JPanel titleArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleArea.setOpaque(false);
        JLabel lblTitle = new JLabel("Daftar Kecamatan Kabupaten Sidoarjo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(new Color(30, 40, 70));
        lblTotalKecamatan = new JLabel("   memuat...");
        lblTotalKecamatan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTotalKecamatan.setForeground(new Color(120, 130, 150));
        titleArea.add(lblTitle);
        titleArea.add(lblTotalKecamatan);

        JPanel searchArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        searchArea.setOpaque(false);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 228), 1, true),
                BorderFactory.createEmptyBorder(7, 11, 7, 11)));
        searchArea.add(searchField);
        
        JButton btnCari = makeButton("🔍 Cari", new Color(30, 60, 114), new Color(50, 90, 160));
        btnCari.addActionListener(e -> loadKecamatanCards(searchField.getText().trim()));
        searchField.addActionListener(e -> loadKecamatanCards(searchField.getText().trim()));
        searchArea.add(btnCari);

        topBar.add(titleArea, BorderLayout.WEST);
        topBar.add(searchArea, BorderLayout.EAST);

        cardGridPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        cardGridPanel.setBackground(new Color(245, 247, 250));
        cardGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        scrollPane = new JScrollPane(cardGridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setBackground(new Color(245, 247, 250));

        wrapper.add(topBar, BorderLayout.NORTH);
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadKecamatanCards(String keyword) {
        cardGridPanel.removeAll();

        List<Kecamatan> listKecamatan = new ArrayList<>();
        try {
            listKecamatan = kecamatanDAO.getAllKecamatan();
        } catch (Exception e) {
            e.printStackTrace();
            lblTotalKecamatan.setText("   ⚠ Gagal memuat data");
            cardGridPanel.revalidate();
            cardGridPanel.repaint();
            return;
        }

        int count = 0;
        for (Kecamatan kecamatan : listKecamatan) {
            String nama = kecamatan.getNamaKecamatan();
            
            boolean matchKeyword = true;
            if (!keyword.isEmpty()) {
                matchKeyword = nama.toLowerCase().contains(keyword.toLowerCase());
            }
            
            if (matchKeyword) {
                cardGridPanel.add(createKecamatanCard(kecamatan));
                count++;
            }
        }

        final int total = count;
        SwingUtilities.invokeLater(() -> {
            lblTotalKecamatan.setText("   —   " + total + " kecamatan ditemukan");
        });

        cardGridPanel.revalidate();
        cardGridPanel.repaint();
    }

    private JPanel createKecamatanCard(Kecamatan kecamatan) {
        int jumlahWarga = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(w.id_warga) FROM warga w " +
                        "JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                        "WHERE d.id_kecamatan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, kecamatan.getIdKecamatan());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) jumlahWarga = rs.getInt(1);
            rs.close(); ps.close();
        } catch (Exception ignored) {}
        
        final int totalWarga = jumlahWarga;
        final int totalDesaKel = kecamatan.getJumlahDesa() + kecamatan.getJumlahKelurahan();
        final String fotoPath = kecamatan.getFotoUrl();

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 320));
        card.setBackground(Color.WHITE);
        card.setBorder(new RoundedBorder(16, new Color(218, 228, 245)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.setPreferredSize(new Dimension(320, 150));
        imgPanel.setBackground(new Color(30, 60, 114));
        
        if (fotoPath != null && !fotoPath.trim().isEmpty()) {
            File imgFile = new File(fotoPath);
            if (!imgFile.exists()) {
                String projectPath = System.getProperty("user.dir");
                imgFile = new File(projectPath + "/" + fotoPath);
            }
            
            if (imgFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(imgFile.getPath());
                    int imgW = icon.getIconWidth();
                    int imgH = icon.getIconHeight();
                    int maxW = 300, maxH = 140;
                    double scale = Math.min((double) maxW / imgW, (double) maxH / imgH);
                    int newW = (int) (imgW * scale);
                    int newH = (int) (imgH * scale);
                    Image scaled = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    JLabel lblFoto = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
                    imgPanel.add(lblFoto, BorderLayout.CENTER);
                } catch (Exception e) {
                    drawGradientPlaceholder(imgPanel, kecamatan.getNamaKecamatan());
                }
            } else {
                drawGradientPlaceholder(imgPanel, kecamatan.getNamaKecamatan());
            }
        } else {
            drawGradientPlaceholder(imgPanel, kecamatan.getNamaKecamatan());
        }
        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(12, 16, 16, 16));

        JLabel lblNama = new JLabel(kecamatan.getNamaKecamatan());
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNama.setForeground(new Color(25, 35, 65));
        lblNama.setAlignmentX(LEFT_ALIGNMENT);
        
        String kepalaText = kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "Belum diisi";
        JLabel lblKepala = new JLabel("👤 " + kepalaText);
        lblKepala.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblKepala.setForeground(new Color(100, 115, 140));
        lblKepala.setAlignmentX(LEFT_ALIGNMENT);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(232, 238, 250));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(LEFT_ALIGNMENT);
        
        JPanel desaStat = createStatItem("📌", "Desa/Kelurahan", String.valueOf(totalDesaKel), new Color(79, 172, 254));
        JPanel wargaStat = createStatItem("👥", "Total Warga", formatNumber(totalWarga), new Color(46, 125, 50));
        JPanel pendudukStat = createStatItem("📊", "Jumlah Penduduk", formatNumber(kecamatan.getJumlahPenduduk()), new Color(255, 152, 0));
        String kontak = kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "-";
        JPanel kontakStat = createStatItem("📞", "Kontak", kontak, new Color(156, 39, 176));
        
        statsPanel.add(desaStat);
        statsPanel.add(wargaStat);
        statsPanel.add(pendudukStat);
        statsPanel.add(kontakStat);
        
        JButton btnDetail = new JButton("Lihat Detail  →");
        btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDetail.setBackground(new Color(30, 60, 114));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnDetail.setFocusPainted(false);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setAlignmentX(LEFT_ALIGNMENT);
        btnDetail.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDetail.setBackground(new Color(50, 90, 160)); }
            public void mouseExited(MouseEvent e)  { btnDetail.setBackground(new Color(30, 60, 114)); }
        });
        btnDetail.addActionListener(e -> bukaDetailKecamatan(kecamatan, totalDesaKel, totalWarga));

        info.add(lblNama);
        info.add(Box.createVerticalStrut(4));
        info.add(lblKepala);
        info.add(Box.createVerticalStrut(10));
        info.add(sep);
        info.add(Box.createVerticalStrut(12));
        info.add(statsPanel);
        info.add(Box.createVerticalStrut(12));
        info.add(btnDetail);

        card.add(imgPanel, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);

        // Handler untuk double click dan hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    bukaFormDetailKecamatan(kecamatan);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new RoundedBorder(16, new Color(79, 172, 254)));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new RoundedBorder(16, new Color(218, 228, 245)));
                card.repaint();
            }
        });

        return card;
    }
    
    private void drawGradientPlaceholder(JPanel panel, String namaKecamatan) {
        panel.removeAll();
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(30, 60, 114),
                        getWidth(), getHeight(), new Color(79, 172, 254));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                String inisial = namaKecamatan.length() >= 2 
                        ? namaKecamatan.substring(0, 2).toUpperCase() 
                        : namaKecamatan.substring(0, 1).toUpperCase();
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                g2.setColor(new Color(255, 255, 255, 200));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inisial, (getWidth() - fm.stringWidth(inisial)) / 2, 
                              (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        panel.add(gradientPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
    
    private JPanel createStatItem(String icon, String label, String value, Color iconColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 250, 254));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        iconLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(iconColor);
        valueLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        labelLabel.setForeground(new Color(120, 130, 150));
        labelLabel.setAlignmentX(LEFT_ALIGNMENT);
        
        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(labelLabel);
        
        return panel;
    }
    
    private String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.1f JT", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1f RB", number / 1000.0);
        }
        return String.valueOf(number);
    }
    
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) return "-";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return sdf.format(timestamp);
    }
    
    private void bukaDetailKecamatan(Kecamatan kecamatan, int totalDesaKel, int totalWarga) {
        JDialog dlg = new JDialog(this, "Detail Kecamatan — " + kecamatan.getNamaKecamatan(), true);
        dlg.setSize(800, 700);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(Color.WHITE);

        JPanel dlgHeader = new JPanel(new BorderLayout());
        dlgHeader.setBackground(new Color(30, 60, 114));
        dlgHeader.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JPanel hInfo = new JPanel();
        hInfo.setLayout(new BoxLayout(hInfo, BoxLayout.Y_AXIS));
        hInfo.setOpaque(false);
        JLabel hNama = new JLabel(kecamatan.getNamaKecamatan());
        hNama.setFont(new Font("Segoe UI", Font.BOLD, 22));
        hNama.setForeground(Color.WHITE);
        JLabel hSub = new JLabel(totalDesaKel + " Desa/Kelurahan  |  " + formatNumber(totalWarga) + " Warga");
        hSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hSub.setForeground(new Color(190, 215, 255));
        hInfo.add(hNama);
        hInfo.add(Box.createVerticalStrut(5));
        hInfo.add(hSub);
        dlgHeader.add(hInfo, BorderLayout.WEST);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JPanel infoPanel = createInfoTab(kecamatan, totalDesaKel, totalWarga);
        tabbedPane.addTab("📋 Informasi", infoPanel);
        
        JPanel fotoPanel = createFotoTab(kecamatan);
        tabbedPane.addTab("📷 Foto Kecamatan", fotoPanel);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        btnRow.setBackground(new Color(248, 250, 254));
        btnRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(218, 228, 245)));
        JButton btnTutup = makeButton("Tutup", new Color(108, 117, 125), new Color(80, 90, 100));
        btnTutup.addActionListener(e -> dlg.dispose());
        btnRow.add(btnTutup);

        dlg.add(dlgHeader, BorderLayout.NORTH);
        dlg.add(tabbedPane, BorderLayout.CENTER);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
    
    private JPanel createInfoTab(Kecamatan kecamatan, int totalDesaKel, int totalWarga) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 26, 20, 26));

        String[][] rows = {
            {"🏙️ Nama Kecamatan", kecamatan.getNamaKecamatan()},
            {"👤 Camat / Kepala", kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "-"},
            {"📌 Jumlah Desa", String.valueOf(kecamatan.getJumlahDesa())},
            {"🏘️ Jumlah Kelurahan", String.valueOf(kecamatan.getJumlahKelurahan())},
            {"🏠 Total Wilayah", totalDesaKel + " Desa/Kelurahan"},
            {"👥 Jumlah Penduduk", formatNumber(kecamatan.getJumlahPenduduk()) + " Jiwa"},
            {"👥 Warga Terdaftar", formatNumber(totalWarga) + " Jiwa"},
            {"🏢 Alamat Kantor", kecamatan.getAlamatKantor() != null ? kecamatan.getAlamatKantor() : "-"},
            {"🏠 Alamat Rumah", kecamatan.getAlamatRumahKepala() != null ? kecamatan.getAlamatRumahKepala() : "-"},
            {"📞 No. HP / Telepon", kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "-"},
            {"📅 Dibuat pada", formatTimestamp(kecamatan.getCreatedAt())},
            {"🔄 Terakhir update", formatTimestamp(kecamatan.getUpdatedAt())}
        };

        for (String[] row : rows) {
            JPanel r = new JPanel(new BorderLayout());
            r.setOpaque(false);
            r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
            r.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(238, 242, 252)));

            JLabel key = new JLabel(row[0]);
            key.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            key.setForeground(new Color(110, 120, 145));
            key.setPreferredSize(new Dimension(160, 35));

            JLabel val = new JLabel(row[1]);
            val.setFont(new Font("Segoe UI", Font.BOLD, 13));
            val.setForeground(new Color(25, 35, 65));

            r.add(key, BorderLayout.WEST);
            r.add(val, BorderLayout.CENTER);
            panel.add(r);
            panel.add(Box.createVerticalStrut(5));
        }

        return panel;
    }
    
    private JPanel createFotoTab(Kecamatan kecamatan) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        String fotoPath = kecamatan.getFotoUrl();
        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (fotoPath != null && !fotoPath.trim().isEmpty()) {
            File imgFile = new File(fotoPath);
            if (!imgFile.exists()) {
                String projectPath = System.getProperty("user.dir");
                imgFile = new File(projectPath + "/" + fotoPath);
            }
            
            if (imgFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(imgFile.getPath());
                    int imgW = icon.getIconWidth();
                    int imgH = icon.getIconHeight();
                    int maxW = 700, maxH = 500;
                    double scale = Math.min((double) maxW / imgW, (double) maxH / imgH);
                    int newW = (int) (imgW * scale);
                    int newH = (int) (imgH * scale);
                    Image scaled = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                    lblFoto.setText("");
                } catch (Exception e) {
                    lblFoto.setText("❌ Gagal memuat gambar");
                    lblFoto.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    lblFoto.setForeground(Color.RED);
                }
            } else {
                lblFoto.setText("📷 Belum ada foto untuk kecamatan ini");
                lblFoto.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                lblFoto.setForeground(Color.GRAY);
            }
        } else {
            lblFoto.setText("📷 Belum ada foto untuk kecamatan ini");
            lblFoto.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblFoto.setForeground(Color.GRAY);
        }
        
        JScrollPane scrollPane = new JScrollPane(lblFoto);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel fp = new JPanel(new BorderLayout());
        fp.setBackground(Color.WHITE);
        fp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(218, 225, 238)),
                BorderFactory.createEmptyBorder(9, 20, 9, 20)));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);
        
        JButton btnLoginDoor = createDoorLoginButton();
        leftPanel.add(btnLoginDoor);
        
        JLabel copyright = new JLabel("© 2025 Pemerintah Kabupaten Sidoarjo");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyright.setForeground(new Color(120, 130, 150));
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(copyright);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        center.setOpaque(false);
        JLabel st = new JLabel("● System Active");
        st.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        st.setForeground(new Color(40, 167, 69));
        JLabel vr = new JLabel("v1.0.0");
        vr.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        vr.setForeground(new Color(150, 160, 175));
        center.add(st); 
        center.add(new JLabel("|")); 
        center.add(vr);

        JLabel right = new JLabel("Developed by Imron");
        right.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        right.setForeground(new Color(120, 130, 150));

        fp.add(leftPanel, BorderLayout.WEST);
        fp.add(center, BorderLayout.CENTER);
        fp.add(right, BorderLayout.EAST);
        return fp;
    }

    private JButton createDoorLoginButton() {
        JButton btnLogin = new JButton("🚪");
        btnLogin.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btnLogin.setBackground(new Color(30, 60, 114));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79, 172, 254), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(50, 90, 160));
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 190, 255), 1),
                        BorderFactory.createEmptyBorder(6, 14, 6, 14)));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(30, 60, 114));
                btnLogin.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(79, 172, 254), 1),
                        BorderFactory.createEmptyBorder(6, 14, 6, 14)));
            }
        });
        
        btnLogin.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });
        
        return btnLogin;
    }

    private void setupFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);
        try {
            setIconImage(new ImageIcon(
                    getClass().getResource("/com/kabupaten/img/logo.jpg")).getImage());
        } catch (Exception ignored) {}
    }

    private JButton makeButton(String text, Color bg, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    // ============================================================
    // METHOD FORM DETAIL KECAMATAN (TANPA STATISTIK)
    // ============================================================
    
    private void bukaFormDetailKecamatan(Kecamatan kecamatan) {
        // Tutup form dashboard_guest saat ini
        this.dispose();
        
        // Buat frame baru untuk detail kecamatan
        JFrame detailFrame = new JFrame("Detail Kecamatan - " + kecamatan.getNamaKecamatan());
        detailFrame.setSize(900, 650);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        
        JPanel headerPanel = createDetailHeader(kecamatan);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        tabbedPane.addTab("📋 Informasi Umum", createInfoPanel(kecamatan));
        tabbedPane.addTab("🏘️ Daftar Desa/Kelurahan", createDesaListPanel(kecamatan));
        tabbedPane.addTab("🏠 Data RT/RW", createRTRWPanel(kecamatan));
        tabbedPane.addTab("👥 Data Warga", createWargaPanel(kecamatan));
        // TAB STATISTIK DIHAPUS
        
        JPanel footerPanel = createDetailFooter(detailFrame, this);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        detailFrame.add(mainPanel);
        detailFrame.setVisible(true);
    }
    
    private JPanel createDetailHeader(Kecamatan kecamatan) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 60, 114));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        int totalDesa = kecamatan.getJumlahDesa();
        int totalKelurahan = kecamatan.getJumlahKelurahan();
        int totalWarga = hitungTotalWarga(kecamatan.getIdKecamatan());
        
        JPanel leftInfo = new JPanel();
        leftInfo.setLayout(new BoxLayout(leftInfo, BoxLayout.Y_AXIS));
        leftInfo.setOpaque(false);
        
        JLabel lblNama = new JLabel(kecamatan.getNamaKecamatan());
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblNama.setForeground(Color.WHITE);
        
        JLabel lblCamat = new JLabel("Camat: " + (kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "-"));
        lblCamat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCamat.setForeground(new Color(220, 230, 255));
        
        leftInfo.add(lblNama);
        leftInfo.add(Box.createVerticalStrut(5));
        leftInfo.add(lblCamat);
        
        JPanel rightInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightInfo.setOpaque(false);
        
        rightInfo.add(createStatBox("🏘️ Desa", String.valueOf(totalDesa), new Color(79, 172, 254)));
        rightInfo.add(createStatBox("🏢 Kelurahan", String.valueOf(totalKelurahan), new Color(46, 125, 50)));
        rightInfo.add(createStatBox("👥 Warga", formatNumber(totalWarga), new Color(255, 152, 0)));
        
        header.add(leftInfo, BorderLayout.WEST);
        header.add(rightInfo, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createStatBox(String label, String value, Color color) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(255, 255, 255, 30));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        box.setPreferredSize(new Dimension(100, 70));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblLabel.setForeground(Color.WHITE);
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        box.add(lblValue);
        box.add(Box.createVerticalStrut(5));
        box.add(lblLabel);
        
        return box;
    }
    
    private JPanel createInfoPanel(Kecamatan kecamatan) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        String[][] data = {
            {"🏙️ Nama Kecamatan", kecamatan.getNamaKecamatan()},
            {"👤 Camat / Kepala", kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "-"},
            {"🏢 Alamat Kantor", kecamatan.getAlamatKantor() != null ? kecamatan.getAlamatKantor() : "-"},
            {"🏠 Alamat Rumah", kecamatan.getAlamatRumahKepala() != null ? kecamatan.getAlamatRumahKepala() : "-"},
            {"📞 No. Telepon", kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "-"},
            {"📌 Jumlah Desa", String.valueOf(kecamatan.getJumlahDesa())},
            {"🏘️ Jumlah Kelurahan", String.valueOf(kecamatan.getJumlahKelurahan())},
            {"👥 Jumlah Penduduk", formatNumber(kecamatan.getJumlahPenduduk()) + " Jiwa"},
            {"📅 Dibuat pada", formatTimestamp(kecamatan.getCreatedAt())},
            {"🔄 Terakhir update", formatTimestamp(kecamatan.getUpdatedAt())}
        };
        
        for (int i = 0; i < data.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel lblKey = new JLabel(data[i][0]);
            lblKey.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblKey.setForeground(new Color(30, 60, 114));
            panel.add(lblKey, gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel lblValue = new JLabel(data[i][1]);
            lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblValue.setForeground(new Color(80, 90, 110));
            panel.add(lblValue, gbc);
        }
        
        return panel;
    }
    
    private JPanel createDesaListPanel(Kecamatan kecamatan) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"No", "Nama Desa / Kelurahan", "Jenis", "Nama Kepala"};
        Object[][] data = {};
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM desa_kelurahan WHERE id_kecamatan = ? ORDER BY nama_desa";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, kecamatan.getIdKecamatan());
            ResultSet rs = ps.executeQuery();
            
            List<Object[]> rows = new ArrayList<>();
            int no = 1;
            while (rs.next()) {
                rows.add(new Object[]{
                    no++,
                    rs.getString("nama_desa"),
                    rs.getString("jenis"),
                    rs.getString("nama_kepala") != null ? rs.getString("nama_kepala") : "-"
                });
            }
            rs.close();
            ps.close();
            data = rows.toArray(new Object[0][]);
            
            if (data.length == 0) {
                JLabel infoLabel = new JLabel("Belum ada data desa/kelurahan");
                infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                infoLabel.setForeground(Color.GRAY);
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(infoLabel, BorderLayout.CENTER);
                return panel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Gagal memuat data: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }
        
        JTable table = new JTable(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createRTRWPanel(Kecamatan kecamatan) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"No", "Nama Desa", "RT", "RW", "Ketua RT/RW", "Kontak"};
        Object[][] data = {};
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            String sqlDesa = "SELECT nama_desa FROM desa_kelurahan WHERE id_kecamatan = ?";
            PreparedStatement psDesa = conn.prepareStatement(sqlDesa);
            psDesa.setInt(1, kecamatan.getIdKecamatan());
            ResultSet rsDesa = psDesa.executeQuery();
            
            List<String> desaList = new ArrayList<>();
            while (rsDesa.next()) {
                desaList.add(rsDesa.getString("nama_desa"));
            }
            rsDesa.close();
            psDesa.close();
            
            if (!desaList.isEmpty()) {
                StringBuilder sqlRTRW = new StringBuilder("SELECT * FROM rtrw WHERE nama_desa IN (");
                for (int i = 0; i < desaList.size(); i++) {
                    if (i > 0) sqlRTRW.append(",");
                    sqlRTRW.append("?");
                }
                sqlRTRW.append(") ORDER BY nama_desa, rt, rw");
                
                PreparedStatement psRTRW = conn.prepareStatement(sqlRTRW.toString());
                for (int i = 0; i < desaList.size(); i++) {
                    psRTRW.setString(i + 1, desaList.get(i));
                }
                ResultSet rs = psRTRW.executeQuery();
                
                List<Object[]> rows = new ArrayList<>();
                int no = 1;
                while (rs.next()) {
                    rows.add(new Object[]{
                        no++,
                        rs.getString("nama_desa"),
                        rs.getString("rt"),
                        rs.getString("rw"),
                        rs.getString("nama_ketua") != null ? rs.getString("nama_ketua") : "-",
                        rs.getString("kontak") != null ? rs.getString("kontak") : "-"
                    });
                }
                rs.close();
                psRTRW.close();
                data = rows.toArray(new Object[0][]);
            }
            
            if (data.length == 0) {
                JLabel infoLabel = new JLabel("Belum ada data RT/RW untuk kecamatan ini");
                infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                infoLabel.setForeground(Color.GRAY);
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(infoLabel, BorderLayout.CENTER);
                return panel;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Gagal memuat data RT/RW: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }
        
        JTable table = new JTable(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createWargaPanel(Kecamatan kecamatan) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"No", "NIK", "Nama Lengkap", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir", "Desa"};
        Object[][] data = {};
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT w.*, d.nama_desa FROM warga w " +
                        "JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                        "WHERE d.id_kecamatan = ? ORDER BY d.nama_desa, w.nama_lengkap";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, kecamatan.getIdKecamatan());
            ResultSet rs = ps.executeQuery();
            
            List<Object[]> rows = new ArrayList<>();
            int no = 1;
            while (rs.next()) {
                rows.add(new Object[]{
                    no++,
                    rs.getString("nik") != null ? rs.getString("nik") : "-",
                    rs.getString("nama_lengkap"),
                    rs.getString("jenis_kelamin") != null ? rs.getString("jenis_kelamin") : "-",
                    rs.getString("tempat_lahir") != null ? rs.getString("tempat_lahir") : "-",
                    rs.getDate("tanggal_lahir") != null ? rs.getDate("tanggal_lahir").toString() : "-",
                    rs.getString("nama_desa")
                });
            }
            rs.close();
            ps.close();
            data = rows.toArray(new Object[0][]);
            
            if (data.length == 0) {
                JLabel infoLabel = new JLabel("Belum ada data warga untuk kecamatan ini");
                infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                infoLabel.setForeground(Color.GRAY);
                infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(infoLabel, BorderLayout.CENTER);
                return panel;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Gagal memuat data warga: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }
        
        JTable table = new JTable(data, columns) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDetailFooter(JFrame currentFrame, JFrame previousFrame) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        footer.setBackground(new Color(248, 250, 254));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(218, 228, 245)));
        
        // Tombol Back untuk kembali ke portal publik
        JButton btnBack = new JButton("← Kembali ke Portal Publik");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(new Color(30, 60, 114));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(new Color(50, 90, 160)); }
            public void mouseExited(MouseEvent e) { btnBack.setBackground(new Color(30, 60, 114)); }
        });
        btnBack.addActionListener(e -> {
            currentFrame.dispose(); // Tutup form detail
            // Buat form dashboard_guest baru
            SwingUtilities.invokeLater(() -> {
                dashboard_guest newDashboard = new dashboard_guest();
                newDashboard.setVisible(true);
            });
        });
        
        JButton btnTutup = new JButton("Tutup");
        btnTutup.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTutup.setBackground(new Color(108, 117, 125));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutup.addActionListener(e -> System.exit(0));
        
        footer.add(btnBack);
        footer.add(btnTutup);
        return footer;
    }
    
    // Helper method untuk hitung total warga
    private int hitungTotalWarga(int idKecamatan) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(w.id_warga) FROM warga w " +
                        "JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                        "WHERE d.id_kecamatan = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idKecamatan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ============================================================
    // WRAPLAYOUT CLASS
    // ============================================================
    static class WrapLayout extends FlowLayout {
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) { return layoutSize(target, true); }
        @Override
        public Dimension minimumLayoutSize(Container target)   { return layoutSize(target, false); }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;
                if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
                Insets insets = target.getInsets();
                int maxW = targetWidth - insets.left - insets.right - getHgap() * 2;
                int totalH = 0, rowH = 0, rowW = 0, maxRowW = 0;
                for (int i = 0; i < target.getComponentCount(); i++) {
                    Component c = target.getComponent(i);
                    if (!c.isVisible()) continue;
                    Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
                    if (rowW > 0 && rowW + d.width > maxW) {
                        maxRowW = Math.max(maxRowW, rowW);
                        totalH += rowH + getVgap();
                        rowW = 0; rowH = 0;
                    }
                    rowW += d.width + getHgap();
                    rowH = Math.max(rowH, d.height);
                }
                maxRowW = Math.max(maxRowW, rowW);
                totalH += rowH + insets.top + insets.bottom + getVgap() * 2;
                return new Dimension(maxRowW, totalH);
            }
        }
    }

    // ============================================================
    // ROUNDEDBORDER CLASS
    // ============================================================
    static class RoundedBorder implements Border {
        private final int r;
        private final Color c;
        public RoundedBorder(int r, Color c) { this.r = r; this.c = c; }
        public Insets getBorderInsets(Component comp) { return new Insets(r/2, r/2, r/2, r/2); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c);
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
            g2.dispose();
        }
    }
}