//import tester.*;
//import java.util.function.Predicate;
//
//class Deque<T> {
//  Sentinel<T> header;
//
//  Deque() {
//    this.header = new Sentinel<T>();
//  }
//
//  Deque(Sentinel<T> header) {
//    this.header = header;
//  }
//
//  // counts the number of nodes in a list Deque
//  public int size() {
//    return this.header.next.size();
//  }
//
//  // consumes a value of type T and inserts it at the front of the list
//  public void addAtHead(T value) {
//    this.header.next.addAtHead(value);
//  }
//
//  // consumes a value of type T and inserts it at the tail of this list
//  public void addAtTail(T value) {
//    this.header.prev.addAtTail(value);
//  }
//
//  // removes the first node from this Deque
//  public INode<T> removeFromHead() {
//    return this.header.removeFromHead();
//  }
//
//  // removes the last node from this Deque
//  public INode<T> removeFromTail() {
//    return this.header.removeFromTail();
//  }
//
//  // produces the first node in this Deque for which the given predicate returns
//  // true
//  public ANode<T> find(Predicate<T> func) {
//    return this.header.next.find(func);
//  }
//
//  // removes the given node from this Deque
//  public void removeNode(ANode<T> other) {
//    if (!other.equals(this.header)) {
//      this.header.removeNode(other);
//    }
//  }
//}
//
//interface INode<T> {
//  // updates the prev item
//  void updatePrev(ANode<T> that);
//
//  // updates the next item
//  void updateNext(ANode<T> next);
//
//  // counts the number of nodes in a Node
//  int size();
//
//  // consumes a value of type T and inserts it at the front of the list
//  void addAtHead(T value);
//
//  // consumes a value of type T and inserts it at the tail of this list
//  void addAtTail(T value);
//
//  // removes the first node from this Deque
//  ANode<T> removeFromHead();
//
//  // removes the first node from this Deque
//  ANode<T> removeFromHeadHelper(ANode<T> prev, int count);
//
//  // removes the last node from this Deque
//  ANode<T> removeFromTail();
//
//  // removes the last node from this Deque
//  ANode<T> removeFromTailHelper(ANode<T> next, int count);
//
//  // produces the first node in this Deque for which the given predicate returns
//  // true
//  public ANode<T> find(Predicate<T> func);
//
//  // removes the given node from this Deque
//  void removeNode(ANode<T> other);
//
//  // removes the given node from this Deque
//  void removeNodeHelper(ANode<T> other, ANode<T> prev);
//}
//
//abstract class ANode<T> implements INode<T> {
//  ANode<T> prev;
//  ANode<T> next;
//
//  ANode(ANode<T> prev, ANode<T> next) {
//    this.prev = prev;
//    this.next = next;
//  }
//
//  // updates the prev item
//  public void updatePrev(ANode<T> prev) {
//    this.prev = prev;
//  }
//
//  // updates the next item
//  public void updateNext(ANode<T> next) {
//    this.next = next;
//  }
//
//  // counts the number of nodes in a Node
//  abstract public int size();
//
//  // consumes a value of type T and inserts it at the front of the list
//  abstract public void addAtHead(T value);
//
//  // consumes a value of type T and inserts it at the tail of this list
//  abstract public void addAtTail(T value);
//
//  // removes the first node from this Deque
//  public ANode<T> removeFromHead() {
//    if (this.next.equals(this)) {
//      throw new RuntimeException("Cannot remove from an empty list");
//    }
//    else {
//      return this.next.removeFromHeadHelper(this, 0);
//    }
//  }
//
//  // removes the first node from this Deque
//  public ANode<T> removeFromHeadHelper(ANode<T> prev, int count) {
//    if (count == 1) {
//      this.prev = prev.prev;
//      prev.updatePrev(null);
//      prev.updateNext(null);
//      return prev;
//    }
//    else {
//      prev.next = this.next;
//      return this.next.removeFromHeadHelper(this, count + 1);
//    }
//  }
//
//  // removes the last node from this Deque
//  public ANode<T> removeFromTail() {
//    if (this.prev.equals(this) && this.next.equals(this)) {
//      throw new RuntimeException("Cannot remove from an empty list");
//    }
//    else {
//      return this.prev.removeFromTailHelper(this, 0);
//    }
//  }
//
//  // removes the last node from this Deque
//  public ANode<T> removeFromTailHelper(ANode<T> next, int count) {
//    if (count == 1) {
//      this.next = next.next;
//      next.updatePrev(null);
//      next.updateNext(null);
//      return next;
//    }
//    else {
//      next.prev = this.prev;
//      return this.prev.removeFromTailHelper(this, count + 1);
//    }
//  }
//
//  // produces the first node in this Deque for which the given predicate returns
//  // true
//  abstract public ANode<T> find(Predicate<T> func);
//
//  // removes the given node from this Deque
//  public void removeNode(ANode<T> other) {
//    this.next.removeNodeHelper(other, this);
//  }
//
//  // removes the given node from this Deque
//  public void removeNodeHelper(ANode<T> other, ANode<T> prev) {
//    if (this.equals(other)) {
//      this.next.updatePrev(this.prev);
//      prev.updateNext(this.next);
//      this.updatePrev(null);
//      this.updateNext(null);
//    }
//    else {
//      this.next.removeNodeHelper(other, this);
//    }
//  }
//}
//
//class Sentinel<T> extends ANode<T> {
//  Sentinel() {
//    super(null, null);
//    this.prev = this;
//    this.next = this;
//  }
//
//  // counts the number of nodes in a Node
//  public int size() {
//    return 0;
//  }
//
//  // consumes a value of type T and inserts it at the front of the list
//  public void addAtHead(T value) {
//    ANode<T> that = new Node<T>(value);
//    this.updatePrev(that);
//    this.updateNext(that);
//    that.updatePrev(this);
//    that.updateNext(this);
//  }
//
//  // consumes a value of type T and inserts it at the tail of this list
//  public void addAtTail(T value) {
//    ANode<T> that = new Node<T>(value);
//    this.updatePrev(that);
//    this.updateNext(that);
//    that.updatePrev(this);
//    that.updateNext(this);
//  }
//
//  // produces the first node in this Deque for which the given predicate returns
//  // true
//  public ANode<T> find(Predicate<T> func) {
//    return this;
//  }
//}
//
//class Node<T> extends ANode<T> {
//  T data;
//
//  Node(T data) {
//    super(null, null);
//    this.data = data;
//  }
//
//  Node(T data, ANode<T> prev, ANode<T> next) {
//    super(prev, next);
//    this.data = data;
//
//    if (prev == null || next == null) {
//      throw new IllegalArgumentException("The given nodes is null");
//    }
//  }
//
//  // consumes a value of type T and inserts it at the front of the list
//  public void addAtHead(T value) {
//    ANode<T> that = new Node<T>(value);
//    that.updateNext(this);
//    that.updatePrev(this.prev);
//    this.prev.updateNext(that);
//    this.updatePrev(that);
//  }
//
//  // consumes a value of type T and inserts it at the tail of this list
//  public void addAtTail(T value) {
//    ANode<T> that = new Node<T>(value);
//    that.updatePrev(this);
//    that.updateNext(this.next);
//    this.next.updatePrev(that);
//    this.updateNext(that);
//  }
//
//  // counts the number of nodes in a Node
//  public int size() {
//    return 1 + this.next.size();
//  }
//
//  // produces the first node in this Deque for which the given predicate returns
//  // true
//  public ANode<T> find(Predicate<T> func) {
//    if (func.test(this.data)) {
//      return this;
//    }
//    else {
//      return this.next.find(func);
//    }
//  }
//}
//
//class ExamplesDeque {
//  ExamplesDeque() {
//  }
//
//  Deque<String> deque1;
//  Deque<String> deque2;
//  Deque<String> deque3;
//  ANode<String> abc;
//  ANode<String> bcd;
//  ANode<String> cde;
//  ANode<String> def;
//  ANode<String> node1;
//  ANode<String> node2;
//  ANode<String> node3;
//  ANode<String> node4;
//
//  void initData() {
//    this.deque1 = new Deque<String>();
//    this.deque2 = new Deque<String>();
//    this.deque3 = new Deque<String>();
//    this.abc = new Node<String>("abc");
//    this.bcd = new Node<String>("bcd");
//    this.cde = new Node<String>("cde");
//    this.def = new Node<String>("def");
//    this.node1 = new Node<String>("aaa");
//    this.node2 = new Node<String>("bbb");
//    this.node3 = new Node<String>("ccc");
//    this.node4 = new Node<String>("ddd");
//
//    this.deque2.header.updatePrev(this.def);
//    this.deque2.header.updateNext(this.abc);
//    this.abc.updatePrev(this.deque2.header);
//    this.abc.updateNext(this.bcd);
//    this.bcd.updatePrev(this.abc);
//    this.bcd.updateNext(this.cde);
//    this.cde.updatePrev(this.bcd);
//    this.cde.updateNext(this.def);
//    this.def.updatePrev(this.cde);
//    this.def.updateNext(this.deque2.header);
//
//    this.deque3.header.updatePrev(this.node3);
//    this.deque3.header.updateNext(this.node2);
//    this.node2.updatePrev(this.deque3.header);
//    this.node2.updateNext(this.node1);
//    this.node1.updatePrev(this.node2);
//    this.node1.updateNext(this.node4);
//    this.node4.updatePrev(this.node1);
//    this.node4.updateNext(this.node3);
//    this.node3.updatePrev(this.node4);
//    this.node3.updateNext(this.deque3.header);
//  }
//
//  void testSize(Tester t) {
//    initData();
//    t.checkExpect(this.deque1.size(), 0);
//    t.checkExpect(this.deque2.size(), 4);
//    t.checkExpect(this.deque3.size(), 4);
//  }
//
//  void testAddAtHead(Tester t) {
//    initData();
//    this.deque1.addAtHead("aaa");
//    t.checkExpect(this.deque1.header.next,
//        new Node<String>("aaa", this.deque1.header, this.deque1.header));
//    this.deque2.addAtHead("aaa");
//    t.checkExpect(this.deque2.header.next, new Node<String>("aaa", this.deque2.header, this.abc));
//    this.deque3.addAtHead("eee");
//    t.checkExpect(this.deque3.header.next, new Node<String>("eee", this.deque3.header, this.node2));
//  }
//
//  void testAddAtTail(Tester t) {
//    initData();
//    this.deque1.addAtTail("aaa");
//    t.checkExpect(this.deque1.header.next,
//        new Node<String>("aaa", this.deque1.header, this.deque1.header));
//    this.deque2.addAtTail("aaa");
//    t.checkExpect(this.deque2.header.prev, new Node<String>("aaa", this.def, this.deque2.header));
//    this.deque3.addAtTail("eee");
//    t.checkExpect(this.deque3.header.prev, new Node<String>("eee", this.node3, this.deque3.header));
//  }
//
//  void testRemoveFromHead(Tester t) {
//    initData();
//    t.checkException(new RuntimeException("Cannot remove from an empty list"), this.deque1,
//        "removeFromHead");
//    t.checkExpect(this.deque2.removeFromHead(), this.abc);
//    t.checkExpect(this.deque3.removeFromHead(), this.node2);
//  }
//
//  void testRemoveFromTail(Tester t) {
//    initData();
//    t.checkException(new RuntimeException("Cannot remove from an empty list"), this.deque1,
//        "removeFromTail");
//    t.checkExpect(this.deque2.removeFromTail(), this.def);
//    t.checkExpect(this.deque3.removeFromTail(), this.node3);
//  }
//
//  void testRemoveNode(Tester t) {
//    initData();
//    this.deque1.removeNode(this.deque1.header);
//    t.checkExpect(this.deque1, this.deque1);
//    this.deque2.removeNode(this.deque2.header);
//    t.checkExpect(this.deque2, this.deque2);
//    this.deque2.removeNode(this.abc);
//    t.checkExpect(this.deque2.header.next, this.bcd);
//  }
//}