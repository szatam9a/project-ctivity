package activity;

public class ActivityWithTrack implements Activity {
    private Track track;
    private ActivityType activityType;

    public ActivityWithTrack(Track track, ActivityType activityType) {
        this.track = track;
        this.activityType = activityType;
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
