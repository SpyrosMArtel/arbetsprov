package se.contribe.resources;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import se.contribe.core.Book;
import se.contribe.core.BookList;
import se.contribe.core.BookStatusEnum;
import se.contribe.db.BookDAO;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Path("/books")
@Api(value = "/books", description = "Operations on the bookstore")
public class BookResource implements BookList {
    private final BookDAO bookDAO;

    public BookResource(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GET
    @Produces("application/json")
    @Timed
    @ApiOperation(value = "Return all books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!")
    })
    @UnitOfWork
    public List<Book> listBooks() {
        return bookDAO.findAll();
    }

    @GET
    @Produces("application/json")
    @Path("/{bookId}")
    @ApiOperation(value = "Return a book", notes = "Provide the bookId to retrieve a specific book")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID")
    })
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
    @ApiOperation(value = "Updates a book specified by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!")
    })
    @UnitOfWork
    public Response.Status updateBook(
            @ApiParam(required = true, value = "A Book in JSON format, where the id specifies the book.") Book book,
            @PathParam("bookId") long bookId) {
        if (book.getId() == null) {
            book.setId(bookId);
        }
        bookDAO.update(book);
        return Response.Status.OK;
    }

    @DELETE
    @Path("/{bookId}")
    @ApiOperation(value = "Deletes a book specified by its id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!")
    })
    @UnitOfWork
    public Response.Status deleteBook(@PathParam("bookId") long bookId) {
        bookDAO.delete(this.getBook(bookId));
        return Response.Status.NO_CONTENT;
    }

    @Override
    @GET
    @Produces("application/json")
    @Path("/search/{searchString}")
    @Timed
    @ApiOperation(value = "Searches for a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
    })
    @UnitOfWork
    public Book[] list(@PathParam("searchString") String searchString) {
        List objects = bookDAO.searchByKeyword(searchString);
        Book[] books = (Book[]) objects.toArray(new Book[objects.size()]);
        return books;
    }

    @Override
    @POST
    @Path("/add/{quantity}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Adds a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
    })
    @UnitOfWork
    public boolean add(
            @ApiParam(required = true, value = "A Book in JSON format. The id field is ignored") Book book,
            @PathParam("quantity") int quantity) {
        book.setId(null);
        book.setQuantity(quantity);
        long bookId = bookDAO.create(book);
        return bookId != -1;
    }

    @Override
    @POST
    @Path("/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @ApiOperation(value = "Perform a purchase of books")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
    })
    @UnitOfWork
    public int[] buy(@ApiParam(required = true, value = "A list of books in JSON format. You can get a list from theCart API or from /api/books.") Book... books) {
        int[] bookStatuses = new int[books.length];
        List<Book> booksFromDb = bookDAO.findAll();
        for (int index = 0; index < books.length; index++) {
            Book book = books[index];
            if (booksFromDb.contains(book)) {
                int bookIndex = booksFromDb.indexOf(book);
                if (booksFromDb.get(bookIndex).getQuantity() <= 0) {
                    bookStatuses[index] = BookStatusEnum.NOT_IN_STOCK.getValue();
                } else if (booksFromDb.get(bookIndex).getQuantity() > 0) {
                    bookStatuses[index] = BookStatusEnum.OK.getValue();
                }
            } else {
                bookStatuses[index] = BookStatusEnum.DOES_NOT_EXIST.getValue();
            }
        }
        return bookStatuses;
    }

    private Book findSafely(long bookId) {
        final Optional<Book> book = Optional.ofNullable(bookDAO.findById(bookId));
        if (!book.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return book.get();
    }
}
