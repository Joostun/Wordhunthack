import java.io.IOException;
import java.util.*;


public class Main {
    static char[][] board = new char[4][4];
    static ArrayList<WordEntry> wordlist = new ArrayList<WordEntry>();

    public static void main(String[] args) throws IOException {
        Tree tree = new Tree();
        //System.out.println("Tree constructed");
        //System.out.println("Please enter grid:");
        String input = args[0];
        //String input = ui.nextLine();
        if(input.length() != 16) {
            System.out.println("invalid grid");
            System.exit(0);
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[row][col] = input.charAt(0);
                input = input.substring(1);
            }
        }

        for(int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                boolean[][] visitGrid = new boolean[4][4];
                DFS(row, col, "", deepCopy(visitGrid), tree.getRootNode(),new ArrayList<int[]>() ,row, col);
            }
        }
        Set<String> wordInstances = new HashSet<String>();
        ListIterator<WordEntry> iter = wordlist.listIterator();
        while (iter.hasNext()){
            WordEntry s = iter.next();
            if(!wordInstances.contains(s.getWord())){
                wordInstances.add(s.getWord());
            } else {
                iter.remove();
            }
        }

        Collections.sort(wordlist);

        StringBuilder gCodeOutput = new StringBuilder("G28\nM220 S1500\nM204 P3000 T3000\nM205 X20.0 Y20.0; Code start\n");
        for(WordEntry sampleWord : wordlist){
            gCodeOutput.append(sampleWord.toGCode());
        }
        String gCode = gCodeOutput.toString();
        System.out.print(gCode);
    }

    public static void DFS(int row, int col, String word, boolean[][] visits, Node root, ArrayList<int[]>directions, int rowOG, int colOG){
        //makes sure recur is in bounds and if it has been visited before
        if(row < 0 || row > 3 || col < 0 || col > 3) {
            return;
        }else if(visits[row][col]){
            return;
        }

        char letter = board[row][col];
        int arrayInd = root.checkNode(letter);

        if(arrayInd == -1) {
            return;
        }
        Node newRoot = root.subNodes.get(arrayInd);
        //tree
        String newWord = word + letter;
        visits[row][col] = true;
        if((newRoot.getEndpoint() || root.isLeaf()) && newWord.length() > 3) {
            WordEntry wordB = new WordEntry(newWord, directions, rowOG, colOG);
            wordlist.add(wordB);
        }
        
        ArrayList<int[]> Sdirection = new ArrayList<int[]>(directions);
        Sdirection.add(new int[]{row+1, col});
        DFS(row+1, col, newWord, deepCopy(visits), newRoot, Sdirection, rowOG, colOG);

        ArrayList<int[]> Ndirection = new ArrayList<int[]>(directions);
        Ndirection.add(new int[]{row-1, col});
        DFS(row-1, col, newWord, deepCopy(visits), newRoot, Ndirection, rowOG, colOG);

        ArrayList<int[]> Edirection = new ArrayList<int[]>(directions);
        Edirection.add(new int[]{row, col+1});
        DFS(row, col+1, newWord, deepCopy(visits), newRoot, Edirection, rowOG, colOG);

        ArrayList<int[]> Wdirection = new ArrayList<int[]>(directions);
        Wdirection.add(new int[]{row, col-1});
        DFS(row, col-1, newWord, deepCopy(visits), newRoot, Wdirection, rowOG, colOG);

        ArrayList<int[]> SEdirection = new ArrayList<int[]>(directions);
        SEdirection.add(new int[]{row+1, col+1});
        DFS(row+1, col+1, newWord, deepCopy(visits), newRoot, SEdirection, rowOG, colOG);

        ArrayList<int[]> SWdirection = new ArrayList<int[]>(directions);
        SWdirection.add(new int[]{row+1, col-1});
        DFS(row+1, col-1, newWord, deepCopy(visits), newRoot, SWdirection, rowOG, colOG);

        ArrayList<int[]> NEdirection = new ArrayList<int[]>(directions);
        NEdirection.add(new int[]{row-1, col+1});
        DFS(row-1, col+1, newWord, deepCopy(visits), newRoot, NEdirection, rowOG, colOG);

        // Was new int[]{row-1, col+1}); - Daniel
        ArrayList<int[]> NWdirections = new ArrayList<int[]>(directions);
        NWdirections.add(new int[]{row-1, col-1});
        DFS(row-1, col-1, newWord, deepCopy(visits), newRoot, NWdirections, rowOG, colOG);
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