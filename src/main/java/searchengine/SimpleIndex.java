package searchengine;

import java.util.ArrayList;
import java.util.List;

    /**
     * SimpleIndex implements Index to build an index from a list of websites.
     */

public class SimpleIndex implements Index {
    private int size = 0; //returns 0 if the database has not been built

    /**
     * @return the size of the index
     */

    @Override
    public int size(){
        return size;
    }

    /**
     * The list of websites stored in the index.
     */
    private List<Website> sites = null;

    /**
     * The build method processes a list of websites into the index data structure.
     *
     * @param sites The list of websites that should be indexed
     */

    @Override
    public void build(List<Website> sites) {
        this.sites = sites;
        size = sites.size();
    }

    /**
     * Given a query string, returns a list of all websites that contain the query.
     *
     * @param query The query
     * @return the list of websites that contains the query word.
     */

    @Override
    public List<Website> lookup(String query) {
        List<Website> result = new ArrayList<Website>();
        for (Website w: sites) {
            if (w.containsWord(query)) {
                result.add(w);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "SimpleIndex{" +
                "sites=" + sites +
                '}';
    }
}
