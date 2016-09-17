import java.awt.*;

import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// to represent a Vertex
class Vertex {

  Posn location; // how to identify each vertex
  // each "wall" of the cell
  // if boolean flag is false, DO NOT draw that wall
  boolean top;
  boolean bottom;
  boolean left;
  boolean right;
  // Vertex neighbors
  Vertex friendtop;
  Vertex friendbottom;
  Vertex friendleft;
  Vertex friendright;
  boolean hasTraveled;
  static final int SIZE = 20; // to represent the size of the cell
  // to override hashcode
  public int hashCode() {
    return this.location.x + this.location.y * 100;
  }

  // constructor
  Vertex(Posn location) {
    this.location = location;
    this.top = true;
    this.bottom = true;
    this.left = true;
    this.right = true;
    this.hasTraveled = false;
  }

  // convenience constructor for testing
  Vertex(Posn location, Vertex friendtop, Vertex friendbottom,
         Vertex friendleft, Vertex friendright) {
    this.location = location;
    this.friendtop = friendtop;
    this.friendbottom = friendbottom;
    this.friendleft = friendleft;
    this.friendright = friendright;
  }

  // to override equals
  public boolean equals(Object obj) {
    if (!(obj instanceof Vertex)) {
      return false;
    }
    Vertex v = (Vertex)obj;
    return this.location.x == v.location.x &&
            this.location.y == v.location.y;
  }


  // to render a horizontal line
  WorldImage makeWallHorizontal() {
    return new LineImage(new Posn(SIZE, 0), Color.BLACK);
  }

  // to render a vertical line
  WorldImage makeWallVertical() {
    return new LineImage(new Posn(0, SIZE), Color.BLACK);
  }

  // to render a square with a color depending on the input
  WorldImage drawSquare(int red, int green, int blue) {
    return new RectangleImage(Vertex.SIZE, Vertex.SIZE, OutlineMode.SOLID,
            new Color(red, green, blue));
  }
}
