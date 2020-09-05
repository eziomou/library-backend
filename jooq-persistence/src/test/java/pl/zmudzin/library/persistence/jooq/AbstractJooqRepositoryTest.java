package pl.zmudzin.library.persistence.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import pl.zmudzin.library.core.domain.common.Entity;
import pl.zmudzin.library.core.domain.common.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractJooqRepositoryTest<R extends Repository<E, ID>, E extends Entity<ID>, ID> {

    protected static final DataSource dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();

    protected static final Settings settings = new Settings()
            .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED)
            .withRenderNameCase(RenderNameCase.AS_IS);

    protected static final DSLContext context = DSL.using(dataSource, SQLDialect.POSTGRES, settings);

    protected final R repository;

    public AbstractJooqRepositoryTest(R repository) {
        this.repository = repository;
    }

    protected abstract E getEntity();

    protected abstract E getUpdatedEntity(E entity);

    protected abstract void assertEntityEquals(E expected, E result);

    @AfterEach
    protected void afterEach() {
        repository.deleteAll();
    }

    @Test
    protected void size_tenEntities_returnsTen() {
        for (int i = 0; i < 10; i++) {
            E entity = getEntity();
            repository.save(entity);
        }
        assertEquals(10, repository.size());
    }

    @Test
    protected void save_validEntity_savesEntity() {
        E entity = getEntity();
        repository.save(entity);
        repository.find(entity.getId()).ifPresentOrElse(e -> assertEntityEquals(entity, e), Assertions::fail);
    }

    @Test
    protected void saveAll_validEntities_savesEntities() {
        Collection<E> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            E entity = getEntity();
            entities.add(entity);
        }
        repository.saveAll(entities);
        assertEquals(10, repository.size());
    }

    @Test
    protected void find_existingEntity_returnsEntity() {
        E entity = getEntity();
        repository.save(entity);
        repository.find(entity.getId()).ifPresentOrElse(e -> assertEntityEquals(entity, e), Assertions::fail);
    }

    @Test
    protected void find_notExistingEntity_returnsEmpty() {
        assertTrue(repository.find(getEntity().getId()).isEmpty());
    }

    @Test
    protected void update_existingEntity_updatesEntity() {
        E entity = getEntity();
        repository.save(entity);

        E updated = getUpdatedEntity(entity);
        repository.update(updated);

        repository.find(entity.getId()).ifPresentOrElse(e -> assertEntityEquals(updated, e), Assertions::fail);
    }

    @Test
    protected void delete_existingEntity_deletesEntity() {
        E entity = getEntity();
        repository.save(entity);

        assertTrue(repository.find(entity.getId()).isPresent());
        repository.delete(entity);
        assertFalse(repository.find(entity.getId()).isPresent());
    }

    @Test
    protected void deleteAll_tenEntities_deletesAll() {
        for (int i = 0; i < 10; i++) {
            E entity = getEntity();
            repository.save(entity);
        }
        assertEquals(10, repository.size());
        repository.deleteAll();
        assertEquals(0, repository.size());
    }
}
