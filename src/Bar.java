package web.jelton.musicgen.generator;

import java.util.Arrays;

/*
 * Bars contain 4 beats by default but this can be
 * changed in future to represent different time signatures
 * read more here: https://hellomusictheory.com/learn/time-signatures/
 */
public class Bar {
    private Beat[] beats;

    public Bar(Beat[] beats) {
        this.beats = beats;
    }

    public Beat[] getBeats() {
        return beats;
    }

    public void setBeats(Beat[] beats) {
        this.beats = beats;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "beats=" + Arrays.toString(beats) +
                '}';
    }
}
