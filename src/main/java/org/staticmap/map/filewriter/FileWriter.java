package org.staticmap.map.filewriter;

import org.jfree.chart.JFreeChart;
import org.staticmap.map.gpx.GpxStyler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface representing writer for writing the map/graph on disk in a given format
 */
public interface FileWriter {
    /**
     * Writes the resulting image from the mapping of the GPX file
     *
     * @param gpxFile        the input GPX file
     * @param outputFolder   the folder where to write the image
     * @param styler         the {@link GpxStyler} to use
     * @param map            the map to write
     * @param elevationGraph the elevation graph to write
     * @param width          width of the image
     * @param height         height of the image
     * @param chartHeight    height of the chart
     * @throws IOException if there is any issue while writing the file on disk
     */
    void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, JFreeChart elevationGraph, int width, int height, int chartHeight) throws IOException;

    void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, int width, int height) throws IOException;

    void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, JFreeChart elevationGraph, int width, int height) throws IOException;
}