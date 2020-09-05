package pl.zmudzin.library.core.domain.common;

public class StringId {

    private final String value;

    protected StringId(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof StringId))
            return false;
        StringId other = (StringId) object;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
