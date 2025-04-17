package com.fyp.api;

import com.fyp.cli.SurahRecommendation;
import com.fyp.cli.UserSurahProgress;
import com.fyp.client.RecommendationProcessingException;
import com.fyp.client.SurahDataNotFoundException;
import com.fyp.db.RecommendationDao;
import com.fyp.db.SurahDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    private final RecommendationDao recommendationDao;
    private final SurahDao surahDao;

    public RecommendationService(RecommendationDao recommendationDao, SurahDao surahDao) {
        this.recommendationDao = recommendationDao;
        this.surahDao = surahDao;
    }

    public List<SurahRecommendation> getRevisionRecommendations(int userId)
            throws SurahDataNotFoundException {
        List<SurahRecommendation> recommendations = new ArrayList<>();
        List<UserSurahProgress> surahsNeedingRevision = recommendationDao.getSurahsNeedingRevision(userId);

        for (UserSurahProgress progress : surahsNeedingRevision) {
            String surahName = surahDao.getSurahName(progress.getSurahId());
            int ayahCount = surahDao.getAyahCount(progress.getSurahId());

            if (surahName == null || ayahCount <= 0) {
                throw new SurahDataNotFoundException("Missing surah data for Surah ID: " + progress.getSurahId());
            }

            long daysSinceLastRevision = calculateDaysSinceLastRevision(progress.getLastRevisedAt());
            double priorityScore = daysSinceLastRevision * 0.1;

            recommendations.add(new SurahRecommendation(
                    progress.getSurahId(),
                    surahName,
                    "REVISION",
                    priorityScore,
                    "Last revised " + daysSinceLastRevision + " days ago"
            ));
        }

        return recommendations;
    }

    public List<SurahRecommendation> getNewSurahRecommendations(int userId)
            throws SurahDataNotFoundException {
        List<SurahRecommendation> recommendations = new ArrayList<>();
        List<Integer> newSurahIds = recommendationDao.getNewSurahsToMemorize(userId);

        for (Integer surahId : newSurahIds) {
            String surahName = surahDao.getSurahName(surahId);
            int ayahCount = surahDao.getAyahCount(surahId);

            if (surahName == null || ayahCount <= 0) {
                throw new SurahDataNotFoundException("Missing surah data for Surah ID: " + surahId);
            }

            double priorityScore = 1.0 / ayahCount;

            recommendations.add(new SurahRecommendation(
                    surahId,
                    surahName,
                    "NEW",
                    priorityScore,
                    "Contains " + ayahCount + " ayahs"
            ));
        }

        return recommendations;
    }

    private long calculateDaysSinceLastRevision(String lastRevisedAt) {
        if (lastRevisedAt == null) {
            return Long.MAX_VALUE;
        }
        try {
            java.sql.Timestamp lastRevised = java.sql.Timestamp.valueOf(lastRevisedAt);
            long diffInMillis = System.currentTimeMillis() - lastRevised.getTime();
            return diffInMillis / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }

    public void markAsRevised(int userId, int surahId)
            throws RecommendationProcessingException {
        try {
            recommendationDao.updateLastRevised(userId, surahId);
        } catch (SQLException e) {
            throw new RecommendationProcessingException(
                    "Failed to update last revised time for user " + userId + " and surah " + surahId, e);
        }
    }
}
