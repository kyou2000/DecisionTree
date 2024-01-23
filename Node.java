import java.util.ArrayList;

public class Node {
    public boolean is_read = false;
    public boolean is_leave = false;
    public ArrayList<Node> next = new ArrayList<>();
    public int feature = -1;
    public int feature_value = -1;
    public double gini;
    public int label = -1;
    public Node(){}
}
