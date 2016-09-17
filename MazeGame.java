import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.CircleImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;

// to represent the MazeGame
class MazeGame extends World {
  int height;
  int width;
  ArrayList<ArrayList<Vertex>> verticesInTree; // all vertices
  ArrayList<Edge> edgesInTree; // spanning tree
  ArrayList<Edge> worklist; // sorted edges
  Random rand = new Random();
  HashMap<Posn, Posn> representatives;
  HashMap<Vertex, Edge> cameFromEdge;
  Stack<Vertex> dfsWorklist;    // for the implementation of Depth First Search
  Queue<Vertex> bfsWorklist;    // for the implementation of Breath First Search
  Players p1;
  Players p2;
  boolean breathcondition;
  boolean depthcondition;
  ArrayList<Vertex> alreadySeen;
  Boolean endSearch;
  Boolean togglevisibility1;
  Boolean togglevisibility2;

  // constructor
  MazeGame(int height, int width) {
    this.height = height;
    this.width = width;
    // to initialize the list that represents all of the vertices in the tree to zero
    this.verticesInTree = new ArrayList<ArrayList<Vertex>>();
    // to add all of the vertices in the board to verticesInTree
    this.createVertices();
    // to initialize the worklist
    this.worklist = new ArrayList<Edge>();
    // to create all of the edges in the board
    this.createEdges(1, 1);
    // to initialize the list that represents the edges in the spanning tree
    this.edgesInTree = new ArrayList<Edge>();
    // to initialize the pairings of nodes based off of the posns
    //NOTE: We identified each Node in the graph by a posn
    this.representatives = new HashMap<Posn, Posn>();
    // to sort all of the edges in the spanning tree
    this.sortEdges();
    // to map the nodes to their representatives
    this.mapVertices();
    // to create a spanning tree
    this.createSpanning();
    // to remove walls of the grid if there is an edge of the spanning
    // tree going through it
    this.updateWall();
    // to initialize the hashmap that maps the path from the starting
    // position to the target
    this.cameFromEdge = new HashMap<Vertex, Edge>();
    // to initialize the worklists for breadth and depth first search
    // in order to contain the first node on the board to start
    // the search algorithm
    this.dfsWorklist = new Stack<Vertex>();
    this.bfsWorklist = new Queue<Vertex>();
    this.dfsWorklist.add(verticesInTree.get(0).get(0));
    this.bfsWorklist.add(verticesInTree.get(0).get(0));
    // to initialize the arraylist that represents all of the nodes
    // already visited in breadth and depth first search
    this.alreadySeen = new ArrayList<Vertex>();

    // GAMEPLAY

    // WEBCAT
    this.p1 = new Players();
    //WEBDOG
    this.p2 = new Players();
    // to initialize the image of the players
    this.p1.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 20, 100));
    this.p2.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 181, 100));
    // to intialize the starting positions of the players
    this.p1.vx = verticesInTree.get(0).get(0);
    this.p2.vx = verticesInTree.get(0).get(0);
    // to initialize the breadth and depth first search conditions to be false
    // they will turn true when keys are pressed to start the search algorithm
    breathcondition = false;
    depthcondition = false;
    // to represent when search is finished for the path to be constructed
    this.endSearch = false;
    //to initialize the visibility of the toggling the path of the players
    this.togglevisibility1 = false;
    this.togglevisibility2 = false;
  }

  // convenience constructor for testing
  MazeGame(int height, int width, ArrayList<ArrayList<Vertex>> verticesInTree) {
    this.height = height;
    this.width = width;
    this.verticesInTree = verticesInTree;
    this.worklist = new ArrayList<Edge>();
    this.createEdges(1, 1);
    this.edgesInTree = new ArrayList<Edge>();
    this.representatives = new HashMap<Posn, Posn>();
    this.sortEdges();
    this.mapVertices();
    this.updateWall();
    this.cameFromEdge = new HashMap<Vertex, Edge>();
    this.dfsWorklist = new Stack<Vertex>();
    this.bfsWorklist = new Queue<Vertex>();
    this.dfsWorklist.add(verticesInTree.get(0).get(0));
    this.bfsWorklist.add(verticesInTree.get(0).get(0));
    this.alreadySeen = new ArrayList<Vertex>();
    // gameplay
    this.p1 = new Players();
    this.p2 = new Players();
    this.p1.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 20, 100));
    this.p2.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 181, 100));
    this.p1.vx = verticesInTree.get(0).get(0);
    this.p2.vx = verticesInTree.get(0).get(0);
    breathcondition = false;
    depthcondition = false;
  }

  // EFFECT: to fix each vertices' links with its neighbors'
  void fixFriends(ArrayList<ArrayList<Vertex>> aav) {
    for (int i = 0; i < width; i = i + 1) {
      for (int j = 0; j < height; j = j + 1) {
        Vertex v = aav.get(i).get(j);
        if (i == 0) {
          v.friendleft = v;
        }
        else {
          v.friendleft = aav.get(i - 1).get(j);
        }
        if (j == 0) {
          v.friendtop = v;
        }
        else {
          v.friendtop = aav.get(i).get(j - 1);
        }
        if (j == height - 1) {
          v.friendbottom = v;
        }
        else {
          v.friendbottom = aav.get(i).get(j + 1);
        }
        if (i == width - 1) {
          v.friendright = v;
        }
        else {
          v.friendright = aav.get(i + 1).get(j);
        }
      }
    }
  }

  // EFFECT: to update the ArrayList<ArrayList<Vertex>> and create vertices
  void createVertices() {
    for (int i = 0; i < width; i = i + 1) { // x coordinates
      ArrayList<Vertex> an = new ArrayList<Vertex>();
      for (int j = 0; j < height; j = j + 1) { // y coordinates
        Vertex n = new Vertex(new Posn(i, j));
        an.add(n);
      }
      verticesInTree.add(an);
    }
    // EFFECT: to initialize fixFriends
    this.fixFriends(verticesInTree);
  }

  // EFFECT: to update and add Edges to our ArrayList with
  // randomly assigned weights and updated "side" fields
  void createEdges(int hb, int vb) {
    for (int i = 0; i < verticesInTree.size(); i = i + 1) {
      ArrayList<Vertex> an = this.verticesInTree.get(i);
      for (int j = 0; j < an.size(); j = j + 1) {
        Vertex n = an.get(j);
        Posn p = n.location;
        if (p.x != width - 1) {
          Edge e1 = new Edge(n, verticesInTree.get(i + 1).get(j),
                  rand.nextInt(width * height) * vb) ;
          worklist.add(e1);
        }
        if (p.y != height - 1) {
          Edge e2 = new Edge(n, verticesInTree.get(i).get(j + 1),
                  rand.nextInt(width * height) * hb) ;
          worklist.add(e2);
        }
      }
    }
  }

  // EFFECT: to map each vertices' representative to itself
  void mapVertices() {
    for (int i = 0; i < verticesInTree.size(); i = i + 1) {
      for (int j = 0; j < verticesInTree.get(i).size(); j = j + 1) {
        representatives.put(verticesInTree.get(i).get(j).location,
                verticesInTree.get(i).get(j).location);
      }
    }
  }

  // EFFECT: to sort the Edges of edgesInTree by weight
  void sortEdges() {
    Collections.sort(worklist, new WeightComparator());
  }

  // to return a given posn's representative
  Posn find(HashMap<Posn, Posn> hm, Posn p) {
    if (hm.get(p) == p) {
      return p;
    }
    else {
      return this.find(hm, hm.get(p));
    }
  }

  // EFFECT: to set the value of one representative's representative to the other
  void union(HashMap<Posn, Posn> hm, Posn p1, Posn p2) {
    hm.put(this.find(hm, p2), this.find(hm, p1));
  }

  // to add edges from the worklist to the spanning
  // tree based on Kruskal's algorithm
  ArrayList<Edge> createSpanning() {
    while (edgesInTree.size() < (height * width) - 1) {
      Edge e = this.worklist.get(0);
      if (this.find(representatives, e.side1.location) ==
              this.find(representatives, e.side2.location)) {
        worklist.remove(0);
      }
      else {
        this.edgesInTree.add(e);
        this.union(representatives, e.side1.location, e.side2.location);
        worklist.remove(0);
      }
    }
    return this.edgesInTree;
  }

  // EFFECT: to update the vertex's field to false if there
  // is an edge running through the vertex's "wall"
  void updateWall() {
    for (Edge e : edgesInTree) {
      if (e.side2.location.x - e.side1.location.x == 1) {
        e.side1.right = false;
        e.side2.left = false;
      }
      else if (e.side1.location.x - e.side2.location.x == 1) {
        e.side2.right = false;
        e.side1.left = false;
      }
      else if (e.side2.location.y - e.side1.location.y == 1) {
        e.side1.bottom = false;
        e.side2.top = false;
      }
      else if (e.side1.location.y - e.side2.location.y == 1) {
        e.side2.bottom = false;
        e.side1.top = false;
      }
    }
  }

  // to find all the Edges who's given vertex is side1 or side2
  ArrayList<Edge> findEdges(ArrayList<Edge> ae, Vertex v) {
    ArrayList<Edge> result = new ArrayList<Edge>();
    for (Edge e : ae) {
      if (e.side1.equals(v) || e.side2.equals(v)) {
        result.add(e);
      }
    }
    return result;
  }

  // to return the path from the source to the target
  ArrayList<Vertex> reconstruct(HashMap<Vertex, Edge> hm, Vertex v) {
    ArrayList<Vertex> av = new ArrayList<Vertex>();
    while (!verticesInTree.get(0).get(0).equals(hm.get(v).side1)) {
      av.add(v);
      if (hm.get(v).side2.equals(v)) {
        v = hm.get(v).side1;
      }
      else {
        v = hm.get(v).side2;
      }
    }
    av.add(hm.get(v).side2);
    av.add(hm.get(v).side1);
    return av;
  }

  // to search using Breath First Search
  ArrayList<Vertex> bfs(Vertex to) {
    return search(to, bfsWorklist);
  }

  // to search using Depth First Search
  ArrayList<Vertex> dfs(Vertex to) {
    return search(to, dfsWorklist);
  }

  // to search for the path from the start (from) to the finish of the maze
  ArrayList<Vertex> search(Vertex to, ICollection<Vertex> worklist) {
    if ((!worklist.isEmpty())) {
      Vertex next = worklist.remove();
      if (next.equals(to)) {
        reconstruct(cameFromEdge, next);
        alreadySeen.add(to);
        endSearch = true;
      }
      else {
        // add all the neighbors of next to the
        // worklist for further processing
        ArrayList<Edge> ae = this.findEdges(edgesInTree, next);
        // NOTE: the method findEdges takes in a Vertex that could either be a
        // side1 or side2 which is why we check both methods in search
        for (Edge e : ae) {
          if (e.side1.equals(next) && !(alreadySeen.contains(e.side2))) {
            worklist.add(e.side2);
            cameFromEdge.put(e.side2, e);
          }
          if (e.side2.equals(next) && !(alreadySeen.contains(e.side1))) {
            worklist.add(e.side1);
            cameFromEdge.put(e.side1, e);
          }
        }
        alreadySeen.add(next);
      }
    }
    return alreadySeen;
  }

  // to draw all the walls of the maze
  // if the boolean flags are true, then render the wall
  // else, don't render the wall that the edge is passing through
  WorldScene drawWalls(WorldScene w) {
    for (Edge e : edgesInTree) {
      int xc1 = (e.side1.location.x * Vertex.SIZE)
              + Vertex.SIZE / 2; // x coordinate
      int yc1 = (e.side1.location.y * Vertex.SIZE)
              + Vertex.SIZE / 2 ; // y coordinate
      if (e.side1.left) {
        w.placeImageXY(e.side1.makeWallVertical(), xc1
                - Vertex.SIZE / 2, yc1);
      }
      if (e.side1.right) {
        w.placeImageXY(e.side1.makeWallVertical(), xc1
                + Vertex.SIZE / 2, yc1);
      }
      if (e.side1.top) {
        w.placeImageXY(e.side1.makeWallHorizontal(), xc1, yc1
                - Vertex.SIZE / 2);
      }
      if (e.side1.bottom) {
        w.placeImageXY(e.side1.makeWallHorizontal(), xc1, yc1
                + Vertex.SIZE / 2);
      }
      int xc2 = (e.side2.location.x * Vertex.SIZE)
              + Vertex.SIZE / 2; // x coordinate
      int yc2 = (e.side2.location.y * Vertex.SIZE)
              + Vertex.SIZE / 2 ; // y coordinate
      if (e.side2.left) {
        w.placeImageXY(e.side2.makeWallVertical(), xc2
                - Vertex.SIZE / 2, yc2);
      }
      if (e.side2.right) {
        w.placeImageXY(e.side2.makeWallVertical(), xc2
                + Vertex.SIZE / 2, yc2);
      }
      if (e.side2.top) {
        w.placeImageXY(e.side2.makeWallHorizontal(), xc2, yc2
                - Vertex.SIZE / 2);
      }
      if (e.side2.bottom) {
        w.placeImageXY(e.side2.makeWallHorizontal(), xc2, yc2
                + Vertex.SIZE / 2);
      }
    }
    return w;
  }

  // to reset the maze and render a new one
  void reset(String input) {
    if (input.equals("r")) {
      new MazeGame(height, width);
      this.verticesInTree = new ArrayList<ArrayList<Vertex>>();
      this.createVertices();
      this.worklist = new ArrayList<Edge>();
      this.createEdges(1, 1);
      this.edgesInTree = new ArrayList<Edge>();
      this.representatives = new HashMap<Posn, Posn>();
      this.sortEdges();
      this.mapVertices();
      this.createSpanning();
      this.updateWall();
      this.cameFromEdge = new HashMap<Vertex, Edge>();
      this.dfsWorklist = new Stack<Vertex>();
      this.bfsWorklist = new Queue<Vertex>();
      this.dfsWorklist.add(verticesInTree.get(0).get(0));
      this.bfsWorklist.add(verticesInTree.get(0).get(0));
      this.alreadySeen = new ArrayList<Vertex>();
      this.endSearch = false;
      // gameplay
      this.p1 = new Players();
      this.p2 = new Players();
      this.p1.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 20, 100));
      this.p2.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 181, 100));
      this.p1.vx = verticesInTree.get(0).get(0);
      this.p2.vx = verticesInTree.get(0).get(0);
      breathcondition = false;
      depthcondition = false;
    }
  }

  // to create mazes with either a horizontal or vertical bias
  void mazeBias(String input) {
    if (input.equals("h")) { // horizontal bias
      new MazeGame(height, width);
      this.verticesInTree = new ArrayList<ArrayList<Vertex>>();
      this.createVertices();
      this.worklist = new ArrayList<Edge>();
      this.createEdges(1 + rand.nextInt(50), 1); // creates a random bias
      this.edgesInTree = new ArrayList<Edge>();
      this.representatives = new HashMap<Posn, Posn>();
      this.sortEdges();
      this.mapVertices();
      this.createSpanning();
      this.updateWall();
      this.cameFromEdge = new HashMap<Vertex, Edge>();
      this.dfsWorklist = new Stack<Vertex>();
      this.bfsWorklist = new Queue<Vertex>();
      this.dfsWorklist.add(verticesInTree.get(0).get(0));
      this.bfsWorklist.add(verticesInTree.get(0).get(0));
      this.alreadySeen = new ArrayList<Vertex>();
      this.endSearch = false;
      // gameplay
      this.p1 = new Players();
      this.p2 = new Players();
      this.p1.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 20, 100));
      this.p2.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 181, 100));
      this.p1.vx = verticesInTree.get(0).get(0);
      this.p2.vx = verticesInTree.get(0).get(0);
      breathcondition = false;
      depthcondition = false;
    }
    if (input.equals("g")) { // vertical bias
      new MazeGame(height, width);
      this.verticesInTree = new ArrayList<ArrayList<Vertex>>();
      this.createVertices();
      this.worklist = new ArrayList<Edge>();
      this.createEdges(1, 1 + rand.nextInt(50)); // creates a random bias
      this.edgesInTree = new ArrayList<Edge>();
      this.representatives = new HashMap<Posn, Posn>();
      this.sortEdges();
      this.mapVertices();
      this.createSpanning();
      this.updateWall();
      this.cameFromEdge = new HashMap<Vertex, Edge>();
      this.dfsWorklist = new Stack<Vertex>();
      this.bfsWorklist = new Queue<Vertex>();
      this.dfsWorklist.add(verticesInTree.get(0).get(0));
      this.bfsWorklist.add(verticesInTree.get(0).get(0));
      this.alreadySeen = new ArrayList<Vertex>();
      this.endSearch = false;
      // gameplay
      this.p1 = new Players();
      this.p2 = new Players();
      this.p1.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 20, 100));
      this.p2.piece = new CircleImage(8, OutlineMode.SOLID, new Color(255, 181, 100));
      this.p1.vx = verticesInTree.get(0).get(0);
      this.p2.vx = verticesInTree.get(0).get(0);
      breathcondition = false;
      depthcondition = false;
    }
  }

  // EFFECT: to let the players move based on the user input
  public void onKeyEvent(String input) {
    this.p1.movePlayer1(input, this.height, this.width);
    this.p2.movePlayer2(input, this.height, this.width);
    this.mazeBias(input);
    if (input.equals("b")) { // breath first search
      breathcondition = true;
      depthcondition = false;
      this.alreadySeen = new ArrayList<Vertex>();
      this.bfsWorklist = new Queue<Vertex>();
      this.bfsWorklist.add(verticesInTree.get(0).get(0));
      this.cameFromEdge = new HashMap<Vertex, Edge>();
      this.endSearch = false;
    }
    if (input.equals("v")) {  // depth first search
      depthcondition = true;
      breathcondition = false;
      this.alreadySeen = new ArrayList<Vertex>();
      this.dfsWorklist = new Stack<Vertex>();
      this.dfsWorklist.add(verticesInTree.get(0).get(0));
      this.cameFromEdge = new HashMap<Vertex, Edge>();
      this.endSearch = false;
    }

    if (input.equals("p")) { //player 2 toggle
      this.togglevisibility2 = !togglevisibility2;
    }

    if (input.equals("o")) { //player 1 toggle
      this.togglevisibility1 = !togglevisibility1;
    }
    this.reset(input);
  }

  // EFFECT: to draw the given ArrayList onto the given scene
  void drawPaths(ArrayList<Vertex> av, WorldScene w, int red, int green, int blue) {
    for (Vertex v : av) {
      w.placeImageXY(v.drawSquare(red, green, blue),
              v.location.x * Vertex.SIZE + Vertex.SIZE / 2,
              v.location.y * Vertex.SIZE + Vertex.SIZE / 2);
    }
  }

  // EFFECT: to animate the search paths
  public void onTick() {
    if (breathcondition && !endSearch) {
      this.bfs(this.verticesInTree.get(width - 1).get(height - 1));
    }
    if (depthcondition && !endSearch) {
      this.dfs(this.verticesInTree.get(width - 1).get(height - 1));
    }
  }

  // to draw the world scene in order to render the maze image, place the
  // starting and ending points and the players, and to keep score
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    scene.placeImageXY(new RectangleImage(width * Vertex.SIZE,
                    height * Vertex.SIZE,
                    OutlineMode.SOLID, Color.gray), (width * Vertex.SIZE) / 2,
            (height * Vertex.SIZE) / 2); // grey background
    scene.placeImageXY(new RectangleImage(Vertex.SIZE, Vertex.SIZE,
                    OutlineMode.SOLID, new Color(0, 200, 0)), 0 + Vertex.SIZE / 2,
            0 + Vertex.SIZE / 2); // starting vertex
    scene.placeImageXY(new RectangleImage(Vertex.SIZE, Vertex.SIZE,
            OutlineMode.SOLID, new Color(150, 0, 150)), width * Vertex.SIZE -
            Vertex.SIZE / 2, height * Vertex.SIZE - Vertex.SIZE / 2); // ending vertex
    this.drawPaths(alreadySeen, scene, 127, 169, 214);
    if (endSearch) {
      this.drawPaths(this.reconstruct(cameFromEdge,
              verticesInTree.get(width - 1).get(height - 1)), scene, 24, 17, 219);
    }
    if (togglevisibility1) {
      this.drawPaths(p1.player1toggle, scene, 230, 243, 47);
    }
    if (togglevisibility2) {
      this.drawPaths(p2.player2toggle, scene, 255, 102, 178);
    }
    WorldScene result = this.drawWalls(scene); // all black lines/"walls"
    if (p1.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p2.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p1.moves > p2.moves) {
      scene.placeImageXY(new TextImage("WEBDOG WINS ◖⚆ᴥ⚆◗", height + Vertex.SIZE / 5,
              Color.white), (width * Vertex.SIZE) / 2, (height * Vertex.SIZE) / 2);
    }
    if (p1.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p2.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p1.moves < p2.moves) {
      scene.placeImageXY(new TextImage("WEBCAT WINS ^ↀᴥↀ^", height + Vertex.SIZE / 5,
              Color.white), (width * Vertex.SIZE) / 2, (height * Vertex.SIZE) / 2);
    }
    if (p1.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p2.vx.equals(verticesInTree.get(width - 1).get(height - 1))
            && p1.moves == p2.moves) {
      scene.placeImageXY(new TextImage("DOGS AND CATS ARE ==",
                      height + Vertex.SIZE / 5, Color.white), (width * Vertex.SIZE) / 2,
              (height * Vertex.SIZE) / 2);
    }
    result.placeImageXY(this.p2.piece, (p2.vx.location.x * Vertex.SIZE) +
            Vertex.SIZE / 2, (p2.vx.location.y * Vertex.SIZE)
            + Vertex.SIZE / 2); // webdog, yellow circle
    result.placeImageXY(this.p1.piece, (p1.vx.location.x * Vertex.SIZE) +
            Vertex.SIZE / 2, (p1.vx.location.y * Vertex.SIZE)
            + Vertex.SIZE / 2); // webcat, red circle
    result.placeImageXY(new TextImage("WEBCAT MOVES: " + p1.moves + "      "
                    + "WEBDOG MOVES: " + p2.moves, height / 2 + height / 4, Color.BLACK),
            width / 2 * Vertex.SIZE, height * Vertex.SIZE + Vertex.SIZE / 2); // score
    return result;
  }
}