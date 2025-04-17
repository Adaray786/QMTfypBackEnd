package com.fyp.api;

import com.fyp.cli.Ayah;
import com.fyp.client.FailedToGetAyah;
import com.fyp.client.FailedToGetAyahs;
import com.fyp.db.AyahDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AyahServiceTest {

    private AyahDao ayahDaoMock;
    private AyahService ayahService;

    @BeforeEach
    void setUp() {
        ayahDaoMock = mock(AyahDao.class);
        ayahService = new AyahService(ayahDaoMock);
    }

    @Test
    void getAyahsBySurahId_returnsListOfAyahs() throws FailedToGetAyahs {
        int surahId = 1;
        List<Ayah> mockAyahs = Arrays.asList(new Ayah(), new Ayah());
        when(ayahDaoMock.getAyahsBySurahId(surahId)).thenReturn(mockAyahs);

        List<Ayah> result = ayahService.getAyahsBySurahId(surahId);

        assertEquals(2, result.size());
        verify(ayahDaoMock, times(1)).getAyahsBySurahId(surahId);
    }

    @Test
    void getAyahsBySurahId_throwsFailedToGetAyahs() throws FailedToGetAyahs {
        int surahId = 1;
        when(ayahDaoMock.getAyahsBySurahId(surahId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(FailedToGetAyahs.class, () -> ayahService.getAyahsBySurahId(surahId));
        verify(ayahDaoMock, times(1)).getAyahsBySurahId(surahId);
    }

    @Test
    void getAyahById_returnsAyah() throws FailedToGetAyah {
        int ayahId = 42;
        Ayah mockAyah = new Ayah();
        when(ayahDaoMock.getAyahById(ayahId)).thenReturn(mockAyah);

        Ayah result = ayahService.getAyahById(ayahId);

        assertNotNull(result);
        verify(ayahDaoMock, times(1)).getAyahById(ayahId);
    }

    @Test
    void getAyahById_throwsFailedToGetAyah() throws FailedToGetAyah {
        int ayahId = 42;
        when(ayahDaoMock.getAyahById(ayahId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(FailedToGetAyah.class, () -> ayahService.getAyahById(ayahId));
        verify(ayahDaoMock, times(1)).getAyahById(ayahId);
    }
}
