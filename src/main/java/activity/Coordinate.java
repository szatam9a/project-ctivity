package activity;

public class Coordinate {
    private final double latitude; // [-90,90]
    private final double longitude;//[-180,180]

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
