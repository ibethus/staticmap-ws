package org.staticmap.map.resources;

import org.staticmap.model.MapCommand;
import org.staticmap.model.MapResult;

import java.io.IOException;

public interface MapUseCases {
    MapResult generateStaticMap(MapCommand command) throws IOException;
}
