public class Node {

    public int data;
    public int counter;
    public Node left;
    public Node right;

    public int orgdepth;


    public Node(int data) {
        this.data = data;
        counter =1;
        left = null;
        right = null;
        orgdepth = 0;
    }
}
