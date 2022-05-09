import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
    this.alphabet = new ArrayList<Character>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> available = this.alphabet;

    for (int i = 0; i < 26; i++) {
      int randChar = this.rand.nextInt(26 - i);
      this.code.add(available.get(randChar));
      available.remove(available.get(randChar));
    }

    return this.code;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    ArrayList<Character> explode = new ArrayList<Character>();

    for (int i = 0; i < source.length(); i = i + 1) {
      explode.add(i, source.charAt(i));
    }

    for (int i = 0; i < explode.size(); i = i + 1) {
      explode.set(i, this.code.get(this.alphabet.indexOf(explode.get(i))));
    }

    String encoded = "";

    for (int i = 0; i < explode.size(); i = i + 1) {
      encoded = encoded + explode.get(i);
    }

    return encoded;
  }

  // produce a decoded String from the given String
  String decode(String code) {
    ArrayList<Character> explode = new ArrayList<Character>();

    for (int i = 0; i < code.length(); i = i + 1) {
      explode.add(code.charAt(i));
    }

    for (int i = 0; i < explode.size(); i = i + 1) {
      explode.set(i, this.alphabet.get(this.code.indexOf(explode.get(i))));
    }

    String decoded = "";

    for (int i = 0; i < explode.size(); i = i + 1) {
      decoded = decoded + explode.get(i);
    }

    return decoded;
  }
}

class ExamplesPermutationCode {
  ArrayList<Character> code1 = new ArrayList<Character>(
      Arrays.asList('b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
          'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'a'));
  PermutationCode pc1 = new PermutationCode(code1);
  PermutationCode pc2 = new PermutationCode();

  void testEncode(Tester t) {
    t.checkExpect(pc1.encode(""), "");
    t.checkExpect(pc1.encode("a"), "b");
    t.checkExpect(pc1.encode("hello"), "ifmmp");
  }

  void testDecode(Tester t) {
    t.checkExpect(pc1.decode(""), "");
    t.checkExpect(pc1.decode("b"), "a");
    t.checkExpect(pc1.decode("ifmmp"), "hello");
    t.checkExpect(pc1.decode(pc1.encode("hello")), "hello");
  }

  void testInitEncoder(Tester t) {
    t.checkExpect(pc2.decode(pc2.encode("")), "");
    t.checkExpect(pc2.decode(pc2.encode("abc")), "abc");
  }
}