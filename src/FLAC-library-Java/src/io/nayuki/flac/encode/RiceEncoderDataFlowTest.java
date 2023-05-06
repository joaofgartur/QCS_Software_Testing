package io.nayuki.flac.encode;

import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RiceEncoderDataFlowTest {

    /* ---- param ---- */
    @org.junit.jupiter.api.Test
    void testCase1() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @org.junit.jupiter.api.Test
    void testCase2() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = 32;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    /* ---- param & val & unsigned ---- */
    @org.junit.jupiter.api.Test
    void testCase3() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = 10;

        assertEquals(0, output.getByteCount());
        RiceEncodedObject result = RiceEncoder.writeRiceSignedInt(val, param, output);

        assertEquals(0, result.getUnary());
        assertEquals(200, result.getUnsigned());
        assertTrue(output.getByteCount() > 0);
    }

    /* ---- val ---- */
    @org.junit.jupiter.api.Test
    void testCase4() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 9007199254740993L;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    /* ---- val & unsigned ---- */
    @org.junit.jupiter.api.Test
    void testCase5() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = -100;
        int param = 10;

        assertEquals(0, output.getByteCount());
        RiceEncodedObject result = RiceEncoder.writeRiceSignedInt(val, param, output);

        assertEquals(0, result.getUnary());
        assertEquals(199, result.getUnsigned());
        assertTrue(output.getByteCount() > 0);
    }

    /* ---- val ---- */
    @org.junit.jupiter.api.Test
    void testCase6() {
        BitOutputStream output = null;
        long val = 100;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    /* ---- val & unary & i---- */
    @org.junit.jupiter.api.Test
    void testCase7() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 1024;
        int param = 10;

        assertEquals(0, output.getByteCount());
        RiceEncodedObject result = RiceEncoder.writeRiceSignedInt(val, param, output);

        assertEquals(2, result.getUnary());
        assertEquals(2048, result.getUnsigned());
        assertTrue(output.getByteCount() > 0);
    }

}