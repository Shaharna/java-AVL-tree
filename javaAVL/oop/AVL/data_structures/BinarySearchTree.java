package oop.ex4.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is an implementation of a basic Binary search Tree.
 * A binary search tree has a root node.
 * The Binary search trees support three main operations: insertion of elements, deletion of elements, and
 * lookup (checking whether a key is present).
 * @author yael.sarusi
 * @author shaharna13
 */

public abstract class BinarySearchTree implements Iterable<Integer>{

	/* Node: Pointer to the tree's root */
	protected Node root;

	/* Flag for the default value */
	private static final int DEFAULT_RETURN_VALUE = -1;

	/**
	 * default constructor.
	 */
	public BinarySearchTree(){
		root = null;
	}

	/**
	 * A constructor that builds the tree by adding the elements in the input array one-by-one.
	 * The BS tree is a set, so if the same values appears more than once in the list, it is  added
	 * only once.
	 * @param data values to add to tree
	 */
	public BinarySearchTree(int[] data){
		if(data!=null){
			for (int number:data){
				this.add(number);
			}
		}
		else {
			root = null;
		}
	}

	/**
	 * A copy constructor that creates a deep copy of the given AvlTree.
	 * The new tree contains all the values of the given tree, but not necessarily in the same structure.
	 * @param tree The BS tree to be copied.
	 */
	public BinarySearchTree(AvlTree tree){
		if (tree!= null && tree.getTreeRoot() != null){
			this.root = new Node(tree.getTreeRoot());
		}
		else {
			this.root = null;
		}
	}

	/*
	 * Getter. Get tree root.
	 */
	Node getTreeRoot(){
		return root;
	}
	/**
	 * Returns an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
	 * ascending order, and does NOT implement the remove() method.
	 * @return an iterator for the Avl Tree.
	 */
	public java.util.Iterator<java.lang.Integer> iterator(){
		return new BinarySearchTreeIterator(this);
	}

	/**
	 * Check whether the tree contains the given input value.
	 * @param searchVal value to search for
	 * @return if val is found in the tree, return the depth of the node (0 for the root) with the given
	 * value if it was found in the tree, -1 otherwise
	 */
	public int contains(int searchVal){
		// Find the node with the data or the parent of where the node should have been.
		Node potentialLoc = findNode(searchVal);
		if (potentialLoc == null){
			return DEFAULT_RETURN_VALUE;
		}
		if (potentialLoc.getData() == searchVal){
			return potentialLoc.getDepth();
		}
		// If the node found's data doesn't match the data given, the data doesn't exist in the tree
		else {
			return DEFAULT_RETURN_VALUE; // Default
		}
	}

	/**
	 * Get the tree size
	 * @return The size of the Avl tree
	 */
	public int size(){
		if (root == null){ // no data in the tree
			return 0;
		}
		return root.size();
	}


	/**
	 * Abstract function.
	 * The function adds the received data to the binary search tree.
	 * @param data the data to add to the tree.
	 * @return true if successfully added into the tree, false otherwise.
	 */
	abstract public boolean add (int data);

	/**
	 * Abstract function.
	 * Removes the node with the given value from the tree, if it exists.
	 * @param toDelete the value to remove from the tree.
	 * @return true if the given value was found and deleted, false otherwise.
	 */
	abstract public boolean delete(int toDelete);

	/*
	 * This function finds the minimum node of the sub tree rooted in the given node.
	 * @param startNode the root of the sub tree
	 * @return the Node which is the minimum node in the sub tree
	 */
	Node findMinNode(Node startNode){
		if(startNode==null){
			return startNode;
		}
		Node curNode = startNode;

		while (curNode.getLeftChild() != null){
			curNode = curNode.getLeftChild();
		}
		return curNode;
	}

	/*
	 * The function returns the successor of the received node;
	 * @param toSuccess An Node to whom to find it's successor
	 * @return the next avl node with the following value if exists, otherwise return null. (No successor
	 * node found)
	 */
	Node successor(Node toSuccess) {
		Node rChild = toSuccess.getRightChild();
		Node parent = toSuccess.getParent();
		if (rChild != null) { // check first for the minimum node of the tree sub-rooted in the right child
			return findMinNode(rChild);
		} else { // return the first parent that has bigger data
			while (parent != null && parent.getData() < toSuccess.getData()){
				parent = parent.getParent();
			}
			return parent; // if gets to this point, the parent is null (return null)
		}
	}

	/*
	 * This function returns the node with the data given, or the designated parent of the future node.
	 * The designated parent will always have a left/right/both children null (otherwise the while loop
	 * will continue down the tree until a parent with a "open slot" for a new children is found.
	 * @param data The data search value
	 * @return the node with the data given (if found),otherwise the designated parent of the future node.
	 */
	Node findNode(int data){
		Node lastNode = root;
		Node curNode = root;
		int nodeData;

		// Find the correct place for the new data.
		while (curNode!= null){
			lastNode = curNode;
			nodeData = curNode.getData();

			// found the node that holds tha data needed
			if (nodeData == data){
				return curNode;
			}
			else if (nodeData > data){ // data is lower, go the left child.
				curNode = curNode.getLeftChild();
			}
			else { // data is higher, go the right child.
				curNode = curNode.getRightChild();
			}
		}
		// While loop stops when gets to null pointer (unless the data is found), return the parent of the
		// null pointer
		return lastNode;
	}

	/**
	 * This class implements an iterator for the Binary Search tree.
	 * @author Yael.Sarusi
	 * @author shaharna13
	 */
	public class BinarySearchTreeIterator implements Iterator<Integer> {

		/* Save the binary tree that this iterator iterates on */
		private BinarySearchTree BSTree;

		/* The current node in the iteration. */
		private Node nextNode;

		/**
		 * a costructor from a tree
		 * @param tree a binary search tree
		 */
		public BinarySearchTreeIterator(BinarySearchTree tree){
			this.BSTree = tree;
			nextNode = this.BSTree.findMinNode(this.BSTree.getTreeRoot());
		}

		/**
		 * The functions returns true if the node has a next node (successor) in the tree,
		 * false otherwise.
		 * @return true if the node has a next node, false otherwise.
		 */
		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		/**
		 * The function returns the next object in the iterator.
		 * If one does not exists it will raise an exception ("NoSuchElementException")
		 * @return next node if it exists.
		 * @throws NoSuchElementException when there are no more elements to iterate over.
		 */
		@Override
		public Integer next() throws NoSuchElementException {
			// Save a pointer to the current node to throw.
			Node curNode = nextNode;
			// Check if the next node to Throw isn't null, if so Throw an exception.
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			else {
				// Update pointer to the nextNode for the next time
				nextNode = this.BSTree.successor(this.nextNode);
			}

			return curNode.getData();
		}

	}

}
