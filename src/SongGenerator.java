package web.jelton.musicgen.generator;
import java.util.Random;
import web.jelton.musicgen.generator.Enum.*;

public class SongGenerator {
    Random random = new Random();

    private Mode mode;
    private Note[] scale;
    private char[] songStructure;
    private char[] trackStructure;

    public Song newSong() {
        Song song = new Song();
        createSongProperties();
        song.setTracks(createTracks(songStructure, trackStructure));
        return song;
    }

    private void createSongProperties() {
        int root = random.nextInt(Note.values().length);
        this.mode = Mode.values()[random.nextInt(Mode.values().length)];
        this.scale = createScale(root, mode);
        this.songStructure = randomSongStructure();
        this.trackStructure = randomtrackStructure();
    }
    // Select a random value from the SongStructure enumerator
    private char[] randomSongStructure() {
        int index = random.nextInt(SongStructure.values().length);
        return SongStructure.values()[index].toString().toCharArray();
    }
    // Select a random value from the instrument structure enumerator. 
    // Create a track for each enum value, generate it's contents based on the character.
    private char[] randomtrackStructure() {
        int index = random.nextInt(TrackStructure.values().length);
        return TrackStructure.values()[index].toString().toCharArray();
    }

    private Track[] createTracks(char[] songStructure, char[] trackStructure) {
        Track[] tracks = new Track[trackStructure.length];
        // Create a new track for every track in the track structure.
        for (int i = 0; i < tracks.length; i++) {
            tracks[i] = new Track(trackStructure[i]);
        }
        // Create a new segment for every segment in the song structure.
        for (int i = 0; i < songStructure.length; i++) {
            // Create a set of root notes for the segment to revolve around
            int[][] rootNotes = generateRootNotes(3, 4);
            // For every track, create a segment, which all revolve around the same root notes, 
            // so that the different instruments are synchronised
            for (int j = 0; j < tracks.length; j++) {
                Segment segment = createSegment(rootNotes, tracks[j].getType());
                tracks[j].addSegment(segment);
            }
        }
        return tracks;
    }
    // The root notes generated will be the root note for the chords on each beat,
    // and melodies will start from them.
    private int[][] generateRootNotes(int setsOfBars, int beatsInBars) {
        int[][] rootNotes = new int[setsOfBars][beatsInBars];
        for (int i = 0; i < setsOfBars; i++) {
            for (int j = 0; j < beatsInBars; j++) {
                rootNotes[i][j] = random.nextInt(7);
            }
        }
        return rootNotes;
    }
    // Create segment based on the root notes and the type.
    private Segment createSegment(int[][] rootNotes, char type){
        Bar[] bars = new Bar[rootNotes.length];
        for (int i = 0; i < rootNotes.length; i++) {
            bars[i] = createBar(rootNotes[i], type);
        }
        return new Segment(bars);
    }
    // Using the root notes, and the track type, generate the notes for the section.
    private Bar createBar(int[] rootNotes, char type){
        Beat[] beats = new Beat[rootNotes.length];
        Note[][] notes = null;
        for (int i = 0; i < beats.length; i++) {
            switch (type) {
                case 'M' -> {
                    notes = new Note[random.nextInt(3) + random.nextInt(2) + 1][1];
                    for (int j = 0; j < notes.length; j++) {
                        notes[j][0] = scale[random.nextInt(scale.length)];
                    }
                }
                case 'C' -> {
                    notes = new Note[1][3];
                    notes[0] = new Chord(rootNotes[i], ChordType.triad, scale).getNotes();
                }
                case 'B' -> {
                    notes = new Note[random.nextInt(2) + 1][1];
                    for (int j = 0; j < notes.length; j++) {
                        notes[j][0] = (j == 0) ? scale[rootNotes[i]] : scale[random.nextInt(scale.length)];
                    }
                }
            }
            beats[i] = new Beat(notes);
        }
        return new Bar(beats);
    }
    // Return an array of musical notes, based on a scale template (mode) and a root note
    private Note[] createScale(int root, Mode mode){
        int[] template = scaleTemplate(mode);
        Note[] scale = new Note[template.length];

        for (int i = 0; i < template.length; i++) {
            int index = (root + template[i]) % 12;
            scale[i] = Note.values()[index];
        }
        return scale;
    }
    // Values represent the number of notes from the scale root note for each position of the scale
    private int[] scaleTemplate(Mode mode){
        return switch (mode){
            case major -> new int[]{0, 2, 4, 5, 7, 9, 11};
            case minor -> new int[]{0, 2, 3, 5, 7, 8, 10};
            /*case dorian -> new int[]{0, 2, 3, 5, 7, 9, 10};
            case phrygian -> new int[]{0, 1, 3, 5, 7, 8, 10};
            case lydian -> new int[]{0, 2, 4, 6, 7, 9, 11};
            case mixolydian -> new int[]{0, 2, 4, 5, 7, 9, 10};
            case locrian -> new int[]{0, 1, 3, 5, 6, 8, 10};
            case hungarian -> new int[]{0, 2, 3, 6, 7, 8, 11};
            case melodic -> new int[]{0, 2, 3, 5, 7, 9, 11};
            case harmonic -> new int[]{0, 2, 3, 5, 7, 8, 11};*/
            default -> null;
        };
    }

    public Note[] getScale() {
        return scale;
    }

    public void setScale(Note[] scale) {
        this.scale = scale;
    }

    public Mode getMode() {
        return mode;
    }
}