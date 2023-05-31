package io.nayuki.flac.encode.tests.black_box;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.nayuki.flac.encode.BitOutputStream;
import io.nayuki.flac.encode.RiceEncoder;
import org.junit.*;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testCase17() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase17.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 1048575;
        int param = 15;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected = "0000000000000001FFFC";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    // THIS FAILS -> supposed to throw assertion and it doesn't

    @Test
    public void testCase18() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase18.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 1048576;
        int param = 15;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    public void testCase19() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase19.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 0;
        int param = 15;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected = "8000";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    public void testCase20() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase20.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 10;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));
    }

    @Test
    public void testCase21() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase21.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 10;
        int param = 0;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected = "000008";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    public void testCase22() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase22.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 10;
        int param = 31;

        RiceEncoder.writeRiceSignedInt(val, param, output);

        output.alignToByte();
        output.close();

        String expected = "80000014";
        String result = readBinaryFileIntoString(filename);

        assertEquals(expected, result);
    }

    @Test
    public void testCase23() throws IOException {
        String filename = "testCasesOutputs/blackBox/testCase23.bin";
        BitOutputStream output = new BitOutputStream(new FileOutputStream(filename));
        long val = 10;
        int param = -1;

        assertThrows(AssertionError.class, () -> RiceEncoder.writeRiceSignedInt(val, param, output));

    }

}
