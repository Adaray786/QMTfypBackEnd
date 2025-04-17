package com.fyp.resources;

import com.fyp.api.RecommendationService;
import com.fyp.cli.SurahRecommendation;
import com.fyp.cli.User;
import com.fyp.client.RecommendationProcessingException;
import com.fyp.client.SurahDataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationControllerTest {

    RecommendationService recommendationService = mock(RecommendationService.class);
    RecommendationController recommendationController;
    User mockUser;

    @BeforeEach
    void setUp() {
        recommendationController = new RecommendationController(recommendationService);
        mockUser = new User(1, "test@email.com", null);
    }

    @Test
    void getRevisionRecommendations_ShouldReturn200_WhenSuccessful() throws SurahDataNotFoundException {
        List<SurahRecommendation> mockRecommendations = new ArrayList<>();
        mockRecommendations.add(new SurahRecommendation(
                1,
                "Al-Fatiha",
                "REVISION",
                9.5,
                "Has not been revised in over a week"
        ));


        when(recommendationService.getRevisionRecommendations(1)).thenReturn(mockRecommendations);

        Response response = recommendationController.getRevisionRecommendations(mockUser);
        assertEquals(200, response.getStatus());
        assertEquals(mockRecommendations, response.getEntity());
    }

    @Test
    void getRevisionRecommendations_ShouldReturn500_OnException() throws SurahDataNotFoundException {
        when(recommendationService.getRevisionRecommendations(1)).thenThrow(RuntimeException.class);

        Response response = recommendationController.getRevisionRecommendations(mockUser);
        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error getting revision recommendations"));
    }

    @Test
    void getNewSurahRecommendations_ShouldReturn200_WhenSuccessful() throws SurahDataNotFoundException {
        List<SurahRecommendation> mockRecommendations = new ArrayList<>();
        mockRecommendations.add(new SurahRecommendation(
                1,
                "Al-Fatiha",
                "REVISION",
                9.5,
                "Has not been revised in over a week"
        ));


        when(recommendationService.getNewSurahRecommendations(1)).thenReturn(mockRecommendations);

        Response response = recommendationController.getNewSurahRecommendations(mockUser);
        assertEquals(200, response.getStatus());
        assertEquals(mockRecommendations, response.getEntity());
    }

    @Test
    void getNewSurahRecommendations_ShouldReturn500_OnException() throws SurahDataNotFoundException {
        when(recommendationService.getNewSurahRecommendations(1)).thenThrow(RuntimeException.class);

        Response response = recommendationController.getNewSurahRecommendations(mockUser);
        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Error getting new surah recommendations"));
    }

    @Test
    void markAsRevised_ShouldReturn200_WhenUserAuthorizedAndSuccessful() throws RecommendationProcessingException {
        doNothing().when(recommendationService).markAsRevised(1, 2);

        Response response = recommendationController.markAsRevised(mockUser, 1, 2);
        assertEquals(200, response.getStatus());
        assertEquals("Surah marked as revised successfully", response.getEntity());
    }

    @Test
    void markAsRevised_ShouldReturn401_WhenUserIsNull() {
        Response response = recommendationController.markAsRevised(null, 1, 2);
        assertEquals(401, response.getStatus());
        assertEquals("Authentication required", response.getEntity());
    }

    @Test
    void markAsRevised_ShouldReturn403_WhenUserIsNotAuthorized() {
        User otherUser = new User(2, "other@email.com", null);

        Response response = recommendationController.markAsRevised(otherUser, 1, 2);
        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to update this user's progress", response.getEntity());
    }

    @Test
    void markAsRevised_ShouldReturn500_WhenExceptionThrown() throws RecommendationProcessingException {
        doThrow(RuntimeException.class).when(recommendationService).markAsRevised(1, 2);

        Response response = recommendationController.markAsRevised(mockUser, 1, 2);
        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("Failed to mark surah as revised"));
    }
}
