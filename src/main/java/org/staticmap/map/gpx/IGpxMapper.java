package org.staticmap.map.gpx;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public interface IGpxMapper {
    /**
     * This method transform a GPX file into an image map.
     *
     * @param gpxFile the GPX file
     * @return the GPF file metadata
     * @throws IOException if the GPX file cannot be read or the image cannot be writen to disk
     */
    BufferedImage map(InputStream gpxFile) throws IOException;
}
