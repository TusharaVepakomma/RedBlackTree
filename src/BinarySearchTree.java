import javax.swing.tree.TreeNode;

public class BinarySearchTree {
    public static Node root;
    public static float performance;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(int id) {
        Node newNode = new Node(id);
        Node tempNode = null;
        if (root == null) {
            root = newNode;
            root.orgdepth = maxDepth(root);
            return;
        }

        Node current = root;
        Node parent = null;
        while (true) {
            parent = current;
            if (id < current.data) {
                current = current.left;
                if (current == null) {
                    parent.left = newNode;
                    return;
                }
            } else {
                current = current.right;
                if (current == null) {
                    parent.right = newNode;
                   return;
                }


            }
        }
    }


    public float display(Node root) {

        if (root != null) {
        performance = performance + (root.counter * root.orgdepth);
            display(root.left);
           System.out.print("\n value = " + root.data + " and counter = " + root.counter + " depth of node = " + root.orgdepth);
           display(root.right);

        }
        return performance;
        
    }

    public boolean find(int id) {
        Node current = root;
        while (current != null) {
            if (current.data == id) {
                current.counter++;
                return true;
            } else if (current.data > id) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public int maxDepth(Node root) {
        if (root == null)
            return 0;

        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);


        int bigger = Math.max(leftDepth, rightDepth);

        return bigger + 1;
    }
    public void updateDepth(Node node, int depth)
    {
        if (node != null)
        {
            node.orgdepth = depth;
            updateDepth(node.left, depth + 1); // left sub-tree
            updateDepth(node.right, depth + 1); // right sub-tree
        }
    }
}