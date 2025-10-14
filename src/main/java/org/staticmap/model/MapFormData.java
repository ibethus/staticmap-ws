package org.staticmap.model;

import java.io.InputStream;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.staticmap.map.gpx.TileProvider;

public class MapFormData {

    @FormParam("gpx")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream gpx;

    @FormParam("size")
    @PartType(MediaType.TEXT_PLAIN)
    public String size;

    @FormParam("zoom")
    @PartType(MediaType.TEXT_PLAIN)
    public Integer zoom;

    @FormParam("center")
    @PartType(MediaType.TEXT_PLAIN)
    public String center;

    @FormParam("tileProvider")
    @PartType(MediaType.TEXT_PLAIN)
    public TileProvider tileProvider;

    @FormParam("markers")
    @PartType(MediaType.TEXT_PLAIN)
    public String markers;

    @FormParam("path")
    @PartType(MediaType.TEXT_PLAIN)
    public String path;
}


