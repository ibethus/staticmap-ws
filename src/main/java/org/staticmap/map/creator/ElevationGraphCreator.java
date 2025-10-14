package org.staticmap.map.creator;

import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.staticmap.map.gpx.GpxStyler;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Composing and drawing an elevation graph from GPX elevation data
 */
public class ElevationGraphCreator {

    /**
     * Creates an elevation graph from the given waypoints.
     *
     * @param wayPoints input waypoints from the GPX file
     * @param styler    the {@link GpxStyler} to use for styling the graph
     * @return an {@link JFreeChart} representing the elevation graph
     */
    public static JFreeChart getElevationGraph(List<WayPoint> wayPoints, GpxStyler styler) {
        double[] indices = getIndices(wayPoints);
        double[] alt = getElevationPoints(wayPoints);
        XYSeries series = new XYSeries("Altitude");
        for (int i = 0; i < indices.length; i++) {
            series.add(indices[i], alt[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                null,
                null,
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        // Application du style (transparence, couleurs, etc.)
        var plot = chart.getXYPlot();
        plot.setOutlineVisible(false);
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);
        plot.getRenderer().setSeriesPaint(0, styler.graphLineColor());
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2f));
        // Remplissage sous la courbe (similaire à Area)
        if (plot.getRenderer() instanceof org.jfree.chart.renderer.xy.XYLineAndShapeRenderer r) {
            r.setSeriesShapesVisible(0, false);
            r.setSeriesLinesVisible(0, true);
            r.setSeriesFillPaint(0, styler.graphFillColor());
            //r.setSeriesFilled(0, true);
        }
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        chart.removeLegend();
        // Suppression des axes
        //plot.setDomainAxisVisible(false);
        //plot.setRangeAxisVisible(false);
        return chart;

    }

    /**
     * Gets all elevation points. If a point has negative elevation, it will be set to 0, thus avoiding ugly representation on the graph
     *
     * @param wayPoints input waypoints from the GPX file
     * @return an array of elevation points (in meters)
     */
    private static double[] getElevationPoints(List<WayPoint> wayPoints) {
        return wayPoints.stream().mapToDouble(wp -> {
            Double v = wp.getElevation()
                    .map(Length::doubleValue)
                    .orElse(0D);
            if (v < 0) {
                return 0;
            }
            return v;
        }).toArray();
    }

    private static double[] getIndices(List<WayPoint> wayPoints) {
        return IntStream.range(0, wayPoints.size())
                .mapToDouble(i -> i)
                .toArray();
    }
}
