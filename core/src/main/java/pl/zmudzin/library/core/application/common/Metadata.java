package pl.zmudzin.library.core.application.common;

public interface Metadata extends Pagination {

    int getTotal();

    static Metadata of(Pagination pagination, int total) {
        return new Metadata() {
            @Override
            public int getOffset() {
                return pagination.getOffset();
            }

            @Override
            public int getLimit() {
                return pagination.getLimit();
            }

            @Override
            public int getTotal() {
                return total;
            }
        };
    }
}
