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
    private static final Color PRIMARY_COLOR = new Color(15, 32, 67);    // Deep Midnight Blue
    private static final Color SECONDARY_COLOR = new Color(34, 69, 128);  // Deep Royal Blue
    private static final Color ACCENT_COLOR = new Color(0, 180, 255);     // Modern Vivid Blue
    private static final Color GOLD_COLOR = new Color(255, 215, 0);       // Gold
    private static final Color GLASS_COLOR = new Color(255, 255, 255, 25); // Semi-transparent white
    private static final Color GLASS_BORDER = new Color(255, 255, 255, 50); // Subtle border
    
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
        
        // Garis-garis dekoratif (lebih halus)
        g2d.setStroke(new BasicStroke(0.8f));
        g2d.setColor(new Color(255, 255, 255, 15));
        
        // Garis diagonal
        for (int i = -height; i < width + height; i += 70) {
            g2d.drawLine(i, 0, (int) (i + height * 0.8f), height);
        }
        
        // Lingkaran-lingkaran dekoratif (lebih subtle)
        g2d.setColor(new Color(255, 255, 255, 10));
        for (int i = 0; i < 5; i++) {
            int size = 100 + (i * 90);
            int x = width - size / 2 - 100;
            int y = height - size / 2 - 100;
            g2d.drawOval(x, y, size, size);
        }
        
        // Bintik-bintik dekoratif (lebih sedikit)
        g2d.setColor(new Color(255, 255, 255, 30));
        for (int i = 0; i < 80; i++) {
            int x = (int) (Math.random() * width);
            int y = (int) (Math.random() * height);
            g2d.fillOval(x, y, 1, 1);
        }
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15);
        
        // Logo panel
        JPanel logoPanel = createLogoPanel();
        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(logoPanel, gbc);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel mainTitle = new JLabel("PEMERINTAH KABUPATEN SIDOARJO");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subTitle = new JLabel("SISTEM PENDATAAN WILAYAH ADMINISTRATIF");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(new Color(200, 220, 255));
        subTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel motto = new JLabel("\"Bersama Membangun Sidoarjo yang Lebih Baik\"");
        motto.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        motto.setForeground(new Color(180, 200, 240));
        motto.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(mainTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        titlePanel.add(subTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
        titlePanel.add(motto);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(titlePanel, gbc);
        
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
        
        // Panel Utama dengan Glassmorphism effect
        JPanel glassPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Glass background
                g2d.setColor(GLASS_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Glass border
                g2d.setColor(GLASS_BORDER);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);
                
                g2d.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints glassGbc = new GridBagConstraints();
        glassGbc.gridwidth = GridBagConstraints.REMAINDER;
        glassGbc.anchor = GridBagConstraints.CENTER;
        
        // Judul besar
        JLabel bigTitle = new JLabel("SELAMAT DATANG");
        bigTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        bigTitle.setForeground(Color.WHITE);
        bigTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(bigTitle, glassGbc);
        
        glassPanel.add(Box.createRigidArea(new Dimension(0, 10)), glassGbc);
        
        // Line separator di bawah judul
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 0), 
                                                    50, 0, new Color(255, 255, 255, 100));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, 50, 2);
                
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillRect(50, 0, 100, 2);
                
                gp = new GradientPaint(150, 0, new Color(255, 255, 255, 100), 
                                      200, 0, new Color(255, 255, 255, 0));
                g2d.setPaint(gp);
                g2d.fillRect(150, 0, 50, 2);
                g2d.dispose();
            }
        };
        separator.setOpaque(false);
        separator.setPreferredSize(new Dimension(200, 2));
        glassPanel.add(separator, glassGbc);
        
        glassPanel.add(Box.createRigidArea(new Dimension(0, 25)), glassGbc);
        
        // Deskripsi
        JLabel description = new JLabel("<html><div style='text-align: center; width: 550px; line-height: 1.6;'>"
                + "Akses sistem informasi terpadu untuk pengelolaan data wilayah administratif "
                + "dan kependudukan Kabupaten Sidoarjo secara digital dan efisien."
                + "</div></html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        description.setForeground(new Color(230, 240, 255));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(description, glassGbc);
        
        glassPanel.add(Box.createRigidArea(new Dimension(0, 45)), glassGbc);
        
        // Tombol Masuk
        btnEnter = createEnterButton();
        glassPanel.add(btnEnter, glassGbc);
        
        panel.add(glassPanel, gbc);
        
        // Animasi hover untuk tombol
        btnEnter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEnter.setForeground(GOLD_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnEnter.setForeground(Color.WHITE);
            }
        });
        
        btnEnter.addActionListener(e -> fadeOutAndNavigate());
        
        return panel;
    }
    
    
    private JButton createEnterButton() {
        JButton button = new JButton("MULAI SEKARANG") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient for button
                GradientPaint gp = new GradientPaint(0, 0, ACCENT_COLOR, 
                                                    getWidth(), 0, new Color(0, 150, 255));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Shine effect
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 15, 15);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));
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
        
        JLabel version = new JLabel("Sistem Pendataan v1.0.0");
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