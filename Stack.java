// to represent a Stack<T>
class Stack<T> implements ICollection<T> {
  Deque<T> contents;

  // constructor
  Stack() {
    this.contents = new Deque<T>();
  }

  // convenience constructor for testing
  Stack(Deque<T> contents) {
    this.contents = contents;
  }

  // is this Stack<T> empty?
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // to return the data of the removed Stack<T>
  // EFFECT: removes the first element of the stack<T>
  public T remove() {
    return this.contents.removeFromHead();
  }

  // EFFECT: to add the given item to the front of the Stack<T>
  public void add(T item) {
    this.contents.addAtHead(item);
  }

  // is the given element in this Stack<T>?
  public boolean contains(T element) {
    return this.contents.contains(element);
  }
}