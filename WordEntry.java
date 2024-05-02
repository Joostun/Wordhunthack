public class WordEntry {
    private String word;
    private int length;
    public WordEntry(String word){
        this.word = word;
        this.length = word.length();
    }

    public String getWord(){return word;}

    public int getLength(){return length;}

    public String toString(){return word;}
}
