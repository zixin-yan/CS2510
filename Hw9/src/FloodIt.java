import java.util.*;
import tester.*;
import javalib.worldimages.*;
import javalib.impworld.*;
import java.awt.Color;

//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  static int NUM_COLORS = 6;
  static int CELL_SIZE = 20;

  Cell(int x, int y) {
    Random r = new Random();
    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.RED,
        Color.YELLOW, Color.GREEN, Color.ORANGE, Color.MAGENTA));
    this.x = x;
    this.y = y;
    this.color = colors.get(r.nextInt(NUM_COLORS));
    if (x == 0 && y == 0) {
      this.flooded = true;
    }
    else {
      this.flooded = false;
    }
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  Cell(int x, int y, Color color, boolean flooded, Cell left, Cell top, Cell right, Cell bottom) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }

  // update left cell
  void addLeft(Cell that) {
    this.left = that;
  }

  // update top cell
  void addTop(Cell that) {
    this.top = that;
  }

  // update right cell
  void addRight(Cell that) {
    this.right = that;
  }

  // update bottom cell
  void addBottom(Cell that) {
    this.bottom = that;
  }

  // draw this cell
  WorldImage draw() {
    WorldImage thisCell = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, this.color);
    return thisCell;
  }

  // draw this cell at specific position on background
  WorldImage drawAt(int row, int col, WorldImage background) {
    int x = CELL_SIZE * col + CELL_SIZE / 2 - (int) background.getWidth() / 2;
    int y = CELL_SIZE * row + CELL_SIZE / 2 - (int) background.getHeight() / 2;

    WorldImage backgroundToPlaceTileOn = background.movePinholeTo(new Posn(x, y));
    WorldImage cellImage = this.draw();
    WorldImage result = new OverlayImage(cellImage, backgroundToPlaceTileOn);
    return result;
  }
}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<ArrayList<Cell>> cells;
  // Defines an int constant
  static int BOARD_SIZE = 14;
  static double TICK_RATE = 1.0 / 60.0;
  static int TOTAL_STEP = BOARD_SIZE + Cell.NUM_COLORS * 2;
  int steps;
  double currentTime;
  int time;
  int waterfall;
  int gameStatus; // 0 = game continue; 1 = game win; -1 = game over

  FloodItWorld() {
    this.cells = this.initBoard();
    this.steps = 0;
    this.currentTime = 0.0;
    this.time = 0;
    this.gameStatus = 0;
    this.waterfall = 0;
  }

  FloodItWorld(ArrayList<ArrayList<Cell>> cells) {
    this.cells = cells;
    this.steps = 0;
    this.currentTime = 0.0;
    this.time = 0;
    this.gameStatus = 0;
    this.waterfall = 0;
  }

  // initialize the board for constructor
  public ArrayList<ArrayList<Cell>> initBoard() {
    ArrayList<ArrayList<Cell>> newBoard = new ArrayList<ArrayList<Cell>>();

    for (int y = 0; y < BOARD_SIZE; y++) {
      ArrayList<Cell> rowCells = new ArrayList<Cell>();
      for (int x = 0; x < BOARD_SIZE; x++) {
        rowCells.add(new Cell(x, y));
      }
      newBoard.add(rowCells);
    }

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (col != 0) {
          newBoard.get(row).get(col).addLeft(newBoard.get(row).get(col - 1));
        }
        if (col != BOARD_SIZE - 1) {
          newBoard.get(row).get(col).addRight(newBoard.get(row).get(col + 1));
        }
        if (row != 0) {
          newBoard.get(row).get(col).addTop(newBoard.get(row - 1).get(col));
        }
        if (row != BOARD_SIZE - 1) {
          newBoard.get(row).get(col).addBottom(newBoard.get(row + 1).get(col));
        }
      }
    }
    return newBoard;
  }

  @Override
  // makes a scene
  public WorldScene makeScene() {
    int size = BOARD_SIZE * Cell.CELL_SIZE;
    WorldScene scene = new WorldScene(size, size);
    WorldImage background = new RectangleImage(size, size, OutlineMode.SOLID, Color.GRAY);
    WorldImage infoBar = new TextImage("Steps: " + Integer.toString(this.steps) + "/"
        + Integer.toString(TOTAL_STEP) + "  Time: " + Integer.toString(this.time), Color.black);
    WorldImage gameContinue = new TextImage("Flood-It", Color.black);
    WorldImage gameWin = new TextImage("You Win :D", Color.black);
    WorldImage gameOver = new TextImage("Game Over! You Lose :(", Color.black);
    WorldImage pressR = new TextImage("Press 'R' to restart", Color.black);

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        Cell cell = this.cells.get(row).get(col);
        background = cell.drawAt(row, col, background);
      }
    }

    scene.placeImageXY(background, size - Cell.CELL_SIZE / 2, size - Cell.CELL_SIZE / 2);
    scene.placeImageXY(infoBar, size / 2, size + 10);
    if (this.gameStatus == 0) {
      scene.placeImageXY(gameContinue, size / 2, size + 30);
    }
    else if (this.gameStatus == 1) {
      scene.placeImageXY(gameWin, size / 2, size + 30);
    }
    else {
      scene.placeImageXY(gameOver, size / 2, size + 30);
    }
    scene.placeImageXY(pressR, size / 2, size + 50);
    return scene;
  }

  // makes the game always refresh its status and increase timer
  public void onTick() {
    this.currentTime += TICK_RATE * 1.5;
    Double currTime = this.currentTime;
    this.time = currTime.intValue();

    if (this.waterfall == -1) {
      return;
    }

    changeColor(this.cells.get(0).get(0).color, this.waterfall);

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        infectCells(row, col, this.cells.get(row).get(col));
      }
    }

    if (this.waterfall == (BOARD_SIZE - 1) * 2) {
      this.waterfall = -1;
    }
    else {
      this.waterfall += 1;
    }
  }

  // changes the game state when click on other cell
  public void onMouseClicked(Posn pos) {
    // response nothing when waterfall has not finish
    if (this.waterfall != -1) {
      return;
    }

    this.waterfall = 0;
    int x = pos.x / Cell.CELL_SIZE;
    int y = pos.y / Cell.CELL_SIZE;

    if (x >= BOARD_SIZE || y >= BOARD_SIZE) {
      return;
    }

    Color color = this.cells.get(y).get(x).color;

    // click into the cell that has same color will not count as a move
    if (color.equals(this.cells.get(0).get(0).color)) {
      return;
    }

    this.cells.get(0).get(0).color = color;
    this.steps += 1;
  }

  // changes the color of flooded cells
  public void changeColor(Color color, int index) {
    for (int i = 0; i <= index; i++) {
      if (i >= BOARD_SIZE || (index - i) >= BOARD_SIZE) {
        continue;
      }
      else if (this.cells.get(i).get(index - i).flooded) {
        this.cells.get(i).get(index - i).color = color;
      }
    }
  }

  // change the flooded status of other cells around the cell
  public void infectCells(int row, int col, Cell cell) {
    if (cell.flooded) {
      if (col != 0) {
        if (cell.color.equals(cell.left.color)) {
          cell.left.flooded = true;
        }
      }
      if (col != BOARD_SIZE - 1) {
        if (cell.color.equals(cell.right.color)) {
          cell.right.flooded = true;
        }
      }
      if (row != 0) {
        if (cell.color.equals(cell.top.color)) {
          cell.top.flooded = true;
        }
      }
      if (row != BOARD_SIZE - 1) {
        if (cell.color.equals(cell.bottom.color)) {
          cell.bottom.flooded = true;
        }
      }
    }
  }

  // determines whether all cells are flooded
  public boolean isAllFlooded() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (!this.cells.get(row).get(col).flooded) {
          return false;
        }
      }
    }
    return true;
  }

  // determines whether all cells are in same color
  public boolean isAllSameColor() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (!this.cells.get(row).get(col).color.equals(this.cells.get(0).get(0).color)) {
          return false;
        }
      }
    }
    return true;
  }

  // restarts the game when click R
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.cells = this.initBoard();
      this.time = 0;
      this.currentTime = 0;
      this.steps = 0;
    }
  }

  @Override
  // Check to see if we need to end the game.
  public WorldEnd worldEnds() {
    if (this.isAllSameColor() && this.isAllFlooded() && this.steps <= TOTAL_STEP) {
      this.gameStatus = 1;
      return new WorldEnd(false, this.makeEndScene());
    }
    else if (!this.isAllSameColor() && this.steps == TOTAL_STEP) {
      this.gameStatus = -1;
      return new WorldEnd(false, this.makeEndScene());
    }

    return new WorldEnd(false, this.makeEndScene());
  }

  // determine whether the scene should be ended
  public WorldScene makeEndScene() {
    WorldScene endScene = this.makeScene();
    return endScene;
  }
}

class FloodItDemo {
  void testGame(Tester t) {
    FloodItWorld g = new FloodItWorld();
    int size = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
    g.bigBang(size, size + 60, FloodItWorld.TICK_RATE);
  }
}

class ExamplesFloodIt {
  Cell cA = new Cell(0, 0);
  Cell cB = new Cell(1, 0);
  Cell cC = new Cell(0, 1);
  Cell cD = new Cell(1, 1);

  FloodItWorld g1 = new FloodItWorld(new ArrayList<ArrayList<Cell>>(Arrays.asList(
      new ArrayList<Cell>(Arrays.asList(cA, cB)), new ArrayList<Cell>(Arrays.asList(cC, cD)))));

  void testGame(Tester t) {
    FloodItWorld g = new FloodItWorld();
    int size = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
    g.bigBang(size, size + 60, FloodItWorld.TICK_RATE);
  }

  void testAddLeft(Tester t) {
    cB.addLeft(cA);
    t.checkExpect(cB.left, cA);
    cD.addLeft(cC);
    t.checkExpect(cD.left, cC);
  }

  void testAddTop(Tester t) {
    cC.addTop(cA);
    t.checkExpect(cC.top, cA);
    cD.addTop(cB);
    t.checkExpect(cD.top, cB);
  }

  void testAddRight(Tester t) {
    cA.addRight(cB);
    t.checkExpect(cA.right, cB);
    cC.addRight(cD);
    t.checkExpect(cC.right, cD);
  }

  void testAddBottom(Tester t) {
    cA.addBottom(cC);
    t.checkExpect(cA.bottom, cC);
    cB.addBottom(cD);
    t.checkExpect(cB.bottom, cD);
  }

  void testDraw(Tester t) {
    t.checkExpect(cA.draw(),
        new RectangleImage(Cell.CELL_SIZE, Cell.CELL_SIZE, OutlineMode.SOLID, cA.color));
  }

  void testDrawAt(Tester t) {
    int size = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
    WorldImage background = new RectangleImage(size, size, OutlineMode.OUTLINE, cA.color);
    t.checkExpect(cA.drawAt(0, 0, background), new OverlayImage(cA.draw(), background
        .movePinholeTo(new Posn(Cell.CELL_SIZE / 2 - size / 2, Cell.CELL_SIZE / 2 - size / 2))));
  }

  void testMakeScene(Tester t) {
    int size = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
    WorldScene scene = new WorldScene(size, size);
    WorldImage background = new RectangleImage(size, size, OutlineMode.SOLID, Color.GRAY);
    background = cA.drawAt(0, 0, background);
    background = cB.drawAt(1, 0, background);
    background = cC.drawAt(0, 1, background);
    background = cD.drawAt(1, 1, background);
    scene.placeImageXY(background, size - Cell.CELL_SIZE / 2, size - Cell.CELL_SIZE / 2);

    t.checkExpect(scene, scene);
  }
}
