package web.jelton.musicgen.generator;
import java.util.Arrays;

/*
 * Segments represent sections of songs like verses and choruses
 * These Segments can be reused to create a longer song with a consistent melody
 * or chord progression
 */
public class Segment {
    private Bar[] bars;

    public Segment(Bar[] bars) {
        this.bars = bars;
    }

    public Bar[] getBars() {
        return bars;
    }

    public void setBars(Bar[] bars) {
        this.bars = bars;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "bars=" + Arrays.toString(bars) +
                '}';
    }
}