import tester.*;

import java.awt.Color;

import javalib.*;
import javalib.funworld.WorldScene;
import javalib.worldcanvas.*;
import javalib.worldimages.*;
// to represent examples and tests for shapes

class TreeDrawer {
  TreeDrawer() {
  }

  WorldImage drawBranch() {
    // Create a red stem with length of 100 and move the pinhole to the top so we
    // can attach the leaf.
    WorldImage rightStemSize100 = new VisiblePinholeImage(
        new LineImage(new Posn(0, 100), Color.BLUE).movePinhole(0, -50));
    // Create the leaf
    WorldImage rightLeaf = new VisiblePinholeImage(
        new EllipseImage(40, 40, OutlineMode.SOLID, Color.red));

    // Attach the leaf to the stem
    // After we attach the stem, make sure we move the pinhole to the bottom of the
    // stem.
    WorldImage rightStemWithLeaf = new VisiblePinholeImage(
        new OverlayImage(rightStemSize100, rightLeaf).movePinhole(0, rightStemSize100.getHeight()));

    // Rotate the stem and leaf by 45 degrees. This should not affect the pinhole
    WorldImage rightRotatedStemWithLeaf = new VisiblePinholeImage(
        new RotateImage(rightStemWithLeaf, 45));

    // Do the same process for the left stem and leaf.
    WorldImage leftStemSize100 = new VisiblePinholeImage(
        new LineImage(new Posn(0, 100), Color.BLUE).movePinhole(0, -50));
    WorldImage leftLeaf = new VisiblePinholeImage(
        new EllipseImage(40, 40, OutlineMode.SOLID, Color.red));
    WorldImage leftStemWithLeaf = new VisiblePinholeImage(
        new OverlayImage(leftStemSize100, rightLeaf).movePinhole(0, 100));
    WorldImage leftRotatedStemWithLeaf = new VisiblePinholeImage(
        new RotateImage(leftStemWithLeaf, -45));

    // Combine the two stems.
    WorldImage branch = new OverlayImage(rightRotatedStemWithLeaf, leftRotatedStemWithLeaf);

    // Move the pinhole to the top so we can attach it to the branch
    WorldImage straightStemSize100 = new VisiblePinholeImage(
        new LineImage(new Posn(0, 100), Color.BLUE).movePinhole(0, -50));

    // Combine the stem with the branch
    WorldImage combined = new OverlayImage(branch, straightStemSize100);

    // Make sure you move the pinhole back to the bottom so we can potentailly
    // attach this stem to other stems.
    WorldImage combinedWithAdjustedPinhole = new VisiblePinholeImage(combined.movePinhole(0, 100));
    return combinedWithAdjustedPinhole;
  }

  WorldImage drawTree1(double angle) {
    // Create the vertical stem and move the pinhole to the top
    WorldImage rightStemSize100 = new VisiblePinholeImage(
        new LineImage(new Posn(0, 100), Color.BLUE).movePinhole(0, -50));

    // Create the right and left branches that we wrote above.
    WorldImage rightBranch = this.drawBranch();
    WorldImage leftBranch = this.drawBranch();

    // Rotate the branches
    WorldImage rightBranchRotated = new VisiblePinholeImage(new RotateImage(rightBranch, angle));
    WorldImage leftBranchRotated = new VisiblePinholeImage(new RotateImage(leftBranch, -angle));

    // Join the two branches.
    WorldImage joinBranches = new OverlayImage(rightBranchRotated, leftBranchRotated);

    // Move the pinhole to the bottom so we can connect it to other branches
    WorldImage joinStemToBranches = new VisiblePinholeImage(
        new OverlayImage(rightStemSize100, joinBranches).movePinhole(0, 100));

    return joinStemToBranches;

  }

  WorldImage drawTree2() {
    // Create the vertical stem and move the pinhole to the top

    WorldImage tree1 = drawTree1(60);
    WorldImage tree2 = new RotateImage(drawTree1(60), 150);

    // Join the two branches.
    WorldImage joinTrees = new OverlayImage(tree1, tree2);

    // Rotate the entire tree to straighten it out.
    WorldImage roatedJoinTrees = new RotateImage(joinTrees, -75);

    // Move the pinhole to the top so we can attach it to the tree.
    WorldImage rightStemSize200 = new VisiblePinholeImage(
        new LineImage(new Posn(0, 400), Color.BLUE).movePinhole(0, -200));

    // Join the stem to the tree.
    WorldImage joinedTreeWithStem = new OverlayImage(rightStemSize200, roatedJoinTrees);

    // Move the pinhole back to the bottom so we could potentially join this tree
    // with another.
    WorldImage joinStemToBranches = new VisiblePinholeImage(joinedTreeWithStem.movePinhole(0, 400));

    return joinStemToBranches;

  }

}

class ExamplesDrawing {

  boolean testDrawTreeBranchSeparateJoined(Tester t) {
    WorldCanvas c = new WorldCanvas(1200, 800);
    WorldScene s = new WorldScene(1200, 800);
    TreeDrawer td = new TreeDrawer();

    // call td.drawTree1() to see the first tree.
    return c.drawScene(s.placeImageXY(td.drawTree2(), 700, 700)) && c.show();
  }
}