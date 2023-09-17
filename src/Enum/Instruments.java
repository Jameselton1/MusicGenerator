package web.jelton.musicgen.generator.Enum;

/*
 * Each letter represents a tracktype value
 * A track will be created with this track type for each letter
 * For the purpose of creating different configurations of tracks
 *
 * M = Melody, single notes played at a higher octave
 * C = Chords, multiple notes played together
 * B = Bass, single notes played at a lower octave
 */
public enum Instruments {
    MCB,
    MC
}
