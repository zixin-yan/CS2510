import tester.*;

// to represent a EmbroideryPiece 
class EmbroideryPiece {
  String name;
  IMotif motif;

  EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.motif ... -- Motif
   * 
   * Methods:
   * ... this.averageDifficulty() ... -- double
   * ... this.embroideryInfo() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // computes the average difficulty of all of the cross-stitch and chain-stitch
  // motifs
  double averageDifficulty() {
    if (this.motif.averageDiffcultyCounterHelper() == 0) {
      return 0.0;
    }
    else {
      return this.motif.averageDifficultyAdderHelper() / this.motif.averageDiffcultyCounterHelper();
    }
  }

  // produces all names of cross-stitch and chain-stitch motifs
  String embroideryInfo() {
    return this.name.concat(": ") + this.motif.embroideryInfoHelper() + ".";
  }
}

// to represent the motif
interface IMotif {
  double averageDifficultyAdderHelper();

  int averageDiffcultyCounterHelper();

  String embroideryInfoHelper();
}

// to represent a cross stitch motif
class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  CrossStitchMotif(String description, Double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.description ... -- String
   * ... this.difficulty ... -- double
   * 
   * Methods:
   * ... this.averageDifficultyAdderHelper() ... -- double
   * ... this.averageDiffcultyCounterHelper() ... -- int
   * ... this.embroideryInfoHelper() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // helper method for averageDifficultyAdder
  public double averageDifficultyAdderHelper() {
    return this.difficulty;
  }

  // counts the number of cross-stitch and chain-stitch
  public int averageDiffcultyCounterHelper() {
    return 1;
  }

  // helper method for embroideryInfo
  public String embroideryInfoHelper() {
    return this.description.concat(" (cross stitch)");
  }
}

// to represent a chain stitch motif
class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  ChainStitchMotif(String description, Double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.description ... -- String
   * ... this.difficulty ... -- double
   * 
   * Methods:
   * ... this.averageDifficultyAdderHelper() ... -- double
   * ... this.averageDiffcultyCounterHelper() ... -- int
   * ... this.embroideryInfoHelper() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // helper method for averageDifficultyAdder
  public double averageDifficultyAdderHelper() {
    return this.difficulty;
  }

  // counts the number of cross-stitch and chain-stitch
  public int averageDiffcultyCounterHelper() {
    return 1;
  }

  // helper method for embroideryInfo
  public String embroideryInfoHelper() {
    return this.description.concat(" (chain stitch)");
  }
}

// to represent a group of motif
class GroupMotif implements IMotif {
  String description;
  ILoMotif motifs;

  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.description ... -- String
   * ... this.motifs ... -- ILoMotif
   * 
   * Methods:
   * ... this.averageDifficultyAdderHelper() ... -- double
   * ... this.averageDiffcultyCounterHelper() ... -- int
   * ... this.embroideryInfoHelper() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // helper method for averageDifficultyAdder
  public double averageDifficultyAdderHelper() {
    return this.motifs.averageDifficultyAdder();
  }

  // counts the number of cross-stitch and chain-stitch
  public int averageDiffcultyCounterHelper() {
    return this.motifs.averageDiffcultyCounter();
  }

  // helper method for embroideryInfo
  public String embroideryInfoHelper() {
    return this.motifs.embroideryInfoLoMotifHelper();
  }
}

// to represent a list of motif
interface ILoMotif {
  boolean emptyList();

  double averageDifficultyAdder();

  int averageDiffcultyCounter();

  String embroideryInfoLoMotifHelper();
}

// to represent an empty list of motif
class MtLoMotif implements ILoMotif {
  MtLoMotif() {
  }

  /*
   * TEMPLATE:
   * Fields:
   * 
   * Methods:
   * ... this.emptyList() ... -- boolean
   * ... this.averageDifficultyAdder() ... -- double
   * ... this.averageDiffcultyCounter() ... -- int
   * ... this.embroideryInfoLoMotifHelper() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // determines whether this ILoMotif is an empty list
  public boolean emptyList() {
    return true;
  }

  // sum all the difficulty in two types stitch
  public double averageDifficultyAdder() {
    return 0;
  }

  // counts the number of two types stitch
  public int averageDiffcultyCounter() {
    return 0;
  }

  // determines whether there is comma after the description
  public String embroideryInfoLoMotifHelper() {
    return "";
  }
}

// to represent the construct of a list of motif
class ConsLoMotif implements ILoMotif {
  IMotif first;
  ILoMotif rest;

  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE:
   * Fields: ... this.first ... -- Motif
   * ... this.rest ... -- ILoMotif
   * 
   * Methods:
   * ... this.emptyList() ... -- boolean
   * ... this.averageDifficultyAdder() ... -- double
   * ... this.averageDiffcultyCounter() ... -- int
   * ... this.embroideryInfoLoMotifHelper() ... -- String
   * 
   * Methods for Fields:
   * N/A
   */

  // determines whether this ILoMotif is an empty list
  public boolean emptyList() {
    return false;
  }

  // sum all the difficulty in two types stitch
  public double averageDifficultyAdder() {
    return this.first.averageDifficultyAdderHelper() + this.rest.averageDifficultyAdder();
  }

  // counts the number of two types stitch
  public int averageDiffcultyCounter() {
    return this.first.averageDiffcultyCounterHelper() + this.rest.averageDiffcultyCounter();
  }

  // determines whether there is comma after the description
  public String embroideryInfoLoMotifHelper() {
    if (this.rest.emptyList()) {
      return this.first.embroideryInfoHelper();
    }
    else {
      return this.first.embroideryInfoHelper() + ", " + this.rest.embroideryInfoLoMotifHelper();
    }
  }
}

class ExamplesEmbroidery {
  CrossStitchMotif rose = new CrossStitchMotif("rose", 5.0);
  ChainStitchMotif poppy = new ChainStitchMotif("poppy", 4.75);
  CrossStitchMotif daisy = new CrossStitchMotif("daisy", 3.2);
  GroupMotif flowers = new GroupMotif("flowers",
      new ConsLoMotif(rose, new ConsLoMotif(poppy, new ConsLoMotif(daisy, new MtLoMotif()))));

  CrossStitchMotif bird = new CrossStitchMotif("bird", 4.5);
  ChainStitchMotif tree = new ChainStitchMotif("tree", 3.0);
  GroupMotif nature = new GroupMotif("nature",
      new ConsLoMotif(bird, new ConsLoMotif(tree, new ConsLoMotif(flowers, new MtLoMotif()))));

  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", nature);

  boolean testAverageDifficulty(Tester t) {
    return t.checkInexact(pillowCover.averageDifficulty(), 4.09, 0.00001);
  }

  boolean testEmbroideryInfo(Tester t) {
    return t.checkExpect(pillowCover.embroideryInfo(),
        "Pillow Cover: bird (cross stitch), tree (chain stitch), "
            + "rose (cross stitch), poppy (chain stitch), daisy (cross stitch).");
  }
}
