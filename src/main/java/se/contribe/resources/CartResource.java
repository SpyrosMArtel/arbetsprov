package se.contribe.resources;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import se.contribe.core.Book;
import se.contribe.core.Cart;
import se.contribe.db.BookDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cart")
@Api(value = "/cart", description = "Operations on the shopping cart")
public class CartResource {
    private Cart cart;
    private final BookDAO bookDAO;

    public CartResource(BookDAO bookDAO) {
        this.cart = new Cart();
        this.bookDAO = bookDAO;
    }

    @GET
    @Produces("application/json")
    @Timed
    @ApiOperation(value = "Returns the cart contents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
    })
    @UnitOfWork
    public List<String> list() {
        return cart.getContents();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    @ApiOperation(value = "Returns the cart contents")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
            @ApiResponse(code = 400, message = "Either book was not found or it was an invalid entry")
    })
    @UnitOfWork
    public Response add(@ApiParam(
            required = true, value = "A Book in JSON format, where the id specifies the book to be added.") Book book) {
        Book bookToAdd = bookDAO.findById(book.getId());
        if (bookToAdd == null || !bookToAdd.equals(book)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(book).build();
        }
        if (cart.addBook(bookToAdd)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.OK).build();
        }
    }

    @DELETE
    @Path("/remove/{bookId}")
    @ApiOperation(value = "Removes a specific book from the cart")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success!!")
    })
    @UnitOfWork
    public Response remove(@PathParam("bookId") long bookId) {
        Book bookToRemove = bookDAO.findById(bookId);
        if (bookToRemove != null) {
            cart.removeBook(bookToRemove);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
