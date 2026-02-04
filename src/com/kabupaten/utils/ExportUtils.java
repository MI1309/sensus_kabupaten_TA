package com.kabupaten.utils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportUtils {

    public static void exportToCSV(JTable table, String filenameIdentifier) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Data ke CSV");

        // Default filename: Data_Kecamatan_20250101_120000.csv
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String defaultFilename = "Data_" + filenameIdentifier + "_" + timestamp + ".csv";
        fileChooser.setSelectedFile(new File(defaultFilename));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure extension
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (FileWriter fw = new FileWriter(fileToSave)) {
                TableModel model = table.getModel();

                // Header
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i));
                    if (i < model.getColumnCount() - 1)
                        fw.write(",");
                }
                fw.write("\n");

                // Rows
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        String valStr = (value != null) ? value.toString() : "";

                        // Escape quotes and commas
                        valStr = valStr.replace("\"", "\"\"");
                        if (valStr.contains(",") || valStr.contains("\n")) {
                            valStr = "\"" + valStr + "\"";
                        }

                        fw.write(valStr);
                        if (j < model.getColumnCount() - 1)
                            fw.write(",");
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(null,
                        "Data berhasil diekspor ke:\n" + fileToSave.getAbsolutePath(),
                        "Ekspor Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Gagal mengekspor data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
