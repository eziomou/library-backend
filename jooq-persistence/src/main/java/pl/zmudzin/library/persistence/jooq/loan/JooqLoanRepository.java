package pl.zmudzin.library.persistence.jooq.loan;

import org.jooq.*;
import pl.zmudzin.library.core.application.common.Metadata;
import pl.zmudzin.library.core.application.common.Paginated;
import pl.zmudzin.library.core.application.loan.LoanData;
import pl.zmudzin.library.core.application.loan.LoanQuery;
import pl.zmudzin.library.core.application.loan.LoanReadonlyRepository;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.application.common.Pagination;
import pl.zmudzin.library.core.domain.loan.Loan;
import pl.zmudzin.library.core.domain.loan.LoanId;
import pl.zmudzin.library.core.domain.loan.LoanRepository;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.persistence.jooq.AbstractJooqRepository;
import pl.zmudzin.library.persistence.jooq.schema.Tables;
import pl.zmudzin.library.persistence.jooq.schema.tables.Account;
import pl.zmudzin.library.persistence.jooq.schema.tables.Book;
import pl.zmudzin.library.persistence.jooq.schema.tables.Member;

import java.time.OffsetDateTime;
import java.util.*;

public class JooqLoanRepository extends AbstractJooqRepository<Loan, LoanId> implements LoanRepository, LoanReadonlyRepository {

    private static final pl.zmudzin.library.persistence.jooq.schema.tables.Loan LOAN = Tables.LOAN;
    private static final Member MEMBER = Tables.MEMBER;
    private static final Account ACCOUNT = Tables.ACCOUNT;
    private static final Book BOOK = Tables.BOOK;

    public JooqLoanRepository(DSLContext context) {
        super(context, LOAN);
    }

    @Override
    protected Map<Field<?>, Object> extractFields(Loan loan) {
        Map<Field<?>, Object> map = new LinkedHashMap<>();
        map.put(LOAN.ID, loan.getId().toString());
        map.put(LOAN.MEMBER_ID, loan.getMemberId().toString());
        map.put(LOAN.BOOK_ID, loan.getBookId().toString());
        map.put(LOAN.LOAN_DATE, loan.getLoanDate().toString());
        map.put(LOAN.DUE_DATE, loan.getDueDate().toString());
        map.put(LOAN.RETURNED, loan.isReturned());
        return map;
    }

    @Override
    protected Loan mapToDomainModel(Record record) {
        return Loan.builder()
                .id(LoanId.of(record.get(LOAN.ID).toString()))
                .memberId(MemberId.of(record.get(LOAN.MEMBER_ID).toString()))
                .bookId(BookId.of(record.get(LOAN.BOOK_ID).toString()))
                .loanDate(record.get(LOAN.LOAN_DATE).toInstant())
                .dueDate(record.get(LOAN.DUE_DATE).toInstant())
                .returned(record.get(LOAN.RETURNED))
                .build();
    }

    private LoanData mapToViewModel(Record record) {
        return LoanData.builder()
                .id(record.getValue(LOAN.ID).toString())
                .member(builder -> builder
                        .id(record.getValue(LOAN.MEMBER_ID).toString())
                        .firstName(record.getValue(ACCOUNT.FIRST_NAME))
                        .lastName(record.getValue(ACCOUNT.LAST_NAME)))
                .book(builder -> builder
                        .id(record.getValue(LOAN.BOOK_ID).toString())
                        .title(record.getValue(BOOK.TITLE)))
                .loanDate(record.getValue(LOAN.LOAN_DATE).toString())
                .dueDate(record.getValue(LOAN.DUE_DATE).toString())
                .returned(record.getValue(LOAN.RETURNED))
                .build();
    }

    @Override
    protected Condition eq(LoanId loanId) {
        return LOAN.ID.eq(UUID.fromString(loanId.toString()));
    }

    @Override
    protected SelectJoinStep<? extends Record> fromStep(SelectFromStep<? extends Record> fromStep) {
        return fromStep.from(LOAN);
    }

    @Override
    protected SelectJoinStep<? extends Record> joinStep(SelectJoinStep<? extends Record> joinStep) {
        return joinStep
                .innerJoin(MEMBER).on(LOAN.MEMBER_ID.eq(MEMBER.ID))
                .innerJoin(ACCOUNT).on(MEMBER.ACCOUNT_ID.eq(ACCOUNT.ID))
                .innerJoin(BOOK).on(LOAN.BOOK_ID.eq(BOOK.ID));
    }

    @Override
    public Optional<Loan> find(MemberId memberId, LoanId loanId) {
        return find(LOAN.MEMBER_ID.eq(UUID.fromString(memberId.toString())),
                LOAN.ID.eq(UUID.fromString(loanId.toString())));
    }

    @Override
    public Optional<Loan> findNotReturned(BookId bookId) {
        return Optional.ofNullable(selectNotReturned(bookId).fetchOne(this::mapToDomainModel));
    }

    @Override
    public boolean existsNotReturned(BookId bookId) {
        return context.fetchExists(selectNotReturned(bookId));
    }

    private Select<? extends Record> selectNotReturned(BookId bookId) {
        return joinStep().where(LOAN.BOOK_ID.eq(UUID.fromString(bookId.toString())), LOAN.RETURNED.eq(false));
    }

    @Override
    public Optional<LoanData> findById(LoanId loanId) {
        return Optional.ofNullable(joinStep()
                .where(LOAN.ID.eq(UUID.fromString(loanId.toString())))
                .fetchOne(this::mapToViewModel));
    }

    @Override
    public Optional<LoanData> findByMemberIdAndLoanId(MemberId memberId, LoanId loanId) {
        return Optional.ofNullable(joinStep()
                .where(LOAN.MEMBER_ID.eq(UUID.fromString(memberId.toString())), LOAN.ID.eq(UUID.fromString(loanId.toString())))
                .fetchOne(this::mapToViewModel));
    }

    @Override
    public Paginated<LoanData> findAllByQuery(LoanQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    @Override
    public Paginated<LoanData> findAllByMemberIdAndQuery(MemberId memberId, LoanQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        conditions.add(LOAN.MEMBER_ID.eq(UUID.fromString(memberId.toString())));
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    @Override
    public Paginated<LoanData> findAllByBookIdAndQuery(BookId bookId, LoanQuery query, Pagination pagination) {
        List<Condition> conditions = createConditions(query);
        conditions.add(LOAN.BOOK_ID.eq(UUID.fromString(bookId.toString())));
        return findAllByQuery(pagination, conditions.toArray(Condition[]::new));
    }

    private Paginated<LoanData> findAllByQuery(Pagination pagination, Condition... conditions) {
        int total = count(conditions);
        return Paginated.of(joinStep()
                .where(conditions)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .fetch(this::mapToViewModel), Metadata.of(pagination, total));
    }

    private List<Condition> createConditions(LoanQuery query) {
        List<Condition> conditions = new ArrayList<>();
        query.minLoanDate().ifPresent(date -> conditions.add(LOAN.LOAN_DATE.ge(OffsetDateTime.parse(date))));
        query.maxLoanDate().ifPresent(date -> conditions.add(LOAN.LOAN_DATE.le(OffsetDateTime.parse(date))));
        query.minDueDate().ifPresent(date -> conditions.add(LOAN.DUE_DATE.ge(OffsetDateTime.parse(date))));
        query.maxDueDate().ifPresent(date -> conditions.add(LOAN.DUE_DATE.le(OffsetDateTime.parse(date))));
        query.returned().ifPresent(returned -> conditions.add(LOAN.RETURNED.eq(returned)));
        return conditions;
    }
}
