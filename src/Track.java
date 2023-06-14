 import Enum.TrackType;

/*
 * A track represents an instrument
 */
public class Track {
    // contents of the track
    Segment[] segments;
    // indicates the role of the track in the song e.g. chords
    TrackType type;

    public Track(TrackType t, int length) {
        this.type = t;
        this.segments = new Segment[length];
    }

    public void setSegment(int index, Segment segment){
        this.segments[index] = segment;
    }
    public TrackType getType() {
        return type;
    }

    public void setType(TrackType type) {
        this.type = type;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }
}
