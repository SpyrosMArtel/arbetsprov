package se.contribe.resources;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import se.contribe.core.Book;
import se.contribe.core.Cart;
import se.contribe.db.BookDAO;

import javax.validation.Valid;
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
    })
    @UnitOfWork
    public Response.Status add(@ApiParam(
            required = true, value = "A Book in JSON format, where the id specifies the book to be added.") Book book) {
        cart.addBook(bookDAO.findById(book.getId()));
        return Response.Status.OK;
    }

    @POST
    @Path("/remove/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Removes a specific book from the cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!!"),
    })
    @UnitOfWork
    public Response.Status remove(@PathParam("bookId") long bookId) {
        cart.removeBook(bookDAO.findById(bookId));
        return Response.Status.NO_CONTENT;
    }
}
