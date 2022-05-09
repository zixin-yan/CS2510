import java.util.Random;
import javalib.funworld.WorldScene;
import tester.*;

interface ILoCircle {
  // moves all circle's position by its speed
  ILoCircle moveAll();

  // determines whether the circle is out of screen
  boolean isOffscreen(int width, int height);

  // removes all circles which is out of screen
  ILoCircle removeOffScreen(int width, int height);

  // adds LoCircle to the world scene
  WorldScene addLoCirclesToScene(WorldScene scene);

  // returns the length of the list of circles
  int length();

  // adds circle to the list
  ILoCircle add(Circle circle);

  // spawns a new ship with random position and direction
  ILoCircle spawn();
  
  // removes the circle that collide with other
  ILoCircle collideHelper(Circle that);
  
  // removes all the collided circle with another list
  ILoCircle collide(ILoCircle other);
  
  // counts the number of circle collides
  int countCollision(ILoCircle other);
  
  // helper method of countCollision
  int countCollisionHelper(Circle that);
}

class MtLoCircle implements ILoCircle {
  MtLoCircle() {
  }

  // moves all circle's position by its speed
  public ILoCircle moveAll() {
    return this;
  }

  // determines whether the circle is out of screen
  public boolean isOffscreen(int width, int hieght) {
    return false;
  }

  // removes all circles which is out of screen
  public ILoCircle removeOffScreen(int width, int height) {
    return this;
  }

  // adds LoCircle to the world scene
  public WorldScene addLoCirclesToScene(WorldScene scene) {
    return scene;
  }

  // returns the length of the list of circles
  public int length() {
    return 0;
  }

  // adds circle to the list
  public ILoCircle add(Circle circle) {
    return new ConsLoCircle(circle, this);
  }

  // spawns a new ship with random position and direction
  public ILoCircle spawn() {
    ILoCircle ships = this;
    int numSpawn = (new Random()).nextInt(3);
    for (int i = 0; i <= numSpawn; i++) {
      Random r = new Random();
      int direction = r.nextInt(2);
      // speed of ship is fixed to 7
      // 0 is left to right, 1 is right to left
      if (direction == 1) {
        MyPosn position = new MyPosn(509, (r.nextInt(225) + 25));
        MyPosn speed = new MyPosn(-7, 0);
        ships = ships.add(new Ship(position, speed));
      }
      else {
        MyPosn position = new MyPosn(-9, (r.nextInt(225) + 25));
        MyPosn speed = new MyPosn(7, 0);
        ships = ships.add(new Ship(position, speed));
      }
    }
    return ships;
  }
  
  // remove the circle that collide with other
  public ILoCircle collideHelper(Circle that) {
    return this;
  }

  // removes all the collided circle from other list
  // returns other list after collided
  public ILoCircle collide(ILoCircle other) {
    return other;
  }

  // counts the number of circle collides
  public int countCollision(ILoCircle other) {
    return 0;
  }
  
  // helper method of countCollision
  public int countCollisionHelper(Circle that) {
    return 0;
  }
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }

  // moves all circle's position by its speed
  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
  }

  // determines whether the circle is out of screen
  public boolean isOffscreen(int width, int height) {
    return this.first.position.isOffScreen(width, height) || this.rest.isOffscreen(width, height);
  }

  // removes all circles which is out of screen
  public ILoCircle removeOffScreen(int width, int height) {
    if (this.first.isOffScreen(width, height)) {
      return this.rest.removeOffScreen(width, height);
    }
    else {
      return new ConsLoCircle(this.first, this.rest.removeOffScreen(width, height));
    }
  }

  // adds LoCircle to the world scene
  public WorldScene addLoCirclesToScene(WorldScene scene) {
    return this.first.addCircleToScene(this.rest.addLoCirclesToScene(scene));
  }

  // returns the length of the list of circles
  public int length() {
    return 1 + this.rest.length();
  }

  // adds circle to the list
  public ILoCircle add(Circle circle) {
    return new ConsLoCircle(circle, this);
  }

  // spawns a new ship with random position and direction
  public ILoCircle spawn() {
    ILoCircle ships = this;
    int numSpawn = (new Random()).nextInt(3);
    for (int i = 0; i <= numSpawn; i++) {
      Random r = new Random();
      int direction = r.nextInt(2);
      // speed of ship is fixed to 7
      // 0 is left to right, 1 is right to left
      if (direction == 1) {
        MyPosn position = new MyPosn(509, (r.nextInt(225) + 25));
        MyPosn speed = new MyPosn(-7, 0);
        ships = ships.add(new Ship(position, speed));
      }
      else {
        MyPosn position = new MyPosn(-9, (r.nextInt(225) + 25));
        MyPosn speed = new MyPosn(7, 0);
        ships = ships.add(new Ship(position, speed));
      }
    }
    return ships;
  }
  
  // remove the circle that collide with other
  public ILoCircle collideHelper(Circle that) {
    if (this.first.touching(that)) {
      return this.rest.collideHelper(that);
    }else {
      return new ConsLoCircle(this.first, this.rest.collideHelper(that));
    }
  }
  
  // removes all the collided circle from other list
  // returns other list after collided
  public ILoCircle collide(ILoCircle other) {
    return this.rest.collide(other.collideHelper(this.first));
  }

  // counts the number of circle collides
  public int countCollision(ILoCircle other) {
    return this.rest.countCollision(other.collideHelper(this.first)) + other.countCollisionHelper(this.first);
  }
  
  // helper method of countCollision
  public int countCollisionHelper(Circle that) {
    if (this.first.touching(that)) {
      return 1 + this.rest.countCollisionHelper(that);
    }else {
      return this.rest.countCollisionHelper(that);
    }
  }
}

class ExamplesCircle {

  MyPosn pos = new MyPosn(100, 100);
  MyPosn outScreenPos = new MyPosn(600, 100);
  MyPosn speed = new MyPosn(0, 20);

  Circle ship = new Ship(pos, speed);
  Circle outScreenShip = new Ship(this.outScreenPos, this.speed);

  ILoCircle mtList = new MtLoCircle();
  ILoCircle outScreenCircles = new ConsLoCircle(this.outScreenShip,
      new ConsLoCircle(this.ship, new MtLoCircle()));
  ILoCircle circles = new ConsLoCircle(this.ship,
      new ConsLoCircle(this.ship, new ConsLoCircle(this.ship, this.mtList)));
  ILoCircle movedCircles = new ConsLoCircle(this.ship.move(),
      new ConsLoCircle(this.ship.move(), new ConsLoCircle(this.ship.move(), this.mtList)));

  // test method moveAll() of list of circles
  boolean testMoveAll(Tester t) {
    return t.checkExpect(this.mtList.moveAll(), this.mtList)
        && t.checkExpect(this.circles.moveAll(), this.movedCircles);
  }

  // test method isOffScreen() of list of circles
  boolean testIsOffscreen(Tester t) {
    return t.checkExpect(this.circles.isOffscreen(500, 300), false)
        && t.checkExpect(this.outScreenCircles.isOffscreen(500, 300), true)
        && t.checkExpect(this.mtList.isOffscreen(500, 300), false);
  }

  // test method removeOffScreen() of list of circles
  boolean testRemoveOffScreen(Tester t) {
    return t.checkExpect(this.mtList.removeOffScreen(500, 300), this.mtList)
        && t.checkExpect(this.circles.removeOffScreen(500, 300), this.circles)
        && t.checkExpect(this.outScreenCircles.removeOffScreen(500, 300),
            new ConsLoCircle(this.ship, new MtLoCircle()));
  }

  // test method length() of list of circles
  boolean testLength(Tester t) {
    return t.checkExpect(this.mtList.length(), 0) && t.checkExpect(this.circles.length(), 3);
  }

  // test method add() of list of circles
  boolean testAdd(Tester t) {
    return t.checkExpect(this.mtList.add(this.ship), new ConsLoCircle(this.ship, new MtLoCircle()))
        && t.checkExpect(this.outScreenCircles.add(this.ship), new ConsLoCircle(this.ship,
            new ConsLoCircle(this.outScreenShip, new ConsLoCircle(this.ship, new MtLoCircle()))));
  } 
  
  // tests collideHelper() of LoCircle
  boolean testcollideHelper(Tester t) {
    return t.checkExpect(this.outScreenCircles.collideHelper(this.ship), new ConsLoCircle(this.outScreenShip,new MtLoCircle()))
        && t.checkExpect(this.mtList.collideHelper(this.ship), this.mtList);
  }
  
  // tests collide() method of LoCircle
  boolean testcollide(Tester t) {
    return t.checkExpect(this.outScreenCircles.collide(this.circles), this.mtList)
        && t.checkExpect(this.circles.collide(this.outScreenCircles), new ConsLoCircle(this.outScreenShip,new MtLoCircle()))
        && t.checkExpect(this.mtList.collide(this.outScreenCircles), this.outScreenCircles);
  }
  
  // test countCollision() of LoCircle
  boolean testCountCollision(Tester t) {
    return t.checkExpect(this.outScreenCircles.countCollision(this.circles), 3)
        && t.checkExpect(this.mtList.countCollision(this.outScreenCircles), 0);
  }

  // test countCollisionHelper() of LoCircle
  boolean testCountCollisionHelper(Tester t) {
    return t.checkExpect(this.mtList.countCollisionHelper(this.ship), 0)
        && t.checkExpect(this.circles.countCollisionHelper(this.ship), 3)
        && t.checkExpect(this.circles.countCollisionHelper(this.outScreenShip),0);
  }
}