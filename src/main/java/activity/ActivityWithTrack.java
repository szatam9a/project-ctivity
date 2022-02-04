package activity;

public class ActivityWithTrack implements Activity {
    private ActivityType activityType;
    private Track track;

    public ActivityWithTrack(ActivityType activityType, Track track) {
        this.activityType = activityType;
        this.track = track;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public int getDistance() {
        return 0;
    }

    @Override
    public ActivityType getType() {
        return this.activityType;
    }
}
