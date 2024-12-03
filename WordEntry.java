import java.util.ArrayList;

public class WordEntry implements Comparable<WordEntry>{
    private final String word;
    private final int length;
    private final ArrayList<int[]> directions;
    private final int row;
    private final int col;

    // this should be changed depending on starting position
    // should be the TOP-LEFT coordinates of the grid - Daniel
    private final double startX = 124.0;
    private final double startY = 27.5;

    //this should be changed depending on the distances between letters
    private final double varX = 13.83;
    private final double varY = 13.83;

    public WordEntry(String word, ArrayList<int[]> directions,int row, int col){
        this.word = word;
        this.length = word.length();
        this.directions = directions;
        this.row = row;
        this.col = col;
    }

    public String toGCode(){

        // this should be changed depending on starting position
        // should be the TOP-LEFT coordinates of the grid - Daniel
        double startX = 124.0;
        double startY = 27.5;

        //this should be changed depending on the distances between letters
        double varX = 13.83;
        double varY = 13.83;

        // changed - Daniel
        StringBuilder gCodeStr = new StringBuilder(
                "G0 X" + (startY + varY * col) + " Y" + (startX - varX * row) + " ; " +  word + "\n" +
                        "G0 X" + (startY + varY * col) + " Y"  + (startX - varX * row) + " Z0.0" + " ; " +  word + "\n");

        for(int[] direction : directions){
            String temp = "G0 X" + (startY + varY * direction[1]) +
                    " Y" + (startX - varX * direction[0]) + "\n";
            gCodeStr.append(temp);
        }

        // Z1.5 can be modified - Daniel
        String end = "G0 Z1.5\n";
        gCodeStr.append(end);

        return gCodeStr.toString();
    }

    public int getLength(){return length;}

    @Override
    public int compareTo(WordEntry o) {
        return Double.compare(o.getLength(), this.length);
    }

    public String getWord(){return word;}

    public String toString(){return word + " "+ row + " " + col + " " + directions;}
}