package se.contribe.resources;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.hibernate.UnitOfWork;
import se.contribe.core.Book;
import se.contribe.core.BookList;
import se.contribe.db.BookDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Path("/books")
public class BookResource implements BookList {
    private final BookDAO bookDAO;

    public BookResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @Produces("application/json")
    @Timed
    @UnitOfWork
    public List<Book> listBooks() {
        return Collections.emptyList();
    }

    @GET
    @Produces("application/json")
    @Path("/{bookId}")
    @UnitOfWork
    public Book getBook(@PathParam("bookId") long bookId) {
        Book book = findSafely(bookId);
        if (book == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book;
    }

    @PUT
    @Path("/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @UnitOfWork
    public Response.Status updateBook(@ApiParam(
            required = true, value = "A Book in JSON format, where the id specifies the book.") Book book,
                                      @PathParam("bookId") long bookId) {
        return Response.Status.OK;
    }

    @DELETE
    @Path("/{bookId}")
    @UnitOfWork
    public Response.Status deleteBook(@PathParam("bookId") long bookId) {
        return Response.Status.NO_CONTENT;
    }

    @Override
    @GET
    @Produces("application/json")
    @Path("/search/{searchString}")
    @Timed
    @UnitOfWork
    public Book[] list(String searchString) {
        return new Book[0];
    }

    @Override
    @POST
    @Path("/add/{quantity}")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public boolean add(Book book, int quantity) {
        return false;
    }

    @Override
    @POST
    @Path("/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @UnitOfWork
    public int[] buy(Book... books) {
        return new int[0];
    }

    private Book findSafely(long bookId) {
        final Optional<Book> book = Optional.empty();
        if (!book.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book.get();
    }
}
