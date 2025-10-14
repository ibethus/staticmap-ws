package org.staticmap.map;


import jakarta.enterprise.context.ApplicationScoped;
import org.staticmap.map.resources.MapUseCases;
import org.staticmap.model.MapCommand;
import org.staticmap.model.MapResult;
import org.staticmap.services.MapGeneratorService;

import java.awt.image.BufferedImage;
import java.io.IOException;

@ApplicationScoped
public class MapApplicationService implements MapUseCases{

    private final MapGeneratorService mapGeneratorService;

    public MapApplicationService(MapGeneratorService mapGeneratorService) {
        this.mapGeneratorService = mapGeneratorService;
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        BufferedImage mapImage = mapGeneratorService.generateMap(command);
        return new MapResult(mapImage);
    }
}

