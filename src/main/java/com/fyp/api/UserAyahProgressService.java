package com.fyp.api;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;
import com.fyp.db.AyahDao;
import com.fyp.db.UserAyahProgressDao;
import com.fyp.db.UserSurahProgressDao;

import java.sql.SQLException;
import java.util.List;

public class UserAyahProgressService {

    private final UserAyahProgressDao userAyahProgressDao;
    private final AyahDao ayahDao;
    private final UserSurahProgressDao userSurahProgressDao;

    // Constructor
    public UserAyahProgressService(UserAyahProgressDao userAyahProgressDao) {
        this.userAyahProgressDao = userAyahProgressDao;
        ayahDao = new AyahDao();
        userSurahProgressDao = new UserSurahProgressDao();
    }

    // Retrieve Ayah progress by user and Ayah
    public UserAyahProgress getUserAyahProgress(int userId, int ayahId) throws FailedToGetAyahProgress {
        return userAyahProgressDao.getUserAyahProgress(userId, ayahId);
    }

    // Retrieve all Ayah progress records for a user
    public List<UserAyahProgress> getAllAyahProgressByUser(int userId) throws FailedToGetAyahProgress {
        return userAyahProgressDao.getAllAyahProgressByUser(userId);
    }

    public void upsertUserAyahProgress(int userId, int ayahId, boolean isMemorized) throws SQLException, FailedToInsertAyahProgress {
        userAyahProgressDao.createOrUpdateAyahProgress(userId, ayahId, isMemorized);

        // Fetch Surah ID associated with this Ayah
        int surahId = ayahDao.getSurahIdByAyahId(ayahId);

        // Check if all Ayahs in the Surah are memorized
        boolean allMemorized = userAyahProgressDao.areAllAyahsMemorized(userId, surahId);

        // Update Surah progress accordingly
        userSurahProgressDao.updateUserSurahProgress(userId, surahId, allMemorized);
    }

    public List<UserAyahProgress> getAyahProgressBySurahAndUser(int userId, int surahId) throws SQLException, FailedToGetAyahProgress {
        return userAyahProgressDao.getAyahProgressBySurahAndUser(userId, surahId);
    }

}
