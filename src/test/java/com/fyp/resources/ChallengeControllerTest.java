package com.fyp.resources;

import com.fyp.api.ChallengeService;
import com.fyp.cli.ActiveChallenge;
import com.fyp.cli.ChallengeRequest;
import com.fyp.cli.CompletedChallenge;
import com.fyp.cli.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChallengeControllerTest {

    ChallengeService challengeServiceMock = mock(ChallengeService.class);
    ChallengeController controller = new ChallengeController(challengeServiceMock);

    User mockUser = new User(1, "user@email.com", null);

    @Test
    void sendChallengeRequest_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(challengeServiceMock).sendChallengeRequest(1, 2, 114);

        Response response = controller.sendChallengeRequest(mockUser, 2, 114);

        assertEquals(200, response.getStatus());
        assertEquals("Challenge request sent successfully", response.getEntity());
    }

    @Test
    void sendChallengeRequest_ShouldReturn400_WhenIllegalArgument() throws Exception {
        doThrow(new IllegalArgumentException("Invalid")).when(challengeServiceMock).sendChallengeRequest(1, 2, 114);

        Response response = controller.sendChallengeRequest(mockUser, 2, 114);

        assertEquals(400, response.getStatus());
        assertEquals("Invalid", response.getEntity());
    }

    @Test
    void sendChallengeRequest_ShouldReturn500_WhenSQLException() throws Exception {
        doThrow(new SQLException()).when(challengeServiceMock).sendChallengeRequest(1, 2, 114);

        Response response = controller.sendChallengeRequest(mockUser, 2, 114);

        assertEquals(500, response.getStatus());
        assertEquals("Error sending challenge request", response.getEntity());
    }

    @Test
    void acceptChallengeRequest_ShouldReturn200_WhenSuccessful() throws SQLException {
        doNothing().when(challengeServiceMock).acceptChallengeRequest(100);

        Response response = controller.acceptChallengeRequest(mockUser, 100);

        assertEquals(200, response.getStatus());
        assertEquals("Challenge accepted successfully", response.getEntity());
    }

    @Test
    void acceptChallengeRequest_ShouldReturn500_WhenSQLException() throws SQLException {
        doThrow(SQLException.class).when(challengeServiceMock).acceptChallengeRequest(100);

        Response response = controller.acceptChallengeRequest(mockUser, 100);

        assertEquals(500, response.getStatus());
        assertEquals("Error accepting challenge", response.getEntity());
    }

    @Test
    void rejectChallengeRequest_ShouldReturn200_WhenSuccessful() throws SQLException {
        doNothing().when(challengeServiceMock).rejectChallengeRequest(200);

        Response response = controller.rejectChallengeRequest(mockUser, 200);

        assertEquals(200, response.getStatus());
        assertEquals("Challenge rejected successfully", response.getEntity());
    }

    @Test
    void rejectChallengeRequest_ShouldReturn500_WhenSQLException() throws SQLException {
        doThrow(SQLException.class).when(challengeServiceMock).rejectChallengeRequest(200);

        Response response = controller.rejectChallengeRequest(mockUser, 200);

        assertEquals(500, response.getStatus());
        assertEquals("Error rejecting challenge", response.getEntity());
    }

    @Test
    void getChallengeRequests_ShouldReturn200_WhenSuccessful() throws SQLException {
        List<ChallengeRequest> mockList = new ArrayList<>();
        mockList.add(new ChallengeRequest());

        when(challengeServiceMock.getChallengeRequests(1)).thenReturn(mockList);

        Response response = controller.getChallengeRequests(mockUser);

        assertEquals(200, response.getStatus());
        assertEquals(mockList, response.getEntity());
    }

    @Test
    void getChallengeRequests_ShouldReturn500_WhenSQLException() throws SQLException {
        when(challengeServiceMock.getChallengeRequests(1)).thenThrow(SQLException.class);

        Response response = controller.getChallengeRequests(mockUser);

        assertEquals(500, response.getStatus());
        assertEquals("Error fetching challenge requests", response.getEntity());
    }

    @Test
    void getActiveChallenges_ShouldReturn200_WhenSuccessful() throws SQLException {
        List<ActiveChallenge> mockList = new ArrayList<>();
        mockList.add(new ActiveChallenge());

        when(challengeServiceMock.getActiveChallenges(1)).thenReturn(mockList);

        Response response = controller.getActiveChallenges(mockUser);

        assertEquals(200, response.getStatus());
        assertEquals(mockList, response.getEntity());
    }

    @Test
    void getActiveChallenges_ShouldReturn500_WhenSQLException() throws SQLException {
        when(challengeServiceMock.getActiveChallenges(1)).thenThrow(SQLException.class);

        Response response = controller.getActiveChallenges(mockUser);

        assertEquals(500, response.getStatus());
        assertEquals("Error fetching active challenges", response.getEntity());
    }

    @Test
    void getCompletedChallenges_ShouldReturn200_WhenSuccessful() throws SQLException {
        List<CompletedChallenge> mockList = new ArrayList<>();
        mockList.add(new CompletedChallenge());

        when(challengeServiceMock.getCompletedChallenges(1)).thenReturn(mockList);

        Response response = controller.getCompletedChallenges(mockUser);

        assertEquals(200, response.getStatus());
        assertEquals(mockList, response.getEntity());
    }

    @Test
    void getCompletedChallenges_ShouldReturn500_WhenSQLException() throws SQLException {
        when(challengeServiceMock.getCompletedChallenges(1)).thenThrow(SQLException.class);

        Response response = controller.getCompletedChallenges(mockUser);

        assertEquals(500, response.getStatus());
        assertEquals("Error fetching completed challenges", response.getEntity());
    }

    @Test
    void winChallenge_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(challengeServiceMock).winChallenge(3, 1);

        Response response = controller.winChallenge(mockUser, 3);

        assertEquals(200, response.getStatus());
        assertEquals("Challenge marked as won successfully", response.getEntity());
    }

    @Test
    void winChallenge_ShouldReturn400_WhenIllegalArgument() throws Exception {
        doThrow(new IllegalArgumentException("Invalid")).when(challengeServiceMock).winChallenge(3, 1);

        Response response = controller.winChallenge(mockUser, 3);

        assertEquals(400, response.getStatus());
        assertEquals("Invalid", response.getEntity());
    }

    @Test
    void winChallenge_ShouldReturn500_WhenSQLException() throws Exception {
        doThrow(new SQLException()).when(challengeServiceMock).winChallenge(3, 1);

        Response response = controller.winChallenge(mockUser, 3);

        assertEquals(500, response.getStatus());
        assertEquals("Error marking challenge as won", response.getEntity());
    }

    @Test
    void getActiveChallengeBySurah_ShouldReturn200_WhenFound() throws SQLException {
        ActiveChallenge challenge = new ActiveChallenge();
        when(challengeServiceMock.getActiveChallengeBySurahAndUser(114, 1)).thenReturn(challenge);

        Response response = controller.getActiveChallengeBySurah(mockUser, 114);

        assertEquals(200, response.getStatus());
        assertEquals(challenge, response.getEntity());
    }

    @Test
    void getActiveChallengeBySurah_ShouldReturn404_WhenNotFound() throws SQLException {
        when(challengeServiceMock.getActiveChallengeBySurahAndUser(114, 1)).thenReturn(null);

        Response response = controller.getActiveChallengeBySurah(mockUser, 114);

        assertEquals(404, response.getStatus());
        assertEquals("No active challenge found for this surah", response.getEntity());
    }

    @Test
    void getActiveChallengeBySurah_ShouldReturn500_WhenSQLException() throws SQLException {
        when(challengeServiceMock.getActiveChallengeBySurahAndUser(114, 1)).thenThrow(SQLException.class);

        Response response = controller.getActiveChallengeBySurah(mockUser, 114);

        assertEquals(500, response.getStatus());
        assertEquals("Error fetching active challenge", response.getEntity());
    }
}
