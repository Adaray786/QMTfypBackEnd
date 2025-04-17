package com.fyp.resources;

import com.fyp.api.SurahService;
import com.fyp.cli.Surah;
import com.fyp.client.FailedToGetSurahs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurahControllerTest {

    private SurahService surahService;
    private SurahController surahController;

    @BeforeEach
    void setUp() {
        surahService = mock(SurahService.class);
        surahController = new SurahController(surahService);
    }

    @Test
    void getAllSurahs_ShouldReturn200_WhenSuccessful() throws FailedToGetSurahs {
        List<Surah> surahList = new ArrayList<>();
        surahList.add(new Surah(1, "الفاتحة", "Al-Fatiha", 7));
        surahList.add(new Surah(2, "البقرة", "Al-Baqarah", 286));

        when(surahService.getAllSurahs()).thenReturn(surahList);

        Response response = surahController.getAllSurahs();

        assertEquals(200, response.getStatus());
        assertEquals(surahList, response.getEntity());
    }

    @Test
    void getAllSurahs_ShouldReturn500_WhenExceptionThrown() throws FailedToGetSurahs {
        when(surahService.getAllSurahs()).thenThrow(RuntimeException.class);

        Response response = surahController.getAllSurahs();

        assertEquals(500, response.getStatus());
        assertEquals("Failed to fetch Surahs", response.getEntity());
    }

    @Test
    void getSurahById_ShouldReturn200_WhenSurahFound() throws FailedToGetSurahs {
        Surah surah = new Surah(1, "الفاتحة", "Al-Fatiha", 7);

        when(surahService.getSurahById(1)).thenReturn(surah);

        Response response = surahController.getSurahById(1);

        assertEquals(200, response.getStatus());
        assertEquals(surah, response.getEntity());
    }

    @Test
    void getSurahById_ShouldReturn404_WhenSurahNotFound() throws FailedToGetSurahs {
        when(surahService.getSurahById(99)).thenReturn(null);

        Response response = surahController.getSurahById(99);

        assertEquals(404, response.getStatus());
        assertEquals("Surah not found", response.getEntity());
    }

    @Test
    void getSurahById_ShouldReturn500_WhenExceptionThrown() throws FailedToGetSurahs {
        when(surahService.getSurahById(1)).thenThrow(RuntimeException.class);

        Response response = surahController.getSurahById(1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to fetch the Surah", response.getEntity());
    }
}
