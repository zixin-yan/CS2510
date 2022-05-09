import java.awt.Color;
import javalib.funworld.*;
import javalib.worldimages.*;
import tester.*;

class MyGame extends World {
  int width;
  int height;
  int nBullets;
  int shipsDestroyed;
  ILoCircle ships;
  ILoCircle bullets;
  int currentTick;

  MyGame(int nBullets) {
    if (nBullets < 0) {
      throw new IllegalArgumentException("Invalid bullets: bullets cannot less than 0");
    }
    this.width = 500;
    this.height = 300;
    this.nBullets = nBullets;
    this.shipsDestroyed = 0;
    this.ships = new MtLoCircle();
    this.bullets = new MtLoCircle();
    this.currentTick = 0;
  }

  MyGame(int nBullets, int shipsDestroyed, ILoCircle ships, ILoCircle bullets, int currentTick) {
    if (nBullets < 0 || shipsDestroyed < 0) {
      throw new IllegalArgumentException("Invalid bullets: bullets cannot less than 0");
    }
    this.width = 500;
    this.height = 300;
    this.nBullets = nBullets;
    this.shipsDestroyed = shipsDestroyed;
    this.ships = ships;
    this.bullets = bullets;
    this.currentTick = currentTick;
  }

  @Override
  // Make a new scene.
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width, this.height);

    scene = this.addInfoToScene(this.addBulletsToScene(scene));

    scene = this.addInfoToScene(this.addShipsToScene(scene));

    return scene;
  }

  // adds the ship to the scene
  WorldScene addShipsToScene(WorldScene scene) {
    return this.ships.addLoCirclesToScene(scene);
  }

  // adds the bullet to the scene
  WorldScene addBulletsToScene(WorldScene scene) {
    return this.bullets.addLoCirclesToScene(scene);
  }

  // adds the information panel to the scene
  WorldScene addInfoToScene(WorldScene scene) {
    return scene.placeImageXY(new TextImage("Bullets: " + Integer.toString(this.nBullets)
        + "  Score: " + Integer.toString(this.shipsDestroyed) + " Rest bullets: "
        + Integer.toString(this.bullets.length()), Color.black), 100, 20);
  }

  // This method gets called every tickrate seconds ( see bellow in example
  // class).
  public MyGame onTick() {
    ILoCircle tempShip = this.ships;
    ILoCircle tempBullet = this.bullets;
    this.bullets = tempShip.collideExplode(tempBullet);
    this.ships = tempBullet.collide(tempShip);

    this.shipsDestroyed += tempBullet.countCollision(tempShip);

    if (this.currentTick % 28 == 0) {
      return new MyGame(this.nBullets, this.shipsDestroyed,
          this.ships.spawn().moveAll().removeOffScreen(width, height),
          this.bullets.moveAll().removeOffScreen(width, height), this.currentTick + 1);
    }
    else {
      return new MyGame(this.nBullets, this.shipsDestroyed,
          this.ships.moveAll().removeOffScreen(width, height),
          this.bullets.moveAll().removeOffScreen(width, height), this.currentTick + 1);
    }
  }

  // returns a new status of game when press space
  public MyGame onKeyEvent(String key) {

    // did we press the space update the final tick of the game by 10.
    if (key.equals(" ") && this.nBullets > 0) {
      return new MyGame(this.nBullets - 1, this.shipsDestroyed, this.ships,
          new ConsLoCircle(new Bullet(new MyPosn(250, 300), new DoublePosn(0.0, -10.0), 1),
              this.bullets),
          this.currentTick);
    }
    else {
      return this;
    }
  }

  @Override
  // Check to see if we need to end the game.
  public WorldEnd worldEnds() {
    if (this.nBullets == 0 && this.bullets.length() < 1) {
      return new WorldEnd(true, this.makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // determine whether the scene should be ended
  public WorldScene makeEndScene() {
    WorldScene endScene = this.makeScene();
    return endScene.placeImageXY(new TextImage("Game Over", Color.red), 250, 250);
  }
}

class ExamplesMyWorldProgram {
  boolean testBigBang(Tester t) {
    MyGame world = new MyGame(10);
    // width, height, tick rate = 0.5 means every 0.5 seconds the onTick method will
    // get called.
    return world.bigBang(500, 300, 1.0 / 28.0);
  }
}
