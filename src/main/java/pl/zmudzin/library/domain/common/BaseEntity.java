package pl.zmudzin.library.domain.common;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Piotr Żmudzin
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BaseEntity && Objects.equals(id, ((BaseEntity) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
