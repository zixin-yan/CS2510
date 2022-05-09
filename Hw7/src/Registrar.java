import tester.*;

interface IList<T> {
  // determines whether two lists have same item
  boolean sameItem(IList<T> other);

  // helper method of sameItem
  boolean sameItemHelper(T that);

  // counts the number of same items in two lists
  int countSameItem(IList<T> other);
}

class MtList<T> implements IList<T> {
  MtList() {
  }

  // determines whether two lists have same item
  public boolean sameItem(IList<T> l) {
    return false;
  }

  // helper method of sameItem
  public boolean sameItemHelper(T that) {
    return false;
  }

  // counts the number of same items in two lists
  public int countSameItem(IList<T> other) {
    return 0;
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // determines whether two lists have same item
  public boolean sameItem(IList<T> l) {
    return l.sameItemHelper(this.first) || this.rest.sameItem(l);
  }

  // helper method of sameItem
  public boolean sameItemHelper(T that) {
    return this.first.equals(that) || this.rest.sameItemHelper(that);
  }

  // counts the number of same items in two lists
  public int countSameItem(IList<T> other) {
    if (other.sameItemHelper(this.first)) {
      return 1 + this.rest.countSameItem(other);
    }
    else {
      return this.rest.countSameItem(other);
    }
  }
}

class Course {
  String name;
  Instructor prof;
  IList<Student> students;

  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new MtList<Student>();
    this.prof.updateCourse(this);
  }

  Course(String name, Instructor prof, IList<Student> students) {
    this.name = name;
    this.prof = prof;
    this.students = students;
    this.prof.updateCourse(this);
  }

  // add student to student list, as a helper method for enroll()
  void addStudent(Student s) {
    this.students = new ConsList<Student>(s, this.students);
  }
}

class Instructor {
  String name;
  IList<Course> courses;

  Instructor(String name, IList<Course> courses) {
    this.name = name;
    this.courses = courses;
  }

  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<Course>();
  }

  void updateCourse(Course that) {
    if (!that.prof.equals(this)) {
      throw new RuntimeException("Course is not tought by this instructor");
    }
    else {
      this.courses = new ConsList<Course>(that, this.courses);
    }
  }

  // determines whether the given Student is in more than one of this Instructor's
  // Courses
  boolean dejavu(Student c) {
    return this.courses.countSameItem(c.courses) >= 2;
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;

  Student(String name, int id, IList<Course> courses) {
    this.name = name;
    this.id = id;
    this.courses = courses;
  }

  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<Course>();
  }

  // enrolls a Student in the given Course
  void enroll(Course c) {
    c.addStudent(this);
    this.courses = new ConsList<Course>(c, this.courses);
  }

  // determines whether the given Student is in any of the same classes as this
  // Student
  boolean classmates(Student c) {
    return this.courses.sameItem(c.courses);
  }
}

class ExamplesRegistrar {

  Course c1;
  Course c2;
  Course c3;
  Course c4;
  Instructor i1;
  Instructor i2;
  Student s1;
  Student s2;
  Student s3;
  Student s4;
  Student s5;

  void initConditions() {
    this.i1 = new Instructor("Hemm");
    this.i2 = new Instructor("Vido");

    this.s1 = new Student("Alex", 1);
    this.s2 = new Student("Brand", 2);
    this.s3 = new Student("Clair", 3);
    this.s4 = new Student("David", 4);
    this.s5 = new Student("Eva", 5);

    this.c1 = new Course("CS2510", this.i1,
        new ConsList<Student>(this.s1,
            new ConsList<Student>(this.s2,
                new ConsList<Student>(this.s3, new ConsList<Student>(this.s4,
                    new ConsList<Student>(this.s5, new MtList<Student>()))))));
    this.c2 = new Course("CS2511", this.i2,
        new ConsList<Student>(this.s2, new ConsList<Student>(this.s3, new MtList<Student>())));
    this.c3 = new Course("CS2500", this.i1,
        new ConsList<Student>(this.s1, new ConsList<Student>(this.s3, new MtList<Student>())));
    this.c4 = new Course("CS2501", this.i2);

    this.s1.courses = new ConsList<Course>(this.c1,
        new ConsList<Course>(this.c2, new ConsList<Course>(this.c3, new MtList<Course>())));
    this.s2.courses = new ConsList<Course>(this.c1,
        new ConsList<Course>(this.c2, new ConsList<Course>(this.c4, new MtList<Course>())));
    this.s3.courses = new ConsList<Course>(this.c2,
        new ConsList<Course>(this.c3, new MtList<Course>()));
    this.s4.courses = new ConsList<Course>(this.c1, new MtList<Course>());
    this.s5.courses = new ConsList<Course>(this.c1, new MtList<Course>());

    this.i1.courses = new ConsList<Course>(this.c1,
        new ConsList<Course>(this.c3, new MtList<Course>()));
    this.i2.courses = new ConsList<Course>(this.c2,
        new ConsList<Course>(this.c4, new MtList<Course>()));
  }

  void testUpdateCourse(Tester t) {
    initConditions();

    t.checkException(new RuntimeException("Course is not tought by this instructor"), this.i1,
        "updateCourse", this.c2);
  }

  void testAddStudent(Tester t) {
    initConditions();

    this.c3.addStudent(s4);
    t.checkExpect(this.c3.students, new ConsList<Student>(this.s4,
        new ConsList<Student>(this.s1, new ConsList<Student>(this.s3, new MtList<Student>()))));
    this.c4.addStudent(s4);
    t.checkExpect(this.c4.students, new ConsList<Student>(this.s4, new MtList<Student>()));
  }

  void testEnroll(Tester t) {
    initConditions();

    this.s5.enroll(this.c4);
    t.checkExpect(this.c4.students, new ConsList<Student>(this.s5, new MtList<Student>()));
    t.checkExpect(this.s5.courses,
        new ConsList<Course>(this.c4, new ConsList<Course>(this.c1, new MtList<Course>())));
    this.s4.enroll(this.c4);
    t.checkExpect(this.c4.students,
        new ConsList<Student>(this.s4, new ConsList<Student>(this.s5, new MtList<Student>())));
    t.checkExpect(this.s4.courses,
        new ConsList<Course>(this.c4, new ConsList<Course>(this.c1, new MtList<Course>())));
  }

  void testClassmates(Tester t) {
    initConditions();

    t.checkExpect(this.s1.classmates(this.s2), true);
    t.checkExpect(this.s3.classmates(this.s4), false);
    t.checkExpect(this.s4.classmates(this.s5), true);
  }

  void testDejavu(Tester t) {
    initConditions();

    t.checkExpect(this.i1.dejavu(this.s1), true);
    t.checkExpect(this.i2.dejavu(this.s2), true);
    t.checkExpect(this.i1.dejavu(this.s3), false);
  }

  void testSameItem(Tester t) {
    IList<String> strings1 = new ConsList<String>("a", new MtList<String>());
    IList<String> strings2 = new ConsList<String>("b", new MtList<String>());
    IList<String> MtStrings = new MtList<String>();

    t.checkExpect(strings1.sameItem(strings1), true);
    t.checkExpect(strings1.sameItem(strings2), false);
    t.checkExpect(strings1.sameItem(MtStrings), false);
    t.checkExpect(MtStrings.sameItem(strings1), false);
  }

  void testSameItemHelper(Tester t) {
    IList<String> strings1 = new ConsList<String>("a", new MtList<String>());
    IList<String> strings2 = new ConsList<String>("b", new MtList<String>());
    IList<String> MtStrings = new MtList<String>();

    t.checkExpect(strings1.sameItemHelper("a"), true);
    t.checkExpect(strings2.sameItemHelper("a"), false);
    t.checkExpect(MtStrings.sameItemHelper("a"), false);
  }

  void testCountSameItem(Tester t) {
    IList<String> strings1 = new ConsList<String>("a",
        new ConsList<String>("b", new MtList<String>()));
    IList<String> strings2 = new ConsList<String>("b", new MtList<String>());
    IList<String> MtStrings = new MtList<String>();

    t.checkExpect(strings1.countSameItem(strings1), 2);
    t.checkExpect(strings1.countSameItem(strings2), 1);
    t.checkExpect(strings1.countSameItem(MtStrings), 0);
    t.checkExpect(MtStrings.countSameItem(strings1), 0);
  }
}