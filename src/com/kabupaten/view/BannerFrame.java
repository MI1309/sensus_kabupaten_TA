package com.kabupaten.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.io.InputStream;

/**
 * Banner Frame untuk tampilan awal aplikasi dengan tombol masuk
 */
public class BannerFrame extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(30, 60, 114);
    private static final Color SECONDARY_COLOR = new Color(42, 82, 152);
    private static final Color ACCENT_COLOR = new Color(79, 172, 254);
    private static final Color GOLD_COLOR = new Color(255, 215, 0);
    private static final Color LIGHT_ACCENT = new Color(100, 190, 255);
    
    private JButton btnEnter;
    private Timer fadeTimer;
    private float opacity = 0f;
    private BufferedImage logoImage;
    
    public BannerFrame() {
        loadLogo();
        initComponents();
        setupFrame();
        startFadeInAnimation();
    }
    
    private void loadLogo() {
        try {
            // Coba load logo dari resource
            InputStream is = getClass().getResourceAsStream("/com/kabupaten/img/logo.jpg");
            if (is != null) {
                logoImage = ImageIO.read(is);
            }
        } catch (Exception e) {
            System.err.println("Logo not found: " + e.getMessage());
        }
    }
    
    private void initComponents() {
        setTitle("Sistem Pendataan Kabupaten Sidoarjo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        
        // Main panel dengan background gradient
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Pola dekoratif
                drawDecorativePattern(g2d);
                
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        
        // Header dengan logo dan judul
        JPanel headerPanel = createHeaderPanel();
        
        // Center panel dengan konten utama
        JPanel centerPanel = createCenterPanel();
        
        // Footer panel
        JPanel footerPanel = createFooterPanel();
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Close button (X) di pojok kanan atas
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        closePanel.setOpaque(false);
        JButton btnClose = createCloseButton();
        closePanel.add(btnClose);
        
        mainPanel.add(closePanel, BorderLayout.NORTH);
        
        add(mainPanel);
    }
    
    private void drawDecorativePattern(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        
        // Garis-garis dekoratif
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.setColor(new Color(255, 255, 255, 25));
        
        // Garis diagonal
        for (int i = -height; i < width + height; i += 60) {
            g2d.drawLine(i, 0, i + height, height);
        }
        
        // Lingkaran-lingkaran dekoratif
        g2d.setColor(new Color(255, 255, 255, 15));
        for (int i = 0; i < 6; i++) {
            int size = 80 + (i * 70);
            int x = width - size - 40;
            int y = height - size - 40;
            g2d.drawOval(x, y, size, size);
        }
        
        for (int i = 0; i < 4; i++) {
            int size = 50 + (i * 45);
            int x = -size + 20;
            int y = -size + 20;
            g2d.drawOval(x, y, size, size);
        }
        
        // Bintik-bintik dekoratif
        g2d.setColor(new Color(255, 255, 255, 40));
        for (int i = 0; i < 150; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            g2d.fillOval(x, y, 2, 2);
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Logo panel (kiri)
        JPanel logoPanel = createLogoPanel();
        
        // Title panel (tengah)
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("PEMERINTAH KABUPATEN SIDOARJO");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("SISTEM PENDATAAN WILAYAH ADMINISTRATIF");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(220, 230, 255));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel motto = new JLabel("\"Bersama Membangun Sidoarjo yang Lebih Baik\"");
        motto.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        motto.setForeground(new Color(200, 210, 250));
        motto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(mainTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        titlePanel.add(motto);
        
        panel.add(logoPanel, BorderLayout.WEST);
        panel.add(titlePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 70;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillOval(x + 3, y + 3, size, size);
                
                // Lingkaran latar putih transparan
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillOval(x, y, size, size);
                
                // Border emas
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.setColor(GOLD_COLOR);
                g2d.drawOval(x, y, size, size);
                
                if (logoImage != null) {
                    // Gambar logo dengan scaling
                    int imgSize = size - 20;
                    int imgX = x + 10;
                    int imgY = y + 10;
                    g2d.drawImage(logoImage, imgX, imgY, imgSize, imgSize, null);
                } else {
                    // Fallback: Gambar lambang
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
                    g2d.setColor(PRIMARY_COLOR);
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = "S";
                    int textX = x + (size - fm.stringWidth(text)) / 2;
                    int textY = y + ((size - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
                
                g2d.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(90, 90));
        panel.setMaximumSize(new Dimension(90, 90));
        panel.setOpaque(false);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 40, 60));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Judul besar
        JLabel bigTitle = new JLabel("SELAMAT DATANG");
        bigTitle.setFont(new Font("Segoe UI", Font.BOLD, 44));
        bigTitle.setForeground(Color.WHITE);
        bigTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(bigTitle, gbc);
        
        panel.add(Box.createRigidArea(new Dimension(0, 15)), gbc);
        
        // Deskripsi
        JLabel description = new JLabel("<html><div style='text-align: center; width: 650px; line-height: 1.5;'>"
                + "Sistem informasi untuk pengelolaan data wilayah administratif Kabupaten Sidoarjo, "
                + "meliputi data Kecamatan, Desa/Kelurahan, RT/RW, dan Kependudukan."
                + "</div></html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        description.setForeground(new Color(240, 245, 255));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(description, gbc);
        
        panel.add(Box.createRigidArea(new Dimension(0, 45)), gbc);
        
        
        panel.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
        
        // Tombol Masuk
        btnEnter = createEnterButton();
        btnEnter.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnEnter, gbc);
        
        // Animasi hover untuk tombol
        btnEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEnter.setBackground(LIGHT_ACCENT);
                btnEnter.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    BorderFactory.createEmptyBorder(14, 50, 14, 50)
                ));
                btnEnter.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnEnter.setBackground(ACCENT_COLOR);
                btnEnter.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
                    BorderFactory.createEmptyBorder(14, 50, 14, 50)
                ));
            }
        });
        
        btnEnter.addActionListener(e -> fadeOutAndNavigate());
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, String subtitle) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255, 25));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(190, 130));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(new Color(220, 230, 255));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(GOLD_COLOR);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subtitleLabel.setForeground(new Color(200, 210, 250));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(subtitleLabel);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 255, 255, 45));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(255, 255, 255, 25));
            }
        });
        
        return card;
    }
    
    private JButton createEnterButton() {
        JButton button = new JButton("➡️  MASUK KE SISTEM  ⬅️");
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(14, 50, 14, 50)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 40, 25, 40));
        
        JLabel copyright = new JLabel("© 2025 Pemerintah Kabupaten Sidoarjo. All Rights Reserved.");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyright.setForeground(new Color(180, 200, 240));
        
        JLabel version = new JLabel("Sistem Pendataan v1.0.0 | Developed by Imron");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(180, 200, 240));
        
        panel.add(copyright, BorderLayout.WEST);
        panel.add(version, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createCloseButton() {
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(0, 0, 0, 0));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setForeground(Color.RED);
                closeBtn.setBackground(new Color(255, 255, 255, 50));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setForeground(Color.WHITE);
                closeBtn.setBackground(new Color(0, 0, 0, 0));
            }
        });
        
        closeBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                BannerFrame.this,
                "Apakah Anda yakin ingin keluar dari aplikasi?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        return closeBtn;
    }
    
private void startFadeInAnimation() {
    // Cek apakah translucency didukung oleh sistem
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    
    if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
        // Jika didukung, jalankan animasi fade
        setOpacity(0f);
        fadeTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeTimer.stop();
                }
                setOpacity(opacity);
            }
        });
        fadeTimer.start();
    } else {
        // Jika tidak didukung, langsung tampilkan tanpa animasi
        System.out.println("Translucency not supported, skipping fade animation");
        setVisible(true);
    }
}
    
private void fadeOutAndNavigate() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    
    if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
        Timer fadeOutTimer = new Timer(16, new ActionListener() {
            float fadeOutOpacity = 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                fadeOutOpacity -= 0.05f;
                if (fadeOutOpacity <= 0.0f) {
                    fadeOutOpacity = 0.0f;
                    ((Timer) e.getSource()).stop();
                    
                    // Navigasi ke guest dashboard
                    BannerFrame.this.dispose();
                    SwingUtilities.invokeLater(() -> {
                        new dashboard_guest().setVisible(true);
                    });
                }
                setOpacity(fadeOutOpacity);
            }
        });
        fadeOutTimer.start();
    } else {
        // Langsung navigasi tanpa animasi
        BannerFrame.this.dispose();
        SwingUtilities.invokeLater(() -> {
            new dashboard_guest().setVisible(true);
        });
    }
}
    
    private void setupFrame() {
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 1100, 650, 25, 25));
    }
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new BannerFrame().setVisible(true);
        });
    }
}