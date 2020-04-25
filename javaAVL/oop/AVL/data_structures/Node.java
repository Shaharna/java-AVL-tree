package oop.ex4.data_structures;

/**
 * This class is the complete and tested implementation of an Node.
 * @author yael.sarusi
 * @author shaharna13
 */
public class Node {

	/* The node's data */
	private int data;

	/* The node's parent */
	private Node parent;

	/* The node's rightChild */
	private Node rightChild;

	/* The node's leftChild */
	private Node leftChild;

	/*
	 * This is a constructor building a new node from an existing one. Meaning this constructor is
	 * copying the node.
	 * @param oldNode the node to copy.
	 */
	Node (Node oldNode){
		this.data = oldNode.data;
		if (oldNode.leftChild != null){
			setLeftChild(new Node(oldNode.leftChild));
		}
		if (oldNode.rightChild != null) {
			setRightChild(new Node(oldNode.rightChild));
		}
	}

	/*
	 * Node constructor with data.
	 * @param data The value to put in the node.
	 */
	Node(int data) {
		resetNode(data);
	}

	/*
	 * Full node constructor (for use when the parents & children of the node are known upon construction
	 * @param data       The data in the node
	 * @param parent     The parent of the Node
	 * @param leftChild  The left child of the Node
	 * @param rightChild The right child of the Node
	 */
	Node(int data, Node parent, Node leftChild, Node rightChild) {
		resetNode(data);
		setLeftChild(leftChild);
		setRightChild(rightChild);
		setParent(parent);
	}

	/*
	 * This function resets the node's data.
	 * @param data the new data of the node
	 */
	void resetNode(int data){
		this.data = data;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;
	}


	/*
	 * Recursive function. Calculates the size of the sub tree rooted in the current Node.
	 * @return the number of nodes in the sub tree rooted in the current Node.
	 */
	int size() {
		int subTreeSizeLeft = 0;
		int subTreeSizeRight = 0;
		if (this.leftChild != null) {
			subTreeSizeLeft = leftChild.size();
		}

		if (this.rightChild != null) {
			subTreeSizeRight= rightChild.size();
		}

		return subTreeSizeLeft + subTreeSizeRight +1;
	}

	/*
	 * Setter. Sets the right child to be the node given, changes the child's node Parent to be this node.
	 * @param rightChild The node to add as right child.
	 */
	void setRightChild(Node rightChild) {
		if (rightChild != null){
			rightChild.setParent(this);
		}
		this.rightChild = rightChild;
	}

	/*
	 * Setter. Sets the left child to be the node given, changes the child's node Parent to be this node.
	 * @param leftChild The node to add as left child.
	 */
	void setLeftChild(Node leftChild) {
		if (leftChild != null){
			leftChild.setParent(this);
		}
		this.leftChild = leftChild;
	}

	/*
	 * Setter. Sets the parent to be the node given.
	 * @param parent The node to add as parent.
	 */
	void setParent(Node parent){
		this.parent = parent;
	}

	/*
	 * Getter. Get the data in the Node.
	 * @return the node's data
	 */
	int getData(){
		return data;
	}

	/**
	 * Getter. Get the right child of the Node.
	 * @return The right child of the node.
	 */
	Node getRightChild(){
		return rightChild;
	}


	/*
	 * Getter. Get the left child of the Node.
	 * @return The left child of the node
	 */
	Node getLeftChild(){
		return leftChild;
	}

	/*
	 * Getter. Get the parent of the Node.
	 * @return the Parent node
	 */
	Node getParent(){
		return parent;
	}

	/*
	 * @return the nodes height
	 */
	int getHeight(){
		return calculateHeight();
	}

	/*
	 * Calculates the node's height, recursively.
	 */
	int calculateHeight(){
		int leftTreeHeight = 0;
		int rightTreeHeight = 0;

		if (getLeftChild() == null && getRightChild() == null){
			return 0;
		}

		if (getLeftChild() != null ){
			leftTreeHeight = getLeftChild().calculateHeight();
		}
		if (getRightChild() != null){
			rightTreeHeight = getRightChild().calculateHeight();
		}
		return Math.max(leftTreeHeight, rightTreeHeight) + 1;
	}

	/*
	 * This function calculates the node's depth from the root of the tree.
	 * @param node
	 * @return
	 */
	int getDepth(){
		int depth = 0;

		Node curNode = this;
		while (curNode.getParent() != null){
			depth++;
			curNode = curNode.getParent();
		}
		return depth;
	}

	/*
	 * Setter. Set the data of the node.
	 */
	void setData(int newData){
		this.data = newData;
	}

}
