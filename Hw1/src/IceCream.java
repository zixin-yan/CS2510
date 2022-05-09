// to represent an IceCream
interface IIceCream {
}

// to represent whether an IceCream is emptyserving
class EmptyServing implements IIceCream {
  boolean cone;

  EmptyServing(boolean cone) {
    this.cone = cone;
  }
}

// to represent how an IceCream to be scooped
class Scooped implements IIceCream {
  IIceCream more;
  String flavor;

  Scooped(IIceCream more, String flavor) {
    this.more = more;
    this.flavor = flavor;
  }
}

// to represent the example IceCream
class ExamplesIceCream {

  IIceCream order1 = new Scooped(
      new Scooped(new Scooped(new Scooped(new EmptyServing(false), "mint chip"), "coffee"),
          "black raspberry"),
      "caramel swirl");
  IIceCream order2 = new Scooped(
      new Scooped(new Scooped(new EmptyServing(true), "chocolate"), "vanilla"), "strawberry");
}