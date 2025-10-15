package org.staticmap.map.model.form;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.InputStream;

public class GpxFormData {

    @FormParam("gpx")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream gpx;
}
