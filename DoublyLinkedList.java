/* Lab Assignment 1: Program 1
 * DoublyLinkedList data structure implementation with lots of lovely methods for adding and removing nodes
 * @author 'Jenny Hamer'
 */

package lab1;

public class DoublyLinkedList {
    public DoubleNode head; // first item of list
    public DoubleNode tail; // last item of list

    public DoublyLinkedList() { // constructor with no nodes/items!
        head = null;
        tail = null;
    }

    public class DoubleNode { // DoubleNode data type class
        long node; // data node item
        DoubleNode previous; // pointer to the previous node in the list
        DoubleNode next; // pointer to the following node in the list

        public DoubleNode(long n) { // constructor for the DoubleNode object
            node = n;
        }

        public void printNode() { // to display the contents of this node
            System.out.print(node + " ");
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void headInsert(long n) { // inserting an item at the head of the list
        DoubleNode newNode = new DoubleNode(n);
        if (isEmpty()) { // if the list is empty, both the tail & head point to the newNode; the rest is set to null
            tail = newNode; // assigning the newNode to last item as stand-alone in the list
            tail.previous = null;
            tail.next = null;
        } else { // if the list isn't empty, the newNode becomes the head
            head.previous = newNode; // the previous head node is now pointing to the new head
        }
        newNode.next = head; // the following element points to the previous first node (a value or null if the list had been empty)
        head = newNode; // assigning the newNode as the first element of the list
    }

    public void tailInsert(long n) { // insert a node at the tail of the list
        DoubleNode newNode = new DoubleNode(n); // new node object instantiation
        if (isEmpty()) { // if the list is empty, both the head & tail point to newNode; the rest is set to null
            head = newNode;
            head.previous = null;
            head.next = null;
        } else { // if the list contains other nodes, the tail node points to the new head
            tail.next = newNode; // the element after the current last element points to newNode
            newNode.previous = tail; // the pointer to the node before newNode is set to the previous tail node object
        }
        tail = newNode; // tail points to the newNode
    }

    public DoubleNode removeHead() { // remove the head element of the list
        DoubleNode removed = head; // stores the current head element which will be removed from list and returned to console
        if (isEmpty()) {
        }  // if there are no nodes to remove, do nothing
        else {
            if (head.next == null) { // if there is no element after the head, remove the head
                head = null; // head & tail are both empty
                tail = null;
            } else {
                head = head.next; // set current head element to the following element
            }
        }
        return removed; // returns the removed node
    }

    public DoubleNode removeTail() { // remove the tail node of the list
        DoubleNode removed = tail; // temp current tail object which will be removed and then returned to the console
        if (isEmpty()) {
        } // if the list is empty, do nothing
        else {
            if (tail.previous == null) { // if the list contains one element, remove it
                tail = null;
            } else {
                tail.previous.next = null; // current tail node --> null
                tail = tail.previous; // points the new tail node to the previous tail node
            }
        }
        return removed;
    }

    public boolean insertBefore(long n, long givenNode) { // insert newNode before givenNode
        DoubleNode newNode = new DoubleNode(n);
        DoubleNode scan = head; // marker node that starts with the beginning node to check for givenNode
        if (isEmpty()) {
        } // empty --> do nothing
        else {
            while (scan.node != givenNode) { // loop to search for the givenNode which will have the newNode inserted before it
                scan = scan.next; // scan points to the following node for checking
                if (scan == null) { // if givenNode is not found in the list
                    return false;
                }
            }
            if (scan.node == head.node) { // if the givenNode is the head of the list, call headInsert
                headInsert(newNode.node);
            } else if (scan.node == givenNode) {
                newNode.previous = scan.previous; // the node that came before givenNode will now come before the newNode
                scan.previous.next = newNode;
                newNode.next = scan; // newNode points to its following node, givenNode
                scan.previous = newNode; // givenNode points to its previous node, newNode
            }
        }
        return true; // insertion a success!
    }

    public boolean insertAfter (long n, long givenNode) { // insert newNode after givenNode
        DoubleNode newNode = new DoubleNode(n);
        DoubleNode scan = head; // marker node that starts with the beginning node to search for givenNode
        if (isEmpty()) {
        } // empty --> do nothing
        else {
            while (scan.node != givenNode) { // loop to search for the givenNode which will have the newNode inserted before it
                scan = scan.next; // scan points to the following node for checking
                if (scan == null) { // if givenNode is not found in the list
                    return false;
                }
            }
            if (scan == tail) { // if the givenNode is the tail of the list, call tailInsert
                tailInsert(newNode.node);
            } else {
                newNode.next = scan.next; // the node that after before givenNode will now following the newNode
                scan.next.previous = newNode;
                newNode.previous = scan; // newNode points to the givenNode which comes before it
                scan.next = newNode; // givenNode points to its following node
            }
        }
        return true;
    }

    public DoubleNode removeNode(long givenNode) { // remove the givenNode
        DoubleNode scan = head; // marker node that starts at the beginning of list and searches down for node
        if (isEmpty()) { } // empty --> do nothing
        else {
            while (scan.node != givenNode) { // loop to search for the givenNode which will have the newNode inserted before it
                scan = scan.next; // scan points to the following node for checking
                if (scan == null) { return null; }// if givenNode is not found in the list
            }
            if (scan == tail) { // if the node is the tail of the list, call removeTail()
                removeTail();
            } else if (scan == head) { // if the node is the head of the list, call removeHead()
                removeHead();
            } else {
                scan.previous.next = scan.next; // the current node's previous node points directly to its following node
                scan.next.previous = scan.previous; // ^^ & vice versa
            }
        }
        return scan; // return the removed node
    }

    public DoubleNode moveToHead(long givenNode) { // move a givenNode to the head of the list
        DoubleNode scan = head; // marker node that searches for the node 'top-down' through the list
        if (isEmpty()) {
        } // empty --> do nothing
        else {
            while (scan.node != givenNode) { // loop to search for the givenNode which will have the newNode inserted before it
                scan = scan.next; // scan points to the following node for checking
                if (scan == null) { return null; } // if givenNode is not found in the list
                }
            if (scan == head) { // if node is already the head
                System.out.print("That node is already the head");
            } else if (scan == tail) { // if node is the tail, insert it at the beginning of the list and remove it from the end
                headInsert(scan.node);
                removeTail();
            } else { // if node is somewhere in between
                head.previous = scan; // current head's previous pointer -> givenNode
                scan.previous.next = scan.next; // givenNode's previous points -> its following node
                scan.next.previous = scan.previous; // givenNode's following node points -> its previous                
                head = scan; // givenNode is assigned as head item
                head.previous = null;
            }
        }
        return head;
    }

    public DoubleNode moveToTail(long givenNode) { // move node to tail of the list
        DoubleNode scan = head; // marker node that searches for the node 'top-down' through the list
        if (isEmpty()) {
        } // empty --> do nothing
        else {
            while (scan.node != givenNode) { // loop to search for the givenNode which will have the newNode inserted before it
                scan = scan.next; // scan points to the following node for checking
                if (scan == null) { // if scan reaches the end & givenNode is not found
                    System.out.println("That node was not found.");
                }
            }
            if (scan == head) { // if node is the head, insert it at the tail and remove it from the beginning
                tailInsert(scan.node);
                removeHead();
            } else { // if node is somewhere in between
                tail.next = scan; // curent tail's next points -> givenNode
                scan.previous.next = scan.next; // node's previous points -> its following node
                scan.next.previous = scan.previous; // node's following node points -> its previous
                scan.previous = tail; // node's previous pointer -> current tail
                tail = scan; // node is assigned as tail
                tail.next = null;
            }
        }
        return tail; // returns the new tail node
    }

    public void printList() { // to display the contents of the list starting at the head --> tail
        DoubleNode scan = head;
        System.out.println("Here is the list, from head to tail: ");
        while (scan != null) { // breaks when the loop reaches the end
            scan.printNode(); // print the contents of the node
            scan = scan.next; // --> to the next node
        } System.out.println("");
    }

    public static void main(String[] args) { // testing
        DoublyLinkedList myList = new DoublyLinkedList();
        myList.headInsert(20); // insert dNode 20 at the head
        myList.headInsert(10); // insert dNode 10 as new head
        myList.printList();

        myList.tailInsert(40); // insert dNode 40 at the tail
        myList.tailInsert(50); 
        myList.insertBefore(30, 40); // insert dNode 30 before dNode 40
        myList.printList();
        
        myList.moveToHead(50); // move dNode 50 to head of list      
        myList.printList();
        
        myList.moveToTail(10); // move dNode 10 to tail
        myList.insertAfter(100, 20); // insert dNode 100 after dNode 20
        myList.printList();
        
        myList.removeNode(40); // remove dNode 40
        myList.printList();
        
        myList.removeHead(); // remove head dNode        
        myList.printList();
        
        myList.removeTail(); // remove tail dNode
        myList.printList();
    }
}

/*
PROGRAM OUTPUT:
run:
Here is the list, from head to tail: 
10 20 
Here is the list, from head to tail: 
10 20 30 40 50 
Here is the list, from head to tail: 
50 10 20 30 40 
Here is the list, from head to tail: 
50 20 100 30 40 10 
Here is the list, from head to tail: 
50 20 100 30 10 
Here is the list, from head to tail: 
20 100 30 10 
Here is the list, from head to tail: 
20 100 30 
*/