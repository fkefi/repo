package com.cybersolvers.mycvision;

import java.util.List;
import java.sql.SQLException;

public class Filter {
    private String[][] id;
    private Txtreader reader;
    private SQLiteHandler dbHandler;

    public Filter() {
        reader = new Txtreader();
        try {
            dbHandler = new SQLiteHandler("my_database.db");
            processCandidates();
        } catch (SQLException e) {
            System.err.println("������ ����� ���������: " + e.getMessage());
        }
    }

    private void processCandidates() throws SQLException {
        // �������������� �� ������ toMap ��� �� ������� �� �������
        List<String> names = reader.getFullNames();
        id = new String[names.size()][2];
        
        // ������� ��� ������ id ���� �� �� ������� ��� ���� ��������
        for (int i = 0; i < names.size(); i++) {
            id[i][0] = names.get(i);  // ������������ �� �����
            id[i][1] = generateRandomCode();  // ������������ ��� ������
        }
    
        // ���������� ��� ���� ���������
        dbHandler.insertArray("id", id, names.size(), 2);
        
        // �������� ��� ������ id
        System.out.println("\n������� id (������� ��� �������):");
        System.out.println("----------------------------------------");
        System.out.println("�����\t\t\t�������");
        System.out.println("----------------------------------------");
        for (String[] candidate : id) {
            System.out.printf("%s\t\t%s\n", candidate[0], candidate[1]);
        }
        System.out.println("----------------------------------------\n");
    }

    public String searchByCode(double searchCode) {
        try {
            String searchCodeStr = String.format("%.0f", searchCode);
            String name = "";
            int position = -1;

            // ��������� ���� ������ id
            for (String[] candidate : id) {
                if (candidate[1].equals(searchCodeStr)) {
                    name = candidate[0];
                    break;
                }
            }

            // ���� ��� ������ finalCandidates ��� �� ����
            String[][] finalCandidatesArray = dbHandler.fetchTable("finalCandidates");
            
            // ��������� ����� ��� finalCandidates
            for (int i = 0; i < finalCandidatesArray.length; i++) {
                if (finalCandidatesArray[i][0].equals(searchCode)) {
                    position = i + 1;
                    System.out.println("������� ��� ����: " + position + 
                                     " �� �����: " + finalCandidatesArray[i][1]);
                    break;
                }
            }

            if (position == -1) {
                return "� ������� ��� �������.";
            }

            return String.format("�����: %s, ���� �����������: %d", 
                name, position);
} catch (Exception e) {
            System.err.println("������ ����������: " + e.getMessage());
            return "������ ���� ��� ���������";
        }
    }

    private String generateRandomCode() {
        double code = Math.random() * 1000000;
        return String.format("%.0f", code);
    }
}