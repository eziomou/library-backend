/*
 * This file is generated by jOOQ.
 */
package pl.zmudzin.library.persistence.jooq.schema.tables.records;


import java.util.UUID;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import pl.zmudzin.library.persistence.jooq.schema.tables.Publisher;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PublisherRecord extends UpdatableRecordImpl<PublisherRecord> implements Record2<UUID, String> {

    private static final long serialVersionUID = 1684274358;

    /**
     * Setter for <code>public.publisher.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.publisher.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.publisher.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.publisher.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<UUID, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return Publisher.PUBLISHER.ID;
    }

    @Override
    public Field<String> field2() {
        return Publisher.PUBLISHER.NAME;
    }

    @Override
    public UUID component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public UUID value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public PublisherRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public PublisherRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public PublisherRecord values(UUID value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PublisherRecord
     */
    public PublisherRecord() {
        super(Publisher.PUBLISHER);
    }

    /**
     * Create a detached, initialised PublisherRecord
     */
    public PublisherRecord(UUID id, String name) {
        super(Publisher.PUBLISHER);

        set(0, id);
        set(1, name);
    }
}
