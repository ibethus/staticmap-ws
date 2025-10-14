package org.staticmap;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.staticmap.model.MapFormData;
import org.staticmap.services.MapGeneratorService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Path("/staticmap")
public class MapResource {

    private final MapGeneratorService mapGeneratorService;

    public MapResource(MapGeneratorService mapGeneratorService) {
        this.mapGeneratorService = mapGeneratorService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/png")
    public Response createStaticMap(@MultipartForm MapFormData formData) throws IOException {
        if (formData.gpx == null && (formData.center == null || formData.zoom == null)) {
            throw new IllegalArgumentException("Either 'gpx' or both 'center' and 'zoom' must be provided.");
        }

        BufferedImage mapImage = mapGeneratorService.generateMap(formData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mapImage, "png", baos);
        byte[] imageData = baos.toByteArray();
        return Response.ok(new ByteArrayInputStream(imageData)).build();
    }
}

