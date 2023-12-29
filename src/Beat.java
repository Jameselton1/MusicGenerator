package web.jelton.musicgen.generator;
import web.jelton.musicgen.generator.Enum.Note;

/*
 * Most fundamental measurement of time in music
 * This is the class which contains notes which are then
 * converted to midi
 */
public class Beat {
    /* dimension 1 = subdivisions
     * dimension 2 = number of notes played at a time
     */
    private Note[][] notes;

    public Beat(Note[][] notes) {
        this.notes = notes;
    }
    
    public Note[][] getNotes() {
        return notes;
    }

    public void setNotes(Note[][] notes) {
        this.notes = notes;
    }

}
