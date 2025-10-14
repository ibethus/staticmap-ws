package org.staticmap.map.filewriter;

import org.jfree.chart.JFreeChart;
import org.slf4j.LoggerFactory;
import org.staticmap.map.gpx.GpxStyler;
import org.staticmap.map.gpx.GraphToMapPosition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of a {@link FileWriter} for writing PNG images
 */
public class PngWriter extends AbstractWriter<BufferedImage> {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PngWriter.class);

    @Override
    protected void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage image, int width,
            int height) throws IOException {
        File file;
        String gpxName = gpxFile.getName().split("\\.")[0];
        String fileName = String.format("%s%s.png", gpxName, suffix == null ? "" : "-" + suffix);
        if (outputFolder != null) {
            file = outputFolder.resolve(fileName).toFile();
        } else {
            file = new File(gpxFile.getParent(), fileName);
        }
        LOGGER.info("Writing finished image to : {}", file.getAbsolutePath());
        ImageIO.write(image, "PNG", file);
    }

    @Override
    protected void writeChart(File gpxFile, Path outputFolder, String suffix, JFreeChart elevationGraph,
            GpxStyler styler, int width, int height) throws IOException {
        LOGGER.info("Drawing elevation graph...");
        BufferedImage mImage = chartToImage(elevationGraph, styler, height, width);
        writeImage(gpxFile, outputFolder, suffix, mImage, width, height);
        LOGGER.info("Drawing of elevation graph finished");
    }

    private static BufferedImage chartToImage(JFreeChart elevationGraph, GpxStyler styler, int height, int width) {
        BufferedImage mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(styler.backgroundColor());
        graphics.fillRect(0, 0, width, height);
        elevationGraph.draw(graphics, new Rectangle(0, 0, width, height));
        return mImage;
    }

    @Override
    protected BufferedImage combineGraphAndMap(GpxStyler styler, BufferedImage map, JFreeChart elevationGraph,
            int chartHeight) {
        BufferedImage combinedImages = new BufferedImage(map.getWidth(),
                map.getHeight() + chartHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = combinedImages.getGraphics();
        if (GraphToMapPosition.BOTTOM == styler.graphPosition()) {
            graphics.drawImage(map, 0, 0, null);
            graphics.drawImage(chartToImage(elevationGraph, styler, map.getHeight(), map.getWidth()), 0,
                    map.getHeight(), null);
        } else {
            graphics.drawImage(map, 0, chartHeight, null);
            graphics.drawImage(chartToImage(elevationGraph, styler, map.getHeight(), map.getWidth()), 0, 0, null);
        }
        graphics.dispose();
        return combinedImages;
    }
}
