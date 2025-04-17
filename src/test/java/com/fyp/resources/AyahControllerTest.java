package com.fyp.resources;

import com.fyp.api.AyahService;
import com.fyp.cli.Ayah;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AyahControllerTest {

    AyahService ayahServiceMock = Mockito.mock(AyahService.class);
    AyahController ayahController = new AyahController(ayahServiceMock);

    @Test
    void getAyahsBySurahId_ShouldReturn200_WhenAyahsExist() throws Exception {
        List<Ayah> ayahs = new ArrayList<>();
        ayahs.add(new Ayah(1, 1, 1, "Sample Ayah", 4));

        when(ayahServiceMock.getAyahsBySurahId(1)).thenReturn(ayahs);

        Response response = ayahController.getAyahsBySurahId(1);

        assertEquals(200, response.getStatus());
        assertEquals(ayahs, response.getEntity());
    }

    @Test
    void getAyahsBySurahId_ShouldReturn404_WhenNoAyahsFound() throws Exception {
        when(ayahServiceMock.getAyahsBySurahId(99)).thenReturn(new ArrayList<>());

        Response response = ayahController.getAyahsBySurahId(99);

        assertEquals(404, response.getStatus());
        assertEquals("No Ayahs found for the given Surah ID", response.getEntity());
    }

    @Test
    void getAyahsBySurahId_ShouldReturn500_WhenExceptionThrown() throws Exception {
        when(ayahServiceMock.getAyahsBySurahId(1)).thenThrow(new RuntimeException("Simulated failure"));

        Response response = ayahController.getAyahsBySurahId(1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to fetch Ayahs", response.getEntity());
    }


    @Test
    void getAyahById_ShouldReturn200_WhenAyahFound() throws Exception {
        Ayah ayah = new Ayah(1, 1, 1, "Test Ayah", 5);

        when(ayahServiceMock.getAyahById(1)).thenReturn(ayah);

        Response response = ayahController.getAyahById(1);

        assertEquals(200, response.getStatus());
        assertEquals(ayah, response.getEntity());
    }

    @Test
    void getAyahById_ShouldReturn404_WhenAyahNotFound() throws Exception {
        when(ayahServiceMock.getAyahById(100)).thenReturn(null);

        Response response = ayahController.getAyahById(100);

        assertEquals(404, response.getStatus());
        assertEquals("Ayah not found", response.getEntity());
    }

    @Test
    void getAyahById_ShouldReturn500_WhenExceptionThrown() throws Exception {
        when(ayahServiceMock.getAyahById(1)).thenThrow(new RuntimeException());

        Response response = ayahController.getAyahById(1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to fetch the Ayah", response.getEntity());
    }
}
