package io.nayuki.flac.encode;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RiceEncoderDataFlowTest {

    @org.junit.jupiter.api.Test
    void testCase1() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

}