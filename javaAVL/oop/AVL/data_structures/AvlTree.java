package oop.ex4.data_structures;

/**
 * This class is the complete and tested implementation of an AVL-tree. A AVL-tree is a self-balancing binary
 * search tree.
 * In an AVL tree, the heights of the two child subtrees of any node differ by at most one; if at any time
 * they differ by more than one, rebalancing is done to restore this property. Lookup, insertion, and
 * deletion all take O(logn) time in both the average and worst cases, where n is the number of nodes
 * in
 * the tree prior to the operation. Insertions and deletions may require the tree to be rebalanced by one or
 * more tree rotations.
 * @author yael.sarusi
 * @author shaharna13
 */
public class AvlTree extends BinarySearchTree {

	/* The size of a tree at height 0*/
	private static final int ONLY_ROOT = 1;


	/* All the indication flags needed for the different balance violations */
	private enum Violation {
		LL, LR, RR, RL, NONE
	}

	/* This flag indicates if rebalancing is needed at this point. */
	private static final boolean REBALANCE_TREE = true;

	/* an indicator its a leaf */
	private static final int LEAF = -1;


	/**
	 * The default constructor.
	 */
	public AvlTree(){
		super();
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree.
	 * The new tree contains all the values of the given tree, but not necessarily in the same structure.
	 * @param tree The AVL tree to be copied.
	 */
	public AvlTree(AvlTree tree){
		super(tree);
		}

	/**
	 * A constructor that builds the tree by adding the elements in the input array one-by-one.
	 * The AVL tree is a set, so if the same values appears more than once in the list, it is  added
	 * only once.
	 * @param data values to add to tree
	 */
	public AvlTree(int[] data){
		super(data);
	}

	/**
	 * Returns an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
	 * ascending order, and does NOT implement the remove() method.
	 * @return an iterator for the Avl Tree.
	 */
	public java.util.Iterator<java.lang.Integer> iterator(){
		return super.iterator();
	}

	/**
	 * This function adds the data provided to the tree, if it's not already there.
	 * @param data To add to the tree
	 * @return True if add successful, False if the data was already in the tree.
	 */
	public boolean add (int data){
		// Returns the Node itself (if the data already exists in tree) or the parent of the new node.
		// Returns null if tree is empty
		Node potentialLoc = findNode(data);

		// The AVL tree is empty, add node as the root.
		if (potentialLoc == null){
			root = new Node(data);
			return true;
		}
		int potentialData = potentialLoc.getData();

		// Check if the potential Loc is a node with the data itself
		if (potentialData == data){
			return false;
		}
		// Otherwise, the PotentialLoc is the parent of the node, insert the data in the correct child.
		Node newNode = new Node(data);
		return insertChildInCorrectPlace(potentialLoc, newNode, REBALANCE_TREE);
	}

	/*
	 * This function receives a parent and a node, and inserts the node to the correct child:
	 * rightChild if the new node's data is bigger than the parent's data, leftChild otherwise.
	 * After insert, call balanceTree method to balance the AvlTree (if flag is true!).
	 * @param parent A pointer to the parent's node
	 * @param newNode A pointer to the new child node
	 * @return true when successfully added node.
	 */
	private boolean insertChildInCorrectPlace(Node parent, Node newNode, boolean rebalanceNeeded) {
		if (parent != null) {
			if (parent.getData() > newNode.getData()){
				parent.setLeftChild(newNode);
			}
			else {
				parent.setRightChild(newNode);
			}
		} else {
			root = newNode;
			newNode.setParent(null);
		}

		if (rebalanceNeeded){
			balanceTree(newNode);
		}

		return true;
	}

	/*
	 * This function receives the last node to be added to the tree and goes up to the root. As it goes up
	 * it checks for unbalanced heights, and rotates the nodes when needed.
	 * @param node The node from which the tree is potentially unbalanced.
	 */
	private void balanceTree(Node node){
		Node curNode = node;
		while (curNode != null){
			// Check what is the balance case sub-tree rooted under the node.
			Violation balanceCase = getBalanceCase(curNode);
			Node toRotate;
			switch (balanceCase){
				case LR:
					toRotate = curNode.getLeftChild().getRightChild();
					rotateLeft(toRotate);
					rotateRight(toRotate);
					break;
				case RL:
					toRotate = curNode.getRightChild().getLeftChild();
					rotateRight(toRotate);
					rotateLeft(toRotate);
					break;
				case LL:
					toRotate = curNode.getLeftChild();
					rotateRight(toRotate);
					break;
				case RR:
					toRotate = curNode.getRightChild();
					rotateLeft(toRotate);
					break;
				default:
					// default case - no balance needed
					break;
				}
			// Move up the tree towards the root node.
			curNode = curNode.getParent();
		}
	}

	/*
	 * This function finds the correct unbalance case: LL, LR, RL, RR, if there is one
	 * @param curNode The node from which to check
	 * @return the balance case found: LL, RR, LR, RL, or no balance needed.
	 */
	private Violation getBalanceCase(Node curNode) {
		int nodeBalance = getBalance(curNode);
		int rCBalance = getBalance(curNode.getRightChild());
		int lCBalance = getBalance(curNode.getLeftChild());

		if (nodeBalance == -2 && (lCBalance == 0 || lCBalance == -1)){
			return Violation.LL;
		}
		else if (nodeBalance == -2 && lCBalance == 1){
			return Violation.LR;
		}

		if (nodeBalance == 2 && (rCBalance == 0 || rCBalance == 1)){
			return Violation.RR;
		}
		else if (nodeBalance == 2 && rCBalance == -1){
			return Violation.RL;
		}
		return Violation.NONE; //default
	}

	/*
	 * Calculate the avl tree balance indicator for the node given.
	 * The balance will be -1 when the leftChild tree is higher  than the rightChild height by 1
	 * @return The balance of the current Node
	 */
	private int getBalance(Node node) {
		// Node is null, no balancing needed.
		if (node == null){
			return 0;
		}

		Node rightChild = node.getRightChild();
		Node leftChild = node.getLeftChild();
		int rightHeight, leftHeight;
		if (rightChild == null){
			rightHeight = LEAF;
		}
		else {
			rightHeight = rightChild.getHeight();
		}
		if (leftChild == null){
			leftHeight = LEAF;
		}
		else {
			leftHeight = leftChild.getHeight();
		}
		return rightHeight - leftHeight;
	}

	/**
	 * Removes the node with the given value from the tree, if it exists.
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	public boolean delete(int toDelete){
		Node toDeleteNode = findNode(toDelete);

		// Tree is empty, nothing to delete.
		if (toDeleteNode == null){
			return false;
		}

		int potentialData = toDeleteNode.getData();
		if (potentialData != toDelete){ // The node doesn't exist, return false (nothing to delete).
			return false;
		}
		Node parent = toDeleteNode.getParent();
		if (toDeleteNode.getLeftChild() == null) {
			if (toDeleteNode.getRightChild() == null){
				return noSonsNodeToDelete(parent, toDeleteNode);
			}
			else {
				// The node has only right child
				return insertChildInCorrectPlace(parent, toDeleteNode.getRightChild(), REBALANCE_TREE);
			}
		}
		else {
			// The node has only left child.
			if (toDeleteNode.getRightChild() == null){
				return insertChildInCorrectPlace(parent, toDeleteNode.getLeftChild(), REBALANCE_TREE);
			}
			// Both children aren't null
			else {
				return twoSonsNodeDelete(toDeleteNode);
			}
		}
	}

	/*
	 * This function deletes a node that has no children.
	 * @param toDelete The value of the node to be deleted.
	 * @param parent The parent of the node to be deleted.
	 * @return
	 */
	private boolean noSonsNodeToDelete(Node parent, Node toDelete) {
		if (parent!=null) {
			// Check if the node to delete was the right child or the left child of it's parent
			if (toDelete.getData() > parent.getData()) {
				parent.setRightChild(null);
			} else {
				parent.setLeftChild(null);
			}
			balanceTree(parent);
		}
		else {
			// if parent is null, the node to delete is the tree root, so change the root to null
			this.root = null;
		}
		return true;
	}

	/*
	 * This function delete a node that has two sons. It swaps the data of the successor with the data of
	 * the node to delete, and deletes the successor node.
	 * @param toDeleteNode The node to be deleted.
	 * @return true when deleted is accomplished.
	 */
	private boolean twoSonsNodeDelete(Node toDeleteNode) {
		int successor = successor(toDeleteNode).getData(); // Find successor data
		// Delete the successor with the delete function (so it will take care of it's children if exists)
		delete(successor);
		// set the data of the successor instead of the data to delete.
		toDeleteNode.setData(successor);
		return true;
	}

	/*
	 * This function rotates the given node left.
	 */
	private void rotateLeft(Node toRotate){
		Node parentNode = toRotate.getParent();
		Node grandParent = parentNode.getParent();
		Node lChild = toRotate.getLeftChild();

		// Don't balance the tree at this point, the rotation is taking care of the balancing factor.
		insertChildInCorrectPlace(grandParent,toRotate, !REBALANCE_TREE);

		toRotate.setLeftChild(parentNode);
		parentNode.setRightChild(lChild);
	}

	/*
	 * This function rotates the given node right.
	 */
	private void rotateRight(Node toRotate) {
		Node parentNode = toRotate.getParent();
		Node grandParent = parentNode.getParent();
		Node rChild = toRotate.getRightChild();

		// Don't balance the tree at this point, the rotation is taking care of the balancing factor.
		insertChildInCorrectPlace(grandParent, toRotate, !REBALANCE_TREE);

		toRotate.setRightChild(parentNode);
		parentNode.setLeftChild(rChild);

	}

	/**
	 * Calculates the minimum number of nodes in an AVL tree of balance h.
	 * @param h the balance of the tree (a non-negative number) in question.
	 * @return the minimum number of nodes in an AVL tree of the given balance.
	 */
	public static int findMinNodes(int h){
		return (int)(Math.round(((Math.sqrt(5)+2) / Math.sqrt(5))*Math.pow((1+Math.sqrt(5))/2, h)-1));
	}

	/**
	 * A method that calculates the maximum number of nodes in an AVL tree of height h. We've proved this in
	 * data structure.
	 * @param h height of the tree (a non-negative number).
	 * @return maximum number of nodes of height h.
	 */
	public static int findMaxNodes(int h){
		if (h==0){
			return ONLY_ROOT;
		}
		return (int)((Math.pow(2,h+1))-1);// the size of a full binary Search tree height h.
	}

}
