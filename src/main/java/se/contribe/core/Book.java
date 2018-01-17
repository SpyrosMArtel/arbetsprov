package se.contribe.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "book")
@NamedQueries({
        @NamedQuery(
                name = "se.contribe.core.Book.findAll",
                query = "SELECT b FROM Book b"
        ),
        @NamedQuery(
                name = "se.contribe.core.Book.findByTerms",
                query = "SELECT b FROM Book b WHERE b.title LIKE :search OR b.author LIKE :search"
        )
})
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "author")
    @NotNull
    private String author;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    public Book() {}
    /**
     * Instantiates a book and specifies its title and the person who wrote it.
     * @param title The name of the book.
     * @param author The person's name who wrote the book.
     */
    public Book(String title, String author){
        this.title = title;
        this.author = author;
    }

    /**
     * Instantiates a book and specifies its title, the person who wrote it and the amount in the inventory.
     * @param title The name of the book.
     * @param author The person's name who wrote the book.
     * @param quantity The amount of a book in the inventory
     */
    public Book(String title, String author, int quantity){
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    /**
     * Retrieves the title of the book.
     * @return The title of the book as a String.
     */
    @JsonProperty
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     * @param title The title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieve the book's unique identifier. Useful for database operations.
     * @return The book's unique identifier as Long
     */
    @JsonProperty
    public Long getId() {
        return id;
    }

    /**
     * Retrieves the author of the book.
     * @return The author of the book as a String.
     */
    @JsonProperty
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     * @param author The name of the person who wrote the book.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Retrieves the price of the book.
     * @return The price of the book as a BigDecimal.
     */
    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price of the book.
     * @param price The value in currency of the book.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Sets the id of the book.
     * @param id The unique identifier of the book.
     */
    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the amount of a specific book in the inventory.
     * @return The quantity of the book as a Integer.
     */
    @JsonProperty
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the book.
     * @param quantity The amount of a book in the inventory.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!getId().equals(book.getId())) return false;
        if (!getTitle().equals(book.getTitle())) return false;
        if (!getAuthor().equals(book.getAuthor())) return false;
        return getPrice() != null ? getPrice().equals(book.getPrice()) : book.getPrice() == null;

    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result = getId().hashCode();
        }
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getAuthor().hashCode();
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        return result;
    }
}
