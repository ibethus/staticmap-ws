package org.staticmap.map.gpx;

import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.staticmap.map.StaticMapCreator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class can be extended to implement your own GpxMapper. However, it can be used <i>as is</i>,
 * to provide default functionalities.
 * <p>
 * The {@link builder} should be used to create and configure an instance.
 */
public record DefaultGpxMapper(int width, int height, int chartHeight, GpxStyler styler) implements IGpxMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultGpxMapper.class);

    public BufferedImage map(InputStream gpxFile) throws IOException {
        LOGGER.info("Parsing GPX file...");
        List<Track> tracks = GpxParser.getTracks(gpxFile);
        List<WayPoint> wayPoints = GpxParser.getWayPoints(tracks);
        LOGGER.info("Parsed {} waypoints", wayPoints.size());
        return StaticMapCreator.createMap(wayPoints, width, height, styler);
    }

    /**
     * Builder for the {@link DefaultGpxMapper}
     */
    public static class builder {
        /**
         * Default map width. The elevation graph will always use the exact same width
         */
        private int width = 1000;
        /**
         * Default map height
         */
        private int height = 1400;
        /**
         * Default chart height. Resulting image height will be {@link #height} + chartHeight
         */
        private int chartHeight = 150;
        private GpxStyler gpxStyler;

        public builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public builder withChartHeight(int chartHeight) {
            this.chartHeight = chartHeight;
            return this;
        }

        public builder withGpxStyler(GpxStyler gpxStyler) {
            this.gpxStyler = gpxStyler;
            return this;
        }

        public DefaultGpxMapper build() {
            if (this.gpxStyler == null) {
                this.gpxStyler = GpxStyler.getDefaultStyler();
            }
            return new DefaultGpxMapper(this.width, this.height, this.chartHeight,
                    this.gpxStyler);
        }
    }
}
