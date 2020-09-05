package pl.zmudzin.library.persistence.jooq;

import org.jooq.DSLContext;
import pl.zmudzin.library.core.domain.account.Account;
import pl.zmudzin.library.core.domain.account.AccountId;
import pl.zmudzin.library.core.domain.account.AccountRepository;
import pl.zmudzin.library.core.domain.account.Profile;
import pl.zmudzin.library.core.domain.catalog.author.Author;
import pl.zmudzin.library.core.domain.catalog.author.AuthorId;
import pl.zmudzin.library.core.domain.catalog.author.AuthorRepository;
import pl.zmudzin.library.core.domain.catalog.book.Book;
import pl.zmudzin.library.core.domain.catalog.book.BookId;
import pl.zmudzin.library.core.domain.catalog.book.BookRepository;
import pl.zmudzin.library.core.domain.catalog.genre.Genre;
import pl.zmudzin.library.core.domain.catalog.genre.GenreId;
import pl.zmudzin.library.core.domain.catalog.genre.GenreRepository;
import pl.zmudzin.library.core.domain.catalog.publisher.Publisher;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherId;
import pl.zmudzin.library.core.domain.catalog.publisher.PublisherRepository;
import pl.zmudzin.library.core.domain.member.Member;
import pl.zmudzin.library.core.domain.member.MemberId;
import pl.zmudzin.library.core.domain.member.MemberRepository;
import pl.zmudzin.library.persistence.jooq.account.JooqAccountRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqAuthorRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqBookRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqGenreRepository;
import pl.zmudzin.library.persistence.jooq.catalog.JooqPublisherRepository;
import pl.zmudzin.library.persistence.jooq.member.JooqMemberRepository;

import java.time.Instant;
import java.util.UUID;

public class PersistenceUtil {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    public PersistenceUtil(DSLContext context) {
        accountRepository = new JooqAccountRepository(context);
        memberRepository = new JooqMemberRepository(context);
        authorRepository = new JooqAuthorRepository(context);
        genreRepository = new JooqGenreRepository(context);
        publisherRepository = new JooqPublisherRepository(context);
        bookRepository = new JooqBookRepository(context);
    }

    public Member randomMember() {
        Member member = new Member(MemberId.of(UUID.randomUUID().toString()), randomAccount());
        memberRepository.save(member);
        return member;
    }

    private void removeAllMembers() {
        memberRepository.deleteAll();
    }

    public Account randomAccount() {
        Account account = new Account(AccountId.of(UUID.randomUUID().toString()),
                "Foo", "Bar", new Profile("Baz", "Qux"));
        accountRepository.save(account);
        return account;
    }

    private void removeAllAccounts() {
        accountRepository.deleteAll();
    }

    public Book randomBook() {
        Book book = Book.builder()
                .id(BookId.of(UUID.randomUUID().toString()))
                .title("Foo")
                .description("Bar")
                .publicationDate(Instant.now())
                .author(randomAuthor())
                .genre(randomGenre())
                .publisher(randomPublisher())
                .build();
        bookRepository.save(book);
        return book;
    }

    private void removeAllBooks() {
        bookRepository.deleteAll();
    }

    private Author randomAuthor() {
        Author author = new Author(AuthorId.of(UUID.randomUUID().toString()), "Foo", "Bar");
        authorRepository.save(author);
        return author;
    }

    private void removeAllAuthors() {
        authorRepository.deleteAll();
    }

    private Genre randomGenre() {
        Genre genre = new Genre(GenreId.of(UUID.randomUUID().toString()), "Foo");
        genreRepository.save(genre);
        return genre;
    }

    private void removeAllGenres() {
        genreRepository.deleteAll();
    }

    private Publisher randomPublisher() {
        Publisher publisher = new Publisher(PublisherId.of(UUID.randomUUID().toString()), "Foo");
        publisherRepository.save(publisher);
        return publisher;
    }

    private void removeALlPublishers() {
        publisherRepository.deleteAll();
    }

    public void removeAll() {
        removeAllMembers();
        removeAllAccounts();
        removeAllBooks();
        removeAllAuthors();
        removeAllGenres();
        removeALlPublishers();
    }
}
