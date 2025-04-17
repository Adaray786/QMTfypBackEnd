package com.fyp.db;

import com.fyp.cli.Ayah;
import com.fyp.client.FailedToGetAyah;
import com.fyp.client.FailedToGetAyahs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class AyahDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private AyahDao ayahDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection); // Simulate static connection
        ayahDao = new AyahDao(new DatabaseConnector());

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
    }

    @Test
    void getAyahsBySurahId_ShouldReturnListOfAyahs() throws SQLException, FailedToGetAyahs {
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true, false);
        Mockito.when(resultSet.getInt("AyahID")).thenReturn(1);
        Mockito.when(resultSet.getInt("SurahID")).thenReturn(2);
        Mockito.when(resultSet.getInt("Ayah_Number")).thenReturn(3);
        Mockito.when(resultSet.getString("Arabic_Text")).thenReturn("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ");
        Mockito.when(resultSet.getInt("Word_Count")).thenReturn(4);

        List<Ayah> ayahs = ayahDao.getAyahsBySurahId(2);

        assertEquals(1, ayahs.size());
        Ayah ayah = ayahs.get(0);
        assertEquals(1, ayah.getAyahId());
        assertEquals(2, ayah.getSurahId());
        assertEquals(3, ayah.getAyahNumber());
        assertEquals(4, ayah.getWordCount());
    }

    @Test
    void getAyahsBySurahId_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        Mockito.when(statement.executeQuery()).thenThrow(new SQLException());
        assertThrows(FailedToGetAyahs.class, () -> ayahDao.getAyahsBySurahId(1));
    }

    @Test
    void getAyahById_ShouldReturnSingleAyah() throws SQLException, FailedToGetAyah {
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt("AyahID")).thenReturn(5);
        Mockito.when(resultSet.getInt("SurahID")).thenReturn(1);
        Mockito.when(resultSet.getInt("Ayah_Number")).thenReturn(1);
        Mockito.when(resultSet.getString("Arabic_Text")).thenReturn("الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ");
        Mockito.when(resultSet.getInt("Word_Count")).thenReturn(5);

        Ayah ayah = ayahDao.getAyahById(5);

        assertNotNull(ayah);
        assertEquals(5, ayah.getAyahId());
        assertEquals(1, ayah.getSurahId());
    }

    @Test
    void getAyahById_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        Mockito.when(statement.executeQuery()).thenThrow(new SQLException());
        assertThrows(FailedToGetAyah.class, () -> ayahDao.getAyahById(10));
    }

    @Test
    void getSurahIdByAyahId_ShouldReturnSurahId() throws SQLException {
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt("SurahID")).thenReturn(7);

        int surahId = ayahDao.getSurahIdByAyahId(11);
        assertEquals(7, surahId);
    }

    @Test
    void getSurahIdByAyahId_ShouldThrowSQLException_WhenNoMatchFound() throws SQLException {
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> ayahDao.getSurahIdByAyahId(999));
    }
}
