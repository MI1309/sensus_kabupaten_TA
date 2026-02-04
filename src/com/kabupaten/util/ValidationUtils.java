package com.kabupaten.util;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Utility class to apply real-time input filtering to JTextFields.
 */
public class ValidationUtils {

    /**
     * Rejects any characters that are not digits.
     * Optionally limits the maximum length.
     */
    public static void applyNumericFilter(JTextField textField, int maxLength) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValid(fb.getDocument().getLength() + string.length(), string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (isValid(fb.getDocument().getLength() - length + text.length(), text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValid(int nextLength, String text) {
                if (text == null)
                    return true;
                if (maxLength > 0 && nextLength > maxLength)
                    return false;
                return text.matches("\\d*");
            }
        });
    }

    /**
     * Rejects any numeric digits.
     * Allows letters, spaces, and common name symbols.
     */
    public static void applyStringOnlyFilter(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (isValid(string)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (isValid(text)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValid(String text) {
                if (text == null)
                    return true;
                // Only allow letters (a-z, A-Z) and spaces
                return text.matches("[a-zA-Z\\s]*");
            }
        });
    }
}
