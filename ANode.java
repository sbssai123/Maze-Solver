// to represent the abstract class ANode<T>
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // constructor
  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // to return the size of ANode<T>
  abstract int size();

  // to return the size of the next ANode<T>
  int sizeHelp() {
    return next.size();
  }

  // EFFECT: to add the given T to an ANode<T>
  void atHelper(T t, ANode<T> x, ANode<T> y, ANode<T> z) {
    x = new Node<T>(t, y, z);
  }

  // to return this ANode's data
  // EFFECT: to remove this ANode<T>
  abstract T remove();

  // does this ANode<T> contain the given element?
  abstract boolean containsHelp(T element);

  // is this ANode<T> empty?
  abstract boolean isEmptyHelp();
}