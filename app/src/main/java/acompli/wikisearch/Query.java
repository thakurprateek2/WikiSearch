package acompli.wikisearch;

import java.util.Map;

/**
 * Created by thakurprateek on 02-11-2015.
 */
public class Query {

    private Map<String, Page> pages;

    public Map<String, Page> getPages() {
        return pages;
    }

    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }
}
