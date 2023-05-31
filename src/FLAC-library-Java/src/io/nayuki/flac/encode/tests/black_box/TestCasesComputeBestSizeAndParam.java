package io.nayuki.flac.encode.tests.black_box;

import io.nayuki.flac.encode.RiceEncoder;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestCasesComputeBestSizeAndParam {

    @Test
    public void testCase24() {
        long[] data = {};
        int start = 0;
        int end = 0;

        long expected = 256;
        long result = RiceEncoder.computeBestSizeAndParam(data, start, end);

        assertEquals(expected, result);
    }

    @Test
    public void testCase25() {
        int size = 199491582;
        long[] data = new long[size];
        for (int i = 0; i < size; i++) {
            data[i] = i + 1;
        }

        int start = 0;
        int end = data.length;
        long result = RiceEncoder.computeBestSizeAndParam(data, start, end);

        assertEquals(Long.class, Long.valueOf(result).getClass());
    }

    @Test
    public void testCase26() {
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int start = -1;
        int end = 1;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase27() {
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int start = 0;
        int end = 1;

        long expected = 448;
        long result = RiceEncoder.computeBestSizeAndParam(data, start, end);

        assertEquals(expected, result);

    }

    @Test
    public void testCase28() {
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int start = 1;
        int end = 1;

        long expected = 256;
        long result = RiceEncoder.computeBestSizeAndParam(data, start, end);

        assertEquals(expected, result);

    }

    @Test
    public void testCase29() {
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int start = 1;
        int end = 0;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase30() {
        long[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int start = 0;
        int end = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }


}
