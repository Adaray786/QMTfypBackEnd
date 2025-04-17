package com.fyp.api;

import com.fyp.cli.SurahRecommendation;
import com.fyp.cli.UserSurahProgress;
import com.fyp.client.RecommendationProcessingException;
import com.fyp.client.SurahDataNotFoundException;
import com.fyp.db.RecommendationDao;
import com.fyp.db.SurahDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    private RecommendationDao recommendationDao;
    private SurahDao surahDao;
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationDao = mock(RecommendationDao.class);
        surahDao = mock(SurahDao.class);
        recommendationService = new RecommendationService(recommendationDao, surahDao);
    }

    @Test
    void getRevisionRecommendations_success() throws Exception {
        UserSurahProgress progress = new UserSurahProgress();
        progress.setSurahId(1);
        progress.setLastRevisedAt("2024-04-01 10:00:00");

        when(recommendationDao.getSurahsNeedingRevision(5)).thenReturn(Collections.singletonList(progress));
        when(surahDao.getSurahName(1)).thenReturn("Al-Fatiha");
        when(surahDao.getAyahCount(1)).thenReturn(7);

        List<SurahRecommendation> result = recommendationService.getRevisionRecommendations(5);

        assertEquals(1, result.size());
        assertEquals("Al-Fatiha", result.get(0).getSurahName());
        assertTrue(result.get(0).getPriorityScore() > 0);
    }

    @Test
    void getRevisionRecommendations_missingSurahData_throwsException() throws Exception {
        UserSurahProgress progress = new UserSurahProgress();
        progress.setSurahId(2);
        progress.setLastRevisedAt("2024-04-01 10:00:00");

        when(recommendationDao.getSurahsNeedingRevision(10)).thenReturn(Arrays.asList(progress));
        when(surahDao.getSurahName(2)).thenReturn(null); // simulate missing surah name

        assertThrows(SurahDataNotFoundException.class, () ->
                recommendationService.getRevisionRecommendations(10));
    }

    @Test
    void getNewSurahRecommendations_success() throws Exception {
        when(recommendationDao.getNewSurahsToMemorize(3)).thenReturn(Arrays.asList(5, 6));
        when(surahDao.getSurahName(5)).thenReturn("Al-Ma'idah");
        when(surahDao.getSurahName(6)).thenReturn("Al-An'am");
        when(surahDao.getAyahCount(5)).thenReturn(120);
        when(surahDao.getAyahCount(6)).thenReturn(165);

        List<SurahRecommendation> result = recommendationService.getNewSurahRecommendations(3);

        assertEquals(2, result.size());
        assertEquals("Al-Ma'idah", result.get(0).getSurahName());
        assertTrue(result.get(0).getPriorityScore() > 0);
    }

    @Test
    void getNewSurahRecommendations_missingSurahData_throwsException() throws Exception {
        when(recommendationDao.getNewSurahsToMemorize(7)).thenReturn(Arrays.asList(9));
        when(surahDao.getSurahName(9)).thenReturn("At-Tawbah");
        when(surahDao.getAyahCount(9)).thenReturn(0); // Invalid data

        assertThrows(SurahDataNotFoundException.class, () ->
                recommendationService.getNewSurahRecommendations(7));
    }

    @Test
    void markAsRevised_success() throws Exception {
        doNothing().when(recommendationDao).updateLastRevised(1, 2);

        assertDoesNotThrow(() -> recommendationService.markAsRevised(1, 2));

        verify(recommendationDao).updateLastRevised(1, 2);
    }

    @Test
    void markAsRevised_sqlExceptionThrown_wrapsInCustomException() throws Exception {
        doThrow(new SQLException("DB error")).when(recommendationDao).updateLastRevised(1, 2);

        RecommendationProcessingException ex = assertThrows(RecommendationProcessingException.class, () ->
                recommendationService.markAsRevised(1, 2));

        assertTrue(ex.getMessage().contains("Failed to update last revised time"));
    }
}
