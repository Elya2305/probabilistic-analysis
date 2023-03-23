fun main() {
    val tree = BinarySearchTree()

    /*
              50
            /	 \
           30	 70
          / \    /
        20  40  60
    */
    tree.insert(50)
    tree.insert(30)
    tree.insert(20)
    tree.insert(40)
    tree.insert(70)
    tree.insert(60)

    println("70 is present: ${tree.search(70)}")
    println("10 is present: ${tree.search(10)}")

    tree.delete(50)
    tree.inorder()
}

internal class BinarySearchTree {

    internal inner class Node(var key: Int) {
        var left: Node? = null
        var right: Node? = null
    }

    private var root: Node? = null

    fun insert(key: Int) {
        root = insertRec(root, key)
    }

    fun search(key: Int): Boolean {
        val value = searchRec(root, key)
        return value != null
    }

    fun inorder() {
        inorderRec(root)
    }

    fun delete(key: Int) {
        root = deleteRec(root, key)
    }

    private fun insertRec(root: Node?, key: Int): Node {
        var _root = root

        if (_root == null) {
            _root = Node(key)
            return _root
        } else if (key < _root.key) {
            _root.left = insertRec(_root.left, key)
        } else if (key > _root.key) {
            _root.right = insertRec(_root.right, key)
        }

        return _root
    }

    private fun searchRec(root: Node?, key: Int): Node? {
        if (root == null || root.key == key) return root
        return if (root.key > key) {
            searchRec(root.left, key)
        } else {
            searchRec(root.right, key)
        }
    }

    private fun inorderRec(root: Node?) {
        if (root != null) {
            inorderRec(root.left)
            println(root.key)
            inorderRec(root.right)
        }
    }

    private fun deleteRec(root: Node?, key: Int): Node? {
        //tree is empty
        if (root == null) return root

        if (key < root.key) { // traverse left subtree
            root.left = deleteRec(root.left, key)
        } else if (key > root.key) {  // traverse right subtree
            root.right = deleteRec(root.right, key)
        } else { // this is a node we want to delete

            // node contains only one child
            if (root.left == null) {
                return root.right
            } else if (root.right == null) {
                return root.left
            }
            // node has two children
            root.key = minValue(root.right)  // get inorder successor (min value in the right subtree)

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.key)
        }
        return root
    }

    private fun minValue(root: Node?): Int {
        var minval = root
        while (minval!!.left != null) {
            minval = minval.left!!
        }
        return minval.key
    }
}
