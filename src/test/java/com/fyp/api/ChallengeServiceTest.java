package com.fyp.api;

import com.fyp.cli.ActiveChallenge;
import com.fyp.cli.ChallengeRequest;
import com.fyp.cli.CompletedChallenge;
import com.fyp.client.ChallengeAlreadyMemorisedException;
import com.fyp.client.ChallengeNotFoundException;
import com.fyp.client.InvalidChallengeParticipantException;
import com.fyp.db.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChallengeServiceTest {

    private ChallengeRequestDao challengeRequestDao;
    private ActiveChallengeDao activeChallengeDao;
    private CompletedChallengeDao completedChallengeDao;
    private UserSurahProgressDao userSurahProgressDao;

    private ChallengeService challengeService;

    @BeforeEach
    void setUp() {
        challengeRequestDao = mock(ChallengeRequestDao.class);
        activeChallengeDao = mock(ActiveChallengeDao.class);
        completedChallengeDao = mock(CompletedChallengeDao.class);
        userSurahProgressDao = mock(UserSurahProgressDao.class);

        challengeService = new ChallengeService(
                challengeRequestDao,
                activeChallengeDao,
                completedChallengeDao,
                userSurahProgressDao
        );
    }

    @Test
    void sendChallengeRequest_successful() throws Exception {
        int senderId = 1, receiverId = 2, surahId = 5;

        when(userSurahProgressDao.hasUserMemorizedSurah(senderId, surahId)).thenReturn(false);
        when(userSurahProgressDao.hasUserMemorizedSurah(receiverId, surahId)).thenReturn(false);

        challengeService.sendChallengeRequest(senderId, receiverId, surahId);

        verify(challengeRequestDao).sendChallengeRequest(senderId, receiverId, surahId);
    }

    @Test
    void sendChallengeRequest_throwsChallengeAlreadyMemorisedException() throws Exception {
        int senderId = 1, receiverId = 2, surahId = 5;

        when(userSurahProgressDao.hasUserMemorizedSurah(senderId, surahId)).thenReturn(true);

        assertThrows(ChallengeAlreadyMemorisedException.class, () -> {
            challengeService.sendChallengeRequest(senderId, receiverId, surahId);
        });

        verify(challengeRequestDao, never()).sendChallengeRequest(anyInt(), anyInt(), anyInt());
    }

    @Test
    void acceptChallengeRequest_successful() throws Exception {
        int requestId = 10;
        challengeService.acceptChallengeRequest(requestId);
        verify(challengeRequestDao).acceptChallengeRequest(requestId);
    }

    @Test
    void rejectChallengeRequest_successful() throws Exception {
        int requestId = 11;
        challengeService.rejectChallengeRequest(requestId);
        verify(challengeRequestDao).rejectChallengeRequest(requestId);
    }

    @Test
    void getChallengeRequests_returnsList() throws Exception {
        int userId = 1;
        List<ChallengeRequest> mockRequests = Arrays.asList(new ChallengeRequest(), new ChallengeRequest());
        when(challengeRequestDao.getChallengeRequests(userId)).thenReturn(mockRequests);

        List<ChallengeRequest> result = challengeService.getChallengeRequests(userId);

        assertEquals(2, result.size());
        verify(challengeRequestDao).getChallengeRequests(userId);
    }

    @Test
    void getActiveChallenges_returnsList() throws Exception {
        int userId = 1;
        List<ActiveChallenge> mockChallenges = Arrays.asList(new ActiveChallenge(), new ActiveChallenge());
        when(activeChallengeDao.getActiveChallenges(userId)).thenReturn(mockChallenges);

        List<ActiveChallenge> result = challengeService.getActiveChallenges(userId);

        assertEquals(2, result.size());
        verify(activeChallengeDao).getActiveChallenges(userId);
    }

    @Test
    void getCompletedChallenges_returnsList() throws Exception {
        int userId = 1;
        List<CompletedChallenge> mockChallenges = Arrays.asList(new CompletedChallenge(), new CompletedChallenge());
        when(completedChallengeDao.getCompletedChallenges(userId)).thenReturn(mockChallenges);

        List<CompletedChallenge> result = challengeService.getCompletedChallenges(userId);

        assertEquals(2, result.size());
        verify(completedChallengeDao).getCompletedChallenges(userId);
    }

    @Test
    void winChallenge_successful() throws Exception {
        int challengeId = 1;
        int winnerId = 2;

        ActiveChallenge challenge = new ActiveChallenge();
        challenge.setChallengeId(challengeId);
        challenge.setUser1Id(2);
        challenge.setUser2Id(3);
        challenge.setSurahId(114);
        challenge.setStartedAt(String.valueOf(LocalDateTime.now()));

        when(activeChallengeDao.getActiveChallengeById(challengeId)).thenReturn(challenge);

        challengeService.winChallenge(challengeId, winnerId);

        verify(completedChallengeDao).createCompletedChallenge(
                challenge.getUser1Id(),
                challenge.getUser2Id(),
                challenge.getSurahId(),
                winnerId,
                challenge.getStartedAt()
        );
        verify(activeChallengeDao).removeActiveChallenge(challengeId);
    }

    @Test
    void winChallenge_throwsChallengeNotFoundException() throws SQLException {
        int challengeId = 99;
        int winnerId = 2;

        when(activeChallengeDao.getActiveChallengeById(challengeId)).thenReturn(null);

        assertThrows(ChallengeNotFoundException.class, () -> {
            challengeService.winChallenge(challengeId, winnerId);
        });
    }

    @Test
    void winChallenge_throwsInvalidChallengeParticipantException() throws SQLException {
        int challengeId = 1;
        int winnerId = 999;

        ActiveChallenge challenge = new ActiveChallenge();
        challenge.setUser1Id(1);
        challenge.setUser2Id(2);
        challenge.setSurahId(10);
        challenge.setStartedAt(String.valueOf(LocalDateTime.now()));

        when(activeChallengeDao.getActiveChallengeById(challengeId)).thenReturn(challenge);

        assertThrows(InvalidChallengeParticipantException.class, () -> {
            challengeService.winChallenge(challengeId, winnerId);
        });
    }

    @Test
    void getActiveChallengeBySurahAndUser_returnsChallenge() throws Exception {
        int surahId = 2;
        int userId = 3;

        ActiveChallenge mockChallenge = new ActiveChallenge();
        when(activeChallengeDao.getActiveChallengeBySurahAndUser(surahId, userId)).thenReturn(mockChallenge);

        ActiveChallenge result = challengeService.getActiveChallengeBySurahAndUser(surahId, userId);

        assertNotNull(result);
        verify(activeChallengeDao).getActiveChallengeBySurahAndUser(surahId, userId);
    }
}
