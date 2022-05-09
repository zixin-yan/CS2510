import tester.*;
import java.util.function.Function;
import java.util.function.BiFunction;

//returns the negative of the given value
class Neg implements Function<Double, Double> {
  public Double apply(Double arg) { 
    return -arg; 
  }
}

//squares the given value
class Sqr implements Function<Double, Double> {
  public Double apply(Double arg) { 
    return Math.pow(arg, 2); 
  }
}

//adds two inputs
class Plus implements BiFunction<Double, Double, Double> {
  public Double apply(Double arg1, Double arg2) { 
    return arg1 + arg2; 
  }
}

//subtracts second input from first
class Minus implements BiFunction<Double, Double, Double> {
  public Double apply(Double arg1, Double arg2) { 
    return arg1 - arg2; 
  }
}

//multiplies two inputs
class Mult implements BiFunction<Double, Double, Double> {
  public Double apply(Double arg1, Double arg2) { 
    return arg1 * arg2; 
  }
}

//divides first input by second
class Div implements BiFunction<Double, Double, Double> {
  public Double apply(Double arg1, Double arg2) { 
    return arg1 / arg2; 
  }
}

//interface of arithmetic functions
interface IArith {
  //allows for visitors to access IArith
  <R> R accept(IArithVisitor<R> visitor);
}

//represents a number
class Const implements IArith {
  double num; //value
  
  Const(double num) {
    this.num = num;
  }
  
  public <Double> Double accept(IArithVisitor<Double> visitor) {
    return visitor.visitIArithConst(this);
  }
}

//represents a function with one IArith as an input
class UnaryFormula implements IArith {
  Function<Double, Double> func;//function
  String name;//title
  IArith child;//IArith the function acts on
  
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }
  
  public <Double> Double accept(IArithVisitor<Double> visitor) {
    return visitor.visitIArithUnaryFormula(this);
  }
}

//represents a function with two IArith as inputs
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;//function
  String name;//title
  IArith left;//first input
  IArith right;//second input
  
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }
  
  public <Double> Double accept(IArithVisitor<Double> visitor) {
    return visitor.visitIArithBinaryFormula(this);
  }
}

interface IArithVisitor<R> extends Function<IArith, R> {
  R visitIArithConst(Const c); //visits Const class
  
  R visitIArithUnaryFormula(UnaryFormula u); //visits UnaryFormula class
  
  R visitIArithBinaryFormula(BinaryFormula b); //visits BinaryFormula class
}

//evaluates the IArith to a Double value
class EvalVisitor implements IArithVisitor<Double> {
  EvalVisitor() {}
  
  //evalutes the value of the Const
  public Double visitIArithConst(Const c) { 
    return c.num;
  }
  
  //applies the function to the given input
  public Double visitIArithUnaryFormula(UnaryFormula u) {
    return u.func.apply(u.child.accept(new EvalVisitor()));
  }
  
  //applies the functions with left as first input and right as second
  public Double visitIArithBinaryFormula(BinaryFormula b) {
    return b.func.apply(b.left.accept(new EvalVisitor()), b.right.accept(new EvalVisitor()));
  }
  
  public Double apply(IArith a) { 
    return a.accept(this); 
  }
}

//prints the formula in Racket notation
class PrintVisitor implements IArithVisitor<String> {
  PrintVisitor() {}
  
  //prints value of Const as a String
  public String visitIArithConst(Const c) { 
    return Double.toString(c.num); 
  }
  
  //prints UnaryFormula in Racket notation
  public String visitIArithUnaryFormula(UnaryFormula u) {
    return "(" + u.name +  " " + new PrintVisitor().apply(u.child) + ")";
  }
  
  //prints BinaryFormula in Racket notation
  public String visitIArithBinaryFormula(BinaryFormula b) {
    return "(" + b.name + " " + new PrintVisitor().apply(b.left) 
        + " " + new PrintVisitor().apply(b.right) + ")"; 
  }
  
  public String apply(IArith a) { 
    return a.accept(this); 
  }
}

//doubles all numbers in the formula
class DoublerVisitor implements IArithVisitor<IArith> {
  DoublerVisitor() {}
  
  //doubles the value of the constant
  public IArith visitIArithConst(Const c) { 
    return new Const(c.num * 2); 
  }
  
  //applies the method to child
  public IArith visitIArithUnaryFormula(UnaryFormula u) {
    return new UnaryFormula(u.func, u.name, new DoublerVisitor().apply(u.child));
  }
  
  //applies the method to left and right
  public IArith visitIArithBinaryFormula(BinaryFormula b) {
    return new BinaryFormula(
        b.func, b.name, new DoublerVisitor().apply(b.left), new DoublerVisitor().apply(b.right));
  }
  
  public IArith apply(IArith a) { 
    return a.accept(this); 
  }
}

//determines if there are no negative numbers in the formula
class NoNegativeResults implements IArithVisitor<Boolean> {
  NoNegativeResults() {}
  
  //determines if the value of the Const is negative
  public Boolean visitIArithConst(Const c) { 
    return (c.num >= 0); 
  }
  
  //determines if func is negation or is there is a negative in child
  public Boolean visitIArithUnaryFormula(UnaryFormula u) {
    return ((u.func.apply(1.0) != -1.0) && new NoNegativeResults().apply(u.child));
  }
  
  //determines if there is a negative in left or right
  public Boolean visitIArithBinaryFormula(BinaryFormula b) {
    return (new NoNegativeResults().apply(b.left) 
        && new NoNegativeResults().apply(b.right)
        && new EvalVisitor().apply(b) >= 0);
  }
  
  public Boolean apply(IArith a) { 
    return a.accept(this); 
  }
}

class ExamplesIAriths {
  
  Neg neg = new Neg();
  Sqr sqr = new Sqr();
  Plus add = new Plus();
  Minus sub = new Minus();
  Mult mul = new Mult();
  Div div = new Div();
  
  EvalVisitor Eval = new EvalVisitor();
  PrintVisitor Print = new PrintVisitor();
  DoublerVisitor Doubler = new DoublerVisitor();
  NoNegativeResults NoNeg = new NoNegativeResults();
  
  Const c1 = new Const(1.0);
  Const c2 = new Const(2.0);
  Const c3 = new Const(3.0);
  Const c4 = new Const(4.0);
  
  UnaryFormula u1 = new UnaryFormula(neg, "neg", c1);
  UnaryFormula u2 = new UnaryFormula(neg, "neg", c2);
  UnaryFormula u3 = new UnaryFormula(sqr, "sqr", u2);
  UnaryFormula u4 = new UnaryFormula(sqr, "sqr", c3);
  
  BinaryFormula b1 = new BinaryFormula(add, "plus", c1, u2);
  BinaryFormula b2 = new BinaryFormula(sub, "minus", c1, u1);
  BinaryFormula b3 = new BinaryFormula(mul, "mul", c2, c4);
  BinaryFormula b4 = new BinaryFormula(div, "div", b3, u2);
  
  void testEvalVisitor(Tester t) {
    t.checkExpect(Eval.apply(c1), 1.0);
    t.checkExpect(Eval.apply(b4), -4.0);
    t.checkExpect(Eval.apply(u3), 4.0);
  }
  
  void testPrintVisitor(Tester t) {
    t.checkExpect(Print.apply(c2), "2.0");
    t.checkExpect(Print.apply(u4), "(sqr 3.0)");
    t.checkExpect(Print.apply(b4), "(div (mul 2.0 4.0) (neg 2.0))");
  }
  
  void testDoublerVisiotr(Tester t) {
    t.checkExpect(Doubler.apply(c1), c2);
    t.checkExpect(Doubler.apply(u1), u2);
    t.checkExpect(Doubler.apply(b3), 
        new BinaryFormula(mul, "mul", c4, new Const(8.0)));
  }
  
  void testNoNegativeResults(Tester t) {
    t.checkExpect(NoNeg.apply(c3), true);
    t.checkExpect(NoNeg.apply(u3), false);
    t.checkExpect(NoNeg.apply(b4), false);
  }
}