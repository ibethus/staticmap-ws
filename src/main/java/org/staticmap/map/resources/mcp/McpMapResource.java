package org.staticmap.map.resources.mcp;

import io.quarkiverse.mcp.server.ImageContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import jakarta.inject.Singleton;
import org.staticmap.map.MapApplicationService;
import org.staticmap.map.gpx.TileProvider;
import org.staticmap.map.resources.MapUseCases;
import org.staticmap.map.model.MapCommand;
import org.staticmap.map.model.MapResult;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Singleton
public class McpMapResource implements MapUseCases {

    private final MapApplicationService delegate;

    public McpMapResource(MapApplicationService delegate) {
        this.delegate = delegate;
    }

    @Tool(description = "Outil de génération de carte statique")
    ImageContent generateStaticMapTool(@ToolArg(description = "Latitude du centre") Double centerLat,
                                       @ToolArg(description = "Longitude du centre") Double centerLon,
                                       @ToolArg(description = "Niveau de zoom, de 1 à 20") int zoom,
                                       @ToolArg(description = "Largeur de l'image") int width,
                                       @ToolArg(description = "Hauteur de l'image") int height) throws IOException {
        MapResult mapResult = generateStaticMap(toMapCommand(centerLat, centerLon, zoom, width, height));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mapResult.image(), "png", baos);
        byte[] imageData = baos.toByteArray();
        return new ImageContent(Base64.getEncoder().encodeToString(imageData), "image/png");
    }

    private MapCommand toMapCommand(Double centerLat, Double centerLon, int zoom, int width, int height) {
        return new MapCommand(null, centerLat, centerLon, zoom, width, height, TileProvider.ARCGIS_ONLINE.getUrl());
    }

    private MapCommand toMapCommand(InputStream input) {
        return new MapCommand(input, 10D, 10D, 1, 100, 100, TileProvider.ARCGIS_ONLINE.getUrl());
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        return delegate.generateStaticMap(command);
    }
}
