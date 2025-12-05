package com.github.alef_torres.data.dto.v1;

import com.fasterxml.jackson.annotation.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Relation(collectionRelation = "books")
@JsonPropertyOrder({"id", "title", "author", "launch_date", "price"})
public class BookDTOV1 extends RepresentationModel<BookDTOV1> implements Serializable {

    @JsonProperty("book_id")
    private long id;

    @JsonProperty("book_title")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;

    @JsonProperty("book_author")
    private String author;

    @JsonProperty("book_launch_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date launch_date;

    @JsonProperty("book_price")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double price;

    public BookDTOV1() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(Date launch_date) {
        this.launch_date = launch_date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookDTOV1 bookDTOV1 = (BookDTOV1) o;
        return getId() == bookDTOV1.getId() && Objects.equals(getTitle(), bookDTOV1.getTitle()) && Objects.equals(getAuthor(), bookDTOV1.getAuthor()) && Objects.equals(getLaunch_date(), bookDTOV1.getLaunch_date()) && Objects.equals(getPrice(), bookDTOV1.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getAuthor(), getLaunch_date(), getPrice());
    }
}
