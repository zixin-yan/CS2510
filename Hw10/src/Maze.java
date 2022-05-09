import java.awt.Color;
import java.util.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;

// represents a vertex
class Vertex {
  String id;
  int x;
  int y;
  ArrayList<Edge> outgoing;

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.id = "x" + String.valueOf(x) + "y" + String.valueOf(y);
    this.outgoing = new ArrayList<Edge>();
  }

  // connects a vertex with a given edge
  void connect(Edge e) {
    if (!this.outgoing.contains(e)) {
      this.outgoing.add(e);
    }
  }

  WorldImage drawVertexScene(int size, WorldImage image, Vertex to, Color color) {
    // draws horizontal wall
    if (this.x == to.x) {
      return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, image, this.x * size,
          to.y * size, new LineImage(new Posn(size, 0), color));
    }
    // draws vertical wall
    if (this.y == to.y) {
      return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, image, to.x * size,
          this.y * size, new LineImage(new Posn(0, size), color));
    }
    return image;
  }

  // returns the position of this vertex in Posn
  Posn getPosition() {
    return new Posn(this.x, this.y);
  }

  @Override
  // overrides the default equals method
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Vertex)) {
      return false;
    }

    Vertex that = (Vertex) o;
    return this.id.equals(that.id);
  }

  @Override
  // override the default hashCode method
  public int hashCode() {
    return ("x" + String.valueOf(this.x) + "y" + String.valueOf(this.y)).hashCode();
  }
}

// represents a edge between two vertices
class Edge {
  Vertex from;
  Vertex to;
  int weight;
  boolean isWall;

  Edge(Vertex from, Vertex to, Random rand) {
    this.weight = rand.nextInt(10);
    this.isWall = true;
    this.from = from;
    this.to = to;
    this.from.connect(this);
    this.to.connect(this);
  }

  // constructor for test
  Edge() {
    this.weight = 0;
    this.from = null;
    this.to = null;
    this.isWall = true;
  }

  // changes the state of wall to false
  public void makePath() {
    this.isWall = false;
  }

  // makes the scene of edge based on its wall state
  WorldImage drawEdgeScene(int size, WorldImage currentImage) {
    if (isWall) {
      return this.from.drawVertexScene(size, currentImage, this.to, Color.BLACK);
    }
    else {
      return this.from.drawVertexScene(size, currentImage, this.to, Color.LIGHT_GRAY);
    }
  }
}

// represents a graph
class Graph {
  Utils u;
  int width;
  int height;
  Random rand;
  ArrayList<Vertex> allVertices;
  ArrayList<Edge> allEdges;
  ArrayList<Edge> edgesInTree;

  Vertex endVertex;
  Stack<Vertex> solutionPath;
  Queue<Vertex> passedVertices;
  ICollection<Vertex> worklist;
  HashMap<Vertex, Boolean> alreadySeen;
  HashMap<Vertex, Vertex> fromVertex;
  boolean isSearchEnd;

  Graph(Random rand, int width, int height) {
    this.u = new Utils();
    this.rand = rand;
    this.width = width;
    this.height = height;
    this.allVertices = new ArrayList<Vertex>();
    this.allEdges = new ArrayList<Edge>();
    this.makeStartingVertices();
    this.makeStartingEdges();
    this.edgesInTree = new ArrayList<Edge>();

    this.endVertex = new Vertex(width - 1, height - 1);
    this.solutionPath = new Stack<Vertex>();
    this.passedVertices = new Queue<Vertex>();
    this.alreadySeen = new HashMap<Vertex, Boolean>();
    for (Vertex v : this.allVertices) {
      this.alreadySeen.put(v, false);
    }
    this.fromVertex = new HashMap<Vertex, Vertex>();
    this.isSearchEnd = false;
  }

  // constructor for test
  Graph(Random rand, ArrayList<Vertex> vList, ArrayList<Edge> eList) {
    this.u = new Utils();
    this.rand = rand;
    this.width = 0;
    this.height = 0;
    this.allVertices = vList;
    this.allEdges = eList;
    this.edgesInTree = new ArrayList<Edge>();
    this.endVertex = new Vertex(width - 1, height - 1);
    this.solutionPath = new Stack<Vertex>();
    this.passedVertices = new Queue<Vertex>();
    this.alreadySeen = new HashMap<Vertex, Boolean>();
    for (Vertex v : this.allVertices) {
      this.alreadySeen.put(v, false);
    }
    this.fromVertex = new HashMap<Vertex, Vertex>();
    this.isSearchEnd = false;
  }

  // creates a list of vertices by given width and height
  void makeStartingVertices() {
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        this.allVertices.add(new Vertex(i, j));
      }
    }
  }

  // creates edges with given width, height and vertex list
  // automatically connects vertices and edges
  void makeStartingEdges() {
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        if (y < height - 1) {
          this.allEdges.add(new Edge(this.allVertices.get(y + (x * this.height)),
              this.allVertices.get(y + (x * this.height) + 1), this.rand));
        }
        if (x < width - 1) {
          this.allEdges.add(new Edge(this.allVertices.get(y + (x * this.height)),
              this.allVertices.get(y + (x * this.height) + height), this.rand));
        }
      }
    }
  }

  // makes the scene of walls
  WorldImage makeWallsScene(int size, int width, int height) {
    WorldImage image = new RectangleImage(width * size, height * size, OutlineMode.OUTLINE,
        Color.BLACK);
    for (Edge e : this.allEdges) {
      image = e.drawEdgeScene(size, image);
    }
    return image;
  }

  // builds a spanning tree using Kruskal's algorithm
  void makeMaze() {
    Comparator<Edge> edgeWeight = (Edge x, Edge y) -> x.weight - y.weight;

    int verticesSize = this.allVertices.size();
    HashMap<Vertex, Vertex> repo = new HashMap<Vertex, Vertex>();
    ArrayList<Edge> worklist = new ArrayList<Edge>(this.allEdges);

    this.u.mergesort(worklist, edgeWeight);

    for (int i = 0; i < verticesSize; i++) {
      repo.put(this.allVertices.get(i), this.allVertices.get(i));
    }

    int edgeNum = 0;
    while (edgeNum < verticesSize - 1) {
      Edge tempEdge = worklist.remove(0);
      // find the source set
      Vertex fromRepo = this.u.findSource(repo, tempEdge.from);
      Vertex toRepo = this.u.findSource(repo, tempEdge.to);

      // if the edge is not in cycle
      if (!fromRepo.equals(toRepo)) {
        this.edgesInTree.add(tempEdge);
        repo.replace(fromRepo, toRepo);
        edgeNum += 1;
      }
    }
  }

  // changes the wall state to false
  public void createPaths() {
    for (Edge e : this.edgesInTree) {
      e.makePath();
    }
  }

  // determines whether there is an edge with no wall
  public boolean isEdgeValid(int x1, int y1, int x2, int y2) {
    boolean result = false;
    for (Edge e : this.edgesInTree) {
      result = result || ((e.from.x == x1 && e.from.y == y1) && (e.to.x == x2 && e.to.y == y2))
          || ((e.from.x == x2 && e.from.y == y2) && (e.to.x == x1 && e.to.y == y1));
    }
    return result;
  }

  // sets fields for Bread First Search
  void bfs() {
    this.worklist = new Queue<Vertex>();
    this.worklist.add(this.allVertices.get(0));
    for (Vertex v : this.allVertices) {
      this.alreadySeen.put(v, false);
    }
    this.fromVertex = new HashMap<Vertex, Vertex>();
    this.isSearchEnd = false;
  }

  // sets fields for Depth First Search
  void dfs() {
    this.worklist = new Stack<Vertex>();
    this.worklist.add(this.allVertices.get(0));
    for (Vertex v : this.allVertices) {
      this.alreadySeen.put(v, false);
    }
    this.fromVertex = new HashMap<Vertex, Vertex>();
    this.isSearchEnd = false;
  }

  // does bfs or dfs depend on field set by bfs() or dfs()
  void search() {
    while (this.worklist.size() > 0 && !this.isSearchEnd) {
      Vertex next = this.worklist.remove();
      if (this.alreadySeen.get(next)) {
        // is next has already seen, pass it
      }
      else if (next.equals(this.endVertex)) {
        this.findSolutionPath(); // Success
        this.isSearchEnd = true;
      }
      else {
        for (Edge e : next.outgoing) {
          if (!e.isWall) {
            if (next.equals(e.from) && !this.alreadySeen.get(e.to)) {
              this.worklist.add(e.to);
              // saves the edge and where is goes in the map
              this.fromVertex.put(e.to, next);
            }
            if (next.equals(e.to) && !this.alreadySeen.get(e.from)) {
              this.worklist.add(e.from);
              // saves the edge and where is goes in the map
              this.fromVertex.put(e.from, next);
            }
          }
        }
        this.alreadySeen.put(next, true);
        this.passedVertices.add(next);
      }
    }
  }

  // finds the solution path by traverse passed vertices from end
  void findSolutionPath() {
    Vertex current = endVertex;
    this.solutionPath = new Stack<Vertex>();
    while (current.x != 0 || current.y != 0) {
      this.solutionPath.add(current);
      current = this.fromVertex.get(current);
    }
  }
}

// Class used for util methods
class Utils {
  // sorts the provided list using mergesort algorithm
  <T> void mergesort(ArrayList<T> list, Comparator<T> comp) {
    ArrayList<T> temp = new ArrayList<T>(list);
    mergesortHelp(list, temp, comp, 0, list.size() - 1);
  }

  // helper method of mergesort()
  <T> void mergesortHelp(ArrayList<T> list, ArrayList<T> temp, Comparator<T> comp, int low,
      int high) {
    if (low < high) {
      int mid = (low + high) / 2;

      mergesortHelp(list, temp, comp, low, mid);
      mergesortHelp(list, temp, comp, mid + 1, high);
      merge(list, temp, comp, low, mid, high);
    }
  }

  // merges the sorted sublists
  <T> void merge(ArrayList<T> list, ArrayList<T> temp, Comparator<T> comp, int low, int mid,
      int high) {
    int curLo = low;
    int curHi = mid + 1;
    int curCopy = low;

    while ((curLo <= mid) && (curHi <= high)) {
      int r = comp.compare(list.get(curLo), list.get(curHi));
      if (r <= 0) { // i<=j
        temp.set(curCopy, list.get(curLo));
        curLo += 1;
      }
      else {
        temp.set(curCopy, list.get(curHi));
        curHi += 1;
      }
      curCopy += 1;
    }

    while (curLo <= mid) {
      temp.set(curCopy, list.get(curLo));
      curLo += 1;
      curCopy += 1;
    }

    while (curHi <= mid) {
      temp.set(curCopy, list.get(curHi));
      curHi += 1;
      curCopy += 1;
    }

    for (curCopy = low; curCopy <= high; curCopy++) {
      list.set(curCopy, temp.get(curCopy));
    }
  }

  // finds the vertex in hashmap by the given vertex
  Vertex findSource(HashMap<Vertex, Vertex> repo, Vertex that) {
    Vertex v = that;
    Vertex mapTo = repo.get(v);
    // keep finding until key is equal to value
    while (!mapTo.equals(v)) {
      v = mapTo;
      mapTo = repo.get(v);
    }
    return v;
  }
}

// represents MazeWorld
class MazeWorld extends World {
  int width;
  int height;
  int size;
  Graph graph;
  WorldImage wallsImage;

  ArrayList<Posn> passedVertices;
  ArrayList<Posn> solutionVertices;
  boolean isRunning;
  int bfsScore;
  int dfsScore;
  int state; // 0 for no searching; 1 for bfs; 2 for dfs

  MazeWorld(int width, int height, int size) {
    this.width = width;
    this.height = height;
    this.size = size;
    this.graph = new Graph(new Random(), this.width, this.height);
    this.graph.makeMaze();
    this.graph.createPaths();
    this.wallsImage = this.graph.makeWallsScene(this.size, this.width, this.height);

    this.passedVertices = new ArrayList<Posn>();
    this.solutionVertices = new ArrayList<Posn>();
    this.isRunning = false;
    this.bfsScore = 0;
    this.dfsScore = 0;
  }

  @Override
  // overrides the default makeScene method
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(this.width * this.size / 2, this.width * this.size / 2);

    // draws background
    scene.placeImageXY(new RectangleImage(this.width * this.size, this.height * this.size,
        OutlineMode.SOLID, new Color(192, 192, 192)), this.width * this.size / 2,
        this.height * this.size / 2);

    // passed vertices with a gradient of colors
    int red = 255;
    int count = 0;
    for (Posn p : this.passedVertices) {
      if (red == 0) {
        red = 0;
      }
      else if (count == this.height * this.width / 255) {
        red -= 1;
        count = 0;
      }
      count += 1;
      scene.placeImageXY(
          new RectangleImage(this.size, this.size, "solid", new Color(red, 184, 242)),
          p.x * this.size + this.size / 2, p.y * this.size + this.size / 2);
    }

    // draws result path
    for (Posn p : this.solutionVertices) {
      scene.placeImageXY(new RectangleImage(this.size, this.size, "solid", new Color(61, 118, 204)),
          p.x * this.size + this.size / 2, p.y * this.size + this.size / 2);
    }

    // draws walls
    scene.placeImageXY(this.wallsImage, this.width * this.size / 2, this.height * this.size / 2);

    // marks start vertex
    scene.placeImageXY(
        new RectangleImage(this.size, this.size, OutlineMode.SOLID, new Color(32, 128, 70)),
        this.size / 2, this.size / 2);

    // marks end vertex
    scene.placeImageXY(
        new RectangleImage(this.size, this.size, OutlineMode.SOLID, new Color(108, 32, 128)),
        this.width * this.size - this.size / 2, this.height * this.size - this.size / 2);

    return scene;
  }

  @Override
  // overrides the default onTick() method
  public void onTick() {
    if (this.graph.passedVertices.size() > 0) {
      // calculates the score depends on state
      if (this.state == 1) {
        this.bfsScore += 1;
      }
      else if (this.state == 2) {
        this.dfsScore += 1;
      }

      Vertex v = this.graph.passedVertices.remove();
      Posn temp = v.getPosition();
      if (this.graph.alreadySeen.get(v) && !this.passedVertices.contains(temp)) {
        this.passedVertices.add(temp);
      }
    }
    else if (this.graph.solutionPath.size() > 0) {
      Posn temp = this.graph.solutionPath.remove().getPosition();
      this.solutionVertices.add(temp);
    }
    else {
      this.isRunning = false;
    }

    if (this.bfsScore > 0 && this.dfsScore > 0 && this.state != 0 && !this.isRunning) {
      System.out.println("BFS steps: " + Integer.toString(this.bfsScore) + "  DFS steps: "
          + Integer.toString(this.dfsScore));
      if (this.bfsScore < this.dfsScore) {
        System.out.println("On this maze, BFS is better than DFS");
      }
      else {
        System.out.println("On this maze, DFS is better than BFS");
      }
      this.state = 0;
    }
  }

  @Override
  // overrides the default onKeyEvent() method
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.dfsScore = 0;
      this.bfsScore = 0;
      this.state = 0;
      this.graph = new Graph(new Random(), this.width, this.height);
      this.graph.makeMaze();
      this.graph.createPaths();
      this.wallsImage = this.graph.makeWallsScene(this.size, this.width, this.height);
      this.passedVertices = new ArrayList<Posn>();
      this.solutionVertices = new ArrayList<Posn>();
      this.isRunning = false;
    }

    // no search key will be accepted when current one is running
    if (this.isRunning) {
      return;
    }
    if (key.equals("b")) {
      this.bfsScore = 0;
      this.state = 1;
      this.passedVertices = new ArrayList<Posn>();
      this.solutionVertices = new ArrayList<Posn>();
      this.graph.bfs();
      this.graph.search();
      this.isRunning = true;
    }
    if (key.equals("d")) {
      this.dfsScore = 0;
      this.state = 2;
      this.passedVertices = new ArrayList<Posn>();
      this.solutionVertices = new ArrayList<Posn>();
      this.graph.dfs();
      this.graph.search();
      this.isRunning = true;
    }
  }
}

// represents a mutable collection of items
abstract class ICollection<T> {
  Deque<T> d;

  ICollection() {
    this.d = new ArrayDeque<T>();
  }

  // determines whether this is empty
  abstract boolean isEmpty();

  // returns and removes that first item
  abstract T remove();

  // adds the item to the collection
  abstract void add(T item);

  // returns the size of this collection
  abstract int size();
}

// represents stack data structure
class Stack<T> extends ICollection<T> {

  // determines whether this is empty
  public boolean isEmpty() {
    return this.d.isEmpty();
  }

  // returns and removes that first item
  public T remove() {
    if (this.isEmpty()) {
      throw new RuntimeException("Invalid call: cannot remove from an empty stack");
    }
    return this.d.removeFirst();
  }

  // adds the item to the collection
  public void add(T item) {
    this.d.addFirst(item);
  }

  // returns the size of this stack
  public int size() {
    return this.d.size();
  }
}

// represents queue data structure
class Queue<T> extends ICollection<T> {

  // determines whether this is empty
  public boolean isEmpty() {
    return this.d.isEmpty();
  }

  // returns and removes that first item
  public T remove() {
    if (this.isEmpty()) {
      throw new RuntimeException("Invalid call: cannot remove from an empty queue");
    }
    return this.d.removeFirst();
  }

  // adds the item to the collection
  public void add(T item) {
    this.d.addLast(item);
  }

  // returns the size of this queue
  public int size() {
    return this.d.size();
  }
}

class ExamplesMaze {
  Random rand;
  Vertex v1;
  Vertex v2;
  Vertex v3;
  Edge e1;
  Edge e2;
  Edge e3;
  Graph g;

  ICollection<Vertex> c1;
  ICollection<Vertex> c2;
  ICollection<Vertex> c3;
  ICollection<Vertex> c4;

  public void initData() {
    this.rand = new Random();
    this.v1 = new Vertex(0, 0);
    this.v2 = new Vertex(0, 1);
    this.v3 = new Vertex(0, 2);
    this.e1 = new Edge(v1, v2, rand);
    this.e2 = new Edge(v1, v3, rand);
    this.e3 = new Edge(v2, v3, rand);
    ArrayList<Vertex> vList = new ArrayList<Vertex>();
    vList.add(v1);
    vList.add(v2);
    vList.add(v3);
    ArrayList<Edge> eList = new ArrayList<Edge>();
    eList.add(e1);
    eList.add(e2);
    eList.add(e3);
    this.g = new Graph(rand, vList, eList);

    this.c1 = new Stack<Vertex>();
    this.c2 = new Stack<Vertex>();
    this.c3 = new Queue<Vertex>();
    this.c4 = new Queue<Vertex>();

    this.c2.add(this.v1);
    this.c2.add(this.v2);
    this.c2.add(this.v3);

    this.c4.add(this.v1);
    this.c4.add(this.v2);
    this.c4.add(this.v3);
  }

  public void testGame(Tester t) {
    int width = 30;
    int height = 30;
    int blockSize;

    // generates adaptive block size depends on width and height
    if (height < 40 && width < 70) {
      blockSize = 20;
    }
    else if (height < 50 && width < 90) {
      blockSize = 16;
    }
    else {
      blockSize = 10;
    }

    MazeWorld m = new MazeWorld(width, height, blockSize);
    m.bigBang(width * blockSize, height * blockSize, 1.0 / 100.0);
  }

  public void testConnect(Tester t) {
    Vertex test1 = new Vertex(0, 0);
    Edge test2 = new Edge();
    t.checkExpect(test1.outgoing.size(), 0);
    test1.connect(test2);
    t.checkExpect(test1.outgoing.get(0), test2);
  }

  public void testGetPosition(Tester t) {
    initData();
    Posn result1 = new Posn(0, 0);
    Posn result2 = new Posn(0, 1);
    t.checkExpect(this.v1.getPosition(), result1);
    t.checkExpect(this.v2.getPosition(), result2);
  }

  public void testEquals(Tester t) {
    initData();
    t.checkExpect(this.v1.equals(this.e1), false);
    t.checkExpect(this.v1.equals(this.v2), false);
    t.checkExpect(this.v1.equals(this.v1), true);
  }

  public void testHashCode(Tester t) {
    initData();
    t.checkExpect(this.v1.hashCode(), v1.id.hashCode());
    t.checkExpect(this.v2.hashCode(), v2.id.hashCode());
  }

  public void testMakePath(Tester t) {
    Edge test2 = new Edge();
    t.checkExpect(test2.isWall, true);
    test2.makePath();
    t.checkExpect(test2.isWall, false);
  }

  public void testGraphConstructor(Tester t) {
    Graph test = new Graph(new Random(), 10, 10);
    t.checkExpect(test.allEdges.size(), 180);
    t.checkExpect(test.allVertices.size(), 100);
  }

  public void testMergesort(Tester t) {
    ArrayList<Integer> source = new ArrayList<Integer>(Arrays.asList(2, 5, 1, 98, 67, 13, 8));
    ArrayList<Integer> sorted = new ArrayList<Integer>(Arrays.asList(1, 2, 5, 8, 13, 67, 98));
    Comparator<Integer> comp = (Integer x, Integer y) -> x - y;
    new Utils().mergesort(source, comp);
    t.checkExpect(source, sorted);
  }

  public void testFind(Tester t) {
    initData();
    HashMap<Vertex, Vertex> testmap = new HashMap<Vertex, Vertex>();
    testmap.put(v1, v2);
    testmap.put(v2, v3);
    testmap.put(v3, v3);
    t.checkExpect(new Utils().findSource(testmap, v1), v3);
    t.checkExpect(new Utils().findSource(testmap, v2), v3);
  }

  // tests method isEmpty for ICollection
  void testIsEmpty(Tester t) {
    initData();
    t.checkExpect(this.c1.isEmpty(), true);
    t.checkExpect(this.c2.isEmpty(), false);
    t.checkExpect(this.c3.isEmpty(), true);
    t.checkExpect(this.c4.isEmpty(), false);
  }

  // tests method remove for ICollection
  void testRemove(Tester t) {
    initData();
    t.checkException(new RuntimeException("Invalid call: cannot remove from an empty stack"),
        this.c1, "remove");
    t.checkExpect(this.c2.remove(), this.v3);

    t.checkException(new RuntimeException("Invalid call: cannot remove from an empty queue"),
        this.c3, "remove");
    t.checkExpect(this.c4.remove(), this.v1);

  }

  // tests method add for ICollection
  void testAdd(Tester t) {
    initData();
    t.checkExpect(this.c1.isEmpty(), true);
    this.c1.add(v1);
    t.checkExpect(this.c1.isEmpty(), false);

    t.checkExpect(this.c3.isEmpty(), true);
    this.c3.add(v1);
    t.checkExpect(this.c3.isEmpty(), false);
  }

  void testCreatePaths(Tester t) {
    Graph test = new Graph(new Random(), 10, 10);
    boolean temp = false;

    for (Edge e : test.allEdges) {
      temp = temp || !e.isWall;
    }
    t.checkExpect(temp, false);

    test.createPaths();

    for (Edge e : test.allEdges) {
      temp = temp || !e.isWall;
    }
    t.checkExpect(temp, false);
  }

  void testIsEdgeValid(Tester t) {
    initData();
    t.checkExpect(this.g.isEdgeValid(0, 0, 0, 1), false);
    t.checkExpect(this.g.isEdgeValid(0, 0, 0, 2), false);
  }

  void testOnKeyEvent(Tester t) {
    MazeWorld test = new MazeWorld(10, 10, 5);
    t.checkExpect(test.passedVertices, new ArrayList<Posn>());
    test.onKeyEvent("r");
    t.checkExpect(test.passedVertices, new ArrayList<Posn>());
    test.onKeyEvent("b");
    t.checkExpect(test.passedVertices, new ArrayList<Posn>());
    test.onKeyEvent("r");
    t.checkExpect(test.passedVertices, new ArrayList<Posn>());
    test.onKeyEvent("d");
    t.checkExpect(test.passedVertices, new ArrayList<Posn>());
  }
}
