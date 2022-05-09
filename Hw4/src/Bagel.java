import tester.*;

class BagelRecipe {
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;

  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    this.flour = flour;
    this.water = (new Utils()).checkPerfectWater(water, flour);
    this.yeast = (new Utils()).checkPerfectYeast(flour, yeast);
    this.salt = (new Utils()).checkPerfectSalt(flour, yeast, salt);
    this.malt = (new Utils()).checkPerfectMalt(malt, yeast);
  }

  BagelRecipe(double flour, double yeast) {
    this.water = flour;
    this.flour = flour;
    this.yeast = (new Utils()).checkPerfectYeast(flour, yeast);
    this.salt = (flour * 0.05) - yeast;
    this.malt = yeast;
  }

  BagelRecipe(double flour, double yeast, double salt) {
    this.flour = flour * 4.25;
    this.water = this.flour;
    this.yeast = (new Utils()).checkPerfectYeast(this.flour, ((yeast / 48) * 5));
    this.salt = (new Utils()).checkPerfectSalt(this.flour, this.yeast, (salt / 48) * 10);
    this.malt = this.yeast;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.flour ... -- double
   * ... this.water ... -- double
   * ... this.yeast ... -- double
   * ... this.salt ... -- double
   * ... this.malt ... -- double
   * 
   * Methods:
   * ... this.sameRecipe(BagelRecipe other) ... -- boolean
   * 
   * Methods for Fields:
   * 
   */

  boolean sameRecipe(BagelRecipe other) {
    return Math.abs(this.flour - other.flour) < 0.001 && Math.abs(this.water - other.water) < 0.001
        && Math.abs(this.yeast - other.yeast) < 0.001 && Math.abs(this.salt - other.salt) < 0.001
        && Math.abs(this.malt - other.malt) < 0.001;
  }
}

class Utils {
  Utils() {
  }

  /*
   * TEMPLATE:
   * Fields:
   * 
   * Methods:
   * ... this.checkPerfectWater(double water, double flour) ... -- double
   * ... this.checkPerfectSalt(double flour, double yeast, double salt) ... --
   * double
   * ... this.checkPerfectMalt(double malt, double yeast) ... -- double
   * ... this.checkPerfectYeast(double flour, double yeast) ... -- double
   * 
   * Methods for Fields:
   * 
   */

  // checks whether the water in recipe is perfect
  double checkPerfectWater(double water, double flour) {
    if (Math.abs(flour - water) < 0.001) {
      return water;
    }
    else {
      throw new IllegalArgumentException("Invalid amounts: Water and flour are not equal.");
    }
  }

  // checks whether the salt in recipe is perfect
  double checkPerfectSalt(double flour, double yeast, double salt) {
    if (Math.abs((yeast + salt) * 20 - flour) < 0.001) {
      return salt;
    }
    else {
      throw new IllegalArgumentException(
          "Invalid amounts: Salt and yeast must be 1/20 of the flour.");
    }
  }

  // checks whether the malt in recipe is perfect
  double checkPerfectMalt(double malt, double yeast) {
    if (Math.abs(yeast - malt) < 0.001) {
      return malt;
    }
    else {
      throw new IllegalArgumentException("Invalid amounts: Yeast and malt are not equal.");
    }
  }

  // checks whether the yeast in recipe is perfect
  double checkPerfectYeast(double flour, double yeast) {
    if (yeast < 0.05 * flour) {
      return yeast;
    }
    else {
      throw new IllegalArgumentException("Invalid amounts: Too much yeast.");
    }
  }
}

class ExamplesBagelRecipes {
  BagelRecipe everything = new BagelRecipe(20, 20, 0.5, 0.5, 0.5);
  BagelRecipe plain = new BagelRecipe(30, 1);
  BagelRecipe cinnamonRasin = new BagelRecipe(58.8235, 24, 48);

  // test the constructors for BagelRecipe
  boolean testCheckConstructorException(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid amounts: Water and flour are not equal."),
        "BagelRecipe", 20.0, 19.0, 0.5, 0.5, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid amounts: Too much yeast."), "BagelRecipe", 20.0,
            20.0, 1.0, 0.5, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException(
                "Invalid amounts: Salt and yeast must be 1/20 of the flour."),
            "BagelRecipe", 20.0, 20.0, 0.5, 1.0, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid amounts: Yeast and malt are not equal."),
            "BagelRecipe", 20.0, 20.0, 0.5, 0.5, 1.0)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid amounts: Too much yeast."), "BagelRecipe", 20.0,
            1.0)
        && t.checkConstructorException(
            new IllegalArgumentException("Invalid amounts: Too much yeast."), "BagelRecipe",
            20 / 4.25, 100.0, 0.5)
        && t.checkConstructorException(
            new IllegalArgumentException(
                "Invalid amounts: Salt and yeast must be 1/20 of the flour."),
            "BagelRecipe", 20 / 4.25, 9.4, 0.5);
  }

  // test the method sameRecipe for BagelRecipe
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(this.plain.sameRecipe(cinnamonRasin), false)
        && t.checkExpect(this.everything.sameRecipe(everything), true)
        && t.checkExpect(this.cinnamonRasin.sameRecipe(new BagelRecipe(249.999, 2.5)), true);
  }

  boolean testCheckPerfectWater(Tester t) {
    return t.checkException(
        new IllegalArgumentException("Invalid amounts: Water and flour are not equal."),
        (new Utils()), "checkPerfectWater", 15.0, 20.0)
        && t.checkExpect((new Utils()).checkPerfectWater(15, 15), 15.0);
  }

  boolean testCheckPerfectSalt(Tester t) {
    return t.checkException(
        new IllegalArgumentException("Invalid amounts: Salt and yeast must be 1/20 of the flour."),
        (new Utils()), "checkPerfectSalt", 20.0, 1.0, 0.5)
        && t.checkExpect((new Utils()).checkPerfectSalt(20.0, 0.5, 0.5), 0.5);
  }

  boolean testCheckPerfectMalt(Tester t) {
    return t.checkException(
        new IllegalArgumentException("Invalid amounts: Yeast and malt are not equal."),
        (new Utils()), "checkPerfectMalt", 1.0, 0.5)
        && t.checkExpect((new Utils()).checkPerfectMalt(0.5, 0.5), 0.5);
  }

  boolean testCheckPerfectYeast(Tester t) {
    return t.checkException(new IllegalArgumentException("Invalid amounts: Too much yeast."),
        (new Utils()), "checkPerfectYeast", 20.0, 1.0)
        && t.checkExpect((new Utils()).checkPerfectYeast(20.0, 0.5), 0.5);
  }
}