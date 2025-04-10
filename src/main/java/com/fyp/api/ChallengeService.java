package com.fyp.api;

import com.fyp.cli.ChallengeRequest;
import com.fyp.cli.ActiveChallenge;
import com.fyp.cli.CompletedChallenge;
import com.fyp.db.ChallengeRequestDao;
import com.fyp.db.ActiveChallengeDao;
import com.fyp.db.CompletedChallengeDao;
import com.fyp.db.UserSurahProgressDao;

import java.sql.SQLException;
import java.util.List;

public class ChallengeService {
    private final ChallengeRequestDao challengeRequestDao;
    private final ActiveChallengeDao activeChallengeDao;
    private final CompletedChallengeDao completedChallengeDao;
    private final UserSurahProgressDao userSurahProgressDao;

    public ChallengeService(ChallengeRequestDao challengeRequestDao,
                          ActiveChallengeDao activeChallengeDao,
                          CompletedChallengeDao completedChallengeDao,
                          UserSurahProgressDao userSurahProgressDao) {
        this.challengeRequestDao = challengeRequestDao;
        this.activeChallengeDao = activeChallengeDao;
        this.completedChallengeDao = completedChallengeDao;
        this.userSurahProgressDao = userSurahProgressDao;
    }

    public void sendChallengeRequest(int senderId, int receiverId, int surahId) throws SQLException {
        // Check if either user has already memorized the Surah
        if (hasUserMemorizedSurah(senderId, surahId) || hasUserMemorizedSurah(receiverId, surahId)) {
            throw new IllegalArgumentException("One or both users have already memorized this Surah");
        }
        
        challengeRequestDao.sendChallengeRequest(senderId, receiverId, surahId);
    }

    public void acceptChallengeRequest(int requestId) throws SQLException {
        challengeRequestDao.acceptChallengeRequest(requestId);
    }

    public void rejectChallengeRequest(int requestId) throws SQLException {
        challengeRequestDao.rejectChallengeRequest(requestId);
    }

    public List<ChallengeRequest> getChallengeRequests(int userId) throws SQLException {
        return challengeRequestDao.getChallengeRequests(userId);
    }

    public List<ActiveChallenge> getActiveChallenges(int userId) throws SQLException {
        return activeChallengeDao.getActiveChallenges(userId);
    }

    public List<CompletedChallenge> getCompletedChallenges(int userId) throws SQLException {
        return completedChallengeDao.getCompletedChallenges(userId);
    }

    public void winChallenge(int challengeId, int winnerId) throws SQLException {
        // Get the active challenge details
        ActiveChallenge challenge = activeChallengeDao.getActiveChallengeById(challengeId);
        
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge not found");
        }
        
        // Verify that the winner is one of the participants
        if (challenge.getUser1Id() != winnerId && challenge.getUser2Id() != winnerId) {
            throw new IllegalArgumentException("User is not a participant in this challenge");
        }
        
        // Move the challenge to completed challenges
        completedChallengeDao.createCompletedChallenge(
            challenge.getUser1Id(),
            challenge.getUser2Id(),
            challenge.getSurahId(),
            winnerId,
            challenge.getStartedAt()
        );
        
        // Remove from active challenges
        activeChallengeDao.removeActiveChallenge(challengeId);
    }

    private boolean hasUserMemorizedSurah(int userId, int surahId) throws SQLException {
        // This method would check if the user has already memorized the Surah
        // Implementation would depend on your UserSurahProgressDao
        return userSurahProgressDao.hasUserMemorizedSurah(userId, surahId);
    }

    public ActiveChallenge getActiveChallengeBySurahAndUser(int surahId, int userId) throws SQLException {
        return activeChallengeDao.getActiveChallengeBySurahAndUser(surahId, userId);
    }
} 