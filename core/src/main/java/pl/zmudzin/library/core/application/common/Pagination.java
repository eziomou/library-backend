package pl.zmudzin.library.core.application.common;

public interface Pagination {

    int getOffset();

    int getLimit();

    static Pagination of(int offset, int limit) {
        return new Pagination() {

            @Override
            public int getOffset() {
                return Math.max(offset, 0);
            }

            @Override
            public int getLimit() {
                return Math.max(limit, 1);
            }
        };
    }
}
