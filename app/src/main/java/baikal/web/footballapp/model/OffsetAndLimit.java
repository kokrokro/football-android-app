package baikal.web.footballapp.model;

public class OffsetAndLimit {
    private final int offset;
    private final int limit;

    public OffsetAndLimit(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}