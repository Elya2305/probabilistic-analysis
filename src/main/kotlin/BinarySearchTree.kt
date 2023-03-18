internal class BinarySearchTree {

    internal inner class Node(var key: Int) {
        var left: Node? = null
        var right: Node? = null
    }

    private var root: Node? = null

    fun insert(key: Int) {
        root = insertRec(root, key)
    }

    fun insertRec(root: Node?, key: Int): Node {
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

    fun inorder() {
        inorderRec(root)
    }

    fun inorderRec(root: Node?) {
        if (root != null) {
            inorderRec(root.left)
            println(root.key)
            inorderRec(root.right)
        }
    }
}

fun main() {
    val tree = BinarySearchTree()

    /*
              50
            /	 \
           30	 70
          / \    / \
        20  40  60 80
    */
    tree.insert(50)
    tree.insert(30)
    tree.insert(20)
    tree.insert(40)
    tree.insert(70)
    tree.insert(60)
    tree.insert(80)

    tree.inorder()
}