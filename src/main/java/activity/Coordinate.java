package activity;

public class Coordinate {
    private final double latitude; // [-90,90]
    private final double longitude;//[-180,180]

    public Coordinate(double latitude, double longitude) {
        validateLat(latitude);
        validateLat(longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void validateLat(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latide out");
        }
    }

    private void validateLong(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("latide out");
        }
    }
}
