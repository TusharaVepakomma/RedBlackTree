import java.util.ArrayList;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;


public class RBTree {

    public static float RBPerformance = 0;
    public enum Color {
        RED,
        BLACK
    }

    public static class Node1 {
        int data;
        int count;
        int depth;
        Color color;
        Node1 left;
        Node1 right;
        Node1 parent;
        boolean isNullLeaf;
    }

    private static Node1 createBlackNode1(int data) {
        Node1 node = new Node1();
        node.count = 1;
        node.data = data;
        node.color = Color.BLACK;
        node.left = createNullLeafNode1(node);
        node.right = createNullLeafNode1(node);
        return node;
    }

    private static Node1 createNullLeafNode1(Node1 parent) {
        Node1 leaf = new Node1();
        leaf.count = 0;
        leaf.color = Color.BLACK;
        leaf.isNullLeaf = true;
        leaf.parent = parent;
        return leaf;
    }

    private static Node1 createRedNode1(Node1 parent, int data) {
        Node1 node = new Node1();
        node.count = 1;
        node.data = data;
        node.color = Color.RED;
        node.parent = parent;
        node.left = createNullLeafNode1(node);
        node.right = createNullLeafNode1(node);
        return node;
    }

    private boolean isLeftChild(Node1 root)
    {
        Node1 parent = root.parent;

        if(parent.left == root)
            return true;
        else
            return false;

    }

    private void rightRotate(Node1 root, boolean changeColor)
    {
        Node1 parent = root.parent;
        root.parent = parent.parent;
        if(parent.parent != null)
            if(parent.parent.right == parent)
                parent.parent.right = root;
            else
                parent.parent.left = root;

        Node1 right = root.right;
        root.right = parent;
        parent.parent = root;
        parent.left = right;
        if(right != null)
            right.parent = parent;

        if(changeColor)
        {
            root.color = Color.BLACK;
            parent.color = Color.RED;
        }
    }


    private void leftRotate(Node1 root, boolean changeColor)
    {
        Node1 parent = root.parent;
        root.parent = parent.parent;
        if(parent.parent != null)
            if(parent.parent.right == parent)
                parent.parent.right = root;
            else
                parent.parent.left = root;

        Node1 left = root.left;
        root.left = parent;
        parent.parent = root;
        parent.right = left;
        if(left != null)
            left.parent = parent;

        if(changeColor)
        {
            root.color = Color.BLACK;
            parent.color = Color.RED;
        }
    }

    private Optional<Node1> findSiblingNode1(Node1 root)
    {
        Node1 parent = root.parent;
        if(isLeftChild(root))
            return Optional.ofNullable(parent.right.isNullLeaf ? null : parent.right);
        else
            return Optional.ofNullable(parent.left.isNullLeaf ? null : parent.left);
    }

    public Node1 insert(Node1 root, int data)
    {
        return insert(null, root, data);
    }


    private Node1 insert(Node1 parent, Node1 root, int data)
    {
        if(root!=null && root.data == data)
        {
            root.count++;
            return root;
        }

        if(root  == null || root.isNullLeaf)
            if(parent != null)
                return createRedNode1(parent, data);
            else
                return createBlackNode1(data);


        boolean isLeft;
        if(root.data > data)
        {
            Node1 left = insert(root, root.left, data);
            if(left == root.parent)
                return left;

            root.left = left;
            isLeft = true;

        }
        else
        {
            Node1 right = insert(root, root.right, data);

            if(right == root.parent)
                return right;

            root.right = right;
            isLeft = false;
        }

        if(isLeft)
        {
            if(root.color == Color.RED && root.left.color == Color.RED)
            {

                Optional<Node1> sibling = findSiblingNode1(root);
                if(!sibling.isPresent() || sibling.get().color == Color.BLACK)

                    if(isLeftChild(root))
                        rightRotate(root, true);

                    else
                    {
                        rightRotate(root.left, false);
                        root = root.parent;
                        leftRotate(root, true);
                    }

                else
                {
                    root.color = Color.BLACK;
                    sibling.get().color = Color.BLACK;
                    if(root.parent.parent != null)
                        root.parent.color = Color.RED;
                }
            }
        }
        else
        {
            if(root.color == Color.RED && root.right.color == Color.RED)
            {
                Optional<Node1> sibling = findSiblingNode1(root);
                if(!sibling.isPresent() || sibling.get().color == Color.BLACK)

                    if(!isLeftChild(root))
                        leftRotate(root, true);

                    else
                    {
                        leftRotate(root.right, false);
                        root = root.parent;
                        rightRotate(root, true);
                    }

                else
                {
                    root.color = Color.BLACK;
                    sibling.get().color = Color.BLACK;
                    if(root.parent.parent != null)
                        root.parent.color = Color.RED;
                }
            }
        }
        return root;
    }

    private void printRedBlackTree(Node1 root)
    {
        if(root == null || root.isNullLeaf)
            return;

        if(root.parent==null)
        {
            System.out.println("");
            System.out.println("root: "+ root.data + (root.color == Color.BLACK ? "B" : "R")+ "  " + "Count:" +root.count  + "Depth:" + root.depth );
            System.out.println("parent: NULL");
        }
        else
            System.out.println("Node1: "+ root.data + (root.color == Color.BLACK ? "B" : "R"));


        if(root.left.isNullLeaf)
            System.out.println("Left Child: NULL");
        else{
            System.out.println("Left Child: "+ root.left.data + (root.left.color == Color.BLACK ? "B" : "R") + "  " + "Count:" +root.left.count  + "Depth:" + root.depth);
        RBPerformance = RBPerformance + (root.depth * root.count);}
        if(root.right.isNullLeaf)
            System.out.println("Right Child: NULL");
        else{
            System.out.println("Right Child: "+ root.right.data + (root.right.color == Color.BLACK ? "B" : "R") + "  " + "Count:" +root.right.count +  "Depth:" + root.depth);
        RBPerformance = RBPerformance + (root.depth * root.count);}
        System.out.println("");

        printRedBlackTree(root.left);
        printRedBlackTree(root.right);
    }

    private boolean noRedRedParentChild(Node1 root, Color parentColor)
    {
        if(root == null)
            return true;

        if(root.color == Color.RED && parentColor == Color.RED)
            return false;

        return noRedRedParentChild(root.left, root.color) && noRedRedParentChild(root.right, root.color);
    }

    private boolean checkBlackNode1sCount(Node1 root, AtomicInteger blackCount, int currentCount)
    {
        if(root.color == Color.BLACK)
            currentCount++;

        if(root.left == null && root.right == null)
            if(blackCount.get() == 0)
            {
                blackCount.set(currentCount);
                return true;
            }
            else
                return currentCount == blackCount.get();

        return checkBlackNode1sCount(root.left, blackCount, currentCount) && checkBlackNode1sCount(root.right, blackCount, currentCount);
    }

    public boolean validateRedBlackTree(Node1 root) {

        if(root == null)
            return true;

        if(root.color != Color.BLACK)
        {
            System.out.print("Root is not black");
            return false;
        }

        AtomicInteger blackCount = new AtomicInteger(0);

        return checkBlackNode1sCount(root, blackCount, 0) && noRedRedParentChild(root, Color.BLACK);
    }
    public int maxDepth(Node1 root) {
        if (root == null)
            return 0;

        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);


        int bigger = Math.max(leftDepth, rightDepth);

        return bigger + 1;
    }
    public void updateDepth(Node1 node, int depth)
    {
        if (node != null)
        {
            node.depth = depth;
            updateDepth(node.left, depth + 1); // left sub-tree
            updateDepth(node.right, depth + 1); // right sub-tree
        }
    }


  public static void main(String args[])
    {
        Node1 root1 = null;
        Node1 root2 = null;
        Random r1 = new Random();
        BinarySearchTree b1 = new BinarySearchTree();
        RBTree b5 = new RBTree();
        RBTree b6 = new RBTree();


        int max = 100;
        int min  = 1;
        int target = 0;
        /* Binary Tree Sequence 500 random numbers*/
        for(int i = 0; i < 500; i++){
            target = min + r1.nextInt(max);
            root1 = b5.insert(root1, target);
            if(i == 0) {
                b1.insert(target);
              

            }

            else {
                if (!b1.find(target)) {
                    b1.insert(target);
                    
                }


            }

        }
        /* Print the Binary Search Tree First Sequence */
        System.out.println("Binary Search Tree for first Sequence of 500 random numbers");
        b1.updateDepth(b1.root,0);
        float performanceb1  = b1.display(b1.root);
        System.out.println("\n Height of the Tree : " + b1.maxDepth(b1.root) );
        System.out.printf("Average number of key comparisions = " + (performanceb1/500)+"\n");
        //b1.maxDepth(b1.root);
        System.out.println("");

        /*Print the Redblack Tree First Sequence*/
        System.out.println("Red black tree for first sequence is");
        //b3.printTree();
        b5.updateDepth(root1,0);
        b5.printRedBlackTree(root1);
        System.out.println("Height of the Tree : " + b5.maxDepth(root1) );
        System.out.printf("Average number of key comparisions = " + (RBPerformance/500)+"\n");
        System.out.println("");



        /* Binary Tree Sequence even,odd, random numbers*/
        BinarySearchTree b2 = new BinarySearchTree();
        ArrayList<Integer> secondSeq= new ArrayList<Integer>();
        for(int i = 1; i < 101; i++){
            if(i%2 == 0)
                secondSeq.add(i);
        }
        for(int i = 1; i < 101; i++){
            if(i%2 != 0)
                secondSeq.add(i);
        }
        for(int i = 0; i < 400; i++){

            secondSeq.add(min + r1.nextInt(max));
        }

        for(int i = 0; i < secondSeq.size(); i++){
           
            root2 = b6.insert(root2, secondSeq.get(i));
            if(i == 0) {
                b2.insert(secondSeq.get(i));
              

            }

            else {
                if (!b2.find(secondSeq.get(i))) {
                    b2.insert(secondSeq.get(i));
                    
                }

            }

        }
        System.out.println("Binary Search Tree for second Sequence ");
        b2.updateDepth(b2.root,0);
        float performanceb2  = b2.display(b2.root);
        System.out.println("\n Height of the Tree : " + b2.maxDepth(b2.root) );
        System.out.printf("Average number of key comparisions = " + (performanceb2/500)+"\n");
        b1.maxDepth(b2.root);
        System.out.println("");

        System.out.println("Red black tree for Second sequence is");
        b6.updateDepth(root2,0);
        RBPerformance = 0;
        b6.printRedBlackTree(root2);
        System.out.println("Height of the Tree : " + b6.maxDepth(root2) );
        System.out.printf("Average number of key comparisions = " + (RBPerformance/500)+"\n");
        System.out.println("");

    }

}



       
