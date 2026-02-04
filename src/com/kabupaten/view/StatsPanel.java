package com.kabupaten.view;

import com.kabupaten.dao.DesaDAO;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.dao.WargaDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatsPanel extends JPanel {
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();
    private DesaDAO desaDAO = new DesaDAO();
    private WargaDAO wargaDAO = new WargaDAO();

    public StatsPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lblTitle = new JLabel("Statistik Data Wilayah Kabupaten Sidoarjo");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 37, 41));
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Chart Panel
        MixedChartPanel chartPanel = new MixedChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        // Footer with refresh
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnRefresh = new JButton("Perbarui Grafik");
        btnRefresh.addActionListener(e -> chartPanel.refreshData());
        footerPanel.add(btnRefresh);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // Inner class for Mixed Charts
    private class MixedChartPanel extends JPanel {
        private java.util.List<com.kabupaten.model.Kecamatan> kecamatanList;
        private int totalDesa;
        private int totalKelurahan;

        public MixedChartPanel() {
            setOpaque(false);
            refreshData();
        }

        public void refreshData() {
            kecamatanList = kecamatanDAO.getAllKecamatan();
            // Calculate totals manually or via query if specialized method exists
            // Here we iterate for simplicity and to match the Pie Chart need
            totalDesa = 0;
            totalKelurahan = 0;
            // Since getAllDesa might be heavy, we can use the counts embedded in kecamatan
            // if reliable
            // But kecamatanDAO queries desa table per row.
            // Better: use getJumlahDesa and getJumlahKelurahan from each Kecamatan
            for (com.kabupaten.model.Kecamatan k : kecamatanList) {
                totalDesa += k.getJumlahDesa();
                totalKelurahan += k.getJumlahKelurahan();
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;

            // Split area: Left for Pie, Right for Bar (or Top/Bottom)
            // Let's do Side-by-Side
            int panelWidth = width / 2;

            drawPieChart(g2, 0, 0, panelWidth, height);
            drawBarChart(g2, panelWidth, 0, panelWidth, height);

            // Draw Separator
            g2.setColor(new Color(220, 220, 220));
            g2.drawLine(centerX, 40, centerX, height - 40);
        }

        private void drawPieChart(Graphics2D g2, int x, int y, int w, int h) {
            int total = totalDesa + totalKelurahan;
            if (total == 0)
                return;

            int padding = 60;
            int diameter = Math.min(w, h) - (2 * padding);
            int cx = x + (w - diameter) / 2;
            int cy = y + (h - diameter) / 2;

            // ARC Angles
            int angleDesa = (int) Math.round((double) totalDesa / total * 360);
            int angleKel = 360 - angleDesa;

            // Draw Desa Slice
            g2.setColor(new Color(40, 167, 69)); // Green
            g2.fillArc(cx, cy, diameter, diameter, 90, angleDesa);

            // Draw Kelurahan Slice
            g2.setColor(new Color(255, 193, 7)); // Yellow/Orange
            g2.fillArc(cx, cy, diameter, diameter, 90 + angleDesa, angleKel);

            // Draw Hole (Donut style) - Optional, looks premium
            int holeSize = diameter / 2;
            int hx = cx + (diameter - holeSize) / 2;
            int hy = cy + (diameter - holeSize) / 2;
            g2.setColor(Color.WHITE);
            g2.fillOval(hx, hy, holeSize, holeSize);

            // Draw Labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String title = "Komposisi Wilayah";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(title, x + (w - fm.stringWidth(title)) / 2, y + 30);

            // Legend
            int lX = x + 20;
            int lY = h - 40;
            drawLegend(g2, lX, lY, new Color(40, 167, 69), "Desa: " + totalDesa);
            drawLegend(g2, lX + 100, lY, new Color(255, 193, 7), "Kelurahan: " + totalKelurahan);
        }

        private void drawLegend(Graphics2D g2, int x, int y, Color c, String text) {
            g2.setColor(c);
            g2.fillRoundRect(x, y, 15, 15, 5, 5);
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(text, x + 20, y + 12);
        }

        private void drawBarChart(Graphics2D g2, int x, int y, int w, int h) {
            // Sort top 5 Kecamatan by Population
            kecamatanList.sort((k1, k2) -> Integer.compare(k2.getJumlahPenduduk(), k1.getJumlahPenduduk()));
            int count = Math.min(kecamatanList.size(), 5);

            if (count == 0 || kecamatanList.isEmpty())
                return;

            int padding = 50;
            int chartW = w - (2 * padding);
            int chartH = h - (2 * padding);
            int startX = x + padding + 20; // Extra shift for axis labels
            int startY = y + padding;
            int endY = y + h - padding;

            // Title
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            String title = "Top 5 Kecamatan (Populasi)";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(title, x + (w - fm.stringWidth(title)) / 2, y + 30);

            // Axis
            g2.setColor(Color.GRAY);
            g2.drawLine(startX, endY, startX + chartW, endY); // X Axis
            g2.drawLine(startX, endY, startX, startY); // Y Axis

            int maxPop = kecamatanList.get(0).getJumlahPenduduk();
            if (maxPop == 0)
                maxPop = 1;

            int barStep = chartW / count;
            int barWidth = barStep - 20;

            for (int i = 0; i < count; i++) {
                com.kabupaten.model.Kecamatan k = kecamatanList.get(i);
                int val = k.getJumlahPenduduk();
                int barHeight = (int) ((double) val / maxPop * chartH);
                if (barHeight < 2)
                    barHeight = 2;

                int bx = startX + 10 + (i * barStep);
                int by = endY - barHeight;

                // Bar
                g2.setColor(new Color(39, 82, 139));
                g2.fillRoundRect(bx, by, barWidth, barHeight, 5, 5); // Rounded top bars logic slightly off but
                                                                     // acceptable for visual

                // Text
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));

                // Truncate name if too long
                String name = k.getNamaKecamatan();
                if (name.length() > 8)
                    name = name.substring(0, 8) + "..";

                fm = g2.getFontMetrics();
                g2.drawString(name, bx + (barWidth - fm.stringWidth(name)) / 2, endY + 15);

                // Value text
                String valStr = String.format("%,d", val); // Short format
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                fm = g2.getFontMetrics();
                g2.drawString(valStr, bx + (barWidth - fm.stringWidth(valStr)) / 2, by - 5);
            }
        }
    }
}
