// to represent an edge
class Edge {
  Vertex side1;
  Vertex side2;
  int weight;

  // constructor
  Edge(Vertex side1, Vertex side2, int weight) {
    this.side1 = side1;
    this.side2 = side2;
    this.weight = weight;
  }

  // convenience constructor for testing purposes
  Edge(int weight, Vertex side1, Vertex side2) {
    this.weight = weight;
    this.side1 = side1;
    this.side2 = side2;
  }
}
