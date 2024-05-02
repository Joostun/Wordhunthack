public class LetterNode extends Node{
    private boolean isEndpoint = false;
    final public char letter;

    public LetterNode(char letter){this.letter = letter;}

    public LetterNode(char letter, boolean boo){
        this.letter = letter;
        isEndpoint = true;
    }

    public void setEndpoint(){isEndpoint = true;}

    public boolean getEndpoint(){return isEndpoint;}

    public String toString(){return ""+ letter;}

}