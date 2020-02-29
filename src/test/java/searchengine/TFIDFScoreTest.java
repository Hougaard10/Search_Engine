package searchengine;

//Imports
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test how the TFIDFScore is able score and rank various searches.
 * First adding Website objects into a local ArrayList sites, and HashMap siteMap, with a URL, title and a random number of words.
 * Then there are performed five tests.
 * 1. Test the score on a single word, on a siteMap Throws NullPointerException, and are expected to handle it, since a Null-object is added to the map.
 * 2. Test the score on a single word, on a test-file.
 * 3. Test the score on a single word, on Tiny-databse.
 * 4. Test whether the system is able to detect and catch IndexOutOfBoundsExceptions.
 * 5. Testing the sorter method from QueryHandler, is able to sort based on the value, which is calculated by getScore() from TFIDF.
 */

public class TFIDFScoreTest {

        //Fields for local map
        private Map<Website, Double> siteMap = new HashMap<>();
        private Index index;
        private QueryHandler qh;
        private Score score;

        //Fields for test-file.txt
        private Index idxTF;
        private QueryHandler qhTF;
        private List<Website> sitesTestFile;


        //Fields for tiny-database
        private Index idxDB;
        private QueryHandler qhDB;
        private List<Website> sitesDB;

        // Creation of objects
        Website web1  = new Website("Web1.com",  "example1",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word88", "word88", "word88"));
        Website web2  = new Website("Web2.com",  "example2",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word88", "word88"));
        Website web3  = new Website("Web3.com",  "example3",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1", "word88"));
        Website web4  = new Website("Web4.com",  "example4",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web5  = new Website("Web5.com",  "example5",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1", "word1"));
        Website web6  = new Website("Web6.com",  "example6",  Arrays.asList("word1", "word1", "word1", "word1", "word1", "word1"));
        Website web7  = new Website("Web7.com",  "example7",  Arrays.asList("word1", "word1", "word1", "word1", "word1"));
        Website web8  = new Website("Web8.com",  "example8",  Arrays.asList("word1", "word1", "word1", "word1"));
        Website web9  = new Website("Web9.com",  "example9",  Arrays.asList("word1", "word1", "word1"));
        Website web10 = new Website("Web10.com", "example10", Arrays.asList("word1", "word1"));
        Website web11 = new Website("Web11.com", "example11", Arrays.asList("word1"));
        Website web12 = new Website("Web12.com", "example12", Arrays.asList("word88", "word88"));
        Website web13 = new Website("Web12.com", "example12", Arrays.asList("word88", "word88"));

    @Before
    public void setUp() {

        List<Website> sites = new ArrayList<>();
        sites.add(web1);
        sites.add(web2);
        sites.add(web3);
        sites.add(web4);
        sites.add(web5);
        sites.add(web6);
        sites.add(web7);
        sites.add(web8);
        sites.add(web9);
        sites.add(web10);
        sites.add(web11);
        sites.add(web12);
        sites.add(web13);

        // Build for local map
        index = new InvertedIndexHashMap();
        index.build(sites);
        qh = new QueryHandler(index);
        score = new TFIDFScore();

        // Build for test-file
        this.sitesTestFile = FileHelper.parseFile("data/test-file.txt");
        idxTF = new InvertedIndexHashMap();
        idxTF.build(sitesTestFile);
        qhTF = new QueryHandler(idxTF);

        // Build for tiny-database
        this.sitesDB = FileHelper.parseFile("data/enwiki-tiny.txt");
        idxDB = new InvertedIndexHashMap();
        idxDB.build(sitesDB);
        qhDB = new QueryHandler(idxDB);

        /**
         *  Adds objects into map. Mixed sequence to make sure, that map is not returned in order they are added into the map.
         *  Key = Website
         *  Value = Score for a given query word, Website, and Index
         *  If Website contains words that are not the query the score will be 0.
         */

        siteMap.put(web1,  score.getScore("word1", web1,  index));
        siteMap.put(web2,  score.getScore("word1", web2,  index));
        siteMap.put(web3,  score.getScore("word1", web3,  index));
        siteMap.put(web4,  score.getScore("word1", web4,  index));
        siteMap.put(web5,  score.getScore("word1", web5,  index));
        siteMap.put(web6,  score.getScore("word1", web6,  index));
        siteMap.put(web7,  score.getScore("word1", web7,  index));
        siteMap.put(web8,  score.getScore("word1", web8,  index));
        siteMap.put(web9,  score.getScore("word1", web9,  index));
        siteMap.put(web10, score.getScore("word1", web10, index));
        siteMap.put(web11, score.getScore("word1", web11, index));
        siteMap.put(web12, score.getScore("word1", web12, index));
        siteMap.put(web13, score.getScore("word1", web13, index));
    }

    @AfterEach
    public void tearDown() {

        idxDB = null;
        idxTF = null;
        qh = null;
        qhTF = null;
        qhDB = null;
    }

    @Test
    public void testSingleWordScore() throws NullPointerException {
        try {

        /**
         *  Test getScore() from TFScore.
         *  Tests whether the calculated score for a given query, match the exact no. of occurrences in the local map.
         */

        assertEquals(1.8375949312948285,  score.getScore("word1",  web1,  index));
        assertEquals(1.6705408466316625,  score.getScore("word1",  web2,  index));
        assertEquals(1.5034867619684962,  score.getScore("word1",  web3,  index));
        assertEquals(1.3364326773053299,  score.getScore("word1",  web4,  index));
        assertEquals(1.1693785926421636,  score.getScore("word1",  web5,  index));
        assertEquals(1.0023245079789973,  score.getScore("word1",  web6,  index));
        assertEquals(0.8352704233158312,  score.getScore("word1",  web7,  index));
        assertEquals(0.6682163386526649,  score.getScore("word1",  web8,  index));
        assertEquals(0.5011622539894987,  score.getScore("word1",  web9,  index));
        assertEquals(0.33410816932633247, score.getScore("word1",  web10, index));
        assertEquals(0.0,                 score.getScore("word1",  web12, index));
        assertEquals(1.9110228900548727,  score.getScore("word88", web12, index));
        assertEquals(1.9110228900548727,  score.getScore("word88", web13, index));
        assertEquals(0.0,score.getScore(null, null, null));
    } catch (NullPointerException anIndexOutOfBoundsException) {
    }
    }

        /**
         *  Test getScore() from TFScore.
         *  Tests whether the calculated score for a given query, match the exact no. of occurrences in the test-file.
         */

    @Test
    public void testSingleWordTestFile() {

            assertEquals(0.22314355131420976, score.getScore("word1", sitesTestFile.get(0), idxTF));
            assertEquals(0.44628710262841953, score.getScore("word1", sitesTestFile.get(1), idxTF));
            assertEquals(0.22314355131420976, score.getScore("word1", sitesTestFile.get(2), idxTF));
            assertEquals(0.22314355131420976, score.getScore("word1", sitesTestFile.get(3), idxTF));
            assertEquals(0.0, score.getScore("word1", sitesTestFile.get(4), idxTF));
    }

        /**
         *  Test getScore() from TFScore.
         *  Tests whether the calculated score for a given query, match the exact no. of occurrences in the tiny-database.
         */

    @Test
    public void testSingeWordDB() {

        assertEquals(1.6218604324326575, score.getScore("denmark",    sitesDB.get(1), idxDB));
        assertEquals(1.791759469228055,  score.getScore("germany",    sitesDB.get(1), idxDB));
        assertEquals(3.58351893845611,   score.getScore("japan",      sitesDB.get(2), idxDB));
        assertEquals(0.8109302162163288, score.getScore("copenhagen", sitesDB.get(3), idxDB));
    }

    /**
     * Tests getScore with TFIDF on tiny-database and local test-file.
     * Throws IndexOutOfBoundsException which are expected to be caught since there is no index "100", and index "10".
     */

    @Test  (expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {

        //Tests on tiny-database.
        score.getScore("denmark", sitesDB.get(100), idxDB);

        //Tests on local test-file.
        score.getScore("word1", sitesTestFile.get(10), idxDB);
    }
    @Test
    public void testSorterMethodSingleWord() {

        /**
         * Test sorter() from QueryHandler.
         * Test whether the map is returned in a sorted order.
         */

        //Tests sorter on local map.
        assertEquals(qh.sorter(siteMap).get(0), web1);
        assertEquals(qh.sorter(siteMap).get(1), web2);
        assertEquals(qh.sorter(siteMap).get(2), web3);
        assertEquals(qh.sorter(siteMap).get(3), web4);
        assertEquals(qh.sorter(siteMap).get(4), web5);
        assertEquals(qh.sorter(siteMap).get(5), web6);
        assertEquals(qh.sorter(siteMap).get(6), web7);
        assertEquals(qh.sorter(siteMap).get(7), web8);
    }
}