package io.nayuki.flac.encode;

public class RiceEncodedObject {
    long unsigned;
    int unary;

    public RiceEncodedObject(long unsigned, int unary) {
        this.unsigned = unsigned;
        this.unary = unary;
    }

    public long getUnsigned() {
        return this.unsigned;
    }

    public void setUnsigned(long unsigned) {
        this.unsigned = unsigned;
    }

    public int getUnary() {
        return this.unary;
    }

    public void setUnary(int unary) {
        this.unary = unary;
    }

}
