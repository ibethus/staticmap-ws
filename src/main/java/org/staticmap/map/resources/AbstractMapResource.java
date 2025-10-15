package org.staticmap.map.resources;

import org.staticmap.map.MapApplicationService;
import org.staticmap.map.model.MapCommand;
import org.staticmap.map.model.MapResult;

import java.io.IOException;

public abstract class AbstractMapResource implements MapUseCases {
    
    private final MapApplicationService mapApplicationService;

    protected AbstractMapResource(MapApplicationService mapApplicationService) {
        this.mapApplicationService = mapApplicationService;
    }

    @Override
    public MapResult generateStaticMap(MapCommand command) throws IOException {
        return null;
    }
}
