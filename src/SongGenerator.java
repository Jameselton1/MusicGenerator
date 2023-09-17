package web.jelton.musicgen.generator;

import java.util.Random;

import web.jelton.musicgen.generator.Enum.Mode;
import web.jelton.musicgen.generator.Enum.Note;
import web.jelton.musicgen.generator.Enum.ChordType;
import web.jelton.musicgen.generator.Enum.Instruments;
import web.jelton.musicgen.generator.Enum.Structure;
import web.jelton.musicgen.generator.Enum.TrackType;

public class SongGenerator {
    Random random = new Random();

    private Song song;

    private Mode mode;
    private Note[] scale;

    public SongGenerator() {
        this.song = createSong();
    }

    /*
     *  Create and return a song object, with generated contents
     */ 
    private Song createSong() {
        Song song = new Song();
        createSongProperties();

        // random structure value into a char array
        int structureIndex = random.nextInt(Structure.values().length);
        char[] structure = Structure.values()[structureIndex].toString().toCharArray();

        // random instruments value into a char array
        int instrumentIndex = random.nextInt(Instruments.values().length);
        char[] instruments = Instruments.values()[instrumentIndex].toString().toCharArray();

        song.setTracks(createTracks(structure, instruments));

        return song;
    }

    /*
     *  Randomly select the root note and mode, and then generate the scale
     */
    private void createSongProperties(){
        int root = random.nextInt(Note.values().length);
        this.mode = Mode.values()[random.nextInt(Mode.values().length)];
        this.scale = createScale(root, mode);
    }

    /*
     * Create a track for each instrument, assign it an appropriate tracktype, generate the music
     */
    private Track[] createTracks(char[] structure, char[] instruments){

        Track[] tracks = new Track[instruments.length];

        // for each track type from the randomly selected instruments enumerator value
        for (int i = 0; i < tracks.length; i++)
            // create a new track with the corresponding TrackType value
            tracks[i] = new Track(getTrackType(instruments[i]), structure.length);

        // for each segment in the song structure
        for (int i = 0; i < structure.length; i++) {
            // generate the root notes for the segment
            int[][] rootNotes = generateRootNotes(3, 4);
            // for each track, create the next segment
            for (int j = 0; j < tracks.length; j++)
                tracks[j].setSegment(i, createSegment(rootNotes, tracks[j].getType()));
        }

        return tracks;
    }

    private TrackType getTrackType(char instrument){
        return switch (instrument){
            case 'M' -> TrackType.Melody;
            case 'C' -> TrackType.Chord;
            case 'B' -> TrackType.Bass;
            default -> null;
        };
    }

    /*
     *  Returns a 2d array of 'root notes' which represent the notes within the scale (0-6) which are
     *  the root notes of the chords in each corresponding position.
     *  
     *  The first dimension represents the set of bars it's for and the second dimension represents
     *  the number of beats in each bar.
     *  
     *  This is done to provide the same root notes for the melody, chord progression and bass line so they can
     *  share the same starting note
     * */
    private int[][] generateRootNotes(int setsOfBars, int beatsInBars){
        int[][] rootNotes = new int[setsOfBars][beatsInBars];

        /*
         *  Retrieve a random note from the scale for each beat in each bar
         *  to act as the root note for the chord
         */
        for (int i = 0; i < setsOfBars; i++)
            for (int j = 0; j < beatsInBars; j++)
                rootNotes[i][j] = random.nextInt(7);

        return rootNotes;
    }

    /*
     *  Create and return a segment which contains notes.
     *  Notes are generated based on the track type.
     */
    private Segment createSegment(int[][] rootNotes, TrackType type){
        Bar[] bars = new Bar[rootNotes.length];
        for (int i = 0; i < rootNotes.length; i++)
            bars[i] = createBar(rootNotes[i], type);
        return new Segment(bars);
    }

    /*
     *  Create a bars array containing the notes to be converted to midi
     */
    private Bar createBar(int[] rootNotes, TrackType type){
        Beat[] beats = new Beat[rootNotes.length];

        /* 
         * Dimension 1 represents the number of subdivisions
         * Dimension 2 represents how many notes are played simultaneously
         */
        Note[][] notes = null;

        for (int i = 0; i < beats.length; i++) {
            // for each note
            switch (type) {
                case Melody -> {
                    notes = new Note[random.nextInt(3) + random.nextInt(2) + 1][1];
                    // randomly select a note from the scale
                    for (int j = 0; j < notes.length; j++)
                        notes[j][0] = scale[random.nextInt(scale.length)];
                }
                case Chord -> {
                    notes = new Note[1][3];
                    // use the chord class to find the notes for the chord
                    notes[0] = new Chord(rootNotes[i], ChordType.triad, scale).getNotes();
                }
                case Bass -> {
                    notes = new Note[random.nextInt(2) + 1][1];
                    // start on the root note then select random notes
                    for (int j = 0; j < notes.length; j++)
                        notes[j][0] = (j == 0) ? scale[rootNotes[i]] : scale[random.nextInt(scale.length)];
                }
            }
            beats[i] = new Beat(notes);
        }


        return new Bar(beats);
    }


    /*
     *  Generate and return a musical scale based on the key (root note) and the mode (i.e. major, minor),
     *  so if the key is B, and the mode is minor, the notes to be returned will correspond to the B minor scale.
     */
    private Note[] createScale(int root, Mode mode){
        int[] template = scaleTemplate(mode);
        Note[] scale = new Note[template.length];

        // For each note in the scale, use the template to retrieve the value of the note
        for (int i = 0; i < template.length; i++) {
            int index = (root + template[i]) % 12;
            scale[i] = Note.values()[index];
        }
        return scale;
    }

    /* 
     * Each integer value represents the number of 
     * intervals from the root note of the scale
     */
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


    public Song getSong() {
        return song;
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
