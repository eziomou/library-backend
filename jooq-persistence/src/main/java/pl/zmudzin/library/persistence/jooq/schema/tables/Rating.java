/*
 * This file is generated by jOOQ.
 */
package pl.zmudzin.library.persistence.jooq.schema.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import pl.zmudzin.library.persistence.jooq.schema.Keys;
import pl.zmudzin.library.persistence.jooq.schema.Public;
import pl.zmudzin.library.persistence.jooq.schema.tables.records.RatingRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Rating extends TableImpl<RatingRecord> {

    private static final long serialVersionUID = -2018742459;

    /**
     * The reference instance of <code>public.rating</code>
     */
    public static final Rating RATING = new Rating();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RatingRecord> getRecordType() {
        return RatingRecord.class;
    }

    /**
     * The column <code>public.rating.id</code>.
     */
    public final TableField<RatingRecord, UUID> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.rating.member_id</code>.
     */
    public final TableField<RatingRecord, UUID> MEMBER_ID = createField(DSL.name("member_id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.rating.book_id</code>.
     */
    public final TableField<RatingRecord, UUID> BOOK_ID = createField(DSL.name("book_id"), org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.rating.value</code>.
     */
    public final TableField<RatingRecord, Integer> VALUE = createField(DSL.name("value"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.rating.rate_date</code>.
     */
    public final TableField<RatingRecord, OffsetDateTime> RATE_DATE = createField(DSL.name("rate_date"), org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE.nullable(false), this, "");

    /**
     * Create a <code>public.rating</code> table reference
     */
    public Rating() {
        this(DSL.name("rating"), null);
    }

    /**
     * Create an aliased <code>public.rating</code> table reference
     */
    public Rating(String alias) {
        this(DSL.name(alias), RATING);
    }

    /**
     * Create an aliased <code>public.rating</code> table reference
     */
    public Rating(Name alias) {
        this(alias, RATING);
    }

    private Rating(Name alias, Table<RatingRecord> aliased) {
        this(alias, aliased, null);
    }

    private Rating(Name alias, Table<RatingRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Rating(Table<O> child, ForeignKey<O, RatingRecord> key) {
        super(child, key, RATING);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<RatingRecord> getPrimaryKey() {
        return Keys.RATING_PKEY;
    }

    @Override
    public List<UniqueKey<RatingRecord>> getKeys() {
        return Arrays.<UniqueKey<RatingRecord>>asList(Keys.RATING_PKEY);
    }

    @Override
    public List<ForeignKey<RatingRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RatingRecord, ?>>asList(Keys.RATING__FK_RATING_MEMBER, Keys.RATING__FK_RATING_BOOK);
    }

    public Member member() {
        return new Member(this, Keys.RATING__FK_RATING_MEMBER);
    }

    public Book book() {
        return new Book(this, Keys.RATING__FK_RATING_BOOK);
    }

    @Override
    public Rating as(String alias) {
        return new Rating(DSL.name(alias), this);
    }

    @Override
    public Rating as(Name alias) {
        return new Rating(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Rating rename(String name) {
        return new Rating(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Rating rename(Name name) {
        return new Rating(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<UUID, UUID, UUID, Integer, OffsetDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
