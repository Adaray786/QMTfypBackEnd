package com.fyp.api;

import com.fyp.cli.Surah;
import com.fyp.client.FailedToGetSurahs;
import com.fyp.db.SurahDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SurahServiceTest {

    private SurahDao surahDao;
    private SurahService surahService;

    @BeforeEach
    void setUp() {
        surahDao = mock(SurahDao.class);
        surahService = new SurahService(surahDao);
    }

    @Test
    void getAllSurahs_success() throws Exception {
        Surah surah1 = new Surah(1, "الفاتحة", "Al-Fatiha", 7);
        Surah surah2 = new Surah(2, "البقرة", "Al-Baqarah", 286);
        List<Surah> mockSurahs = Arrays.asList(surah1, surah2);

        when(surahDao.getAllSurahs()).thenReturn(mockSurahs);

        List<Surah> result = surahService.getAllSurahs();

        assertEquals(2, result.size());
        assertEquals("الفاتحة", result.get(0).getSurahNameArabic());
        assertEquals("Al-Baqarah", result.get(1).getSurahNameEnglish());
        assertEquals(286, result.get(1).getTotalAyahs());

        verify(surahDao).getAllSurahs();
    }

    @Test
    void getAllSurahs_throwsFailedToGetSurahs() throws Exception {
        when(surahDao.getAllSurahs()).thenThrow(new RuntimeException("DB error"));

        assertThrows(FailedToGetSurahs.class, () -> surahService.getAllSurahs());
        verify(surahDao).getAllSurahs();
    }

    @Test
    void getSurahById_success() throws Exception {
        Surah mockSurah = new Surah(1, "الفاتحة", "Al-Fatiha", 7);
        when(surahDao.getSurahById(1)).thenReturn(mockSurah);

        Surah result = surahService.getSurahById(1);

        assertNotNull(result);
        assertEquals(1, result.getSurahId());
        assertEquals("Al-Fatiha", result.getSurahNameEnglish());
        assertEquals("الفاتحة", result.getSurahNameArabic());
        assertEquals(7, result.getTotalAyahs());

        verify(surahDao).getSurahById(1);
    }

    @Test
    void getSurahById_throwsFailedToGetSurahs() throws Exception {
        when(surahDao.getSurahById(1)).thenThrow(new RuntimeException("Not found"));

        assertThrows(FailedToGetSurahs.class, () -> surahService.getSurahById(1));
        verify(surahDao).getSurahById(1);
    }
}
