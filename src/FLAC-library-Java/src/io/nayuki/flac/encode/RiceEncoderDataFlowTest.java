package io.nayuki.flac.encode;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class RiceEncoderDataFlowTest {

    public String readBinaryFileIntoString(String filename) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
        StringBuilder hexString = new StringBuilder();
        for (byte b : fileBytes) {
            String hex = String.format("%02X", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /* ---- param ---- */
    @Test
    void testCase1() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase2() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = 32;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    /* ---- param & val & unsigned ---- */
    @Test
    void testCase3() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 100;
        int param = 10;

        assertEquals(0, output.getByteCount());
        /*
        RiceEncodedObject result = RiceEncoder.writeRiceSignedInt(val, param, output);

        assertEquals(0, result.getUnary());
        assertEquals(200, result.getUnsigned());
        assertTrue(output.getByteCount() > 0);
        */
    }

    /* ---- val ---- */
    @Test
    void testCase4() throws IOException {
        BitOutputStream output = new BitOutputStream(new FileOutputStream("testCase.txt"));
        long val = 9007199254740993L;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    /* ---- val & unsigned ---- */
    @Test
    void testCase5() throws IOException {
        String filename = "testCase5.bin";

        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = -100;
        int param = 10;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        while (output.getBitBufferLen() % 8 != 0) output.writeInt(1, 0);
        output.close();

        String expected  = "98E0";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
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
        /*
        RiceEncodedObject result = RiceEncoder.writeRiceSignedInt(val, param, output);

        assertEquals(2, result.getUnary());
        assertEquals(2048, result.getUnsigned());
        assertTrue(output.getByteCount() > 0);
         */
    }

}