
// Main method to run the maze
public class MazeSolverMain {
  public static void main(String[] args) {
    MazeGame m = new MazeGame(20, 30);
    m.bigBang(m.width * Vertex.SIZE, (m.height * Vertex.SIZE) + Vertex.SIZE, 0.00001);
  }
}
