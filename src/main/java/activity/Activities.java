package activity;

import java.util.LinkedList;
import java.util.List;

public class Activities {
    private List<Activity> activities;

    public Activities(List<Activity> activities) {
        this.activities = activities;
    }

    public int numberOfTrackActivities() {
        return (int) activities.stream().filter(e -> (e instanceof ActivityWithTrack)).count();

    }

    public int numberOfWithoutTrackActivities() {
        return (int) activities.stream().filter(e -> (e instanceof ActivityWithoutTrack)).count();
    }

    public List<Report> distancesByTypes() {
        List<Report> result = new LinkedList<>();
        activities.stream().forEach(e -> result.add(new Report(e.getType(), e.getDistance())));
        return result;
    }
}
