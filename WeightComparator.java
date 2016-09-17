import java.util.Comparator;

// to compare the weights of two edges
class WeightComparator implements Comparator<Edge> {

  // to return the lesser of the two weights
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}