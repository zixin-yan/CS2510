import java.util.Comparator;
import tester.Tester;

class BooksByTitle implements Comparator<Book> {

  @Override
  // does this String come before the given String lexicographically?
  // produce value < 0 --- if b1's title comes before b2's title
  // produce value zero --- if b1's title is the same as b2's title
  // produce value > 0 --- if b1's title comes after b2's title
  public int compare(Book b1, Book b2) {
    return b1.title.toLowerCase().compareTo(b2.title.toLowerCase());
  }
}

class BooksByAuthor implements Comparator<Book> {

  @Override
  // does this String come before the given String lexicographically?
  // produce value < 0 --- if b1's author comes before b2's author
  // produce value zero --- if b1's author is the same as b2's author
  // produce value > 0 --- if b1's author comes after b2's author
  public int compare(Book b1, Book b2) {
    return b1.author.toLowerCase().compareTo(b2.author.toLowerCase());
  }
}

class BooksByPrice implements Comparator<Book> {

  @Override
  // does this Integer more than given integer?
  // produce value < 0 --- if b1's price less than b2's price
  // produce value zero --- if b1's price is the same as b2's price
  // produce value > 0 --- if b1's price more than b2's price
  public int compare(Book b1, Book b2) {
    return b1.price - b2.price;
  }
}

class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}

interface IBST<T> {
  // produces a new binary search tree with the given item inserted in the correct
  // place
  ABST<T> insert(T that);

  // returns whether that item is present in the binary search tree
  boolean present(T that);

  // returns the leftmost item contained in this tree
  T getLeftmost();

  // helper method of getLeftmost()
  T getLeftmostHelper(T previous);

  // returns the tree containing all but the leftmost item of this tree
  ABST<T> getRight();

  // determines whether this binary search tree is the same as the given one
  boolean sameTree(ABST<T> that);

  // determines whether the ABST is a leaf
  boolean sameLeaf(Leaf<T> that);

  // determines whether two nodes are same
  boolean sameNode(Node<T> that);

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  boolean sameData(ABST<T> that);

  // helper method of sameData
  boolean sameDataHelper(ABST<T> that);

  // represent the binary search tree of type T that produces a list of items in
  // the tree in the sorted order
  IList<T> buildList();
}

abstract class ABST<T> implements IBST<T> {
  Comparator<T> order;
}

class Leaf<T> extends ABST<T> {

  Leaf(Comparator<T> order) {
    this.order = order;
  }

  // produces a new binary search tree with the given item inserted in the correct
  // place
  public ABST<T> insert(T that) {
    return new Node<T>(this.order, that, new Leaf<T>(this.order), new Leaf<T>(this.order));
  }

  // returns whether that item is present in the binary search tree
  public boolean present(T that) {
    return false;
  }

  // returns the leftmost item contained in this tree
  public T getLeftmost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // helper method of getLeftmost()
  public T getLeftmostHelper(T previous) {
    return previous;
  }

  // returns the tree containing all but the leftmost item of this tree
  public ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  // determines whether this binary search tree is the same as the given one
  public boolean sameTree(ABST<T> that) {
    return that.sameLeaf(this);
  }

  // determines whether the ABST is a leaf
  public boolean sameLeaf(Leaf<T> that) {
    return true;
  }

  // determines whether two nodes are same
  public boolean sameNode(Node<T> that) {
    return false;
  }

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  public boolean sameData(ABST<T> that) {
    return that.sameDataHelper(this);
  }

  // helper method of sameData
  public boolean sameDataHelper(ABST<T> that) {
    return true;
  }

  // represent the binary search tree of type T that produces a list of items in
  // the tree in the sorted order
  public IList<T> buildList() {
    return new MtList<T>();
  }
}

class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    this.data = data;
    this.left = left;
    this.right = right;
    this.order = order;
  }

  // produces a new binary search tree with the given item inserted in the correct
  // place
  public ABST<T> insert(T that) {
    if (this.order.compare(this.data, that) < 1) {
      return new Node<T>(this.order, this.data, this.left, this.right.insert(that));
    }
    else {
      return new Node<T>(this.order, this.data, this.left.insert(that), this.right);
    }
  }

  // returns whether that item is present in the binary search tree
  public boolean present(T that) {
    return (this.order.compare(this.data, that) == 0) || this.left.present(that)
        || this.right.present(that);
  }

  // returns the leftmost item contained in this tree
  public T getLeftmost() {
    return this.left.getLeftmostHelper(this.data);
  }

  // helper method of getLeftmost()
  public T getLeftmostHelper(T previous) {
    return this.left.getLeftmostHelper(this.data);
  }

  // returns the tree containing all but the leftmost item of this tree
  public ABST<T> getRight() {
    if (this.data == this.getLeftmost()) {
      return this.right;
    }
    else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  // determines whether this binary search tree is the same as the given one
  public boolean sameTree(ABST<T> that) {
    return that.sameNode(this);
  }

  // determines whether the ABST is a leaf
  public boolean sameLeaf(Leaf<T> that) {
    return false;
  }

  // determines whether two nodes are same
  public boolean sameNode(Node<T> that) {
    return this.data == that.data && this.left.sameTree(that.left)
        && this.right.sameTree(that.right);
  }

  // determines whether this binary search tree contains the same data in the same
  // order as the given tree
  public boolean sameData(ABST<T> that) {
    return this.sameDataHelper(that) && that.sameDataHelper(this);
  }

  // helper method of sameData
  public boolean sameDataHelper(ABST<T> that) {
    return that.present(this.getLeftmost()) && this.getRight().sameDataHelper(that);
  }

  // represent the binary search tree of type T that produces a list of items in
  // the tree in the sorted order
  public IList<T> buildList() {
    return new ConsList<T>(this.getLeftmost(), this.getRight().buildList());
  }
}

interface IList<T> {
}

class MtList<T> implements IList<T> {
  MtList() {
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

class ExamplesABST {
  Book b1 = new Book("a", "a", 1);
  Book b2 = new Book("b", "b", 2);
  Book b3 = new Book("c", "c", 3);
  Book b4 = new Book("d", "d", 4);
  Book b5 = new Book("e", "e", 5);

  IList<Book> mtbList = new MtList<Book>();
  IList<Book> bListA = new ConsList<Book>(b1,
      new ConsList<Book>(b2, new ConsList<Book>(b3, new ConsList<Book>(b4, mtbList))));
  IList<Book> bListD = new ConsList<Book>(b1,
      new ConsList<Book>(b3, new ConsList<Book>(b4, new ConsList<Book>(b5, mtbList))));

  Comparator<Book> compTitle = new BooksByTitle();
  Comparator<Book> compAuthor = new BooksByAuthor();
  Comparator<Book> compPrice = new BooksByPrice();

  ABST<Book> mtBSTT = new Leaf<Book>(compTitle);
  ABST<Book> mtBSTA = new Leaf<Book>(compAuthor);
  ABST<Book> mtBSTP = new Leaf<Book>(compPrice);

  ABST<Book> bstA = new Node<Book>(compTitle, b3,
      new Node<Book>(compTitle, b2, new Node<Book>(compTitle, b1, mtBSTT, mtBSTT), mtBSTT),
      new Node<Book>(compTitle, b4, mtBSTT, mtBSTT));

  ABST<Book> bstB = new Node<Book>(compTitle, b3, new Node<Book>(compTitle, b2, mtBSTT, mtBSTT),
      new Node<Book>(compTitle, b4, mtBSTT, mtBSTT));

  ABST<Book> bstC = new Node<Book>(compPrice, b2, new Node<Book>(compPrice, b1, mtBSTP, mtBSTP),
      new Node<Book>(compPrice, b4, new Node<Book>(compPrice, b3, mtBSTP, mtBSTP), mtBSTP));

  ABST<Book> bstD = new Node<Book>(compAuthor, b3, new Node<Book>(compAuthor, b1, mtBSTA, mtBSTA),
      new Node<Book>(compAuthor, b4, mtBSTA, new Node<Book>(compAuthor, b5, mtBSTA, mtBSTA)));

  ABST<Book> bstE = new Node<Book>(compAuthor, b3,
      new Node<Book>(compAuthor, b1, mtBSTA, new Node<Book>(compAuthor, b2, mtBSTA, mtBSTA)),
      new Node<Book>(compAuthor, b4, mtBSTA, new Node<Book>(compAuthor, b5, mtBSTA, mtBSTA)));

  ABST<Book> bstF = new Node<Book>(compTitle, b3,
      new Node<Book>(compTitle, b2, new Node<Book>(compTitle, b1, mtBSTT, mtBSTT), mtBSTT),
      new Node<Book>(compTitle, b4, mtBSTT, new Node<Book>(compTitle, b5, mtBSTT, mtBSTT)));

  // test method insert() of ABST
  boolean testInsert(Tester t) {
    return t.checkExpect(mtBSTT.insert(b5), new Node<Book>(compTitle, b5, mtBSTT, mtBSTT))
        && t.checkExpect(bstD.insert(b2), bstE) && t.checkExpect(bstA.insert(b5), bstF);
  }

  // test method present of ABST
  boolean testPresent(Tester t) {
    return t.checkExpect(mtBSTT.present(b1), false) && t.checkExpect(bstA.present(b1), true)
        && t.checkExpect(bstD.present(b2), false);
  }

  // test method getLeftmost of ABST
  boolean testGetLeftmost(Tester t) {
    return t.checkException(new RuntimeException("No leftmost item of an empty tree"),
        (new Leaf<Book>(compTitle)), "getLeftmost") && t.checkExpect(bstA.getLeftmost(), b1)
        && t.checkExpect(bstE.getLeftmost(), b1);
  }

  // test method getLeftmost of ABST
  boolean testGetLeftmostHelper(Tester t) {
    return t.checkExpect(mtBSTT.getLeftmostHelper(b1), b1)
        && t.checkExpect(bstA.getLeftmostHelper(b2), b1)
        && t.checkExpect(bstE.getLeftmostHelper(b2), b1);
  }

  // test method getRight of ABST
  boolean testGetRight(Tester t) {
    return t.checkException(new RuntimeException("No right of an empty tree"),
        (new Leaf<Book>(compTitle)), "getRight") && t.checkExpect(bstA.getRight(), bstB)
        && t.checkExpect((new Node<Book>(compTitle, b1, mtBSTT, mtBSTT)).getRight(), mtBSTT);
  }

  // test method sameTree of ABST
  boolean testSameTree(Tester t) {
    return t.checkExpect(mtBSTT.sameTree(bstA), false)
        && t.checkExpect(mtBSTT.sameTree(mtBSTT), true) && t.checkExpect(bstA.sameTree(bstA), true)
        && t.checkExpect(bstB.sameTree(bstA), false);
  }

  // test method sameLeaf of ABST
  boolean testSameLeaf(Tester t) {
    return t.checkExpect(mtBSTT.sameLeaf(new Leaf<Book>(compTitle)), true)
        && t.checkExpect(bstA.sameLeaf(new Leaf<Book>(compTitle)), false);
  }

  // test method sameNode of ABST
  boolean testSameNode(Tester t) {
    return t.checkExpect(mtBSTT.sameNode(new Node<Book>(compTitle, b1, mtBSTT, mtBSTT)), false)
        && t.checkExpect(
            bstA.sameNode(new Node<Book>(compTitle, b3,
                new Node<Book>(compTitle, b2, new Node<Book>(compTitle, b1, mtBSTT, mtBSTT),
                    mtBSTT),
                new Node<Book>(compTitle, b4, mtBSTT, mtBSTT))),
            true)
        && t.checkExpect(bstA.sameNode(new Node<Book>(compTitle, b1, mtBSTT, mtBSTT)), false);
  }

  // test method sameData of ABST
  boolean testSameData(Tester t) {
    return t.checkExpect(mtBSTT.sameData(bstA), false)
        && t.checkExpect(mtBSTT.sameData(mtBSTT), true) && t.checkExpect(bstA.sameData(bstA), true)
        && t.checkExpect(bstA.sameData(bstD), false);
  }

  // test method sameDataHelper of ABST
  boolean testSameDataHelper(Tester t) {
    return t.checkExpect(mtBSTT.sameDataHelper(bstA), true)
        && t.checkExpect(mtBSTT.sameDataHelper(mtBSTT), true)
        && t.checkExpect(bstA.sameDataHelper(bstA), true)
        && t.checkExpect(bstA.sameDataHelper(bstD), false);
  }

  // test method buildList of ABST
  boolean testBuildList(Tester t) {
    return t.checkExpect(mtBSTT.buildList(), mtbList) && t.checkExpect(bstA.buildList(), bListA)
        && t.checkExpect(bstD.buildList(), bListD);
  }
}