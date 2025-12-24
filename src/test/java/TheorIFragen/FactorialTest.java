package TheorIFragen;

import static org.junit.jupiter.api.Assertions.*;

import TheorieFr.FragenTeil5.Factorial;
import org.junit.jupiter.api.Test;

public class FactorialTest {

    @Test
    void testFactorialOfZero() {
        //Message wird nur angezeigt wenn der Test scheitert
        assertEquals(1, Factorial.factorial(0),"Falsches Ergebnis für 0!");
    }

    @Test
    void testFactorialOfPositiveNumber() {
        assertEquals(120, Factorial.factorial(5), "Falsches Ergebnis für 5!");
    }

    @Test
    void testFactorialOfOne() {
        assertEquals(1, Factorial.factorial(1));
    }

    @Test
    void testLargeFactorial() {
        assertEquals(3628800L, Factorial.factorial(10));
    }

    @Test
    void testNegativeInputThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(-3));
    }
}
