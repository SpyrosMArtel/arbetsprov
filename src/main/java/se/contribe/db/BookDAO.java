package se.contribe.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import se.contribe.core.Book;

public class BookDAO extends AbstractDAO<Book> {
    public BookDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
