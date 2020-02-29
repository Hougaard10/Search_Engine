
package searchengine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


    /**
     * This test is responsible for testing the InvertedIndex classes InvertedIndexHashMap and InvertedIndex TreeMap.
     * The test are performed by creating a local ArrayList, where Website objects are added into, with a URL, title and different numbers of words.
     * There are performed to different tests on each of the InvertedIndex classes. The build method is tested on the structure of the maps.
     * The lookUp method are tested combined, with first HashMap and then TreeMap. We are using the AssertEquals to test the expected number
     * of output, versus the actual number returned. We are testing for how many objects that contains are given word.
     * To make sure that we are getting a broad scope of tests, we are adding special case letters, numbers and both lower and upper case letters.
     * By doing this we will insure that our search engine can take in multiple different types of input.
     */

    class InvertedIndexTest {
        Index invertedIndexTreeMap = null;
        Index invertedIndexHashMap = null;

        /**
         * Setting up our local ArrayList, and adding into that with different Website objects.
         */

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word1")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("example3.com", "example3", Arrays.asList("denmark", "word3")));
        sites.add(new Website("example4.com", "example4", Arrays.asList("denmark", "germany")));
        sites.add(new Website("example5.com", "example5", Arrays.asList("London", "london")));
        sites.add(new Website("example6.com", "example6", Arrays.asList("rome", "paris", "København")));
        sites.add(new Website("example7.com", "example7", Arrays.asList("Rome", "paris", "Madrid", "den-mark")));

        invertedIndexTreeMap = new InvertedIndexTreeMap();
        invertedIndexHashMap = new InvertedIndexHashMap();
        invertedIndexTreeMap.build(sites);
        invertedIndexHashMap.build(sites);
    }

    @AfterEach
    void tearDown() {
        invertedIndexHashMap = null;
        invertedIndexTreeMap = null;
    }

        /**
         * Tests the structure of the InvertedIndexHashMap is returned.
         */

    @Test
    void buildInvertedIndexHashMap() {
        assertEquals("InvertedIndex{map={rome=[Website{title='example6', url='example6.com', words=[rome, paris, København]}], den-mark=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], london=[Website{title='example5', url='example5.com', words=[London, london]}], København=[Website{title='example6', url='example6.com', words=[rome, paris, København]}], word1=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}], germany=[Website{title='example4', url='example4.com', words=[denmark, germany]}], word3=[Website{title='example2', url='example2.com', words=[word2, word3]}, Website{title='example3', url='example3.com', words=[denmark, word3]}], word2=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}], denmark=[Website{title='example3', url='example3.com', words=[denmark, word3]}, Website{title='example4', url='example4.com', words=[denmark, germany]}], paris=[Website{title='example6', url='example6.com', words=[rome, paris, København]}, Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], Rome=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], Madrid=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], London=[Website{title='example5', url='example5.com', words=[London, london]}]}}", invertedIndexHashMap.toString());
    }

        /**
         * Tests the structure of the InvertedIndexTreeMap is returned.
         */

    @Test
    void buildInvertedIndexTreeMap() {
        assertEquals("InvertedIndex{map={København=[Website{title='example6', url='example6.com', words=[rome, paris, København]}], London=[Website{title='example5', url='example5.com', words=[London, london]}], Madrid=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], Rome=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], den-mark=[Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], denmark=[Website{title='example3', url='example3.com', words=[denmark, word3]}, Website{title='example4', url='example4.com', words=[denmark, germany]}], germany=[Website{title='example4', url='example4.com', words=[denmark, germany]}], london=[Website{title='example5', url='example5.com', words=[London, london]}], paris=[Website{title='example6', url='example6.com', words=[rome, paris, København]}, Website{title='example7', url='example7.com', words=[Rome, paris, Madrid, den-mark]}], rome=[Website{title='example6', url='example6.com', words=[rome, paris, København]}], word1=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}], word2=[Website{title='example1', url='example1.com', words=[word1, word2, word1]}, Website{title='example2', url='example2.com', words=[word2, word3]}], word3=[Website{title='example2', url='example2.com', words=[word2, word3]}, Website{title='example3', url='example3.com', words=[denmark, word3]}]}}", invertedIndexTreeMap.toString());
    }

    /**
     * Tests the lookUp method on InvertedIndex HashMap, and makes sure that the right number of matches are returned.
     * Both with numbers, special characters, and lower - and upper case letters.
     */

    @Test
    void lookupHashMap() {

        // Tests on InvertedIndexHashMap
        assertEquals(1, invertedIndexHashMap.lookup("word1").size());
        assertEquals(2, invertedIndexHashMap.lookup("word2").size());
        assertEquals(0, invertedIndexHashMap.lookup("word4").size());
        assertEquals(1, invertedIndexHashMap.lookup("london").size());
        assertEquals(1, invertedIndexHashMap.lookup("London").size());
        assertEquals(1, invertedIndexHashMap.lookup("København").size());
        assertEquals(2, invertedIndexHashMap.lookup("paris").size());
        assertEquals(1, invertedIndexHashMap.lookup("Rome").size());
        assertEquals(0, invertedIndexHashMap.lookup("madrid").size());
        assertEquals(1, invertedIndexHashMap.lookup("den-mark").size());
    }

        /**
         * Tests that a given title is matching with the query.
         */

    @Test
    void testHashMapTitle() {
        assertEquals("example3", invertedIndexHashMap.lookup("denmark").get(0).getTitle());
        assertEquals("example4", invertedIndexHashMap.lookup("denmark").get(1).getTitle());
        assertEquals("example6", invertedIndexHashMap.lookup("paris").get(0).getTitle());
    }

        /**
         * Tests the lookUp method on InvertedIndexTreeMap, and makes sure that the right number of matches are returned.
         * With, special characters, and lower - and upper case letters.
         */

    @Test
    void lookupTreeMap() {
        // Tests on InvertedIndexTreeMap
        assertEquals(1, invertedIndexTreeMap.lookup("word1").size());
        assertEquals(2, invertedIndexTreeMap.lookup("word2").size());
        assertEquals(0, invertedIndexTreeMap.lookup("word4").size());
        assertEquals(1, invertedIndexTreeMap.lookup("london").size());
        assertEquals(1, invertedIndexTreeMap.lookup("London").size());
        assertEquals(1, invertedIndexTreeMap.lookup("København").size());
        assertEquals(2, invertedIndexTreeMap.lookup("paris").size());
        assertEquals(1, invertedIndexTreeMap.lookup("Rome").size());
        assertEquals(0, invertedIndexTreeMap.lookup("madrid").size());
        assertEquals(1, invertedIndexTreeMap.lookup("den-mark").size());
    }

        /**
         * Tests that a given title is matching with the query.
         */

    @Test
    void testTreeMapTitle() {
        assertEquals("example3", invertedIndexHashMap.lookup("denmark").get(0).getTitle());
        assertEquals("example4", invertedIndexHashMap.lookup("denmark").get(1).getTitle());
        assertEquals("example6", invertedIndexHashMap.lookup("paris").get(0).getTitle());
    }
}




