package io.nayuki.flac.encode.tests.white_box.control_flow;

import io.nayuki.flac.encode.RiceEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCasesComputeBestSizeAndParam {

    @Test
    public void testCase4() {
        long[] data = null;
        int start = 0;
        int end = 0;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase5() {
        long[] data = {1};
        int start = 0;
        int end = 1;

        long result = 448;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase6() {
        long[] data = {10, 20};
        int start = 0;
        int end = 2;

        long result = 1092;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase7() {
        long[] data = {-1};
        int start = 0;
        int end = 1;

        long result = 384;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase8() {
        long[] data = {-10, -5};
        int start = 0;
        int end = 2;

        long result = 963;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

}
