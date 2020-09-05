package pl.zmudzin.library.persistence.jooq.reservation;

import org.jooq.*;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.reservation.ReservationData;
import pl.zmudzin.library.core.application.reservation.ReservationEventData;
import pl.zmudzin.library.core.application.reservation.ReservationQuery;
import pl.zmudzin.library.core.application.reservation.ReservationReadonlyRepository;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.reservation.*;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;

import java.time.OffsetDateTime;
import java.util.*;

import static org.jooq.impl.DSL.*;

public class JooqReservationRepository extends AbstractJooqRepository<Reservation, ReservationId> implements ReservationRepository, ReservationReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Reservation RESERVATION = Tables.RESERVATION;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.ReservationEvent RESERVATION_EVENT = Tables.RESERVATION_EVENT;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Member MEMBER = Tables.MEMBER;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Account ACCOUNT = Tables.ACCOUNT;
    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Book BOOK = Tables.BOOK;

    private static final Field<String> STATUS = field(name("status"), String.class);
    private static final Field<OffsetDateTime> FIRST_EVENT_DATE = field(name("first_event_date"), OffsetDateTime.class);
    private static final Field<OffsetDateTime> LAST_EVENT_DATE = field(name("last_event_date"), OffsetDateTime.class);

    public JooqReservationRepository(DSLContext context) {
        super(context, RESERVATION);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Reservation reservation) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(RESERVATION.ID, reservation.getId().toString());
        map.put(RESERVATION.MEMBER_ID, reservation.getMemberId().toString());
        map.put(RESERVATION.BOOK_ID, reservation.getBookId().toString());
        return map;
    }

    @Override
    protected Reservation mapToDomainModel(Record record) {
        Reservation.Builder builder = Reservation.builder()
                .id(ReservationId.of(record.get(RESERVATION.ID).toString()))
                .memberId(MemberId.of(record.get(RESERVATION.MEMBER_ID).toString()))
                .bookId(BookId.of(record.get(RESERVATION.BOOK_ID).toString()));

        context.select()
                .from(RESERVATION_EVENT)
                .where(RESERVATION_EVENT.RESERVATION_ID.eq(record.get(RESERVATION.ID)))
                .orderBy(RESERVATION_EVENT.OCCURRENCE_DATE)
                .fetch(this::mapToEvent)
                .forEach(builder::event);

        return builder.build();
    }

    private ReservationEvent mapToEvent(Record record) {
        return new ReservationEvent(
                ReservationEventId.of(record.get(RESERVATION_EVENT.ID).toString()),
                ReservationStatus.valueOf(record.get(RESERVATION_EVENT.RESERVATION_STATUS)),
                record.get(RESERVATION_EVENT.OCCURRENCE_DATE).toInstant()
        );
    }

    private ReservationData mapToViewModel(Record record) {
        List<ReservationEventData> events = context.select()
                .from(RESERVATION_EVENT)
                .where(RESERVATION_EVENT.RESERVATION_ID.eq(record.get(RESERVATION.ID)))
                .orderBy(RESERVATION_EVENT.OCCURRENCE_DATE)
                .fetch(this::mapToEventViewModel);

        return ReservationData.builder()
                .id(record.getValue(RESERVATION.ID).toString())
                .member(builder -> builder
                        .id(record.getValue(RESERVATION.MEMBER_ID).toString())
                        .firstName(record.getValue(ACCOUNT.FIRST_NAME))
                        .lastName(record.getValue(ACCOUNT.LAST_NAME)))
                .book(builder -> builder
                        .id(record.getValue(RESERVATION.BOOK_ID).toString())
                        .title(record.getValue(BOOK.TITLE)))
                .events(events)
                .build();
    }

    private ReservationEventData mapToEventViewModel(Record record) {
        return new ReservationEventData(
                record.getValue(RESERVATION_EVENT.RESERVATION_STATUS),
                record.getValue(RESERVATION_EVENT.OCCURRENCE_DATE).toString()
        );
    }

    @Override
    protected Condition eq(ReservationId reservationId) {
        return RESERVATION.ID.eq(UUID.fromString(reservationId.toString()));
    }

    @Override
    public void save(Reservation reservation) {
        super.save(reservation);
        saveEvents(reservation.getId(), reservation.getEvents());
    }

    private void saveEvents(ReservationId reservationId, Collection<ReservationEvent> events) {
        events.forEach(e -> context.insertInto(RESERVATION_EVENT)
                .values(extractEventFields(reservationId, e).values()).execute());
    }

    private Map<Field<?>, Object> extractEventFields(ReservationId reservationId, ReservationEvent event) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(RESERVATION_EVENT.ID, event.getId().toString());
        map.put(RESERVATION_EVENT.RESERVATION_ID, reservationId.toString());
        map.put(RESERVATION_EVENT.RESERVATION_STATUS, event.getStatus().toString());
        map.put(RESERVATION_EVENT.OCCURRENCE_DATE, event.getOccurrenceDate().toString());
        return map;
    }

    @Override
    public Optional<Reservation> find(MemberId memberId, ReservationId reservationId) {
        return find(RESERVATION.MEMBER_ID.eq(UUID.fromString(memberId.toString())),
                RESERVATION.ID.eq(UUID.fromString(reservationId.toString())));
    }

    @Override
    public boolean existsSubmitted(BookId bookId) {
        return context.fetchExists(selectFirst(bookId, ReservationStatus.SUBMITTED));
    }

    @Override
    public boolean existsSubmitted(MemberId memberId, BookId bookId) {
        return hasReservation(memberId, bookId, ReservationStatus.SUBMITTED);
    }

    @Override
    public Optional<Reservation> findFirstSubmitted(BookId bookId) {
        return Optional.ofNullable(selectFirst(bookId, ReservationStatus.SUBMITTED).fetchOne(this::mapToDomainModel));
    }

    @Override
    public boolean existsPrepared(BookId bookId) {
        return context.fetchExists(selectFirst(bookId, ReservationStatus.PREPARED));
    }

    @Override
    public boolean existsPrepared(MemberId memberId, BookId bookId) {
        return hasReservation(memberId, bookId, ReservationStatus.PREPARED);
    }

    @Override
    public Optional<Reservation> findFirstPrepared(BookId bookId) {
        return Optional.ofNullable(selectFirst(bookId, ReservationStatus.PREPARED).fetchOne(this::mapToDomainModel));
    }

    private boolean hasReservation(MemberId memberId, BookId bookId, ReservationStatus status) {
        return context.fetchExists(statusJoinStep().where(
                RESERVATION.MEMBER_ID.eq(UUID.fromString(memberId.toString())),
                RESERVATION.BOOK_ID.eq(UUID.fromString(bookId.toString())),
                STATUS.eq(status.toString())));
    }

    private Select<?> selectFirst(BookId bookId, ReservationStatus status) {
        return statusJoinStep()
                .where(RESERVATION.BOOK_ID.eq(UUID.fromString(bookId.toString())), STATUS.eq(status.toString()))
                .orderBy(LAST_EVENT_DATE)
                .limit(1);
    }

    @Override
    protected SelectJoinStep<? extends Record> joinStep(SelectJoinStep<? extends Record> joinStep) {
        return joinStep
                .join(MEMBER).on(RESERVATION.MEMBER_ID.eq(MEMBER.ID))
                .join(ACCOUNT).on(MEMBER.ACCOUNT_ID.eq(ACCOUNT.ID))
                .join(BOOK).on(RESERVATION.BOOK_ID.eq(BOOK.ID));
    }

    private SelectJoinStep<Record> statusJoinStep() {
        Table<?> re1 = statusTable();
        return context
                .select(
                        RESERVATION.asterisk(),
                        MEMBER.asterisk(),
                        ACCOUNT.asterisk(),
                        BOOK.asterisk(),
                        STATUS,
                        FIRST_EVENT_DATE,
                        LAST_EVENT_DATE
                )
                .from(RESERVATION)
                .join(MEMBER).on(RESERVATION.MEMBER_ID.eq(MEMBER.ID))
                .join(ACCOUNT).on(MEMBER.ACCOUNT_ID.eq(ACCOUNT.ID))
                .join(BOOK).on(RESERVATION.BOOK_ID.eq(BOOK.ID))
                .join(re1)
                .on(RESERVATION.ID.eq(re1.field(RESERVATION_EVENT.RESERVATION_ID)));
    }

    private Table<?> firstAndLastEventDateTable() {
        return context
                .select(
                        RESERVATION_EVENT.RESERVATION_ID,
                        min(RESERVATION_EVENT.OCCURRENCE_DATE).as(FIRST_EVENT_DATE),
                        max(RESERVATION_EVENT.OCCURRENCE_DATE).as(LAST_EVENT_DATE)
                )
                .from(RESERVATION_EVENT)
                .groupBy(RESERVATION_EVENT.RESERVATION_ID)
                .asTable("re2");
    }

    private Table<?> statusTable() {
        Table<?> re2 =  firstAndLastEventDateTable();
        return context
                .select(
                        RESERVATION_EVENT.RESERVATION_ID,
                        RESERVATION_EVENT.RESERVATION_STATUS.as(STATUS),
                        FIRST_EVENT_DATE,
                        LAST_EVENT_DATE
                )
                .from(RESERVATION_EVENT)
                .join(re2)
                .on(RESERVATION_EVENT.RESERVATION_ID.eq(re2.field(RESERVATION_EVENT.RESERVATION_ID)),
                        RESERVATION_EVENT.OCCURRENCE_DATE.eq(LAST_EVENT_DATE))
                .asTable("re1");
    }

    @Override
    public void update(Reservation reservation) {
        super.update(reservation);
        deleteEvents(reservation.getId());
        saveEvents(reservation.getId(), reservation.getEvents());
    }

    private void deleteEvents(ReservationId reservationId) {
        context.delete(RESERVATION_EVENT)
                .where(RESERVATION_EVENT.RESERVATION_ID.eq(UUID.fromString(reservationId.toString())))
                .execute();
    }

    @Override
    public Optional<ReservationData> findById(ReservationId reservationId) {
        return Optional.ofNullable(joinStep()
                .where(RESERVATION.ID.eq(UUID.fromString(reservationId.toString())))
                .fetchOne(this::mapToViewModel));
    }

    @Override
    public Optional<ReservationData> findByMemberIdAndReservationId(MemberId memberId, ReservationId reservationId) {
        return Optional.ofNullable(joinStep()
                .where(RESERVATION.MEMBER_ID.eq(UUID.fromString(memberId.toString())), RESERVATION.ID.eq(UUID.fromString(reservationId.toString())))
                .fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<ReservationData> findAllByQuery(ReservationQuery query, Pagination pagination) {
        List<Condition> conditions = getConditions(query);
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    @Override
    public Paginated<ReservationData> findAllByMemberIdAndQuery(MemberId memberId, ReservationQuery query, Pagination pagination) {
        List<Condition> conditions = getConditions(query);
        conditions.add(RESERVATION.MEMBER_ID.eq(UUID.fromString(memberId.toString())));
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    @Override
    public Paginated<ReservationData> findAllByBookIdAndQuery(BookId bookId, ReservationQuery query, Pagination pagination) {
        List<Condition> conditions = getConditions(query);
        conditions.add(RESERVATION.BOOK_ID.eq(UUID.fromString(bookId.toString())));
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    private Paginated<ReservationData> findAllByQuery(Pagination pagination, Condition... conditions) {
        int total = count(conditions);
        return Paginated.of(statusJoinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> getConditions(ReservationQuery query) {
        List<Condition> conditions = new ArrayList<>();
        query.status().ifPresent(status -> conditions.add(field(name("status")).equalIgnoreCase(status)));
        return conditions;
    }
}
