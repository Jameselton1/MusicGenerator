package web.jelton.musicgen.generator;

import java.util.Random;

import javax.sound.midi.*;

import web.jelton.musicgen.generator.Enum.Note;

public class MidiGenerator {
    private Sequence midiFile;
    private int ticksInBar = 768;
    private int quarter = ticksInBar / 4;

    // Take the data from a song object and convert it to a midi sequencee
    public Sequence generateMIDI(Song song) throws InvalidMidiDataException {
        Sequence sequence = new Sequence(0.0F, quarter);
        int time = 0;
        for (Track track : song.getTracks()) {
            javax.sound.midi.Track t = sequence.createTrack();
            t = changeInstrument(t, track.type);
            time = 0;
            for (Segment segment : track.getSegments()) {
                for (Bar bar : segment.getBars()) {
                    for (Beat beat : bar.getBeats()) {
                        for (Note[] notes : beat.getNotes()) {
                            int length = (quarter / beat.getNotes().length);
                            t = addEvent(t, notesToMidiNotes(notes, track.type), time, length);
                            time += length;
                        }
                    }
                }
            }
        }
        return sequence;
    }

    private javax.sound.midi.Track changeInstrument(javax.sound.midi.Track t, char type) throws InvalidMidiDataException {
        Random random = new Random();
        int instrument = switch(type) {
            case 'C' -> random.nextInt(32);                // acoustic grand piano
            case 'M' -> random.nextInt(8) + 80;            // synth strings 1
            case 'B' -> random.nextInt(8) + 32;            // electric bass (pick)
            default -> 0;
        };
        ShortMessage programChange = new ShortMessage();
        programChange.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0); // Change instrument to 20 (0-127)
        t.add(new MidiEvent(programChange, 0));
        return t;
    }

    private int[] notesToMidiNotes(Note[] notes, char type){
        int[] midiNotes = new int[notes.length];
        int octave = switch (type){
            case 'C' -> 4;
            case 'M' -> 6;
            case 'B' -> 3;
            default -> 0;
        };
        for (int i = 0; i < notes.length; i++) {
            midiNotes[i] = notes[i].ordinal() + (12 * octave) - 3;
        }
        return midiNotes;
    }

    private javax.sound.midi.Track addEvent(javax.sound.midi.Track track, int notes[], int time, int length) throws InvalidMidiDataException {
        for (int n : notes){
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, n, 100), time));
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, n, 100), time + length));
        }
        return track;
    }
}