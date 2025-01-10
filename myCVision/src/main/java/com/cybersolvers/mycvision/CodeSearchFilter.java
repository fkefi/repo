package com.cybersolvers.mycvision;

import javax.swing.*;
import java.awt.*;

public class CodeSearchFilter extends JDialog {
    private Filter filter;

    public CodeSearchFilter(Frame parent) {
        super(parent, "��������� �� ������", true);
        filter = new Filter();
        requestCode();
    }

    private void requestCode() {
        String input = JOptionPane.showInputDialog(this,
            "�������� �������� ��� ������ ���:",
            "�������� �������",
            JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                double code = Double.parseDouble(input);
                String result = filter.searchByCode(code);
                JOptionPane.showMessageDialog(this, result);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "�������� �������� ���� ������ ���������� ������.",
                    "������",
                    JOptionPane.ERROR_MESSAGE);
                requestCode(); // ������ ���� ��� ������ �� ��������� ������
            }
        } else {
            dispose(); // ������� �� �������� �� � ������� ������� �������
        }
    }
}