package com.fyp.api;

import com.fyp.cli.Ayah;
import com.fyp.client.FailedToGetAyah;
import com.fyp.client.FailedToGetAyahs;
import com.fyp.db.AyahDao;

import java.util.List;

public class AyahService {
    private final AyahDao ayahDao;

    // Constructor to initialize AyahDao
    public AyahService(AyahDao ayahDao) {
        this.ayahDao = new AyahDao();
    }

    // Method to retrieve Ayahs by Surah ID
    public List<Ayah> getAyahsBySurahId(int surahId) throws FailedToGetAyahs {
        try {
            return ayahDao.getAyahsBySurahId(surahId);
        } catch (Exception e) {
            System.err.println("Error retrieving Ayahs for Surah ID: " + surahId + ". " + e.getMessage());
            throw new FailedToGetAyahs();
        }
    }

    // Method to retrieve a specific Ayah by ID
    public Ayah getAyahById(int ayahId) throws FailedToGetAyah {
        try {
            return ayahDao.getAyahById(ayahId);
        } catch (Exception e) {
            System.err.println("Error retrieving Ayah with ID: " + ayahId + ". " + e.getMessage());
            throw new FailedToGetAyah();
        }
    }
}
