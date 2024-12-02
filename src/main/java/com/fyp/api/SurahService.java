package com.fyp.api;

import com.fyp.cli.Surah;
import com.fyp.client.FailedToGetSurahs;
import com.fyp.db.SurahDao;

import java.util.List;

public class SurahService {
    private final SurahDao surahDao;

    // Constructor to initialize SurahDao
    public SurahService(SurahDao surahDao) {
        this.surahDao = new SurahDao();
    }

    // Method to retrieve all Surahs
    public List<Surah> getAllSurahs() throws FailedToGetSurahs {
        try {
            return surahDao.getAllSurahs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new FailedToGetSurahs();
        }
    }

    // Method to retrieve a Surah by its ID
    public Surah getSurahById(int surahId) throws FailedToGetSurahs {
        try {
            return surahDao.getSurahById(surahId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new FailedToGetSurahs();
        }
    }
}
