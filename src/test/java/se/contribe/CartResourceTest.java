package se.contribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;
import org.junit.runners.MethodSorters;
import se.contribe.core.Book;
import se.contribe.db.BookDAO;
import se.contribe.resources.CartResource;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartResourceTest {
    private static final BookDAO dao = mock(BookDAO.class);
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String BOOKS_JSON = "fixtures/books.json";
    private Book[] books;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(new CartResource(dao)).build();

    @Before
    public void setup() throws IOException {
        books = MAPPER.readValue(fixture(BOOKS_JSON), Book[].class);
    }

    @After
    public void tearDown() {
        // we have to reset the mock after each test because of the
        // @ClassRule, or use a @Rule as mentioned below.
        reset(dao);
    }

    @Test
    public void test001GetCartContentsEmpty() {
        String[] actualContents = resources.client()
                .target("/cart")
                .request()
                .get(String[].class);

        String[] expectedContents = {"Total Price: 0"};
        assertThat(actualContents).isEqualTo(expectedContents);
    }

    @Test
    public void test002AddItemsToCart() throws IOException {
        for (Book book : books) {
            when(dao.findById(eq(book.getId()))).thenReturn(book);
            Response result = resources.client()
                    .target("/cart")
                    .request()
                    .post(Entity.entity(book, MediaType.APPLICATION_JSON_TYPE), Response.class);

            assertThat(result.getStatus()).isEqualTo(200);
        }
    }

    @Test
    public void test003GetCartContents() {
        String[] actualContents = resources.client()
                .target("/cart")
                .request()
                .get(String[].class);

        String[] expectedContents = {"Mastering åäö, Average Swede, 762.00",
                "How To Spend Money, Rich Bloke, 100000000.00",
                "Generic Title, First Author, 185.50",
                "Generic Title, Second Author, 1748.00",
                "Random Sales, Cunning Bastard, 999.00",
                "Random Sales, Cunning Bastard, 499.50",
                "Desired, Rich Bloke, 564.50",
                "Total Price: 100004758.50"};
        assertThat(actualContents).isEqualTo(expectedContents);
    }

    @Test
    public void test004RemoveNonExistingItemsFromCart() throws IOException {
        Response result = resources.client()
                .target(String.format("/cart/remove/%d", books[0].getId()))
                .request()
                .delete(Response.class);

        assertThat(result.getStatus()).isEqualTo(404);
    }

    @Test
    public void test005RemoveItemsFromCart() throws IOException {
        for (Book book : books) {
            when(dao.findById(eq(book.getId()))).thenReturn(book);
            Response result = resources.client()
                    .target(String.format("/cart/%d", book.getId()))
                    .request()
                    .delete(Response.class);

            assertThat(result.getStatus()).isEqualTo(204);
        }
    }
}
