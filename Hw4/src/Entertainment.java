import tester.*;

interface IEntertainment {
  //compute the total price of this Entertainment
  double totalPrice();
  
  //computes the minutes of entertainment of this IEntertainment
  int duration();
  
  //produce a String that shows the name and price of this IEntertainment
  String format();
  
  //is this IEntertainment the same as that one?
  boolean sameEntertainment(IEntertainment that);
  
  //is this IEntertainment the same as that Magazine?
  boolean sameMagazine(Magazine that);
  
  //is this IEntertainment the same as that Magazine?
  boolean sameTVSeries(TVSeries that);
  
  //is this IEntertainment the same as that Magazine?
  boolean samePodcast(Podcast that);
}

abstract class AEntertainment implements IEntertainment {
  String name;
  double price; //represents price per issue
  int installments; //number of installments 
  
  AEntertainment(String name, double price, int installments) {
    this.name = name;
    this.price = price;
    this.installments = installments;
  }
  
  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.price ... -- double
   * ... this.installments ... -- int
   * 
   * Methods:
   * ... this.totalPrice() ... -- double
   * ... this.duration() ... -- int
   * ... this.format() ... -- String
   * ... this.sameEntertainment() ... -- boolean
   * ... this.sameMagazine() ... -- boolean
   * ... this.sameTVSeries() ... -- boolean
   * ... this.samePodcast() ... -- boolean
   * 
   * Methods for Fields:
   * 
   */
  
  //computes the price of a year of Magazine or all installments of other
  public double totalPrice() {
    return this.price * this.installments;
  }
  
  //computes the minutes of entertainment of this Entertainment
  public int duration() {
    return 50 * this.installments;
  }
  
  // returns the name and total price of this Entertainment as a String
  public String format() {
    return this.name + ", " + this.price + ".";
  }
  
  // determines if this Entertainment is the same as that Entertainment
  public abstract boolean sameEntertainment(IEntertainment that);
  
  //determines if this Entertainment is the same as that Magazine
  public boolean sameMagazine(Magazine that) {
    return false;
  }
  
  //determines if this Entertainment is the same as that TVSeries
  public boolean sameTVSeries(TVSeries that) {
    return false;
  }
  
  //determines if this Entertainment is the same as that Podcast
  public boolean samePodcast(Podcast that) {
    return false;
  }
}

class Magazine extends AEntertainment {
  String genre;
  int pages;
    
  Magazine(String name, double price, String genre, int pages, int installments) {
    super(name, price, installments);
    this.genre = genre;
    this.pages = pages;
  }
    
  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.price ... -- double
   * ... this.installments ... -- int
   * ... this.genre ... -- String
   * ... this.pages ... -- int
   * 
   * Methods:
   * ... this.totalPrice() ... -- double
   * ... this.duration() ... -- int
   * ... this.format() ... -- String
   * ... this.sameEntertainment() ... -- boolean
   * ... this.sameMagazine() ... -- boolean
   * ... this.sameTVSeries() ... -- boolean
   * ... this.samePodcast() ... -- boolean
   * 
   * Methods for Fields:
   * 
   */
    
  //computes the minutes of entertainment of this Entertainment
  public int duration() {
    return 5 * this.installments * this.pages;
  }
    
  //determines if this Magazine is the same as that Magazine
  public boolean sameMagazine(Magazine that) {
    return this.name == that.name
        && this.price == that.price
        && this.genre == that.genre
        && this.pages == that.pages
        && this.installments == that.installments;
  }
    
  //is this Magazine the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameMagazine(this);
  }
}

class TVSeries extends AEntertainment {
  String corporation;
    
  TVSeries(String name, double price, int installments, String corporation) {
    super(name, price, installments);
    this.corporation = corporation;
  }
    
  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.price ... -- double
   * ... this.installments ... -- int
   * ... this.corporation ... -- String
   * 
   * Methods:
   * ... this.totalPrice() ... -- double
   * ... this.duration() ... -- int
   * ... this.format() ... -- String
   * ... this.sameEntertainment() ... -- boolean
   * ... this.sameMagazine() ... -- boolean
   * ... this.sameTVSeries() ... -- boolean
   * ... this.samePodcast() ... -- boolean
   * 
   * Methods for Fields:
   * 
   */
    
  //determines if this TVSeries is the same as that TVSeries
  public boolean sameTVSeries(TVSeries that) {
    return this.name == that.name
        && this.price == that.price
        && this.installments == that.installments
        && this.corporation == that.corporation;
  }
    
  //is this TVSeries the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.sameTVSeries(this);
  }
}

class Podcast extends AEntertainment {
    
  Podcast(String name, double price, int installments) {
    super(name, price, installments);
  }
    
  /*
   * TEMPLATE:
   * Fields:
   * ... this.name ... -- String
   * ... this.price ... -- double
   * ... this.installments ... -- int
   * 
   * Methods:
   * ... this.totalPrice() ... -- double
   * ... this.duration() ... -- int
   * ... this.format() ... -- String
   * ... this.sameEntertainment() ... -- boolean
   * ... this.sameMagazine() ... -- boolean
   * ... this.sameTVSeries() ... -- boolean
   * ... this.samePodcast() ... -- boolean
   * 
   * Methods for Fields:
   * 
   */
    
  //determines if this Podcast is the same as that Podcast
  public boolean samePodcast(Podcast that) {
    return this.name == that.name
        && this.price == that.price
        && this.installments == that.installments;
  }
    
  //is this Podcast the same as that IEntertainment?
  public boolean sameEntertainment(IEntertainment that) {
    return that.samePodcast(this);
  }
}

class ExamplesEntertainment {
  IEntertainment rollingStone = new Magazine("Rolling Stone", 2.55, "Music", 60, 12);
  IEntertainment houseOfCards = new TVSeries("House of Cards", 5.25, 13, "Netflix");
  IEntertainment serial = new Podcast("Serial", 0.00, 8);
    
  IEntertainment time = new Magazine("Time", 2.00, "News", 100, 3);
  IEntertainment euphoria = new TVSeries("Euphoria", 4.00, 13, "HBO");
  IEntertainment nnpercent = new Podcast("99% Invisible", 0.00, 400); 
    
  //testing totalPrice method
  boolean testTotalPrice(Tester t) {
    return t.checkInexact(this.rollingStone.totalPrice(), 2.55 * 12, .0001) 
        && t.checkInexact(this.houseOfCards.totalPrice(), 5.25 * 13, .0001)
        && t.checkInexact(this.serial.totalPrice(), 0.0, .0001)
        && t.checkInexact(this.time.totalPrice(), 2.00 * 3, 0.0001)
        && t.checkInexact(this.euphoria.totalPrice(), 4.00 * 13, 0.0001)
        && t.checkInexact(this.nnpercent.totalPrice(), 0.0, 0.0001);
  }
    
  //testing duration method
  boolean testDuration(Tester t) {
    return t.checkExpect(this.time.duration(), 1500)
        && t.checkExpect(this.euphoria.duration(), 650)
        && t.checkExpect(this.serial.duration(), 400);
  }
    
  //testing sameEntertainment method
  boolean testSameEntertainment(Tester t) {
    return t.checkExpect(this.time.sameEntertainment(euphoria), false)
        && t.checkExpect(this.rollingStone.sameEntertainment(time), false)
        && t.checkExpect(this.serial.sameEntertainment(serial), true);
  }
    
  //testing format method
  boolean testFormat(Tester t) {
    return t.checkExpect(this.time.format(), "Time, 6.0")
        && t.checkExpect(this.euphoria.format(), "Euphoria, 52.0")
        && t.checkExpect(this.serial.format(), "Serial, 0.0");
  }
}