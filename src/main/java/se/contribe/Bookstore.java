package se.contribe;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import se.contribe.core.Book;
import se.contribe.db.BookDAO;
import se.contribe.resources.BookResource;
import se.contribe.resources.CartResource;

import javax.sql.DataSource;

public class Bookstore extends Application<BookstoreConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BOOKSTORE_SERVICE = "Dropwizard Bookstore service";
    private static final String BEARER = "Bearer";

    private final HibernateBundle<BookstoreConfiguration> hibernate = new HibernateBundle<BookstoreConfiguration>(Book.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(BookstoreConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new Bookstore().run(args);
    }

    @Override
    public void initialize(Bootstrap<BookstoreConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<BookstoreConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(BookstoreConfiguration bookstoreConfiguration) {
                return bookstoreConfiguration.swaggerBundleConfiguration;
            }
        });
        bootstrap.addBundle(hibernate);
    }

    public void run(BookstoreConfiguration bookstoreConfiguration, Environment environment) throws Exception {
        // Datasource configuration
        final DataSource dataSource = bookstoreConfiguration.getDataSourceFactory().build(environment.metrics(), SQL);
        final BookDAO bookDAO = new BookDAO(hibernate.getSessionFactory());

        // Register resources
        environment.jersey().register(new BookResource(bookDAO));
        environment.jersey().register(new CartResource(bookDAO));
    }
}