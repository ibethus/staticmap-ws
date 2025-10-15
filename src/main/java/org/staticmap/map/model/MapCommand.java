package org.staticmap.map.model;

import java.io.InputStream;

public record MapCommand(
        InputStream gpxFile,
        Double centerLat,
        Double centerLon,
        Integer zoom,
        int width,
        int height,
        String tileProvider
) {
    public boolean hasGpx() {
        return gpxFile != null;
    }
}

