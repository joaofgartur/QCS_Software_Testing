package io.nayuki.flac.encode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiceEncoderControlFlowTest {

    @Test
    public void testCase1() {
        long data[] = null;
        int start = 0;
        int end = 0;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase2() {
        long data[] = {10};
        int start = 0;
        int end = 1;

        long result = 643;

        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase3() {
        long data[] = {10, 20};
        int start = 0;
        int end = 2;

        long result = 1092;

        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

}