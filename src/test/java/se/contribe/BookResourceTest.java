package se.contribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import se.contribe.core.Book;
import se.contribe.db.BookDAO;
import se.contribe.resources.BookResource;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BookResourceTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final BookDAO dao = mock(BookDAO.class);
    private static final String BOOKS_JSON = "fixtures/books.json";
    private Book[] books;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(new BookResource(dao)).build();

    @Before
    public void setup() throws IOException {
        books = MAPPER.readValue(fixture(BOOKS_JSON), Book[].class);
    }

    @After
    public void tearDown(){
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(dao);
    }

    @Test
    public void testFindAllBooks() throws IOException {
        when(dao.findAll()).thenReturn(Arrays.asList(books));
        assertThat(resources.client().target("/books").request().get(Book[].class)).isEqualTo(books);
        verify(dao).findAll();
    }

    @Test
    public void testSearchBooksByKeyword() throws IOException {
        Book[] expectedBooks = Arrays.stream(books)
                .filter(book -> book.getTitle().contains("Rich") || book.getAuthor().contains("Rich"))
                .toArray(Book[]::new);

        /*Search for books with "Rich" either in title or author */
        when(dao.searchByKeyword("Rich")).thenReturn(Arrays.asList(expectedBooks));
        assertThat(resources.client().target("/books/search/Rich").request().get(Book[].class)).isEqualTo(expectedBooks);
        verify(dao).searchByKeyword("Rich");
    }

    @Test
    public void testAddBook() throws IOException {
        Boolean result = resources.client()
                .target("/books/add/3")
                .request()
                .post(Entity.entity(books[0], MediaType.APPLICATION_JSON_TYPE), Boolean.class);

        assertThat(result).isNotNull();
        assertThat(result).isTrue();
        verify(dao).create(any(Book.class));
    }

    @Test
    public void testSingleBook() {
        when(dao.findById(books[0].getId())).thenReturn(books[0]);
        assertThat(resources.client().target("/books/1").request().get(Book.class)).isEqualTo(books[0]);
        verify(dao).findById(1L);
    }

    @Test
    public void testBuyBooks() {
        List<Book> booksToBuy = Arrays.stream(books).collect(Collectors.toList());
        Book book = new Book("Should return", "it does not exist", 1);
        book.setId(12L);
        book.setPrice(new BigDecimal(Math.random() * 100));
        booksToBuy.add(book);

        when(dao.findAll()).thenReturn(Arrays.asList(books));

        Integer[] actualStatuses = resources.client().target("/books/buy").request()
                .post(Entity.entity(booksToBuy, MediaType.APPLICATION_JSON_TYPE), Integer[].class);

        Integer[] expectedStatuses = Arrays.stream( new int[]{0, 0, 0, 0, 0, 0, 1, 2} ).boxed().toArray( Integer[]::new );
        assertThat(actualStatuses).isEqualTo(expectedStatuses);
    }
}
