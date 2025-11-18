// LoadingScreen.java
package com.kabupaten.sistem;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;



/**
 * Loading Screen untuk Sistem Pendataan Kabupaten
 * Implementasi dengan Java Swing dan animasi custom
 */
public class LoadingScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Konstanta
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color PRIMARY_COLOR = new Color(30, 60, 114);
    private static final Color SECONDARY_COLOR = new Color(42, 82, 152);
    private static final Color ACCENT_COLOR = new Color(79, 172, 254);
    
    // Komponen animasi
    private Timer animationTimer;
    private Timer progressTimer;
    private Timer statusTimer;
    private int progress = 0;
    private int currentStatusIndex = 0;
    private float logoRotation = 0;
    private float circleRotation = 0;
    private List<Sparkle> sparkles = new ArrayList<>();
    private Random random = new Random();
    
    // Status loading
    private final String[] statusMessages = {
        "Menginisialisasi database dan komponen sistem",
        "Memuat konfigurasi aplikasi", 
        "Menghubungkan ke server database",
        "Menyiapkan antarmuka pengguna",
        "Aplikasi siap digunakan"
    };
    
    // Panel custom untuk rendering
    private LoadingPanel loadingPanel;
    
    public LoadingScreen() {
        initializeComponents();
        setupWindow();
        startAnimations();
    }
    
    private void initializeComponents() {
        loadingPanel = new LoadingPanel();
        add(loadingPanel);
    }
    
    private void setupWindow() {
        setTitle("Sistem Pendataan Kabupaten - Loading");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        
        // Membuat window rounded (Java 11+)
        try {
            setShape(new RoundRectangle2D.Double(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, 20, 20));
        } catch (Exception e) {
            // Fallback untuk versi Java lama
            System.out.println("Rounded corners not supported in this Java version");
        }
    }
    
    private void startAnimations() {
        // Timer untuk animasi utama (60 FPS)
        animationTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnimations();
                loadingPanel.repaint();
            }
        });
        animationTimer.start();
        
        // Timer untuk progress bar
        progressTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progress < 100) {
                    progress += 1;
                } else {
                    progressTimer.stop();
                    // Setelah selesai loading, bisa memanggil aplikasi utama
                    Timer closeTimer = new Timer(1000, evt -> {
                        dispose();
                        // Panggil main application di sini
                        new com.kabupaten.view.LoginFrame().setVisible(true);
                    });
                    closeTimer.setRepeats(false);
                    closeTimer.start();
                }
            }
        });
        progressTimer.start();
        
        // Timer untuk update status text
        statusTimer = new Timer(1300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStatusIndex < statusMessages.length - 1) {
                    currentStatusIndex++;
                } else {
                    statusTimer.stop();
                }
            }
        });
        statusTimer.start();
    }
    
    private void updateAnimations() {
        // Update rotasi logo
        logoRotation += 0.5f;
        if (logoRotation >= 360) logoRotation = 0;
        
        // Update rotasi circle
        circleRotation += 0.2f;
        if (circleRotation >= 360) circleRotation = 0;
        
        // Update sparkles
        updateSparkles();
        
        // Tambah sparkle baru secara random
        if (random.nextInt(20) == 0) {
            sparkles.add(new Sparkle(
                random.nextInt(WINDOW_WIDTH),
                random.nextInt(WINDOW_HEIGHT)
            ));
        }
    }
    
    private void updateSparkles() {
        sparkles.removeIf(sparkle -> {
            sparkle.update();
            return sparkle.isExpired();
        });
    }
    
    // Inner class untuk panel custom
    private class LoadingPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        public LoadingPanel() {
            setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            
            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Gambar background gradient
            paintBackground(g2d);
            
            // Gambar decorative circles
            paintDecorativeCircles(g2d);
            
            // Gambar sparkles
            paintSparkles(g2d);
            
            // Gambar logo
            paintLogo(g2d);
            
            // Gambar text
            paintText(g2d);
            
            // Gambar progress bar
            paintProgressBar(g2d);
            
            // Gambar loading dots
            paintLoadingDots(g2d);
            
            // Gambar status text
            paintStatusText(g2d);
            
            // Gambar footer
            paintFooter(g2d);
            
            g2d.dispose();
        }
        
        private void paintBackground(Graphics2D g2d) {
            GradientPaint gradient = new GradientPaint(
                0, 0, PRIMARY_COLOR,
                WINDOW_WIDTH, WINDOW_HEIGHT, SECONDARY_COLOR
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        }
        
        private void paintDecorativeCircles(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(255, 255, 255, 30));
            
            // Circle 1
            AffineTransform oldTransform = g2d.getTransform();
            g2d.translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            g2d.rotate(Math.toRadians(circleRotation));
            g2d.draw(new Ellipse2D.Double(-150, -150, 300, 300));
            g2d.setTransform(oldTransform);
            
            // Circle 2
            g2d.translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
            g2d.rotate(Math.toRadians(-circleRotation * 1.5));
            g2d.draw(new Ellipse2D.Double(-200, -200, 400, 400));
            g2d.setTransform(oldTransform);
        }
        
        private void paintSparkles(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 200));
            for (Sparkle sparkle : sparkles) {
                sparkle.paint(g2d);
            }
        }
        
private void paintLogo(Graphics2D g2d) {
    int logoSize = 100;
    int logoX = WINDOW_WIDTH / 2 - logoSize / 2;
    int logoY = 100;

    try {
        BufferedImage img = ImageIO.read(
            getClass().getResource("/com/kabupaten/img/logo.jpg")
        );

        // ✅ Buat buffer transparan
        BufferedImage circleImage = new BufferedImage(logoSize, logoSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Circle = circleImage.createGraphics();

        // Aktifkan anti-aliasing + quality
        g2Circle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2Circle.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Buat lingkaran
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, logoSize, logoSize);

        // Clip lingkaran
        g2Circle.setClip(circle);

        // Gambar logo ke buffer (otomatis hanya dalam lingkaran)
        g2Circle.drawImage(img, 0, 0, logoSize, logoSize, null);

        // ✅ Tambah border luar
        g2Circle.setClip(null); // reset
        g2Circle.setStroke(new BasicStroke(2f));
        g2Circle.setColor(new Color(255, 255, 255, 200));
        g2Circle.draw(circle);

        g2Circle.dispose();

        // ✅ Tempel hasil akhir ke layar
        g2d.drawImage(circleImage, logoX, logoY, null);

    } catch (Exception e) {
        System.out.println("⚠️ Gagal load logo: " + e.getMessage());
        e.printStackTrace();

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g2d.drawString("LOGO", logoX + logoSize / 2, logoY + logoSize / 2);
    }
}




        
        private void paintText(Graphics2D g2d) {
            // Title
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
            g2d.setColor(Color.WHITE);
            String title = "Sistem Pendataan Kabupaten";
            FontMetrics fm = g2d.getFontMetrics();
            int titleX = (WINDOW_WIDTH - fm.stringWidth(title)) / 2;
            g2d.drawString(title, titleX, 250);
            
            // Subtitle
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            g2d.setColor(new Color(255, 255, 255, 200));
            String subtitle = "Aplikasi Pengelolaan Data Terpadu";
            fm = g2d.getFontMetrics();
            int subtitleX = (WINDOW_WIDTH - fm.stringWidth(subtitle)) / 2;
            g2d.drawString(subtitle, subtitleX, 280);
        }
        
        private void paintProgressBar(Graphics2D g2d) {
            int barWidth = 300;
            int barHeight = 8;
            int barX = (WINDOW_WIDTH - barWidth) / 2;
            int barY = 320;
            
            // Progress bar background
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.fill(new RoundRectangle2D.Double(barX, barY, barWidth, barHeight, barHeight, barHeight));
            
            // Progress bar fill
            int fillWidth = (int) (barWidth * (progress / 100.0));
            if (fillWidth > 0) {
                GradientPaint progressGradient = new GradientPaint(
                    barX, barY, ACCENT_COLOR,
                    barX + fillWidth, barY, new Color(0, 242, 254)
                );
                g2d.setPaint(progressGradient);
                g2d.fill(new RoundRectangle2D.Double(barX, barY, fillWidth, barHeight, barHeight, barHeight));
            }
            
            // Progress text
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2d.setColor(new Color(255, 255, 255, 230));
            String progressText = "Memuat aplikasi... " + progress + "%";
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (WINDOW_WIDTH - fm.stringWidth(progressText)) / 2;
            g2d.drawString(progressText, textX, 350);
        }
        
        private void paintLoadingDots(Graphics2D g2d) {
            int dotSize = 12;
            int dotSpacing = 8;
            int totalWidth = 3 * dotSize + 2 * dotSpacing;
            int startX = (WINDOW_WIDTH - totalWidth) / 2;
            int dotY = 380;
            
            long time = System.currentTimeMillis();
            for (int i = 0; i < 3; i++) {
                double phase = (time + i * 200) / 500.0;
                double scale = 0.8 + 0.4 * Math.abs(Math.sin(phase));
                int opacity = (int) (100 + 155 * Math.abs(Math.sin(phase)));
                
                g2d.setColor(new Color(ACCENT_COLOR.getRed(), ACCENT_COLOR.getGreen(), ACCENT_COLOR.getBlue(), opacity));
                int scaledSize = (int) (dotSize * scale);
                int dotX = startX + i * (dotSize + dotSpacing) + (dotSize - scaledSize) / 2;
                g2d.fill(new Ellipse2D.Double(dotX, dotY + (dotSize - scaledSize) / 2, scaledSize, scaledSize));
            }
        }
        
        private void paintStatusText(Graphics2D g2d) {
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2d.setColor(new Color(255, 255, 255, 180));
            String status = statusMessages[currentStatusIndex];
            FontMetrics fm = g2d.getFontMetrics();
            int statusX = (WINDOW_WIDTH - fm.stringWidth(status)) / 2;
            g2d.drawString(status, statusX, 430);
        }
        
        private void paintFooter(Graphics2D g2d) {
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2d.setColor(new Color(255, 255, 255, 128));
            
            // Version info
            String version = "v1.0.0 - Build 2025.09";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(version, WINDOW_WIDTH - fm.stringWidth(version) - 20, WINDOW_HEIGHT - 20);
            
            // Copyright
            String copyright = "© 2025 Pemerintah Kabupaten";
            g2d.drawString(copyright, 20, WINDOW_HEIGHT - 20);
        }
    }
    
    // Inner class untuk sparkle effects
    private static class Sparkle {
        private int x, y;
        private int life = 60; // 60 frames = 1 second at 60fps
        private int maxLife = 60;
        private Random random = new Random();
        
        public Sparkle(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void update() {
            life--;
            y -= 2; // Move upward
        }
        
        public boolean isExpired() {
            return life <= 0;
        }
        
        public void paint(Graphics2D g2d) {
            float alpha = (float) life / maxLife;
            int opacity = (int) (255 * alpha);
            g2d.setColor(new Color(255, 255, 255, Math.max(0, opacity)));
            g2d.fill(new Ellipse2D.Double(x, y, 3, 3));
        }
    }
    
    // Method untuk cleanup
    public void cleanup() {
        if (animationTimer != null) animationTimer.stop();
        if (progressTimer != null) progressTimer.stop();
        if (statusTimer != null) statusTimer.stop();
    }
    
    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }
    
    // Main method untuk testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new LoadingScreen().setVisible(true);
        });
    }
}