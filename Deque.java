// to represent a Deque
class Deque<T> {
  Sentinel<T> header;

  // constructor
  Deque() {
    this.header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> s) {
    this.header = s;
  }

  // counts the number of nodes in a list Deque, not including the header node
  int size() {
    return header.sizeHelp();
  }

  // EFFECT: to insert the given value at the front of this list
  void addAtHead(T t) {
    this.header.atHelper(t, this.header.next, this.header.next, this.header);
  }

  // EFFECT: to insert the given value at the end of this list
  void addAtTail(T t) {
    this.header.atHelper(t, this.header.prev, this.header, this.header.prev);
  }

  // returns the data of the first node of this list
  // EFFECT: removes the first node
  T removeFromHead() {
    return this.header.removeHelp(this.header.next);
  }

  // returns the data of the last node of this list
  // EFFECT: removes the last node
  T removeFromTail() {
    return this.header.removeHelp(this.header.prev);
  }

  // does this Deque contain the given element?
  boolean contains(T element) {
    return this.header.next.containsHelp(element);
  }

  // to remove the given node from this Deque<T>
  void removeNode(ANode<T> a) {
    this.header.removeHelp(a);
  }

  // is this Deque empty?
  boolean isEmpty() {
    return this.header.next.isEmptyHelp();
  }
}