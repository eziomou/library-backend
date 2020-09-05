/*
 * This file is generated by jOOQ.
 */
package pl.zmudzin.library.persistence.jooq.schema.tables;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import pl.zmudzin.library.persistence.jooq.schema.Keys;
import pl.zmudzin.library.persistence.jooq.schema.Public;
import pl.zmudzin.library.persistence.jooq.schema.tables.records.PublisherRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Publisher extends TableImpl<PublisherRecord> {

    private static final long serialVersionUID = 2021579467;

    /**
     * The reference instance of <code>public.publisher</code>
     */
    public static final Publisher PUBLISHER = new Publisher();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PublisherRecord> getRecordType() {
        return PublisherRecord.class;
    }

    /**
     * The column <code>public.publisher.id</code>.
     */
    public final TableField<PublisherRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.publisher.name</code>.
     */
    public final TableField<PublisherRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * Create a <code>public.publisher</code> table reference
     */
    public Publisher() {
        this(DSL.name("publisher"), null);
    }

    /**
     * Create an aliased <code>public.publisher</code> table reference
     */
    public Publisher(String alias) {
        this(DSL.name(alias), PUBLISHER);
    }

    /**
     * Create an aliased <code>public.publisher</code> table reference
     */
    public Publisher(Name alias) {
        this(alias, PUBLISHER);
    }

    private Publisher(Name alias, Table<PublisherRecord> aliased) {
        this(alias, aliased, null);
    }

    private Publisher(Name alias, Table<PublisherRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Publisher(Table<O> child, ForeignKey<O, PublisherRecord> key) {
        super(child, key, PUBLISHER);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<PublisherRecord> getPrimaryKey() {
        return Keys.PUBLISHER_PKEY;
    }

    @Override
    public List<UniqueKey<PublisherRecord>> getKeys() {
        return Arrays.<UniqueKey<PublisherRecord>>asList(Keys.PUBLISHER_PKEY);
    }

    @Override
    public Publisher as(String alias) {
        return new Publisher(DSL.name(alias), this);
    }

    @Override
    public Publisher as(Name alias) {
        return new Publisher(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Publisher rename(String name) {
        return new Publisher(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Publisher rename(Name name) {
        return new Publisher(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}