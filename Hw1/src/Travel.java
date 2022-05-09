// to represent a IHousing
interface IHousing {
}

// to represent a hut
class Hut implements IHousing {
  int capacity;
  int population;

  Hut(int capacity, int population) {
    this.capacity = capacity;
    this.population = population;
  }
}

// to represent a inn
class Inn implements IHousing {
  String name;
  int capacity;
  int population;
  int stalls;

  Inn(String name, int capacity, int population, int stalls) {
    this.name = name;
    this.capacity = capacity;
    this.population = population;
    this.stalls = stalls;
  }
}

// to represent a castle
class Castle implements IHousing {
  String name;
  String familyName;
  int population;
  int carriageHouse;

  Castle(String name, String familyName, int population, int carriageHouse) {
    this.name = name;
    this.familyName = familyName;
    this.population = population;
    this.carriageHouse = carriageHouse;
  }
}

// to represent a ITransportation
interface ITransportation {
}

// to represent a horse
class Horse implements ITransportation {
  IHousing from;
  IHousing to;
  String name;
  String color;

  Horse(IHousing from, IHousing to, String name, String color) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.color = color;
  }
}

// to represent a carriage
class Carriage implements ITransportation {
  IHousing from;
  IHousing to;
  int tonnage;

  Carriage(IHousing from, IHousing to, int tonnage) {
    this.from = from;
    this.to = to;
    this.tonnage = tonnage;
  }
}

// to represent examples of the game, Summer is Coming
class ExamplesTravel {
  ExamplesTravel() {
  }

  Hut hovel = new Hut(5, 1);
  Castle winterfell = new Castle("Winterfell", "Stark", 500, 6);
  Inn crossroads = new Inn("Inn At The Crossroads", 40, 20, 12);
  Castle highgarden = new Castle("Highgarden", "Tyrell", 750, 12);
  Castle myhome = new Castle("MyHome", "Yan", 1000, 100);
  Inn huntington = new Inn("Inn At The Huntington", 100, 50, 25);

  Horse horse1 = new Horse(highgarden, myhome, "Hunter", "pink");
  Horse horse2 = new Horse(crossroads, hovel, "Pun", "cyan");
  Carriage carriage1 = new Carriage(crossroads, highgarden, 32);
  Carriage carriage2 = new Carriage(myhome, huntington, 1);
}