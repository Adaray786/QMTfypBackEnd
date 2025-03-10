package com.fyp.api;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;
import com.fyp.db.AyahDao;
import com.fyp.db.UserAyahProgressDao;
import com.fyp.db.UserSurahProgressDao;

import java.sql.SQLException;
import java.util.List;

public class UserAyahProgressService {

    private UserAyahProgressDao userAyahProgressDao;

    // Constructor
    public UserAyahProgressService(UserAyahProgressDao userAyahProgressDao) {
        this.userAyahProgressDao = userAyahProgressDao;
    }

    // Retrieve Ayah progress by user and Ayah
    public UserAyahProgress getUserAyahProgress(int userId, int ayahId) throws FailedToGetAyahProgress {
        return userAyahProgressDao.getUserAyahProgress(userId, ayahId);
    }

    // Retrieve all Ayah progress records for a user
    public List<UserAyahProgress> getAllAyahProgressByUser(int userId) throws FailedToGetAyahProgress {
        return userAyahProgressDao.getAllAyahProgressByUser(userId);
    }

    public void upsertUserAyahProgress(int userId, int ayahId, boolean isMemorized) throws SQLException, FailedToInsertAyahProgress, FailedToCheckAyahProgressException {
        userAyahProgressDao.updateAyahProgress(userId, ayahId, isMemorized);
    }

    public List<UserAyahProgress> getAyahProgressBySurahAndUser(int userId, int surahId) throws SQLException, FailedToGetAyahProgress, FailedToCheckAyahProgressException {
        return userAyahProgressDao.getAyahProgressBySurahAndUser(userId, surahId);
    }

}
