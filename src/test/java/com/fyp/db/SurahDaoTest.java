package com.fyp.db;

import com.fyp.cli.Surah;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SurahDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private Statement statement = Mockito.mock(Statement.class);
    private PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private SurahDao surahDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        surahDao = new SurahDao(new DatabaseConnector());
    }

    @Test
    void getAllSurahs_ShouldReturnListOfSurahs() throws SQLException {
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("SurahID")).thenReturn(1);
        when(resultSet.getString("Surah_Name_Arabic")).thenReturn("الفاتحة");
        when(resultSet.getString("Surah_Name_English")).thenReturn("Al-Fatihah");
        when(resultSet.getInt("Total_Ayahs")).thenReturn(7);

        List<Surah> surahs = surahDao.getAllSurahs();

        assertEquals(1, surahs.size());
        Surah surah = surahs.get(0);
        assertEquals(1, surah.getSurahId());
        assertEquals("الفاتحة", surah.getSurahNameArabic());
        assertEquals("Al-Fatihah", surah.getSurahNameEnglish());
        assertEquals(7, surah.getTotalAyahs());
    }

    @Test
    void getSurahById_ShouldReturnSurah() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("SurahID")).thenReturn(2);
        when(resultSet.getString("Surah_Name_Arabic")).thenReturn("البقرة");
        when(resultSet.getString("Surah_Name_English")).thenReturn("Al-Baqarah");
        when(resultSet.getInt("Total_Ayahs")).thenReturn(286);

        Surah surah = surahDao.getSurahById(2);

        assertNotNull(surah);
        assertEquals(2, surah.getSurahId());
        assertEquals("البقرة", surah.getSurahNameArabic());
        assertEquals("Al-Baqarah", surah.getSurahNameEnglish());
        assertEquals(286, surah.getTotalAyahs());
    }

    @Test
    void getSurahById_ShouldReturnNull_WhenNotFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Surah surah = surahDao.getSurahById(999);
        assertNull(surah);
    }

    @Test
    void getSurahName_ShouldReturnName() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("Surah_Name_English")).thenReturn("Al-Ikhlas");

        String name = surahDao.getSurahName(112);
        assertEquals("Al-Ikhlas", name);
    }

    @Test
    void getSurahName_ShouldReturnNull_WhenNotFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        String name = surahDao.getSurahName(999);
        assertNull(name);
    }

    @Test
    void getAyahCount_ShouldReturnTotalAyahs() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("Total_Ayahs")).thenReturn(5);

        int ayahCount = surahDao.getAyahCount(108);
        assertEquals(5, ayahCount);
    }

    @Test
    void getAyahCount_ShouldReturnZero_WhenNotFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int ayahCount = surahDao.getAyahCount(404);
        assertEquals(0, ayahCount);
    }
}
