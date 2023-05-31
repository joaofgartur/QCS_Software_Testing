package io.nayuki.flac.encode.tests.white_box.control_flow;

import io.nayuki.flac.encode.BitOutputStream;
import io.nayuki.flac.encode.RiceEncoder;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCasesWriteRiceSignedInt {

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
    void testCase1() {
        BitOutputStream output = null;
        long val = 100;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase2() throws IOException {
        String filename = "testCasesOutputs/whiteBox/dataFlow/testCase2.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 9007199254740993L;
        int param = 10;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    void testCase3() throws IOException {
        String filename = "testCasesOutputs/whiteBox/controlFlow/testCase3.bin";
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
