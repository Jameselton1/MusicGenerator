import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import Enum.Note;
import Enum.TrackType;

import java.util.Random;

public class MidiGenerator {
    private Sequence midiFile;
    private int ticksInBar = 768;
    private int quarter = ticksInBar / 4;

    public Sequence generateMIDI(Song song) throws InvalidMidiDataException {
        Sequence sequence = new Sequence(0.0F, quarter);
        int time = 0;

        for (Track track : song.getTracks()){
            javax.sound.midi.Track t = sequence.createTrack();

            t = changeInstrument(t, track.type);

            time = 0;
            for (Segment segment : track.getSegments()) {
                for (Bar bar : segment.getBars())
                    for (Beat beat : bar.getBeats())
                        // add events stored in beat object to track then move on to next beat
                        for (Note[] notes : beat.getNotes()) {
                            int length = (quarter / beat.getNotes().length);
                            t = addEvent(t, notesToMidiNotes(notes, track.type), time, length);
                            // time += length of the beat, divided by the number of sub divisions
                            time += length;
                        }
            }
        }
        return sequence;
    }

    // pick a random instrument based on track type
    private javax.sound.midi.Track changeInstrument(javax.sound.midi.Track t, TrackType type) throws InvalidMidiDataException {
        Random random = new Random();
        int instrument = switch(type){
            case Chord -> random.nextInt(32);               // acoustic grand piano
            case Melody -> random.nextInt(8) + 80;          // synth strings 1
            case Bass -> random.nextInt(8) + 32;            // electric bass (pick)
            default -> 0;
        };

        // Create a MIDI Event to set the instrument
        ShortMessage programChange = new ShortMessage();
        programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0); // Change instrument to 20 (0-127)

        // Add the MIDI Events to the Track
        t.add(new MidiEvent(programChange, 0));

        return t;
    }


    // convert a note object to a number which
    // represents the note for the midi generator
    private int[] notesToMidiNotes(Note[] notes, TrackType type){
        int[] midiNotes = new int[notes.length];

        int octave = switch (type){
            case Chord -> 4;
            case Melody -> 6;
            case Bass -> 3;
            default -> 0;
        };

        for (int i = 0; i < notes.length; i++)
            // set midinotes[s][n] to the note at the octave which the octave parameter represents
            midiNotes[i] = notes[i].ordinal() + (12 * octave) - 3;

        return midiNotes;
    }

    // return a track object which is an updated version of the first parameter 'track'
    // notesize = note length i.e. 192 = quarter note
    private javax.sound.midi.Track addEvent(javax.sound.midi.Track track, int notes[], int time, int length) throws InvalidMidiDataException {
        for (int n : notes){
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, n, 100), time));
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, n, 100), time + length));
        }
        return track;
    }
}




