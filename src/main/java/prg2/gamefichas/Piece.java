package prg2.gamefichas;

public class Piece {
    private char colorP;
    private int groupNumP;

    Piece(char color) {
        this.groupNumP = 0;
        this.colorP = color;
    }

    public char getColorP() {
        return colorP;
    }

    public void setColorP(char colorP) {
        this.colorP = colorP;
    }

    public int getGroupNumP() {
        return groupNumP;
    }

    public void setGroupNumP(int groupNumP) {
        this.groupNumP = groupNumP;
    }

}
