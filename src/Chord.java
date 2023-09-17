package web.jelton.musicgen.generator;

import java.util.Arrays;

import web.jelton.musicgen.generator.Enum.ChordType;
import web.jelton.musicgen.generator.Enum.Note;


public class Chord {
    // root note of the chord
    private Note root;
    // all of the notes of the chord including the root note
    private Note[] notes;

    public Chord(int root, ChordType type, Note[] scale){
        this.root = scale[root];
        this.notes = generateNotes(root, type, scale);
    }

    // retrieve the notes for the chord using the formula and root note like in a scale
    private Note[] generateNotes(int root, ChordType type, Note[] scale) {

        int[] chordFormula = chordFormula(type);
        Note[] notes = new Note[chordFormula.length];

        // iterate over the length of the chord formula
        for (int i = 0; i < chordFormula.length; i++) {
            // calculate the index of the next note in the chord
            int index = (root + chordFormula[i]) % scale.length;
            // assign the note value to the corresponding element in the notes array
            notes[i] = scale[index];
        }
        return notes;
    }

    // values represent intervals from chord's root note
    private int[] chordFormula(ChordType type){
        return switch (type) {
            case triad -> new int[]{0, 2, 4};
            case seventh -> new int[]{0, 2, 4, 6};
            case power -> new int[]{0, 4};
            default -> null;
        };
    }
    public Note getRoot() {
        return root;
    }

    public void setRoot(Note root) {
        this.root = root;
    }

    public Note[] getNotes() {
        return notes;
    }

    public void setNotes(Note[] notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Chord{" +
                "root=" + root +
                ", notes=" + Arrays.toString(notes) +
                '}';
    }
}
