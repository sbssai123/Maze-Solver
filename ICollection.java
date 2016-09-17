// to represent a mutable collection of items
interface ICollection<T> {

  // is this collection empty?
  boolean isEmpty();

  // EFFECT: adds the given item to the collection
  void add(T item);

  // to return the first item of the collection
  // EFFECT: removes that first item
  T remove();

  // is the given element in this?
  boolean contains(T element);
}