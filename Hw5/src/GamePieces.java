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
  MyPosn addPosn(MyPosn that) {
    return new MyPosn(this.x + that.x, this.y + that.y);
  }

  // determines whether the ship or bullets is out of screen
  boolean isOffScreen(int width, int height) {
    // more space for ships' spawn
    return this.x > width || this.y > height || this.x < 0 || this.y < 0;
  }
}

class Circle {
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  int radius;
  Color color;

  Circle(MyPosn position, MyPosn velocity, int radius, Color color) {
    this.position = position;
    this.velocity = velocity;
    this.radius = radius;
    this.color = color;
  }
  
  public Circle move() {
    return this.move();
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
    return Math.hypot(this.position.x - that.position.x, this.position.y - that.position.y)
        <= this.radius + that.radius;
  }
}

class Ship extends Circle {
  Ship(MyPosn position, MyPosn velocity) {
    super(position, velocity, 10, Color.blue);
  }

  // moves the ship by its speed
  public Ship move() {
    return new Ship(this.position.addPosn(this.velocity), this.velocity);
  }
}

class Bullet extends Circle {
  int tier;

  Bullet(MyPosn position, MyPosn velocity, int tier) {
    super(position, velocity, tier+1, Color.pink);
    this.tier = tier;
    
    if (this.radius > 10) {
      this.radius = 10;
    }
  }

  // moves the bullet by its speed
  public Bullet move() {
    return new Bullet(this.position.addPosn(this.velocity), this.velocity, this.tier);
  }
}

class ExamplesGamePieces {
  MyPosn posn1 = new MyPosn(100, 100);
  MyPosn posn2 = new MyPosn(posn1.addPosn(posn1));
  MyPosn outScreen = new MyPosn(600, 100);

  Ship ship = new Ship(posn1, posn1);
  Ship movedShip = new Ship(this.posn2, this.posn1);
  Bullet bullet = new Bullet(posn1, posn1, 0);

  // tests method isOffScreen() of MyPosn
  boolean testIsOffScreen(Tester t) {
    return t.checkExpect(this.outScreen.isOffScreen(500, 300), true)
        && t.checkExpect(this.posn1.isOffScreen(500, 300), false);
  }

  // tests method addPosn() of MyPosn
  boolean testAddPosn(Tester t) {
    return t.checkExpect(this.posn1.addPosn(this.posn1), this.posn2);
  }

  // tests method move() of Circle
  boolean testMove(Tester t) {
    return t.checkExpect(this.ship.move(), new Ship(this.posn2, this.posn1))
        && t.checkExpect(this.bullet.move(), new Bullet(this.posn2, this.posn1, 0));
  }
  
  // tests method touching() of Circle
  boolean testTouching(Tester t) {
    return t.checkExpect(this.ship.touching(this.ship), true)
        && t.checkExpect(this.ship.touching(this.movedShip), false);
  }
}