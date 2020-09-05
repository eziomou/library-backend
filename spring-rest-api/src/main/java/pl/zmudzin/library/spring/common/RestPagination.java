package pl.zmudzin.library.spring.common;

import pl.zmudzin.library.core.application.common.Pagination;

import javax.validation.constraints.Min;
import java.util.Objects;

public final class RestPagination implements Pagination {

    @Min(0)
    private int offset = 0;

    @Min(1)
    private int limit = 10;

    @Override
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RestPagination))
            return false;
        RestPagination other = (RestPagination) object;
        return offset == other.offset &&
                limit == other.limit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, limit);
    }
}
