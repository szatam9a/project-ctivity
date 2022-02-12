package activity;

public class ActivityWithoutTrack implements Activity{
    private ActivityType activityType;

    public ActivityWithoutTrack(ActivityType activityType) {
        this.activityType = activityType;
    }


    @Override
    public ActivityType getType() {
        return this.activityType;
    }

    @Override
    public int getDistance() {
        return 0;
    }
}
