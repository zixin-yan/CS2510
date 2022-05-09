import java.awt.Color;
import tester.*;
import javalib.funworld.*;
import javalib.worldcanvas.*;
import javalib.worldimages.*;

interface ITree {
  // renders ITree to a picture
  WorldImage draw();

  // computes whether any of the twigs in the tree are pointing downward rather
  // than upward
  boolean isDrooping();

  // produces tree and produces a Branch using the given arguments with a twist
  ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree);

  // returns the width of the tree
  double getWidth();

  // returns the width of the left tree
  double getLeftWidth();

  // returns the width of the right tree
  double getRightWidth();
}

class Leaf implements ITree {
  int size; // represents the radius of the leaf
  Color color; // the color to draw it

  Leaf(int size, Color color) {
    this.size = size;
    this.color = color;
  }

  // renders ITree to a picture
  public WorldImage draw() {
    return new CircleImage(this.size, OutlineMode.SOLID, this.color);
  }

  // computes whether any of the twigs in the tree are pointing downward rather
  // than upward
  public boolean isDrooping() {
    return false;
  }

  // produces tree and produces a Branch using the given arguments with a twist
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return this;
  }

  // returns the width of the tree
  public double getWidth() {
    return this.size * 2;
  }

  // returns the width of the left tree
  public double getLeftWidth() {
    return this.size;
  }

  // returns the width of the right tree
  public double getRightWidth() {
    return this.size;
  }
}

class Stem implements ITree {
  // How long this stick is
  int length;
  // The angle (in degrees) of this stem, relative to the +x axis
  double theta;
  // The rest of the tree
  ITree tree;

  Stem(int length, double theta, ITree tree) {
    this.length = length;
    this.theta = theta;
    this.tree = tree;
  }

  /*
   * TEMPLATE
   * FIELDS:
   * ... this.length ... -- int
   * ... this.theta ... -- double
   * ... this.tree ... -- ITree
   * 
   * METHODS
   * ... this.draw() ... -- WorldImage
   * ... this.isDrooping() ... -- boolean
   * ... this.combine(int, int, double, double, ITree) ... -- ITree
   * ... this.getWidth() ... -- double
   * ... this.getLeftWidth() ... -- double
   * ... this.getRightWidth() ... -- double
   * 
   * METHODS FOR FIELDS
   * 
   */

  // renders ITree to a picture
  public WorldImage draw() {
    if (this.length == -1) {
      WorldImage tree = this.tree.draw();
      WorldImage treeRotated = new RotateImage(tree, -this.theta + 90);
      return treeRotated;
    }
    else {
      WorldImage tree = this.tree.draw();
      WorldImage stem = new LineImage(new Posn(0, this.length), Color.BLUE).movePinhole(0,
          -this.length / 2);
      // only rotate the stem to keep the tree upward
      WorldImage stemRotated = new RotateImage(stem, -this.theta + 90);
      WorldImage treeWithStem = new OverlayImage(tree, stemRotated).movePinhole(
          -this.length * Math.cos(Math.toRadians(this.theta)), stemRotated.getHeight());
      return treeWithStem;
    }
  }

  // computes whether any of the twigs in the tree are pointing downward rather
  // than upward
  public boolean isDrooping() {
    return (180 < this.theta % 360 && this.theta % 360 < 360) && this.tree.isDrooping();
  }

  // produces tree and produces a Branch using the given arguments with a twist
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta,
        new Stem(leftLength, leftTheta, this), new Stem(rightLength, rightTheta, otherTree));
  }

  // returns the width of the tree
  public double getWidth() {
    if (this.theta >= 90 && this.theta <= 270) {
      if (this.tree.getRightWidth() <= -this.length * Math.cos(Math.toRadians(this.theta))) {
        return this.tree.getLeftWidth() - this.length * Math.cos(Math.toRadians(this.theta));
      }
      else {
        return this.tree.getWidth();
      }
    }
    else {
      if (this.tree.getLeftWidth() <= this.length * Math.cos(Math.toRadians(this.theta))) {
        return this.tree.getRightWidth() + this.length * Math.cos(Math.toRadians(this.theta));
      }
      else {
        return this.tree.getWidth();
      }
    }
  }

  // returns the width of the left tree
  public double getLeftWidth() {
    return -this.length * Math.cos(Math.toRadians(this.theta)) + this.tree.getLeftWidth();
  }

  // returns the width of the right tree
  public double getRightWidth() {
    return this.length * Math.cos(Math.toRadians(this.theta)) + this.tree.getLeftWidth();
  }
}

class Branch implements ITree {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the tree
  ITree left;
  ITree right;

  Branch(int leftLength, int rightLength, double leftTheta, double rightTheta, ITree left,
      ITree right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }

  /*
   * TEMPLATE
   * FIELDS:
   * ... this.leftLength ... -- int
   * ... this.rightLength ... -- int
   * ... this.leftTheta ... -- double
   * ... this.rightTheta ... -- double
   * ... this.left ... -- ITree
   * ... this.right ... -- ITree
   * 
   * METHODS
   * ... this.draw() ... -- WorldImage
   * ... this.isDrooping() ... -- boolean
   * ... this.combine(int, int, double, double, ITree) ... -- ITree
   * ... this.getWidth() ... -- double
   * ... this.getLeftWidth() ... -- double
   * ... this.getRightWidth() ... -- double
   * 
   * 
   * METHODS FOR FIELDS
   * 
   * 
   */

  // renders ITree to a picture
  public WorldImage draw() {
    WorldImage leftRotatedStemWithLeaf = (new Stem(leftLength, leftTheta, left)).draw();

    WorldImage rightRotatedStemWithLeaf = (new Stem(rightLength, rightTheta, right)).draw();

    WorldImage branch = new OverlayImage(rightRotatedStemWithLeaf, leftRotatedStemWithLeaf);
    return branch;
  }

  // computes whether any of the twigs in the tree are pointing downward rather
  // than upward
  public boolean isDrooping() {
    return ((180 < this.leftTheta % 360 && this.leftTheta % 360 < 360)
        || (180 < this.rightTheta % 360 && this.rightTheta % 360 < 360));
  }

  // produces tree and produces a Branch using the given arguments with a twist
  public ITree combine(int leftLength, int rightLength, double leftTheta, double rightTheta,
      ITree otherTree) {
    return new Branch(leftLength, rightLength, leftTheta, rightTheta, new Stem(-1, leftTheta, this),
        new Stem(-1, rightTheta, otherTree));
  }

  // returns the width of the tree
  public double getWidth() {
    return this.getLeftWidth() + this.getRightWidth();
  }

  // returns the width of the left tree
  public double getLeftWidth() {
    // the largest absolute left width of right branch
    double rightTreeLeftWidthAbs = this.right.getLeftWidth()
        - this.rightLength * Math.cos(Math.toRadians(this.rightTheta));

    if (this.left.getLeftWidth() >= rightTreeLeftWidthAbs) {
      return -this.leftLength * Math.cos(Math.toRadians(this.leftTheta)) + this.left.getLeftWidth();
    }
    else {
      return rightTreeLeftWidthAbs;
    }
  }

  // returns the width of the right tree
  public double getRightWidth() {
    // the largest absolute right width of left branch
    double leftTreeRightWidthAbs = this.left.getRightWidth()
        + this.leftLength * Math.cos(Math.toRadians(this.leftTheta));

    if (this.right.getRightWidth() >= leftTreeRightWidthAbs) {
      return this.rightLength * Math.cos(Math.toRadians(this.rightTheta))
          + this.right.getRightWidth();
    }
    else {
      return leftTreeRightWidthAbs;
    }
  }
}

class ExamplesTrees {
  int WIDTH = 800;
  int HEIGHT = 800;
  ITree leftLeafDemo = new Leaf(30, Color.RED);
  ITree rightLeafDemo = new Leaf(45, Color.BLUE);
  ITree leafWithStem = new Stem(100, 60, new Leaf(40, Color.GREEN));
  ITree tree1 = new Branch(90, 90, 135, 40, leftLeafDemo, rightLeafDemo);
  ITree tree2 = new Branch(90, 90, 115, 65, new Leaf(45, Color.GREEN), new Leaf(24, Color.ORANGE));
  ITree treeWithStem1 = new Stem(120, 90, tree1);
  ITree treeWithStem2 = new Stem(150, 90, tree2);
  ITree branch = new Branch(120, 150, 135, 45, tree1, tree2);
  ITree branchDrooping = new Branch(100, 100, 270, 45, leftLeafDemo, rightLeafDemo);
  ITree crossedTree = new Branch(100, 100, 120, 60,
      new Branch(100, 100, 120, 60, new Leaf(40, Color.GREEN),
          new Branch(100, 100, 120, 60,
              new Branch(100, 100, 120, 60, new Leaf(60, Color.RED), new Leaf(40, Color.GREEN)),
              new Leaf(40, Color.GREEN))),
      new Leaf(40, Color.GREEN));

  boolean testDraw(Tester t) {
    WorldCanvas canvas = new WorldCanvas(this.HEIGHT, this.WIDTH);
    WorldScene scene = new WorldScene(this.HEIGHT, this.WIDTH);
    WorldScene full = scene.placeImageXY(this.crossedTree.draw(), this.WIDTH / 2, this.HEIGHT / 2);
    return canvas.drawScene(full) && canvas.show();
  }

  boolean testIsDrooping(Tester t) {
    return t.checkExpect(branchDrooping.isDrooping(), true)
        && t.checkExpect(branch.isDrooping(), false);
  }

  boolean testCombine(Tester t) {
    WorldCanvas canvas = new WorldCanvas(this.HEIGHT, this.WIDTH);
    WorldScene scene = new WorldScene(this.HEIGHT, this.WIDTH);
    WorldScene full = scene.placeImageXY(this.tree1.combine(120, 150, 150, 30, tree2).draw(),
        this.WIDTH / 2, this.HEIGHT / 2);
    return canvas.drawScene(full) && canvas.show();
  }

  boolean testGetWidth(Tester t) {
    return t.checkInexact(leafWithStem.getWidth(), 90.0, 0.00001);
  }
}