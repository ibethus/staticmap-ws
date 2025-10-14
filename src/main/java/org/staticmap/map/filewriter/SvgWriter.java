package org.staticmap.map.filewriter;


import org.jfree.chart.JFreeChart;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import org.slf4j.Logger;
import org.staticmap.map.gpx.GpxStyler;
import org.staticmap.map.gpx.GraphToMapPosition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Writer pour générer un graphique d'élévation au format SVG.
 */
public class SvgWriter extends AbstractWriter<SVGGraphics2D> {

    public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SvgWriter.class);

    @Override
    protected void writeImage(File gpxFile, Path outputFolder, String suffix, SVGGraphics2D map, int width, int height) throws IOException {
        // Prépare le nom du fichier de sortie
        File svgFile = getBaseName(gpxFile, outputFolder, suffix);

        // Écrit le SVG dans le fichier
        SVGUtils.writeToSVG(svgFile, map.getSVGElement());
        logSuccess(svgFile);
    }

    @Override
    protected void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage map, int width, int height) throws IOException {
        File svgFile = getBaseName(gpxFile, outputFolder, suffix);
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(width, height);
        svgGraphics2D.drawImage(map, 0, 0, width, height, null);
        SVGUtils.writeToSVG(svgFile, svgGraphics2D.getSVGElement());
        logSuccess(svgFile);
    }

    @Override
    protected void writeChart(File gpxFile, Path outputFolder, String suffix, JFreeChart elevationGraph, GpxStyler styler, int width, int height) throws IOException {
        File svgFile = getBaseName(gpxFile, outputFolder, suffix);
        SVGGraphics2D svg2d = new SVGGraphics2D(width, height);
        elevationGraph.draw(svg2d, new Rectangle(0, 0, width, height));
        SVGUtils.writeToSVG(svgFile, svg2d.getSVGElement());
        logSuccess(svgFile);
    }

    @Override
    protected SVGGraphics2D combineGraphAndMap(GpxStyler styler, BufferedImage map, JFreeChart elevationGraph, int chartHeight) {
        SVGGraphics2D graphics = new SVGGraphics2D(map.getWidth(),
                map.getHeight() + chartHeight);
        if (GraphToMapPosition.BOTTOM == styler.graphPosition()) {
            graphics.drawImage(map, 0, 0, null);
            elevationGraph.draw(graphics, new Rectangle(0, map.getHeight(), map.getWidth(), chartHeight));
        } else {
            graphics.drawImage(map, 0, chartHeight, null);
            elevationGraph.draw(graphics, new Rectangle(0, 0, map.getWidth(), chartHeight));
        }
        return graphics;
    }

    private static File getBaseName(File gpxFile, Path outputFolder, String suffix) {
        String baseName = gpxFile.getName().replaceFirst("\\.gpx$", "");
        String fileSuffix = (suffix != null && !suffix.isEmpty()) ? ("-" + suffix) : "";
        return outputFolder.resolve(baseName + fileSuffix + ".svg").toFile();
    }

    private static void logSuccess(File svgFile) {
        LOGGER.info("SVG file written to: {}", svgFile.getAbsolutePath());
    }
}