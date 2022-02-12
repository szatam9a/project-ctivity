package activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Track {
    private List<TrackPoint> trackPoints = new LinkedList<>();

    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public void addTrackPoint(TrackPoint trackPoint) {
        trackPoints.add(trackPoint);
    }

    public void loadFromGpx(Path path) {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String aLine;
            String secondline;
            while ((aLine = br.readLine()) != null) {
                if (aLine.contains("<trkpt")) {
                    checkTheLinesForDataType(aLine, (secondline = br.readLine()));
                }
            }
        } catch (IOException io) {
            throw new IllegalArgumentException("no file to read in");
        }
    }
    public void loadFromGpx(InputStream path) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(path))) {
            String aLine;
            String secondline;
            while ((aLine = br.readLine()) != null) {
                if (aLine.contains("<trkpt")) {
                    checkTheLinesForDataType(aLine, (secondline = br.readLine()));
                }
            }
        } catch (IOException io) {
            throw new IllegalArgumentException("no file to read in");
        }
    }

    private void checkTheLinesForDataType(String firstLine, String secondLine) {
        String coordinate = "<trkpt";
        String trackPoint = "<ele";
        Optional<Coordinate> aCord = Optional.empty();
        if (firstLine.contains(coordinate)) {
            aCord = Optional.of(createCoordinate(firstLine));
        }
        if (secondLine.contains(trackPoint)) {
            createTrackPoint(secondLine, aCord);
            return;
        }
    }

    private Coordinate createCoordinate(String aLine) {
        int indexOfLati = aLine.indexOf("lat=");
        int indexOfLongi = aLine.indexOf("lon=");
        double lati = Double.parseDouble(aLine.substring(indexOfLati + 5, indexOfLongi - 2));
        double longi = Double.parseDouble(aLine.substring(indexOfLongi + 5, indexOfLongi + 15));
        return new Coordinate(lati, longi);
    }

    private void createTrackPoint(String aLine, Optional<Coordinate> aCord) {
        double elevation;
        elevation = getTheValue(aLine);
        if (aCord.isPresent()) {
            trackPoints.add(new TrackPoint(aCord.get(), elevation));
        } else {
            throw new IllegalArgumentException("no coordinate to create TrackPoint");
        }
    }

    public double getTheValue(String aLine) {
        int indexOfElevation = aLine.indexOf("<ele>");
        double elevation = Double.parseDouble(aLine.substring(indexOfElevation + 5, indexOfElevation + 10));
        return elevation;
    }

    public Double getFullElevation() {
        AtomicReference<Double> elevation = new AtomicReference<>((double) 0);
        AtomicReference<Double> last = new AtomicReference<>(trackPoints.get(0).getElevation());
        trackPoints.stream().mapToDouble(e -> e.getElevation()).forEach(e -> {
            if (e > last.get()) {
                elevation.updateAndGet(v -> v + e - last.get());
            }
            last.set(e);
        });
        return elevation.get();
    }

    public double getFullDecrease() {
        if (trackPoints.isEmpty()) {
            throw new IllegalArgumentException("empty list");
        }
        double decrease = 0;
        double tempElev;
        double lastPoint = trackPoints.get(0).getElevation();
        for (TrackPoint tp : trackPoints) {
            if ((tempElev = tp.getElevation()) < lastPoint) {
                decrease += lastPoint - tempElev;
                lastPoint = tempElev;
            }
            lastPoint = tempElev;
        }
        return decrease;
    }

    public Coordinate findMinimumCoordinate() {
        Coordinate result = new Coordinate(
                trackPoints.stream().mapToDouble(e -> e.getCoordinate().getLatitude()).min().getAsDouble()
                ,
                trackPoints.stream().mapToDouble(e -> e.getCoordinate().getLongitude()).min().getAsDouble()
        );
        return result;
    }

    public Coordinate findMaximumCoordinate() {
        Coordinate result = new Coordinate(
                trackPoints.stream().mapToDouble(e -> e.getCoordinate().getLatitude()).max().getAsDouble()
                ,
                trackPoints.stream().mapToDouble(e -> e.getCoordinate().getLongitude()).max().getAsDouble()
        );
        return result;
    }

    public double getDistance() {
        if (trackPoints.size() < 2) {
            throw new IllegalArgumentException("Cant calculate distance without track points");
        }
        TrackPoint last = trackPoints.get(0);
        double distance = 0;
        for (TrackPoint t : trackPoints) {
            distance += t.getDistanceFrom(last);
            last = t;
        }
        return distance;
    }

    public double getRectangleArea() {
        return makeRectangle(findMinimumCoordinate(), findMaximumCoordinate());
    }

    private double makeRectangle(Coordinate min, Coordinate max) {
        Coordinate minMin = new Coordinate(min.getLatitude(), min.getLongitude());
        Coordinate minMax = new Coordinate(min.getLatitude(), max.getLongitude());
        Coordinate maxMin = new Coordinate(max.getLatitude(), min.getLongitude());
        double rectangleASide =  (minMax.getLongitude()-minMin.getLongitude());
        double rectangleBSide =  (maxMin.getLatitude()-minMin.getLatitude());

        return rectangleASide * rectangleBSide;
    }
}
