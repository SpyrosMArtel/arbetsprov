package se.contribe.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Book> bookList;
    private BigDecimal totalPrice;

    /**
     * Constructs an empty cart.
     */
    public Cart() {
        this.bookList = new ArrayList<>();
        this.totalPrice = BigDecimal.ZERO;
    }

    /**
     * Retrieves all the books that are currently in the cart.
     * @return A @{@link List} with the books present in the cart
     */
    public List<Book> getBookList() {
        return bookList;
    }

    public boolean addBook(Book book) {
        if (bookList.contains(book)) {
            return false;
        } else {
            bookList.add(book);
            totalPrice = totalPrice.add(book.getPrice());
            return true;
        }
    }

    /**
     * Removes the specified book from the cart.
     * @param book @{@link Book} to be removed.
     */
    public void removeBook(Book book) {
        if (bookList.contains(book)) {
            bookList.remove(book);
            totalPrice = totalPrice.subtract(book.getPrice());
        }
    }

    /**
     * Retrieves the total price of the items in the cart.
     * @return @{@link BigDecimal} representing the total price.
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retrieves the cart contents.
     * @return @{@link List} holding all the items in the cart along with the total price.
     */
    public List<String> getContents() {
        List<String> contents = new ArrayList<>();
        for (Book book : bookList) {
            contents.add(String.format("%s, %s, %s", book.getTitle(), book.getAuthor(), book.getPrice()));
        }
        contents.add(String.format("Total Price: %s", totalPrice.toString()));
        return contents;
    }
}
