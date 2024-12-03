package com.fyp.db;

import com.fyp.cli.Surah;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurahDao {

    private  DatabaseConnector databaseConnector = new DatabaseConnector();

    public SurahDao () {
        this.databaseConnector=databaseConnector;
    }

    // Method to retrieve all Surahs
    public List<Surah> getAllSurahs() {
        List<Surah> surahs = new ArrayList<>();
        String query = "SELECT * FROM Surahs";

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Surah surah = new Surah();
                surah.setSurahId(rs.getInt("SurahID"));
                surah.setSurahNameArabic(rs.getString("Surah_Name_Arabic"));
                surah.setSurahNameEnglish(rs.getString("Surah_Name_English"));
                surah.setTotalAyahs(rs.getInt("Total_Ayahs"));
                surahs.add(surah);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Surahs: " + e.getMessage());
        }
        return surahs;
    }

    // Method to retrieve a Surah by ID
    public Surah getSurahById(int surahId) {
        Surah surah = null;
        String query = "SELECT * FROM Surahs WHERE SurahID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, surahId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    surah = new Surah();
                    surah.setSurahId(rs.getInt("SurahID"));
                    surah.setSurahNameArabic(rs.getString("Surah_Name_Arabic"));
                    surah.setSurahNameEnglish(rs.getString("Surah_Name_English"));
                    surah.setTotalAyahs(rs.getInt("Total_Ayahs"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Surah by ID: " + e.getMessage());
        }
        return surah;
    }

}

