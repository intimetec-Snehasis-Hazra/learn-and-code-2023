package LNC_Assignment_1;
import org.junit.Test;
import static org.junit.Assert.*;

public class KaprekarIterationsTest {

    @Test
    public void testValidInput() {
        assertEquals(3, KaprekarIterations.calculateIterations(3524));
    }

    @Test
    public void testInvalidInputSameDigit() {
        assertEquals(0, KaprekarIterations.calculateIterations(1111));
    }

    @Test
    public void testInvalidInputLength() {
        assertEquals(0, KaprekarIterations.calculateIterations(123));
    }

    @Test
    public void testInvalidInputNegativeNumber() {
        assertEquals(0, KaprekarIterations.calculateIterations(-1234));
    }

    @Test
    public void testZeroInput() {
        assertEquals(0, KaprekarIterations.calculateIterations(0));
    }

    @Test
    public void testKaprekarConstant() {
        assertEquals(0, KaprekarIterations.calculateIterations(6174));
    }

    @Test
    public void testValidInputReverseOrder() {
        assertEquals(3, KaprekarIterations.calculateIterations(4321));
    }

    @Test
    public void testValidInputDescendingOrder() {
        assertEquals(3, KaprekarIterations.calculateIterations(9876));
    }

    @Test
    public void testGetDigitsArray() {
        assertArrayEquals(new int[]{1, 2, 3, 4}, KaprekarIterations.getDigitsArray(1234));
    }

    @Test
    public void testGetNumberFromDigits() {
        assertEquals(1234, KaprekarIterations.getNumberFromDigits(new int[]{1, 2, 3, 4}));
    }

    @Test
    public void testReverseArray() {
        assertArrayEquals(new int[]{4, 3, 2, 1}, KaprekarIterations.reverseArray(new int[]{1, 2, 3, 4}));
    }
}
