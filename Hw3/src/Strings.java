// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();

  // insert a string follow the alphabetical order
  ILoString insert(String that);

  // produces a new list, sorted in alphabetical order
  ILoString sort();

  // determines whether this list is sorted in alphabetical order
  boolean isSorted();

  // helper method for isSorted
  boolean isSortedHelper(String that);

  // produces a list where the first, third, fifth... elements are from this list,
  // and the second, fourth, sixth... elements are from the given list
  ILoString interleave(ILoString strings);

  // produces a sorted list of Strings that contains all items in both original
  // lists, including duplicates
  ILoString merge(ILoString strings);

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  ILoString reverse();

  // helper method for reverse
  ILoString reverseHelper(ILoString that);

  // determines if this list contains pairs of identical strings, that is, the
  // first and second strings are the same, etc.
  boolean isDoubledList();

  // helper method for isDoubledList
  boolean isDoubledListHelper(String that);

  // determines whether this list contains the same words reading the list in
  // either order
  boolean isPalindromeList();

  // helper method for isPalindromeList
  boolean isPalindromeListHelper(ILoString that, int len);

  // returns the string in specified position
  String position(int posi);

  // returns the length of this list of strings
  int length();
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString() {
  }

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  // insert a string follow the alphabetical order
  public ILoString insert(String that) {
    return new ConsLoString(that, this);
  }

  // produces a new list, sorted in alphabetical order
  public ILoString sort() {
    return this;
  }

  // determines whether this list is sorted in alphabetical order
  public boolean isSorted() {
    return true;
  }

  // helper method for isSorted
  public boolean isSortedHelper(String that) {
    return true;
  }

  // produces a list where the first, third, fifth... elements are from this list,
  // and the second, fourth, sixth... elements are from the given list
  public ILoString interleave(ILoString strings) {
    return strings;
  }

  // produces a sorted list of Strings that contains all items in both original
  // lists, including duplicates
  public ILoString merge(ILoString strings) {
    return strings;
  }

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  public ILoString reverse() {
    return this;
  }

  // helper method for reverse
  public ILoString reverseHelper(ILoString that) {
    return that;
  }

  // determines if this list contains pairs of identical strings, that is, the
  // first and second strings are the same, etc.
  public boolean isDoubledList() {
    return true;
  }

  // helper method for isDoubledList
  public boolean isDoubledListHelper(String that) {
    return false;
  }

  // determines whether this list contains the same words reading the list in
  // either order
  public boolean isPalindromeList() {
    return true;
  }

  // helper method for isPalindromeList
  public boolean isPalindromeListHelper(ILoString that, int len) {
    return true;
  }

  // returns the string in specified position
  public String position(int posi) {
    return "";
  }

  // returns the length of this list of strings
  public int length() {
    return 0;
  }
}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE
   * FIELDS:
   * ... this.first ... -- String
   * ... this.rest ... -- ILoString
   * 
   * METHODS
   * ... this.combine() ... -- String
   * ... this.insert(String) ... -- ILoString
   * ... this.sort() ... -- ILoString
   * ... this.isSorted() ... -- boolean
   * ... this.isSortedHelper(String) ... -- boolean
   * ... this.interleave(ILoString) ... -- ILoString
   * ... this.merge(ILoString) ... -- ILoString
   * ... this.reverse() ... -- ILoString
   * ... this.reverseHelper(ILoString) ... -- ILoString
   * ... this.isDoubledList() ... -- boolean
   * ... this.isDoubledListHelper(String) ... -- boolean
   * ... this.isPalindromeList() ... -- boolean
   * ... this.isPalindromeListHelper(String, ILoString) ... -- boolean
   * ... this.position(int) ... -- String
   * ... this.length() ... -- int
   * 
   * METHODS FOR FIELDS
   * ... this.first.concat(String) ... -- String
   * ... this.first.compareTo(String) ... -- int
   * ... this.rest.combine() ... -- String
   * ... this.first.equals(String) ... -- boolean
   * ... this.first.toLowerCase() ... String
   * 
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }

  // insert a string follow the alphabetical order
  public ILoString insert(String that) {
    if (this.first.toLowerCase().compareTo(that.toLowerCase()) >= 0) {
      return new ConsLoString(that, this);
    }
    else {
      return new ConsLoString(this.first, this.rest.insert(that));
    }
  }

  // produces a new list, sorted in alphabetical order
  public ILoString sort() {
    return (this.rest.sort()).insert(this.first);
  }

  // determines whether this list is sorted in alphabetical order
  public boolean isSorted() {
    return this.rest.isSortedHelper(this.first) && this.rest.isSorted();
  }

  // helper method for isSorted
  public boolean isSortedHelper(String that) {
    return this.first.toLowerCase().compareTo(that.toLowerCase()) >= 0;
  }

  // produces a list where the first, third, fifth... elements are from this list,
  // and the second, fourth, sixth... elements are from the given list
  public ILoString interleave(ILoString strings) {
    return new ConsLoString(this.first, strings.interleave(this.rest));
  }

  // produces a sorted list of Strings that contains all items in both original
  // lists, including duplicates
  public ILoString merge(ILoString strings) {
    return this.rest.merge(strings.insert(this.first));
  }

  // produces a new list of Strings containing the same elements as this list of
  // Strings, but in reverse order
  public ILoString reverse() {
    return this.rest.reverseHelper(new ConsLoString(this.first, new MtLoString()));
  }

  // helper method for reverse
  public ILoString reverseHelper(ILoString that) {
    return this.rest.reverseHelper(new ConsLoString(this.first, that));
  }

  // determines if this list contains pairs of identical strings, that is, the
  // first and second strings are the same, etc.
  public boolean isDoubledList() {
    return this.rest.isDoubledListHelper(this.first);
  }

  // helper method for isDoubledList
  public boolean isDoubledListHelper(String that) {
    // if first two strings are same
    if (this.first.equals(that)) {
      // then compare the next two strings
      return this.rest.isDoubledList();
    }
    else {
      // otherwise returns false
      return false;
    }
  }

  // determines whether this list contains the same words reading the list in
  // either order
  public boolean isPalindromeList() {
    return this.reverse().isPalindromeListHelper(this, this.length());
  }

  // helper method for isPalindromeList
  // compare two first string in both lists of strings
  public boolean isPalindromeListHelper(ILoString that, int len) {
    if (len == 0) {
      return this.first.equals(that.position(0));
    }
    else {
      return this.position(len - 1).equals(that.position(len - 1))
          && this.isPalindromeListHelper(that, len - 1);
    }
  }

  // returns the string in specified position
  public String position(int posi) {
    if (posi == 0) {
      return this.first;
    }
    else {
      return this.rest.position(posi - 1);
    }
  }

  // returns the length of this list of strings
  public int length() {
    return 1 + this.rest.length();
  }
}

// to represent examples for lists of strings
class ExamplesStrings {
  ILoString emptyLoS = new MtLoString();
  ILoString mary = new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
      new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))));
  ILoString john = new ConsLoString("John ",
      new ConsLoString("takes ", new ConsLoString("CS2510 ", new MtLoString())));
  ILoString checkerTest = new ConsLoString("All", new ConsLoString("Broken",
      new ConsLoString("any", new ConsLoString("before", new MtLoString()))));
  ILoString doubledList = new ConsLoString("fundies2 ", new ConsLoString("fundies2 ",
      new ConsLoString("CS2510 ", new ConsLoString("CS2510 ", new MtLoString()))));
  ILoString palindromeList1 = new ConsLoString("A",
      new ConsLoString("B", new ConsLoString("C", new ConsLoString("D",
          new ConsLoString("C", new ConsLoString("B", new ConsLoString("A", new MtLoString())))))));
  ILoString palindromeList2 = new ConsLoString("a", new ConsLoString("a", new ConsLoString("b",
      new ConsLoString("c",
          new ConsLoString("d", new ConsLoString("e", new ConsLoString("d", new ConsLoString("c",
              new ConsLoString("b", new ConsLoString("a", new MtLoString()))))))))));
  ILoString palindromeList3 = new ConsLoString("a",
      new ConsLoString("b",
          new ConsLoString("c",
              new ConsLoString("d",
                  new ConsLoString("e", new ConsLoString("f",
                      new ConsLoString("f", new ConsLoString("d", new ConsLoString("c",
                          new ConsLoString("b", new ConsLoString("a", new MtLoString())))))))))));
  ILoString palindromeList4 = new ConsLoString("a", new ConsLoString("b", new ConsLoString("c",
      new ConsLoString("d",
          new ConsLoString("e", new ConsLoString("f", new ConsLoString("d", new ConsLoString("c",
              new ConsLoString("b", new ConsLoString("a", new MtLoString()))))))))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return t.checkExpect(this.mary.combine(), "Mary had a little lamb.")
        && t.checkExpect(this.john.combine(), "John takes CS2510 ")
        && t.checkExpect(this.emptyLoS.combine(), "");
  }

  // test the method insert for the lists of Strings
  boolean testInsert(Tester t) {
    return t.checkExpect(this.mary.insert("b"),
        new ConsLoString("b",
            new ConsLoString("Mary ", new ConsLoString("had ",
                new ConsLoString("a ",
                    new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))))))
        && t.checkExpect(this.mary.insert("n"),
            new ConsLoString("Mary ",
                new ConsLoString("had ",
                    new ConsLoString("a ",
                        new ConsLoString("little ",
                            new ConsLoString("lamb.", new ConsLoString("n", new MtLoString())))))))
        && t.checkExpect(this.emptyLoS.insert("a"), new ConsLoString("a", this.emptyLoS));
  }

  // test the method sort for the lists of Strings
  boolean testSort(Tester t) {
    return t.checkExpect(this.mary.sort(),
        new ConsLoString("a ",
            new ConsLoString("had ",
                new ConsLoString("lamb.",
                    new ConsLoString("little ", new ConsLoString("Mary ", new MtLoString()))))))
        && t.checkExpect(this.john.sort(),
            new ConsLoString("CS2510 ",
                new ConsLoString("John ", new ConsLoString("takes ", new MtLoString()))))
        && t.checkExpect(this.emptyLoS.sort(), this.emptyLoS);
  }

  // test the method isSorted for the lists of Strings
  boolean testIsSorted(Tester t) {
    return t.checkExpect(this.mary.isSorted(), false)
        && t.checkExpect(this.mary.sort().isSorted(), true)
        && t.checkExpect(this.checkerTest.isSorted(), false)
        && t.checkExpect(this.emptyLoS.isSorted(), true);
  }

  // test the method isSortedHelper for the lists of Strings
  boolean testIsSortedHelper(Tester t) {
    return t.checkExpect(this.mary.isSortedHelper("b"), true)
        && t.checkExpect(this.mary.isSortedHelper("n"), false)
        && t.checkExpect(this.mary.isSortedHelper("Z"), false)
        && t.checkExpect(this.mary.isSortedHelper("A"), true)
        && t.checkExpect(this.emptyLoS.isSortedHelper("a"), true);
  }

  // test the method interleave for the lists of Strings
  boolean testInterleave(Tester t) {
    return t
        .checkExpect(this.mary.interleave(this.john), new ConsLoString("Mary ", new ConsLoString(
            "John ",
            new ConsLoString("had ", new ConsLoString("takes ", new ConsLoString("a ",
                new ConsLoString("CS2510 ",
                    new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))))))))
        && t.checkExpect(this.john.interleave(this.mary),
            new ConsLoString("John ",
                new ConsLoString("Mary ",
                    new ConsLoString("takes ",
                        new ConsLoString("had ",
                            new ConsLoString("CS2510 ",
                                new ConsLoString("a ",
                                    new ConsLoString("little ",
                                        new ConsLoString("lamb.", new MtLoString())))))))))
        && t.checkExpect(this.emptyLoS.interleave(this.mary), this.mary);
  }

  // test the method merge for the lists of Strings
  boolean testMerge(Tester t) {
    return t.checkExpect(this.mary.sort().merge(this.john.sort()),
        new ConsLoString("a ", new ConsLoString("CS2510 ",
            new ConsLoString("had ", new ConsLoString("John ", new ConsLoString("lamb.",
                new ConsLoString("little ",
                    new ConsLoString("Mary ", new ConsLoString("takes ", new MtLoString())))))))))
        && t.checkExpect(this.john.sort().merge(this.doubledList.sort()), new ConsLoString(
            "CS2510 ",
            new ConsLoString("CS2510 ", new ConsLoString("CS2510 ", new ConsLoString("fundies2 ",
                new ConsLoString("fundies2 ",
                    new ConsLoString("John ", new ConsLoString("takes ", new MtLoString()))))))))
        && t.checkExpect(this.emptyLoS.merge(this.john.sort()), this.john.sort());
  }

  // test the method reverse for the lists of Strings
  boolean testReverse(Tester t) {
    return t.checkExpect(this.mary.reverse(),
        new ConsLoString("lamb.",
            new ConsLoString("little ",
                new ConsLoString("a ",
                    new ConsLoString("had ", new ConsLoString("Mary ", new MtLoString()))))))
        && t.checkExpect(this.john.reverse(),
            new ConsLoString("CS2510 ",
                new ConsLoString("takes ", new ConsLoString("John ", new MtLoString()))))
        && t.checkExpect(this.palindromeList1.reverse(), this.palindromeList1)
        && t.checkExpect(this.palindromeList2.reverse(),
            new ConsLoString("a",
                new ConsLoString("b", new ConsLoString("c", new ConsLoString("d",
                    new ConsLoString("e", new ConsLoString("d", new ConsLoString("c",
                        new ConsLoString("b",
                            new ConsLoString("a", new ConsLoString("a", new MtLoString())))))))))))
        && t.checkExpect(this.emptyLoS.reverse(), this.emptyLoS);
  }

  // test the method reverseHelper for the lists of Strings
  boolean testReverseHelper(Tester t) {
    return t.checkExpect(this.emptyLoS.reverseHelper(this.mary), this.mary)
        && t.checkExpect((new ConsLoString("b", this.emptyLoS)).reverseHelper(this.mary),
            new ConsLoString("b",
                new ConsLoString("Mary ", new ConsLoString("had ", new ConsLoString("a ",
                    new ConsLoString("little ", new ConsLoString("lamb.", new MtLoString())))))));
  }

  // test the method isDoubledList for the lists of Strings
  boolean testIsDoubledList(Tester t) {
    return t.checkExpect(this.mary.isDoubledList(), false)
        && t.checkExpect(this.doubledList.isDoubledList(), true);
  }

  // test the method isDoubledListHelper for the lists of Strings
  boolean testIsDoubledListHelper(Tester t) {
    return t.checkExpect((new ConsLoString("a", this.emptyLoS)).isDoubledListHelper("a"), true)
        && t.checkExpect(this.emptyLoS.isDoubledListHelper("a"), false)
        && t.checkExpect((new ConsLoString("a", this.emptyLoS)).isDoubledListHelper("b"), false);
  }

  // test the method isPalindromeList for the lists of Strings
  boolean testIsPalindromeList(Tester t) {
    return t.checkExpect(this.mary.isPalindromeList(), false)
        && t.checkExpect(this.palindromeList1.isPalindromeList(), true)
        && t.checkExpect(this.palindromeList2.isPalindromeList(), false)
        && t.checkExpect(this.palindromeList3.isPalindromeList(), false)
        && t.checkExpect(this.palindromeList4.isPalindromeList(), false);
  }

  // test the method isPalindromeList for the lists of Strings
  boolean testIsPalindromeListHelper(Tester t) {
    return t.checkExpect(this.john.isPalindromeListHelper(this.john, this.john.length()), true)
        && t.checkExpect(this.emptyLoS.isPalindromeListHelper(this.emptyLoS, 0), true);
  }

  // test the method position for the lists of strings
  boolean testPosition(Tester t) {
    return t.checkExpect(this.mary.position(0), "Mary ")
        && t.checkExpect(this.emptyLoS.position(0), "");
  }

  // test the method length for the lists of Strings
  boolean testLength(Tester t) {
    return t.checkExpect(this.mary.length(), 5) && t.checkExpect(this.emptyLoS.length(), 0);
  }
}