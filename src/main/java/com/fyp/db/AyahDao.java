package com.fyp.db;

import com.fyp.cli.Ayah;
import com.fyp.client.FailedToGetAyah;
import com.fyp.client.FailedToGetAyahs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AyahDao {

    private DatabaseConnector databaseConnector = new DatabaseConnector();

    public AyahDao() {
        this.databaseConnector = databaseConnector;
    }

    // Method to retrieve Ayahs by SurahID
    public List<Ayah> getAyahsBySurahId(int surahId) throws FailedToGetAyahs {
        List<Ayah> ayahs = new ArrayList<>();
        String query = "SELECT * FROM Ayahs WHERE SurahID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, surahId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ayah ayah = new Ayah();
                    ayah.setAyahId(rs.getInt("AyahID"));
                    ayah.setSurahId(rs.getInt("SurahID"));
                    ayah.setAyahNumber(rs.getInt("Ayah_Number"));
                    ayah.setArabicText(rs.getString("Arabic_Text"));
                    ayah.setWordCount(rs.getInt("Word_Count"));
                    ayahs.add(ayah);
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetAyahs();
        }
        return ayahs;
    }

    // Method to retrieve a specific Ayah by ID
    public Ayah getAyahById(int ayahId) throws FailedToGetAyah {
        Ayah ayah = null;
        String query = "SELECT * FROM Ayahs WHERE AyahID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ayahId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ayah = new Ayah();
                    ayah.setAyahId(rs.getInt("AyahID"));
                    ayah.setSurahId(rs.getInt("SurahID"));
                    ayah.setAyahNumber(rs.getInt("Ayah_Number"));
                    ayah.setArabicText(rs.getString("Arabic_Text"));
                    ayah.setWordCount(rs.getInt("Word_Count"));
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetAyah();
        }
        return ayah;
    }

    public int getSurahIdByAyahId(int ayahId) throws SQLException {
        String query = "SELECT SurahID FROM Ayahs WHERE AyahID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ayahId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SurahID");
                }
            }
        }
        throw new SQLException("Surah ID not found for Ayah ID: " + ayahId);
    }

}
