import java.util.ArrayList;
import javalib.worldimages.WorldImage;
// to represent a player
class Players {
  Vertex vx;
  WorldImage piece;
  int moves;
  ArrayList<Vertex> player1toggle;
  ArrayList<Vertex> player2toggle;

  Players() {
    // empty constructor
    player1toggle = new ArrayList<Vertex>();
    player2toggle = new ArrayList<Vertex>();
  }

  // EFFECT: to move Player 1 (webcat) based on the user input
  void movePlayer1(String input, int height, int width) {
    if (this.vx.location.x >= 0 && input.equals("left")
            && (!this.vx.left)) {
      this.vx = this.vx.friendleft;
      player1toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.x < (width * Vertex.SIZE) - Vertex.SIZE / 2
            && input.equals("right") && (!this.vx.right)) {
      this.vx = this.vx.friendright;
      player1toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.y >= 0 && input.equals("up")
            && (!this.vx.top)) {
      this.vx = this.vx.friendtop;
      player1toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.y < (height * Vertex.SIZE) - Vertex.SIZE
            && input.equals("down") && (!this.vx.bottom)) {
      this.vx = this.vx.friendbottom;
      player1toggle.add(vx);
      moves = moves + 1;
    }
  }

  // EFFECT: to move Player 2 (webdog) based on the user input
  void movePlayer2(String input, int height, int width) {
    if (this.vx.location.x >= 0 && input.equals("a")
            && (!this.vx.left)) {
      this.vx = this.vx.friendleft;
      player2toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.x < (width * Vertex.SIZE) - Vertex.SIZE / 2
            && input.equals("d") && (!this.vx.right)) {
      this.vx = this.vx.friendright;
      player2toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.y >= 0 && input.equals("w")
            && (!this.vx.top)) {
      this.vx = this.vx.friendtop;
      player2toggle.add(vx);
      moves = moves + 1;
    }
    if (this.vx.location.y < (height * Vertex.SIZE) - Vertex.SIZE
            && input.equals("s") && (!this.vx.bottom)) {
      this.vx = this.vx.friendbottom;
      player2toggle.add(vx);
      moves = moves + 1;
    }
  }
}