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
import org.staticmap.map.resources.MapUseCases;
import org.staticmap.model.MapCommand;
import org.staticmap.model.MapFormData;
import org.staticmap.model.MapResult;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Path("/staticmap")
public class RestMapResource implements MapUseCases {

    private final MapApplicationService delegate;

    public RestMapResource(MapApplicationService delegate) {
        this.delegate = delegate;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/png")
    public Response createStaticMap(@MultipartForm MapFormData formData) {
        try {
            if (formData.gpx == null && (formData.center == null || formData.zoom == null)) {
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
        return new MapCommand(formData.gpx, 10D, 10D, 1, 100, 100, TileProvider.ARCGIS_ONLINE.getUrl());
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        return delegate.generateStaticMap(command);
    }
}

