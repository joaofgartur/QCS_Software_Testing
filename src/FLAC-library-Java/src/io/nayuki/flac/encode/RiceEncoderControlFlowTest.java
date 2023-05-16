package io.nayuki.flac.encode;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class RiceEncoderControlFlowTest {

    public String readBinaryFileIntoString(String filename) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
        StringBuilder hexString = new StringBuilder();
        for (byte b : fileBytes) {
            String hex = String.format("%02X", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Test
    public void testCase1() {
        long data[] = null;
        int start = 0;
        int end = 0;

        assertThrows(AssertionError.class, () -> RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase2() {
        long data[] = {1};
        int start = 0;
        int end = 1;

        long result = 448;
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

    @Test
    public void testCase4() {
        long data[] = {-1};
        int start = 0;
        int end = 1;

        long result = 384;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    public void testCase5() {
        long data[] = {-10, -5};
        int start = 0;
        int end = 2;

        long result = 963;
        assertEquals(result, RiceEncoder.computeBestSizeAndParam(data, start, end));
    }

    @Test
    void testCase6() {
        BitOutputStream output = null;
        long val = 100;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase7() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase4.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 9007199254740993L;
        int param = 10;

        System.out.println(val >> 52);

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase8() throws IOException {
        String filename = "testCasesOutputs/whiteBox/controlFlow/testCase8.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 100;
        int param = 10;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected  = "9900";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

}