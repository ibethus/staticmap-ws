package org.staticmap.map.resources;

import org.staticmap.map.model.MapCommand;
import org.staticmap.map.model.MapResult;

import java.io.IOException;

public interface MapUseCases {
    MapResult generateStaticMap(MapCommand command) throws IOException;
}
