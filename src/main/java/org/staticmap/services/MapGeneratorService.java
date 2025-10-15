package org.staticmap.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.staticmap.map.creator.StaticMapCreator;
import org.staticmap.map.gpx.DefaultGpxMapper;
import org.staticmap.map.gpx.GpxStyler;
import org.staticmap.map.model.MapCommand;

import java.awt.image.BufferedImage;
import java.io.IOException;

@ApplicationScoped
public class MapGeneratorService {
    public BufferedImage generateMap(MapCommand command) throws IOException {
        if (command.hasGpx()) {
            DefaultGpxMapper gpxMapper = new DefaultGpxMapper.builder().build();
            return gpxMapper.map(command.gpxFile());
        } else
            return StaticMapCreator.createMap(command, GpxStyler.getDefaultStyler());

    }
}

