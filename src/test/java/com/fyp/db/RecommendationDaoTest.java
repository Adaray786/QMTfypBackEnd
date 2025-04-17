package com.fyp.db;

import com.fyp.cli.UserSurahProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RecommendationDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private RecommendationDao recommendationDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        recommendationDao = new RecommendationDao(new DatabaseConnector());
    }

    @Test
    void getSurahsNeedingRevision_ShouldReturnSurahList() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getInt("ProgressID")).thenReturn(101);
        when(resultSet.getInt("UserID")).thenReturn(1);
        when(resultSet.getInt("SurahID")).thenReturn(2);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);
        when(resultSet.getString("Last_Revised_At")).thenReturn("2024-04-01 09:00:00");

        List<UserSurahProgress> result = recommendationDao.getSurahsNeedingRevision(1);

        assertEquals(1, result.size());
        UserSurahProgress surah = result.get(0);
        assertEquals(101, surah.getProgressId());
        assertEquals(1, surah.getUserId());
        assertEquals(2, surah.getSurahId());
        assertTrue(surah.isMemorized());
        assertEquals("2024-04-01 09:00:00", surah.getLastRevisedAt());
    }

    @Test
    void getSurahsNeedingRevision_ShouldReturnEmptyList_WhenNoSurahsFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<UserSurahProgress> result = recommendationDao.getSurahsNeedingRevision(1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNewSurahsToMemorize_ShouldReturnSurahIds() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("SurahID")).thenReturn(2).thenReturn(3);

        List<Integer> result = recommendationDao.getNewSurahsToMemorize(1);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0));
        assertEquals(3, result.get(1));
    }

    @Test
    void getNewSurahsToMemorize_ShouldReturnEmptyList_WhenNoneFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Integer> result = recommendationDao.getNewSurahsToMemorize(1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateLastRevised_ShouldExecuteUpdate() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        recommendationDao.updateLastRevised(1, 114);

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 114);
        verify(statement).executeUpdate();
    }
}
