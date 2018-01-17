package se.contribe.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import se.contribe.core.Book;

import java.util.List;

public class BookDAO extends AbstractDAO<Book> {
    /**
     * Instantiates the DAO with the hibernate's session factory.
     * @param sessionFactory The hibernate's session factory.
     */
    public BookDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Searches for a book by its id.
     * @param id The id of the book.
     * @return A Book object from the database
     */
    public Book findById(Long id) {
        return get(id);
    }

    /**
     * Retrieves a list of books after querying it with a search keyword.
     * @param searchTerms The keyword to search for books.
     * @return A list of books from the database
     */
    public List searchByKeyword(String searchTerms) {
        Query query = namedQuery("se.contribe.core.Book.findByTerms");
        query.setParameter("search","%"+searchTerms+"%");
        return query.list();
    }

    /**
     * Retrieves all books in the database.
     * @return A list of all books in the database.
     */
    public List<Book> findAll() {
        return list(namedQuery("se.contribe.core.Book.findAll"));
    }

    /**
     * Persists a book in the database.
     * @param book The book to be added in the database.
     * @return If successful the newly added book's unique identifier.
     */
    public Long create(Book book) {
        return persist(book).getId();
    }

    /**
     * Updates a book in the database.
     * @param book The book to be added in the database.
     * @return The modified book.
     */
    public Book update(Book book) {
        return persist(book);
    }

    /**
     * Deletes a book from the database.
     * @param book The book to be deleted.
     */
    public void delete(Book book) {
        currentSession().delete(book);
    }

}
