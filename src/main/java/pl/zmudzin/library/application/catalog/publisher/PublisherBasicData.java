package pl.zmudzin.library.application.catalog.publisher;

import java.io.Serializable;

/**
 * @author Piotr Żmudzin
 */
public class PublisherBasicData implements Serializable {

    protected Long id;
    protected String name;

    PublisherBasicData() {
    }

    public PublisherBasicData(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
