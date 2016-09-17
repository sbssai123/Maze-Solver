// to represent a Node<T>
class Node<T> extends ANode<T> {
  T data;

  // constructor
  Node(T data) {
    super(null, null);
    this.data = data;
  }

  // convenience constructor
  Node(T d, ANode<T> n, ANode<T> p) {
    super(n, p);
    updateNodes(n, p);
    this.data = d;
  }

  // EFFECT: to update the fields of this node
  // throws an exception if either ANode<T> is null
  void updateNodes(ANode<T> n, ANode<T> p) {
    if ((n == null) || (p == null)) {
      throw new IllegalArgumentException("VALUE NULL");
    }
    else
    {
      this.next = n;
      n.prev = this;
      this.prev = p;
      p.next = this;
    }
  }

  // to return the size of a list of Nodes<T>
  int size() {
    return 1 + this.next.size();
  }

  // to return the removed Nodes<T> data
  // EFFECT: to fix the links/remove this Node<T>
  T remove() {
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return this.data;
  }

  // is this Node<T>'s data the same as the given element?
  boolean containsHelp(T element) {
    if (this.data.equals(element)) {
      return true;
    }
    else {
      return this.next.containsHelp(element);
    }
  }

  // is this Node<T> empty?
  boolean isEmptyHelp() {
    return false;
  }
}