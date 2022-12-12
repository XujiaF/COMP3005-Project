package bookstore.model;

import java.math.BigDecimal;

public class Book {
    private String isbn;
    private String title;
    private Integer numberOfPages;
    private Float price;
    private Integer warehouseId;
    private Integer pubId;

    public Book() {
    }

    public Book(String ISBN, String title, Integer numberOfPages, Float price, Integer warehouseId, Integer pubId) {
        this.isbn = ISBN;
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.price = price;
        this.warehouseId = warehouseId;
        this.pubId = pubId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getPubId() {
        return pubId;
    }

    public void setPubId(Integer pubId) {
        this.pubId = pubId;
    }

    @Override
    public String toString() {
        return "isbn='" + isbn + '\'' +
                        ", title='" + title + '\'' +
                        ", numberOfPages=" + numberOfPages +
                        ", price=" + price +
                        ", warehouseId=" + warehouseId +
                        ", pubId=" + pubId;
    }
}
