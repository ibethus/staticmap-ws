package org.staticmap.map.filewriter;

import org.jfree.chart.JFreeChart;
import org.staticmap.map.gpx.GpxStyler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractWriter<U> implements FileWriter {

    @Override
    public void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, JFreeChart elevationGraph, int width, int height, int chartHeight) throws IOException {
        if (styler.separateFiles()) {
            writeImage(gpxFile, outputFolder, "map", map, width, height);
            writeChart(gpxFile, outputFolder, "elevation", elevationGraph, styler, width, chartHeight);
        } else {
            U resultingImage = combineGraphAndMap(styler, map, elevationGraph, chartHeight);
            writeImage(gpxFile, outputFolder, null, resultingImage, width, height);
        }
    }

    @Override
    public void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, int width, int height) throws IOException {
        writeImage(gpxFile, outputFolder, null, map, width, height);
    }

    @Override
    public void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, JFreeChart elevationGraph, int width, int height) throws IOException {
        writeChart(gpxFile, outputFolder, null, elevationGraph, styler, width, height);
    }

    protected abstract void writeImage(File gpxFile, Path outputFolder, String suffix, U map, int width, int height) throws IOException;

    protected abstract void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage map, int width, int height) throws IOException;

    protected abstract void writeChart(File gpxFile, Path outputFolder, String suffix, JFreeChart elevationGraph, GpxStyler styler, int width, int height) throws IOException;

    protected abstract U combineGraphAndMap(GpxStyler styler, BufferedImage map, JFreeChart elevationGraph, int chartHeight);
}
