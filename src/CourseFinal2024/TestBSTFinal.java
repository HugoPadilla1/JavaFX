package CourseFinal2024;


public class TestBSTFinal {
    public static void main(String[] args) {
        Integer[] numbers = {2, 8, 1, 0, 7, 4, 6, 9};
        BSTFinal<Integer> intTree = new BSTFinal<Integer>(numbers);
        System.out.println("\nThe sum of the even nodes in the first tree is " + intTree.sumEvenNodes());

        System.out.println("The even nodes in the first tree are : ");
        intTree.printEvenNodes();

        // Create a ICs.newIC6.BST
        BSTFinal<Integer> tree = new BSTFinal<>();
        tree.insert(5);
        tree.insert(10);
        tree.insert(1);
        tree.insert(-1);
        tree.insert(2);
        tree.insert(6);
        tree.insert(140);
        tree.insert(131);
        tree.insert(120);
        tree.insert(111);
        tree.insert(11);
        tree.insert(100);
        tree.insert(151);


        System.out.print("\n\nThe sum of the even nodes in the second tree is " + tree.sumEvenNodes());
        System.out.println("\nThe even nodes in the second tree are : ");
        tree.printEvenNodes();

        BSTFinal<String> emptyTree = new BSTFinal<String>();
        System.out.println("\n\nThe sum of the even nodes in the first tree is " + emptyTree.sumEvenNodes());

        System.out.println("The even nodes in the third tree are : ");
        emptyTree.printEvenNodes();
    }
}
