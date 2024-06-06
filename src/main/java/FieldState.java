public enum FieldState {
    EMPTY(' '), MISS('X'), OCCUPIED_HIDDEN('O'), OCCUPPIED_SALVAGED('#');

    public final char output;
    FieldState(char output){
        this.output = output;
    }

    public static FieldState fromOutput(char output){
        return switch (output) {
            case ' ' -> EMPTY;
            case 'X' -> MISS;
            case 'O' -> OCCUPIED_HIDDEN;
            case '#' -> OCCUPPIED_SALVAGED;
            default -> throw new IllegalArgumentException ("Unknown output");
        };
    }
}
