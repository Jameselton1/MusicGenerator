package web.jelton.musicgen.generator;

import java.util.List;

/*
 * A track represents an instrument
 */
public class Track {
    // contents of the track
    List<Segment> segments;
    // indicates the role of the track in the song e.g. chords
    char type;

    public Track(char t) {
        this.type = t;
    }

    public void addSegment(Segment segment){
        this.segments.add(segment);
    }
    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}
