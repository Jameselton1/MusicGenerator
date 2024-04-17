package web.jelton.musicgen.generator;
import javax.sound.midi.*;
import java.io.IOException;

public class Main {
    // Basic test, create a song and play it
    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
        Sequence sequence = createMidiSong();
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.start();
        sequencer.close();
    }

    private static Sequence createMidiSong() throws InvalidMidiDataException {
        SongGenerator generator = new SongGenerator();
        MidiGenerator midiGenerator = new MidiGenerator();
        Song song = generator.newSong();

        System.out.println("Scale: " + generator.getScale()[0] + " " + generator.getMode());

        return midiGenerator.generateMidi(song);
    }
}