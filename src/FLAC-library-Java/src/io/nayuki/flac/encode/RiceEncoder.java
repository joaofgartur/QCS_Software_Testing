/* 
 * FLAC library (Java)
 * 
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/flac-library-java
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (see COPYING.txt and COPYING.LESSER.txt).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package io.nayuki.flac.encode;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

/* 
 * Calculates/estimates the encoded size of a vector of residuals, and also performs the encoding to an output stream.
 */
final class RiceEncoder {

	/*---- Functions for size calculation ---*/

	// Calculates the best number of bits and partition order needed to encode the
	// values data[warmup : data.length].
	// Each value in that subrange of data must fit in a signed 53-bit integer. The
	// result is packed in the form
	// ((bestSize << 4) | bestOrder), where bestSize is an unsigned integer and
	// bestOrder is a uint4.
	// Note that the partition orders searched, and hence the resulting bestOrder,
	// are in the range [0, maxPartOrder].
	public static long computeBestSizeAndOrder(long[] data, int warmup, int maxPartOrder) {
		// Check arguments strictly
		Objects.requireNonNull(data);
		if (warmup < 0 || warmup > data.length)
			throw new IllegalArgumentException();
		if (maxPartOrder < 0 || maxPartOrder > 15)
			throw new IllegalArgumentException();
		for (long x : data) {
			x >>= 52;
			if (x != 0 && x != -1) // Check that it fits in a signed int53
				throw new IllegalArgumentException();
		}

		long bestSize = Integer.MAX_VALUE;
		int bestOrder = -1;

		int[] escapeBits = null;
		int[] bitsAtParam = null;
		for (int order = maxPartOrder; order >= 0; order--) {
			int partSize = data.length >>> order;
			if ((partSize << order) != data.length || partSize < warmup)
				continue;
			int numPartitions = 1 << order;

			if (escapeBits == null) { // And bitsAtParam == null
				escapeBits = new int[numPartitions];
				bitsAtParam = new int[numPartitions * 16];
				for (int i = warmup; i < data.length; i++) {
					int j = i / partSize;
					long val = data[i];
					escapeBits[j] = Math.max(65 - Long.numberOfLeadingZeros(val ^ (val >> 63)), escapeBits[j]);
					val = (val >= 0) ? (val << 1) : (((-val) << 1) - 1);
					for (int param = 0; param < 15; param++, val >>>= 1)
						bitsAtParam[param + j * 16] += val + 1 + param;
				}
			} else { // Both arrays are non-null
				// Logically halve the size of both arrays (but without reallocating to the true
				// new size)
				for (int i = 0; i < numPartitions; i++) {
					int j = i << 1;
					escapeBits[i] = Math.max(escapeBits[j], escapeBits[j + 1]);
					for (int param = 0; param < 15; param++)
						bitsAtParam[param + i * 16] = bitsAtParam[param + j * 16] + bitsAtParam[param + (j + 1) * 16];
				}
			}

			long size = 4 + (4 << order);
			for (int i = 0; i < numPartitions; i++) {
				int min = Integer.MAX_VALUE;
				if (escapeBits[i] <= 31)
					min = 5 + escapeBits[i] * (partSize - (i == 0 ? warmup : 0));
				for (int param = 0; param < 15; param++)
					min = Math.min(bitsAtParam[param + i * 16], min);
				size += min;
			}
			if (size < bestSize) {
				bestSize = size;
				bestOrder = order;
			}
		}

		if (bestSize == Integer.MAX_VALUE || (bestOrder >>> 4) != 0)
			throw new AssertionError();
		return bestSize << 4 | bestOrder;
	}

	// Calculates the number of bits needed to encode the sequence of values
	// data[start : end] with an optimally chosen Rice parameter.
	public static long computeBestSizeAndParam(long[] data, int start, int end) {
		assert data != null && 0 <= start && start <= end && end <= data.length; // 1

		// Use escape code
		int bestParam; //2
		long bestSize;
		long accumulator = 0;
		for (int i = start; i < end; i++) { //3, 6 & 5
			long val = data[i]; // 4
			accumulator |= val ^ (val >> 63);
		}

		int numBits = 65 - Long.numberOfLeadingZeros(accumulator); // 7 accumulator >= 35
		assert 1 <= numBits && numBits <= 65; // 8
		if (numBits <= 31) { // 9
			bestSize = 4 + 5 + (end - start) * numBits; // 10
			bestParam = 16 + numBits;
			if ((bestParam >>> 6) != 0) // 11 -> best param < 0 || best param >= 64
				throw new AssertionError();
		} else { // 12
			bestSize = Long.MAX_VALUE;
			bestParam = 0;
		}

		// Use Rice coding
		for (int param = 0; param <= 14; param++) { // 13, 26 & 25
			long size = 4; // 14
			for (int i = start; i < end; i++) { // 15, 22 & 21
				long val = data[i]; // 16
				if (val >= 0) // 17
					val <<= 1; // 18
				else
					val = ((-val) << 1) - 1; // 19
				System.out.println(val);
				size += (val >>> param) + 1 + param; // 20
			}
			if (size < bestSize) { // 23
				bestSize = size; // 24
				bestParam = param;
			}
		}

		return bestSize << 6 | bestParam; // 27
	}

	/*---- Functions for encoding data ---*/

	// Encodes the sequence of values data[warmup : data.length] with an
	// appropriately chosen order and Rice parameters.
	// Each value in data must fit in a signed 53-bit integer.
	public static void encode(long[] data, int warmup, int order, BitOutputStream out) throws IOException {
		// Check arguments strictly
		Objects.requireNonNull(data);
		Objects.requireNonNull(out);
		if (warmup < 0 || warmup > data.length)
			throw new IllegalArgumentException();
		if (order < 0 || order > 15)
			throw new IllegalArgumentException();
		for (long x : data) {
			x >>= 52;
			if (x != 0 && x != -1) // Check that it fits in a signed int53
				throw new IllegalArgumentException();
		}

		out.writeInt(2, 0);
		out.writeInt(4, order);
		int numPartitions = 1 << order;
		int start = warmup;
		int end = data.length >>> order;
		for (int i = 0; i < numPartitions; i++) {
			int param = (int) computeBestSizeAndParam(data, start, end) & 0x3F;
			encode(data, start, end, param, out);
			start = end;
			end += data.length >>> order;
		}
	}

	// Encodes the sequence of values data[start : end] with the given Rice
	// parameter.
	private static void encode(long[] data, int start, int end, int param, BitOutputStream out) throws IOException {
		assert 0 <= param && param <= 31 && data != null && out != null;
		assert 0 <= start && start <= end && end <= data.length;

		if (param < 15) {
			out.writeInt(4, param);
			for (int j = start; j < end; j++)
				writeRiceSignedInt(data[j], param, out);
		} else {
			out.writeInt(4, 15);
			int numBits = param - 16;
			out.writeInt(5, numBits);
			for (int j = start; j < end; j++)
				out.writeInt(numBits, (int) data[j]);
		}
	}

	public static void writeRiceSignedInt(long val, int param, BitOutputStream out)
			throws AssertionError, IOException {

		assert 0 <= param && param <= 31 && out != null; // 1
		assert (val >> 52) == 0 || (val >> 52) == -1; // Fits in a signed int53 // 2

		long unsigned = val >= 0 ? val << 1 : ((-val) << 1) - 1; // 3
		int unary = (int) (unsigned >>> param);
		for (int i = 0; i < unary; i++) // 4, 7 & 6
			out.writeInt(1, 0);	// 5
		out.writeInt(1, 1); // 8
		out.writeInt(param, (int) unsigned);
	}

}
