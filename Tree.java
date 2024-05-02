import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Tree{
    public RootNode root = new RootNode();
    private final ArrayList<String> wordBank = new ArrayList<>();
    public Tree() throws IOException {
        // load data from file
        BufferedReader bf = new BufferedReader(
                new FileReader("dictionary.txt"));
        String line = bf.readLine();
        while (line != null) {
            wordBank.add(line);
            line = bf.readLine();
        }
        bf.close();
        parse();
    }

    private void parse(){
        for(String wordA : wordBank){
            buildTree(this.root, wordA);
        }
    }
    public void testTree(Node ARoot){
        Scanner input = new Scanner(System.in);
        System.out.print(ARoot.subNodes);
        String testString= input.nextLine();
        char character = testString.charAt(0);
        int index = ARoot.checkNode(character);
        if(index == -1){
            return;
        }
        LetterNode newRoot = ARoot.subNodes.get(index);
        testTree(newRoot);
    }
    public void buildTree(Node subRoot, String word){
        char letter = word.charAt(0);
        String leftovers = word.substring(1);
        Node nextRoot;
        int checkNodeResult = subRoot.checkNode(letter);
        if(checkNodeResult == -1 && word.length() == 1) {
            subRoot.createNode(letter, true);
            return;
        } else if (checkNodeResult > -1 && word.length() == 1) {
            subRoot.subNodes.get(checkNodeResult).setEndpoint();
            return;
        }else if (checkNodeResult == -1){
            subRoot.createNode(letter);
            nextRoot = subRoot.subNodes.get(subRoot.subNodes.size()-1);
            buildTree(nextRoot, leftovers);
        }else if (checkNodeResult > -1){
            nextRoot = subRoot.subNodes.get(checkNodeResult);
            buildTree(nextRoot, leftovers);
        }
    }

    public Node getRootNode(){return root;}
}