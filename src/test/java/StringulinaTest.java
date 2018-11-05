import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StringulinaTest {
  @Test
  public void substringPos_noNeedleFound() {
    final int expected = -1;
    final int actual = Stringulina.substringPos("abc", "d");

    assertEquals(actual, expected);
  }

  @Test
  public void substringPos_needleFound() {
    final int expected = 3;
    final int actual = Stringulina.substringPos("abcd", "d");

    assertEquals(actual, expected);
  }

  @Test
  public void countSubstring_noOccurrences() {
    final int expected = 0;
    final int actual = Stringulina.countSubstring("aaaa", "b");

    assertEquals(actual, expected);
  }

  @Test
  public void countSubstring_someOccurrences() {
    final int expected = 3;
    final int actual = Stringulina.countSubstring("aaaa", "aa");

    assertEquals(actual, expected);
  }

  @Test
  public void correctlyBracketed_exampleOne() {
    final boolean expected = true;
    final boolean actual = Stringulina.correctlyBracketed("a(xx(]))");

    assertEquals(actual, expected);
  }

  @Test
  public void correctlyBracketed_exampleTwo() {
    final boolean expected = false;
    final boolean actual = Stringulina.correctlyBracketed("a(xx))");

    assertEquals(actual, expected);
  }

  @Test
  public void correctlyBracketed_exampleThree() {
    final boolean expected = false;
    final boolean actual = Stringulina.correctlyBracketed("a(xx)(");

    assertEquals(actual, expected);
  }

  @Test
  public void correctlyBracketed_exampleFour() {
    final boolean expected = false;
    final boolean actual = Stringulina.correctlyBracketed("a)xx()(");

    assertEquals(actual, expected);
  }

  @Test
  public void matches_exampleOne() {
    final boolean expected = true;
    final boolean actual = Stringulina.matches("Pijnguin", "P.{2}nguin");

    assertEquals(actual, expected);
  }

  @Test
  public void matches_exampleTwo() {
    final boolean expected = false;
    final boolean actual = Stringulina.matches("Pijknguin", "P.{2}nguin");

    assertEquals(actual, expected);
  }

  @Test
  public void matches_exampleThree() {
    final boolean expected = true;
    final boolean actual = Stringulina.matches("Haaaaaaaaaawko", "Ha{10}..o");

    assertEquals(actual, expected);
  }

  @Test
  public void matches_exampleFour() {
    final boolean expected = false;
    final boolean actual = Stringulina.matches("Haaawko", "Ha{10}..o");

    assertEquals(actual, expected);
  }
}