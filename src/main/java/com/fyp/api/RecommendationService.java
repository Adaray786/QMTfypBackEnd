package com.fyp.api;

import com.fyp.cli.SurahRecommendation;
import com.fyp.cli.UserSurahProgress;
import com.fyp.db.RecommendationDao;
import com.fyp.db.SurahDao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    private final RecommendationDao recommendationDao;
    private final SurahDao surahDao;

    public RecommendationService(RecommendationDao recommendationDao, SurahDao surahDao) {
        this.recommendationDao = recommendationDao;
        this.surahDao = surahDao;
    }

    // Get revision recommendations (surahs that need to be revised)
    public List<SurahRecommendation> getRevisionRecommendations(int userId) {
        List<SurahRecommendation> recommendations = new ArrayList<>();
        List<UserSurahProgress> surahsNeedingRevision = recommendationDao.getSurahsNeedingRevision(userId);

        for (UserSurahProgress progress : surahsNeedingRevision) {
            String surahName = surahDao.getSurahName(progress.getSurahId());
            int ayahCount = surahDao.getAyahCount(progress.getSurahId());
            
            // Calculate priority score based on time since last revision
            // The longer it's been since last revision, the higher the priority
            long daysSinceLastRevision = calculateDaysSinceLastRevision(progress.getLastRevisedAt());
            double priorityScore = daysSinceLastRevision * 0.1; // Adjust weight as needed
            
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

    // Get new surah recommendations (surahs to memorize)
    public List<SurahRecommendation> getNewSurahRecommendations(int userId) {
        List<SurahRecommendation> recommendations = new ArrayList<>();
        List<Integer> newSurahIds = recommendationDao.getNewSurahsToMemorize(userId);

        for (Integer surahId : newSurahIds) {
            String surahName = surahDao.getSurahName(surahId);
            int ayahCount = surahDao.getAyahCount(surahId);
            
            // Calculate priority score based on surah length
            // Shorter surahs get higher priority
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

    // Helper method to calculate days since last revision
    private long calculateDaysSinceLastRevision(String lastRevisedAt) {
        if (lastRevisedAt == null) {
            return Long.MAX_VALUE; // If never revised, highest priority
        }
        try {
            java.sql.Timestamp lastRevised = java.sql.Timestamp.valueOf(lastRevisedAt);
            long diffInMillis = System.currentTimeMillis() - lastRevised.getTime();
            return diffInMillis / (1000 * 60 * 60 * 24); // Convert to days
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }

    public void markAsRevised(int userId, int surahId) throws SQLException {
        recommendationDao.updateLastRevised(userId, surahId);
    }
} 