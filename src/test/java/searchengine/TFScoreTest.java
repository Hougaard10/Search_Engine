package searchengine;

//Imports
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

    /**
     * Test how the TFScore is able score and rank various searches.
     * First adding Website objects into a local ArrayList sites, and HashMap siteMap, with a URL, title and a random number of words.
     * Then there are performed five tests.
     * 1. Test the score on a single word, on a siteMap Throws NullPointerException, and are expected to handle it, since a Null-object is added to the map.
     * 2. Test the score on a single word, on Tiny-databse.
     * 3. Test the score on a single word, on a test-file.
     * 4. Test whether the system is able to detect and catch IndexOutOfBoundsExceptions.
     * 5. Testing the sorter method from QueryHandler, is able to sort based on the value, which is calculated by getScore() from TFIDF.
     */

public class TFScoreTest {

        //Fields for local map.
        private InvertedIndexHashMap invertedIndexHashMap = new InvertedIndexHashMap();
        private QueryHandler qh = new QueryHandler(invertedIndexHashMap);
        private TFScore tf = new TFScore();
        private Map<Website, Double> siteMap = new HashMap<>();

        //Fields for tiny-database
        private Index idxDB;
        private QueryHandler qhDB;
        private List<Website> sitesDB;

        //Fields for test-file.txt
        private Index idxTF;
        private QueryHandler qhTF;
        private List<Website> sitesTF;


        // Creation of objects
        Website web1  = new Website("Web1.com",  "example1",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web2  = new Website("Web2.com",  "example2",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web3  = new Website("Web3.com",  "example3",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web4  = new Website("Web4.com",  "example4",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web5  = new Website("Web5.com",  "example5",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web6  = new Website("Web6.com",  "example6",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1"));
        Website web7  = new Website("Web7.com",  "example7",  Arrays.asList("word1", "word1", "word1", "word1", "word1"));
        Website web8  = new Website("Web8.com",  "example8",  Arrays.asList("word1", "word1", "word1", "word1"));
        Website web9  = new Website("Web9.com",  "example9",  Arrays.asList("word1", "word1", "word1"));
        Website web10 = new Website("Web10.com", "example10", Arrays.asList("word1", "word1"));
        Website web11 = new Website("Web11.com", "example11", Arrays.asList("word1"));
        Website web12 = new Website("Web12.com", "example12", Arrays.asList("word88", "word88"));

    /**
     *  Adds objects into map. Mixed sequence to make sure, that map is not returned in order they are added into the map.
     *  Key = Website
     *  Value = Score for a given query word, Website, and Index
     *  If Website contains words that are not the query the score will be 0.
     */

    @Before
    public void setUp() {

        // Build for tiny-database
        this.sitesDB = FileHelper.parseFile("data/enwiki-tiny.txt");
        idxDB = new InvertedIndexHashMap();
        idxDB.build(sitesDB);
        qhDB = new QueryHandler(idxDB);

        // Build for test-file
        this.sitesTF = FileHelper.parseFile("data/test-file.txt");
        idxTF = new InvertedIndexHashMap();
        idxTF.build(sitesTF);
        qhTF = new QueryHandler(idxTF);


        siteMap.put(web6, tf.getScore("word1", web6, invertedIndexHashMap));
        siteMap.put(web1, tf.getScore("word1", web1, invertedIndexHashMap));
        siteMap.put(web12, tf.getScore("word1", web12, invertedIndexHashMap));
        siteMap.put(web5, tf.getScore("word1", web5, invertedIndexHashMap));
        siteMap.put(web11, tf.getScore("word1", web11, invertedIndexHashMap));
        siteMap.put(web7, tf.getScore("word1", web7, invertedIndexHashMap));
        siteMap.put(web10, tf.getScore("word1", web10, invertedIndexHashMap));
        siteMap.put(web8, tf.getScore("word1", web8, invertedIndexHashMap));
        siteMap.put(web2, tf.getScore("word1", web2, invertedIndexHashMap));
        siteMap.put(web3, tf.getScore("word1", web3, invertedIndexHashMap));
        siteMap.put(web9, tf.getScore("word1", web9, invertedIndexHashMap));
        siteMap.put(web4, tf.getScore("word1", web4, invertedIndexHashMap));
    }

    @AfterEach
    public void tearDown() {

        qh = null;
        qhTF = null;
        qhDB = null;
        idxDB = null;
        idxTF = null;
    }

        /**
         *  Test getScore() from TFScore.
         *  Tests whether the calculated score for a given query, match the exact no. of occurrences in the map.
         */

    @Test
    public void testSingleWordScore() {

        //Tests on local map.
        assertEquals(11.0, tf.getScore("word1", web1, invertedIndexHashMap));
        assertEquals(10.0, tf.getScore("word1", web2, invertedIndexHashMap));
        assertEquals(9.0, tf.getScore("word1", web3, invertedIndexHashMap));
        assertEquals(8.0, tf.getScore("word1", web4, invertedIndexHashMap));
        assertEquals(7.0, tf.getScore("word1", web5, invertedIndexHashMap));
        assertEquals(6.0, tf.getScore("word1", web6, invertedIndexHashMap));
        assertEquals(5.0, tf.getScore("word1", web7, invertedIndexHashMap));
        assertEquals(4.0, tf.getScore("word1", web8, invertedIndexHashMap));
        assertEquals(3.0, tf.getScore("word1", web9, invertedIndexHashMap));
        assertEquals(2.0, tf.getScore("word1", web10, invertedIndexHashMap));
        assertEquals(1.0, tf.getScore("word1", web11, invertedIndexHashMap));
        assertEquals(0.0, tf.getScore("word2", web11, invertedIndexHashMap));
        assertEquals(0.0, tf.getScore("word1", web12, invertedIndexHashMap));
        assertEquals(2.0, tf.getScore("word88", web12, invertedIndexHashMap));
    }

    /**
     *  Test getScore() from TFScore.
     *  Tests whether the calculated score for a given query, match the exact no. of occurrences in Tiny-database.
     */

    @Test
    public void testSingleWordScoreDB() {

        //Tests on tiny-database.
        assertEquals(4.0, tf.getScore("denmark", sitesDB.get(1), idxDB));
        assertEquals(1.0, tf.getScore("denmark", sitesDB.get(3), idxDB));
        assertEquals(2.0, tf.getScore("copenhagen", sitesDB.get(3), idxDB));
        assertEquals(2.0, tf.getScore("japan", sitesDB.get(2), idxDB));
    }

    /**
     *  Test getScore() from TFScore.
     *  Tests whether the calculated score for a given query, match the exact no. of occurrences in test-file.
     */

    @Test
    public void testSingleWordScoreTF() {

        //Tests on test-file.
        assertEquals(2.0, tf.getScore("word1", sitesTF.get(1), idxTF));
        assertEquals(0.0, tf.getScore("wordx", sitesTF.get(1), idxTF));
        assertEquals(1.0, tf.getScore("wordxxx", sitesTF.get(4), idxTF));
    }

    /**
     * Tests getScore on tiny-database and local test-file.
     * Throws IndexOutOfBoundsException which are expected to be caught since there is no index "100", and index "10".
    */

    @Test  (expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {

            //Tests on tiny-database.
            tf.getScore("denmark", sitesDB.get(100), idxDB);

            //Tests on local test-file.
            tf.getScore("word1", sitesTF.get(10), idxDB);
    }

    @Test
     public void testSorterMethodSingleWord() {

        /**
         * Test sorter() from QueryHandler.
         * Test whether the map is returned in a sorted order, based on value.
         * Value calculated when adding into the map using getScore() from TFScore.
         */

        // Test if the sorting method from QueryHandler returns the Websites in the expected order, based on their score when searching on "word1".
        // Tests on local map.

        assertEquals(qh.sorter(siteMap).get(0), web1);
        assertEquals(qh.sorter(siteMap).get(1), web2);
        assertEquals(qh.sorter(siteMap).get(2), web3);
        assertEquals(qh.sorter(siteMap).get(3), web4);
        assertEquals(qh.sorter(siteMap).get(4), web5);
        assertEquals(qh.sorter(siteMap).get(5), web6);
        assertEquals(qh.sorter(siteMap).get(6), web7);
        assertEquals(qh.sorter(siteMap).get(7), web8);
        assertEquals(qh.sorter(siteMap).get(8), web9);
        assertEquals(qh.sorter(siteMap).get(9), web10);
        assertEquals(qh.sorter(siteMap).get(10), web11);
        assertEquals(qh.sorter(siteMap).get(11), web12);
    }
}
