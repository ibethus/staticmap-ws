package org.staticmap.map.resources.mcp;

import io.quarkiverse.mcp.server.*;
import jakarta.inject.Singleton;
import org.staticmap.map.MapApplicationService;
import org.staticmap.map.gpx.TileProvider;
import org.staticmap.map.model.MapCommand;
import org.staticmap.map.model.MapResult;
import org.staticmap.map.model.dto.GpxBucketDto;
import org.staticmap.map.resources.MapUseCases;
import org.staticmap.services.StorageService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Singleton
public class McpMapResource implements MapUseCases {

    public static final String GPX_ITEM = "gpx://item";
    private final MapApplicationService delegate;
    private final StorageService storageService;

    public McpMapResource(MapApplicationService delegate, StorageService storageService) {
        this.delegate = delegate;
        this.storageService = storageService;
    }

    @Tool(description = "Outil de génération de carte statique à partir d'un fichier GPX")
    ImageContent generateStaticMapToolFromGpx(@ToolArg(description = "Id du fichier GPX précédemment téléversé") String gpxFileId,
                                              @ToolArg(description = "Largeur de l'image", defaultValue = "500") int width,
                                              @ToolArg(description = "Hauteur de l'image", defaultValue = "500") int height) throws IOException {
        InputStream gpx = null;
        if (gpxFileId != null) {
            gpx = storageService.retrieveGpx(gpxFileId);
        }
        MapResult mapResult = generateStaticMap(toMapCommand(gpx, null, null, 0, width, height));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mapResult.image(), "png", baos);
        byte[] imageData = baos.toByteArray();
        return new ImageContent(Base64.getEncoder().encodeToString(imageData), "image/png");
    }

    @Tool(description = "Outil de génération de carte statique à partir de coordonnées GPS")
    ImageContent generateStaticMapTool(@ToolArg(description = "Latitude du centre", required = false) Double centerLat,
                                       @ToolArg(description = "Longitude du centre", required = false) Double centerLon,
                                       @ToolArg(description = "Niveau de zoom, de 1 à 20", required = false) int zoom,
                                       @ToolArg(description = "Largeur de l'image") int width,
                                       @ToolArg(description = "Hauteur de l'image") int height) throws IOException {

        MapResult mapResult = generateStaticMap(toMapCommand(null, centerLat, centerLon, zoom, width, height));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mapResult.image(), "png", baos);
        byte[] imageData = baos.toByteArray();
        return new ImageContent(Base64.getEncoder().encodeToString(imageData), "image/png");
    }

    @Resource(uri = GPX_ITEM + "/all", description = "Ressource GPX pour les outils de génération de carte")
    public List<TextResourceContents> allGpxFiles() {
        return storageService.listAllGpxFiles().stream()
                .map(res -> new TextResourceContents("%s/%s".formatted(GPX_ITEM, res.objectName()), new GpxBucketDto(res.objectName()).toString(), "application/json"))
                .toList();
    }

    private MapCommand toMapCommand(InputStream gpx, Double centerLat, Double centerLon, int zoom, int width, int height) {
        return new MapCommand(gpx, centerLat, centerLon, zoom, width, height, TileProvider.ARCGIS_ONLINE.getUrl());
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        return delegate.generateStaticMap(command);
    }
}
