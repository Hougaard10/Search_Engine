package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

    /**
     * This class creates a test for the ranking of search results using the TF-IDF Score.
     */

class RankingTest {

    private QueryHandler qh = null;

    /**
     * This method creates a database with 3 websites.
     */

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<>();
        sites.add(new Website("1.com", "example1", Arrays.asList("america", "queen")));
        sites.add(new Website("2.com", "example2", Arrays.asList("poland", "president", "poland")));
        sites.add(new Website("3.com", "example3", Arrays.asList("denmark", "copenhagen", "denmark", "denmark")));

        Index idx = new InvertedIndexHashMap();
        idx.build(sites);
        qh = new QueryHandler(idx);
    }

    /**
     * This method tests the ranking of websites when queries are performed on the database.
     * The number of websites containing all of the searched queries, tested.
     * After the result of the total, each individual query is tested, to make sure that the total matches the individual queries sum.
     */

    @Test
    void testRanking() {
        List<Website> results = qh.getMatchingWebsites("poland OR america");
        assertEquals(2.0, results.size());
        assertEquals(1.0, qh.getMatchingWebsites("poland").size());
        assertEquals(1.0, qh.getMatchingWebsites("america").size());
        assertEquals("example2", results.get(0).getTitle());
        assertEquals("example1", results.get(1).getTitle());

        List<Website> results2 = qh.getMatchingWebsites("poland OR denmark");
        assertEquals(2.0, results2.size());
        assertEquals(1.0, qh.getMatchingWebsites("poland").size());
        assertEquals(1.0, qh.getMatchingWebsites("denmark").size());
        assertEquals("example3", results2.get(0).getTitle());
        assertEquals("example2", results2.get(1).getTitle());

        List<Website> results3 = qh.getMatchingWebsites("america OR poland OR denmark");
        assertEquals(3.0, results3.size());
        assertEquals(1.0, qh.getMatchingWebsites("america").size());
        assertEquals(1.0, qh.getMatchingWebsites("poland").size());
        assertEquals(1.0, qh.getMatchingWebsites("denmark").size());
        assertEquals("example3", results3.get(0).getTitle());
        assertEquals("example2", results3.get(1).getTitle());
        assertEquals("example1", results3.get(2).getTitle());

        List<Website> results4 = qh.getMatchingWebsites("america queen OR denmark copenhagen");
        assertEquals(2.0, results4.size());
        assertEquals(1.0, qh.getMatchingWebsites("america queen").size());
        assertEquals(1.0, qh.getMatchingWebsites("denmark copenhagen").size());
        assertEquals("example3", results4.get(0).getTitle());
        assertEquals("example1", results4.get(1).getTitle());
    }
}
