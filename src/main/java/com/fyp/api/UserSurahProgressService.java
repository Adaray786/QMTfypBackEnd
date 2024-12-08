package com.fyp.api;

import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;
import com.fyp.db.UserSurahProgressDao;

import java.sql.SQLException;
import java.util.List;

public class UserSurahProgressService {

    private final UserSurahProgressDao userSurahProgressDao;

    // Constructor
    public UserSurahProgressService(UserSurahProgressDao userSurahProgressDao) {
        this.userSurahProgressDao = userSurahProgressDao;
    }

    // Create or update Surah progress
    public void createOrUpdateSurahProgress(int userId, int surahId, boolean isMemorized) throws FailedToGetSurahProgress, SQLException {
        userSurahProgressDao.createOrUpdateSurahProgress(userId, surahId, isMemorized);
    }

    // Retrieve Surah progress by user and Surah
    public UserSurahProgress getUserSurahProgress(int userId, int surahId) throws FailedToGetSurahProgress {
        return userSurahProgressDao.getUserSurahProgress(userId, surahId);
    }

    // Retrieve all Surah progress records for a user
    public List<UserSurahProgress> getAllSurahProgressByUser(int userId) throws FailedToGetSurahProgress {
        return userSurahProgressDao.getAllSurahProgressByUser(userId);
    }
}
