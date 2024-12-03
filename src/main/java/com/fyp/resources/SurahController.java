package com.fyp.resources;

import io.swagger.annotations.Api;
import com.fyp.cli.Surah;
import com.fyp.api.SurahService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api("Surah API")
@Path("/api")
public class SurahController {
    private final SurahService surahService;

    // Constructor
    public SurahController(SurahService surahService) {
        this.surahService = surahService;
    }

    // Endpoint to fetch all Surahs
    @GET
    @Path("/surahs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSurahs() {
        try {
            List<Surah> surahs = surahService.getAllSurahs();
            return Response.ok(surahs).build();
        } catch (Exception e) {
            return Response.serverError().entity("Failed to fetch Surahs").build();
        }
    }

    // Endpoint to fetch a single Surah by ID
    @GET
    @Path("/surahs/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSurahById(@PathParam("id") int id) {
        try {
            Surah surah = surahService.getSurahById(id);
            if (surah != null) {
                return Response.ok(surah).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Surah not found").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity("Failed to fetch the Surah").build();
        }
    }
}