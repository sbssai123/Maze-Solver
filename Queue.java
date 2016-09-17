// to represent a Queue<T>
class Queue<T> implements ICollection<T> {
  Deque<T> contents;

  // constructor
  Queue() {
    this.contents = new Deque<T>();
  }

  // convenience constructor for testing
  Queue(Deque<T> contents) {
    this.contents = contents;
  }

  // is this Queue<T> empty?
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // to return the data of the removed Queue<T>
  // EFFECT: removes the last element of the Queue<T>
  public T remove() {
    return this.contents.removeFromHead();
  }

  // EFFECT: to add the given item to the end of the Queue
  public void add(T item) {
    this.contents.addAtTail(item);
  }

  // is the given element in this Queue<T>?
  public boolean contains(T element) {
    return this.contents.contains(element);
  }
}