import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
    static char[][] board = new char[4][4];
    static ArrayList<WordEntry> wordlist = new ArrayList<WordEntry>();

    public static void main(String[] args) throws IOException {
        Scanner ui = new Scanner(System.in);
        Tree tree = new Tree();
        System.out.println("Tree constructed");
        System.out.println("Please enter grid:");
        String input = ui.nextLine();
        if(input.length() != 16) {
            System.out.println("invalid grid");
            System.exit(0);
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[col][row] = input.charAt(0);
                input = input.substring(1);
            }
        }

        for(int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                boolean[][] visitGrid = new boolean[4][4];
                DFS(row, col, "", deepCopy(visitGrid), tree.getRootNode());
            }
        }
        insertionSort(wordlist);
        for(WordEntry sampleWord : wordlist){
            System.out.println(sampleWord);
        }
    }

    public static void DFS(int row, int col, String word, boolean[][] visits, Node root){
        //makes sure recur is in bounds and if it has been visited before
        if(row < 0 || row > 3 || col < 0 || col > 3) {
            return;
        }else if(visits[row][col]){
            return;
        }

        char letter = board[row][col];
        int arrayInd = root.checkNode(letter);

        if(arrayInd == -1){
            return;
        }
        if(word.contains("TIT")){
            System.out.println(word);
            System.out.println(letter);
            System.out.println(Arrays.deepToString(visits));
            System.out.println("------");
        }
        Node newRoot = root.subNodes.get(arrayInd);
        //tree
        if(root.isLeaf()){
            WordEntry wordB = new WordEntry(word + letter);
            wordlist.add(wordB);
        }
        String newWord = word + letter;
        visits[row][col] = true;
        if(newRoot.getEndpoint()) {
            WordEntry wordB = new WordEntry(newWord);
            wordlist.add(wordB);
        }

        DFS(row+1, col, newWord, deepCopy(visits), newRoot);
        DFS(row-1, col, newWord, deepCopy(visits), newRoot);
        DFS(row, col+1, newWord, deepCopy(visits), newRoot);
        DFS(row, col-1, newWord, deepCopy(visits), newRoot);
        DFS(row+1, col+1, newWord, deepCopy(visits), newRoot);
        DFS(row+1, col-1, newWord, deepCopy(visits), newRoot);
        DFS(row-1, col+1, newWord, deepCopy(visits), newRoot);
        DFS(row-1, col-1, newWord, deepCopy(visits), newRoot);
    }

    public static void insertionSort(ArrayList<WordEntry> array) {
        int i, j;
        for (i = 1; i < array.size(); i++) {
            WordEntry tmp = array.get(i);
            j = i;
            while ((j > 0) && (array.get(j - 1).getLength() > tmp.getLength())) {
                array.set(j, array.get(j - 1));
                j--;
            }
            array.set(j, tmp);
        }
    }
    public static boolean[][] deepCopy(boolean[][] original) {
        if (original == null) {
            return null;
        }

        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

}