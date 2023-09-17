package web.jelton.musicgen.generator;
/*
 * songs are made up of a class hierarchy:
 *      - Tracks
 *      - Segments
 *      - Bars
 *      - Beats
 *      - Notes
 */
public class Song {

    private Track[] tracks;
    public Track[] getTracks() {
        return tracks;
    }
    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }

}
