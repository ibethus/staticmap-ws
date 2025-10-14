package org.staticmap.map.resources;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.staticmap.map.MapApplicationService;
import org.staticmap.model.MapCommand;
import org.staticmap.model.MapResult;

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
