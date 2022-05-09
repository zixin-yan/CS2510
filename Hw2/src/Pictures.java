import tester.*;

// to represent a picture
interface IPicture {
  int getWidth();

  int countShapes();

  int comboDepth();

  IPicture mirror();

  String pictureRecipe(int depth);
}

// to represent a shape
class Shape implements IPicture {
  String kind;
  int size;

  Shape(String kind, int size) {
    this.kind = kind;
    this.size = size;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.kind ... -- String
   * ... this.size ... -- int
   * 
   * Methods:
   * ... this.getWidth() ... -- int
   * ... this.countShapes() ... -- int
   * ... this.comboDepth() ... -- int
   * ... this.mirror() ... -- IPicture
   * ... this.pictureRecipe(int) ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // computes the overall width of this picture
  public int getWidth() {
    return this.size;
  }

  // computes the number of single shapes involved in the picture
  public int countShapes() {
    return 1;
  }

  // computes how deeply operations are nested in the construction of this picture
  public int comboDepth() {
    return 0;
  }

  // flips this picture
  public IPicture mirror() {
    return this;
  }

  // produces a string representing the contents of this picture
  public String pictureRecipe(int depth) {
    return this.kind;
  }
}

// to represent a combo of picture
class Combo implements IPicture {
  String name;
  IOperation operation;

  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.operation ... -- IOperation
   * 
   * Methods:
   * ... this.getWidth() ... -- int
   * ... this.countShapes() ... -- int
   * ... this.comboDepth() ... -- int
   * ... this.mirror() ... -- IPicture
   * ... this.pictureRecipe(int) ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // computes the overall width of this picture
  public int getWidth() {
    return this.operation.getWidthHelper();
  }

  // computes the number of single shapes involved in the picture
  public int countShapes() {
    return this.operation.countShapesHelper();
  }

  // computes how deeply operations are nested in the construction of this picture
  public int comboDepth() {
    return this.operation.comboDepthHelper();
  }

  // flips this picture
  public IPicture mirror() {
    return this.operation.mirrorHelper(this.name);
  }

  // produces a string representing the contents of this picture
  public String pictureRecipe(int depth) {
    /* Fields on Parameters:
     * N/A
     */
    if (depth == 0) {
      return this.name;
    }
    else {
      return this.operation.pictureRecipeHelper(depth);
    }
  }
}

interface IOperation {
  int getWidthHelper();

  int countShapesHelper();

  int comboDepthHelper();

  IPicture mirrorHelper(String name);

  String pictureRecipeHelper(int depth);
}

// to represent a scaled picture
class Scale implements IOperation {
  IPicture picture;

  Scale(IPicture picture) {
    this.picture = picture;
  }

  /*
   * TEMPLATE:
   * Fields: ... this.picture1 ... -- IPicture
   * 
   * Methods:
   * ... this.getWidthHelper() ... -- int
   * ... this.countShapesHelper() ... -- int
   * ... this.comboDepthHelper() ... -- int
   * ... this.mirrorHelper() ... -- IPicture
   * ... this.pictureRecipeHelper(int) ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // returns the size of picture after operation
  public int getWidthHelper() {
    return this.picture.getWidth() * 2;
  }

  // returns the total shapes in operations
  public int countShapesHelper() {
    return this.picture.countShapes();
  }

  // returns the depth in one operation
  public int comboDepthHelper() {
    return 1 + this.picture.comboDepth();
  }

  // flips the picture if this using Beside operation
  public IPicture mirrorHelper(String name) {
    /* Fields on Parameters:
     * N/A
     */
    return new Combo(name, new Scale(this.picture.mirror()));
  }

  // expands the operation to show all the operations by depth
  public String pictureRecipeHelper(int depth) {
    /* Fields on Parameters:
     * N/A
     */
    return "scale(" + this.picture.pictureRecipe(depth - 1) + ")";
  }
}

// to represent a picture on left of the other picture
class Beside implements IOperation {
  IPicture picture1;
  IPicture picture2;

  Beside(IPicture picture1, IPicture picture2) {
    this.picture1 = picture1;
    this.picture2 = picture2;
  }

  /*
   * TEMPLATE:
   * Fields: ... this.picture1 ... -- IPicture
   * ... this.picture2 ... -- IPicture
   * 
   * Methods:
   * ... this.getWidthHelper() ... -- int
   * ... this.countShapesHelper() ... -- int
   * ... this.comboDepthHelper() ... -- int
   * ... this.mirrorHelper() ... -- IPicture
   * ... this.pictureRecipeHelper(int) ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // returns the size of picture after operation
  public int getWidthHelper() {
    return this.picture1.getWidth() + this.picture2.getWidth();
  }

  // returns the total shapes in operations
  public int countShapesHelper() {
    return this.picture1.countShapes() + this.picture2.countShapes();
  }

  // returns the depth in one operation
  public int comboDepthHelper() {
    if (this.picture1.comboDepth() > this.picture2.comboDepth()) {
      return 1 + this.picture1.comboDepth();
    }
    else {
      return 1 + this.picture2.comboDepth();
    }
  }

  // flips the picture if this using Beside operation
  public IPicture mirrorHelper(String name) {
    /* Fields on Parameters:
     * N/A
     */
    return new Combo(name, (new Beside(this.picture2.mirror(), this.picture1.mirror())));
  }

  // expands the operation to show all the operations by depth
  public String pictureRecipeHelper(int depth) {
    /* Fields on Parameters:
     * N/A
     */
    return "beside(" + this.picture1.pictureRecipe(depth - 1) + ", "
        + this.picture2.pictureRecipe(depth - 1) + ")";
  }
}

// to represent a picture on top of the other
class Overlay implements IOperation {
  IPicture topPicture;
  IPicture bottomPicture;

  Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.topPicture ... -- IPicture
   * ... this.bottomPicture ... -- IPicture
   * 
   * Methods:
   * ... this.getWidthHelper() ... -- int
   * ... this.countShapesHelper() ... -- int
   * ... this.comboDepthHelper() ... -- int
   * ... this.mirrorHelper() ... -- IPicture
   * ... this.pictureRecipeHelper(int) ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // returns the size of picture after operation
  public int getWidthHelper() {
    if (this.topPicture.getWidth() >= this.bottomPicture.getWidth()) {
      return this.topPicture.getWidth();
    }
    else {
      return this.bottomPicture.getWidth();
    }
  }

  // returns the total shapes in operations
  public int countShapesHelper() {
    return this.topPicture.countShapes() + this.bottomPicture.countShapes();
  }

  // returns the depth in one operation
  public int comboDepthHelper() {
    if (this.topPicture.comboDepth() > this.bottomPicture.comboDepth()) {
      return 1 + this.topPicture.comboDepth();
    }
    else {
      return 1 + this.bottomPicture.comboDepth();
    }
  }

  // flips the picture if this using Beside operation
  public IPicture mirrorHelper(String name) {
    /* Fields on Parameters:
     * N/A
     */
    return new Combo(name, new Overlay(this.topPicture.mirror(), this.bottomPicture.mirror()));
  }

  // expands the operation to show all the operations by depth
  public String pictureRecipeHelper(int depth) {
    /* Fields on Parameters:
     * N/A
     */
    return "overlay(" + this.topPicture.pictureRecipe(depth - 1) + ", "
        + this.bottomPicture.pictureRecipe(depth - 1) + ")";
  }
}

class ExamplesPicture {
  Shape circle = new Shape("circle", 20);
  Shape square = new Shape("square", 30);
  Combo bigCircle = new Combo("big circle", new Scale(circle));
  Combo squareOnCircle = new Combo("square on circle", new Overlay(square, bigCircle));
  Combo doubledSquareOnCircle = new Combo("doubled square on circle",
      new Beside(squareOnCircle, squareOnCircle));
  Combo doubledSquare = new Combo("doubled square", new Beside(square, square));
  Combo circleOnSquareLeft = new Combo("circle on square left", new Beside(circle, square));

  boolean testGetWidth(Tester t) {
    return t.checkExpect(circle.getWidth(), 20) && t.checkExpect(bigCircle.getWidth(), 40)
        && t.checkExpect(squareOnCircle.getWidth(), 40)
        && t.checkExpect(doubledSquareOnCircle.getWidth(), 80);
  }

  boolean testCountShapes(Tester t) {
    return t.checkExpect(circle.countShapes(), 1) && t.checkExpect(bigCircle.countShapes(), 1)
        && t.checkExpect(squareOnCircle.countShapes(), 2)
        && t.checkExpect(doubledSquareOnCircle.countShapes(), 4);
  }

  boolean testComboDepth(Tester t) {
    return t.checkExpect(circle.comboDepth(), 0) && t.checkExpect(bigCircle.comboDepth(), 1)
        && t.checkExpect(squareOnCircle.comboDepth(), 2)
        && t.checkExpect(doubledSquareOnCircle.comboDepth(), 3);
  }

  boolean testMirror(Tester t) {
    return t.checkExpect(circle.mirror(), circle) && t.checkExpect(bigCircle.mirror(), bigCircle)
        && t.checkExpect(squareOnCircle.mirror(), squareOnCircle)
        && t.checkExpect(doubledSquareOnCircle.mirror(), doubledSquareOnCircle)
        && t.checkExpect(circleOnSquareLeft.mirror(),
            new Combo("circle on square left", new Beside(square, circle)));
  }

  boolean testPictureRecipe(Tester t) {
    return t.checkExpect(doubledSquareOnCircle.pictureRecipe(0), "doubled square on circle")
        && t.checkExpect(doubledSquareOnCircle.pictureRecipe(1),
            "beside(square on circle, square on circle)")
        && t.checkExpect(doubledSquareOnCircle.pictureRecipe(2),
            "beside(overlay(square, big circle), overlay(square, big circle))")
        && t.checkExpect(doubledSquareOnCircle.pictureRecipe(3),
            "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))")
        && t.checkExpect(doubledSquareOnCircle.pictureRecipe(4),
            "beside(overlay(square, scale(circle)), overlay(square, scale(circle)))");
  }
}