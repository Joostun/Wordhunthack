import java.util.ArrayList;

public class Node{
    public final ArrayList<LetterNode> subNodes = new ArrayList<LetterNode>();
    public Node(){}

    public int checkNode(char character){
        int returnInt = -1;
        for(int i = 0; i < subNodes.size();i++){
            if(subNodes.get(i).letter == character){
                returnInt = i;
            }
        }
        return returnInt;
    }

    public void createNode(char character){
        subNodes.add(new LetterNode(character));
    }

    public void createNode(char character, boolean boo){
        subNodes.add(new LetterNode(character, boo));
    }

    public boolean isLeaf(){return subNodes.size() == 0;}

    public boolean getEndpoint(){return false;}
}
