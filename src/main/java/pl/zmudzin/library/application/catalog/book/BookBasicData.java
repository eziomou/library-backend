package pl.zmudzin.library.application.catalog.book;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class BookBasicData implements Serializable {

    protected Long id;
    protected String title;

    BookBasicData() {
    }

    public BookBasicData(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
