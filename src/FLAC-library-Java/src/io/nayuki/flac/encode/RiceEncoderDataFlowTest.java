package io.nayuki.flac.encode;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @Test
    void testCase1() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase1.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 100;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase2() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase2.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 100;
        int param = 32;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase3() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase3.bin";
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

    @Test
    void testCase4() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase4.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 9007199254740993L;
        int param = 10;

        System.out.println(val >> 52);

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase5() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase5.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = -100;
        int param = 10;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected  = "98E0";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    void testCase6() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase6.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = -100;
        int param = 10;

        output.close();
        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected  = "";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    void testCase7() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase7.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 1024;
        int param = 10;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected  = "3000";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    void testCase8() {
        BitOutputStream output = null;
        long val = 100;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

}