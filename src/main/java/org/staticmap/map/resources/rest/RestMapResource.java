package org.staticmap.map.resources.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.staticmap.map.MapApplicationService;
import org.staticmap.map.gpx.TileProvider;
import org.staticmap.map.model.dto.GpxBucketDto;
import org.staticmap.map.model.form.GpxFormData;
import org.staticmap.map.resources.MapUseCases;
import org.staticmap.map.model.MapCommand;
import org.staticmap.map.model.form.MapFormData;
import org.staticmap.map.model.MapResult;
import org.staticmap.services.StorageService;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Path("/staticmap")
public class RestMapResource implements MapUseCases {

    private final MapApplicationService delegate;
    private final StorageService storageService;

    public RestMapResource(MapApplicationService delegate, StorageService storageService) {
        this.delegate = delegate;
        this.storageService = storageService;
    }

    @POST
    @Path("/uploadGpx")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStaticMap(@MultipartForm GpxFormData formData) {
        try {
            String storedFilename = storageService.storeGpx(formData.gpx);
            return Response.ok(new GpxBucketDto(storedFilename)).build();
        } catch (Exception e) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error storing GPX file: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/generate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/png")
    public Response createStaticMap(@MultipartForm MapFormData formData) {
        try {
            if (formData.gpxFileId == null && (formData.center == null || formData.zoom == null)) {
                throw new IllegalArgumentException("Either 'gpx' or both 'center' and 'zoom' must be provided.");
            }
            MapResult mapResult = generateStaticMap(toMapCommand(formData));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(mapResult.image(), "png", baos);
            byte[] imageData = baos.toByteArray();
            return Response.ok(new ByteArrayInputStream(imageData)).build();
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Invalid input: " + e.getMessage())
                        .build();
            }
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generating map: " + e.getMessage())
                    .build();
        }
    }

    private MapCommand toMapCommand(MapFormData formData) {
        InputStream gpx = storageService.retrieveGpx(formData.gpxFileId);
        return new MapCommand(gpx, 10D, 10D, 1, 100, 100, TileProvider.ARCGIS_ONLINE.getUrl());
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        return delegate.generateStaticMap(command);
    }
}

