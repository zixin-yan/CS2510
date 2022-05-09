import java.awt.Color;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.*;

class MyPosn extends Posn {
  MyPosn(int x, int y) {
    super(x, y);
  }

  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // adds two posn into one
  MyPosn myAddPosn(DoublePosn that) {
    return new MyPosn((int) (this.x + that.x), (int) (this.y + that.y));
  }

  // determines whether the ship or bullets is out of screen
  boolean isOffScreen(int width, int height) {
    // more space for ships' spawn
    return this.x > width || this.y > height || this.x < 0 || this.y < 0;
  }
}

class DoublePosn {
  double x;
  double y;

  DoublePosn(double x, double y) {
    this.x = x;
    this.y = y;
  }
}

class Circle {
  MyPosn position; // in pixels
  DoublePosn velocity; // in pixels/tick
  int radius;
  Color color;

  Circle(MyPosn position, DoublePosn velocity, int radius, Color color) {
    this.position = position;
    this.velocity = velocity;
    this.radius = radius;
    this.color = color;
  }

  // moves this circle
  public Circle move() {
    return new Circle(this.position.myAddPosn(this.velocity), this.velocity, this.radius,
        this.color);
  }

  // draws a circle
  WorldScene addCircleToScene(WorldScene scene) {
    return scene.placeImageXY(new CircleImage(this.radius, OutlineMode.SOLID, this.color),
        position.x, position.y);
  }

  boolean isOffScreen(int width, int height) {
    // more space for ships' spawn
    return this.position.x > width + this.radius || this.position.y > height + this.radius
        || this.position.x < 0 - this.radius || this.position.y < 0 - this.radius;
  }

  boolean touching(Circle that) {
    return Math.hypot(this.position.x - that.position.x,
        this.position.y - that.position.y) <= this.radius + that.radius;
  }
}

class Ship extends Circle {
  Ship(MyPosn position, DoublePosn velocity) {
    super(position, velocity, 10, Color.blue);
  }

  // moves the ship by its speed
  public Ship move() {
    return new Ship(this.position.myAddPosn(this.velocity), this.velocity);
  }
}

class Bullet extends Circle {
  int tier;

  Bullet(MyPosn position, DoublePosn velocity, int tier) {
    super(position, velocity, 5, Color.pink);
    this.tier = tier;
  }

  // moves the bullet by its speed
  public Bullet move() {
    return new Bullet(this.position.myAddPosn(this.velocity), this.velocity, this.tier);
  }
}

class ExamplesGamePieces {
  MyPosn posn1 = new MyPosn(100, 100);
  DoublePosn dposn1 = new DoublePosn(100.0, 100.0);
  MyPosn posn2 = new MyPosn(posn1.myAddPosn(dposn1));
  MyPosn outScreen = new MyPosn(600, 100);

  Ship ship = new Ship(posn1, dposn1);
  Ship movedShip = new Ship(this.posn2, this.dposn1);
  Bullet bullet = new Bullet(posn1, dposn1, 0);

  // tests method isOffScreen() of MyPosn
  boolean testIsOffScreen(Tester t) {
    return t.checkExpect(this.outScreen.isOffScreen(500, 300), true)
        && t.checkExpect(this.posn1.isOffScreen(500, 300), false);
  }

  // tests method addPosn() of MyPosn
  boolean testAddPosn(Tester t) {
    return t.checkExpect(this.posn1.myAddPosn(this.dposn1), this.posn2);
  }

  // tests method move() of Circle
  boolean testMove(Tester t) {
    return t.checkExpect(this.ship.move(), new Ship(this.posn2, this.dposn1))
        && t.checkExpect(this.bullet.move(), new Bullet(this.posn2, this.dposn1, 0));
  }

  // tests method touching() of Circle
  boolean testTouching(Tester t) {
    return t.checkExpect(this.ship.touching(this.ship), true)
        && t.checkExpect(this.ship.touching(this.movedShip), false);
  }
}