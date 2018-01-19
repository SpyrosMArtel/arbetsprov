package se.contribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import se.contribe.core.Book;

import java.math.BigDecimal;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Representation test for a single book
 */
public class BookRepresentationTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String BOOK_JSON = "fixtures/book.json";
    private static final String BOOK_TITLE = "Mastering åäö";
    private static final String BOOK_AUTHOR = "Average Swede";

    @Test
    public void serializesToJSON() throws Exception {
        final Book book = new Book(BOOK_TITLE, BOOK_AUTHOR, 15);
        book.setId(1L);
        book.setPrice(new BigDecimal(762));
        final String expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture(BOOK_JSON), Book.class));
        assertThat(MAPPER.writeValueAsString(book)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Book book = new Book(BOOK_TITLE, BOOK_AUTHOR, 15);
        book.setId(1L);
        book.setPrice(new BigDecimal(762));

        assertThat(MAPPER.readValue(fixture(BOOK_JSON), Book.class).getId()).isEqualTo(book.getId());
        assertThat(MAPPER.readValue(fixture(BOOK_JSON), Book.class).getTitle()).isEqualTo(book.getTitle());
        assertThat(MAPPER.readValue(fixture(BOOK_JSON), Book.class).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(MAPPER.readValue(fixture(BOOK_JSON), Book.class).getPrice()).isEqualTo(book.getPrice());
        assertThat(MAPPER.readValue(fixture(BOOK_JSON), Book.class).getQuantity()).isEqualTo(book.getQuantity());
    }
}
