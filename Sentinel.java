// to represent a Sentinel<T>
class Sentinel<T> extends ANode<T> {

  // constructor
  Sentinel() {
    super(null, null);
    updateSentinel(this, this);
  }

  // EFFECT: to update the fields of this Sentinel<T>
  void updateSentinel(ANode<T> n, ANode<T> p) {
    this.next = n;
    this.prev = p;
  }

  // to return the size of this Sentinel<T>
  int size() {
    return 0;
  }

  // returns the data of the ANode
  // EFFECT: to remove the given ANode<T>
  T removeHelp(ANode<T> x) {
    return x.remove();
  }

  // throws an exception because a Sentinel<T> does not have data to return
  T remove() {
    throw new RuntimeException("CANNOT REMOVE FROM A SENTINEL");
  }

  // does this Sentinel contain the given data?
  boolean containsHelp(T element) {
    return false;
  }

  // is this Sentinel<T> empty?
  boolean isEmptyHelp() {
    return true;
  }
}