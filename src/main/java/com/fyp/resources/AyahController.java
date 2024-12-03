package com.fyp.resources;

import io.swagger.annotations.Api;
import com.fyp.cli.Ayah;
import com.fyp.api.AyahService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api("Ayah API")
@Path("/api")
public class AyahController {
    private final AyahService ayahService;

    // Constructor
    public AyahController(AyahService ayahService) {
        this.ayahService = ayahService;
    }

    // Endpoint to fetch all Ayahs of a specific Surah
    @GET
    @Path("/ayahs/surah/{surahId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAyahsBySurahId(@PathParam("surahId") int surahId) {
        try {
            List<Ayah> ayahs = ayahService.getAyahsBySurahId(surahId);
            if (!ayahs.isEmpty()) {
                return Response.ok(ayahs).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No Ayahs found for the given Surah ID").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity("Failed to fetch Ayahs").build();
        }
    }

    // Endpoint to fetch a single Ayah by ID
    @GET
    @Path("/ayahs/{ayahId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAyahById(@PathParam("ayahId") int ayahId) {
        try {
            Ayah ayah = ayahService.getAyahById(ayahId);
            if (ayah != null) {
                return Response.ok(ayah).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Ayah not found").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity("Failed to fetch the Ayah").build();
        }
    }
}

