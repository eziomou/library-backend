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

import pl.zmudzin.library.persistence.jooq.schema.tables.Genre;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GenreRecord extends UpdatableRecordImpl<GenreRecord> implements Record2<UUID, String> {

    private static final long serialVersionUID = 1573954072;

    /**
     * Setter for <code>public.genre.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.genre.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.genre.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.genre.name</code>.
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
        return Genre.GENRE.ID;
    }

    @Override
    public Field<String> field2() {
        return Genre.GENRE.NAME;
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
    public GenreRecord value1(UUID value) {
        setId(value);
        return this;
    }

    @Override
    public GenreRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public GenreRecord values(UUID value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GenreRecord
     */
    public GenreRecord() {
        super(Genre.GENRE);
    }

    /**
     * Create a detached, initialised GenreRecord
     */
    public GenreRecord(UUID id, String name) {
        super(Genre.GENRE);

        set(0, id);
        set(1, name);
    }
}
