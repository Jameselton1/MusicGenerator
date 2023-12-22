package web.jelton.musicgen.generator;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException {
        for (int i = 0; i < 1; i++) {
            Sequence s = createMidiSong();
            writeMidiFile(s, "midi" + i + ".mid");
        }
        Sequencer seq = MidiSystem.getSequencer();
    }

    private static Sequence createMidiSong() throws InvalidMidiDataException {
        SongGenerator generator = new SongGenerator();
        MidiGenerator midiGenerator = new MidiGenerator();
        Song song = generator.newSong();

        System.out.println(generator.getScale()[0]);
        System.out.println(generator.getMode());

        return midiGenerator.generateMIDI(song);
    }

    private static void writeMidiFile(Sequence sequence, String filePath) throws IOException {
        File midiFile = new File(filePath);
        MidiSystem.write(sequence, 1, new File(filePath));
    }



}