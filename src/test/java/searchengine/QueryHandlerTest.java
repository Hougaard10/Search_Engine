package searchengine;

//Imports
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    /**
     * Test how the QueryHandler is able to perform various searches.
     * First adding Website objects into a local ArrayList with a URL, title and a random number of words.
     * Then there are performed three tests.
     * 1. Test that the expected output is correct when searching for one word.
     * 2. Test that the expected output is correct when searching for multiple words and "OR" statements.
     *  - Also tests special characters, numbers and lower and upper case letters.
     * 3. Test for if a word at a given index returns the expected title.
     * 4. Test for if a word at a given index returns the expected URL.
     * 5. Testing the "Corner cases" Such as queries with lots of spaces, spaces and "OR" statements, empty strings.
     * Also test that a given title/URL will return the right word from a given index point in the ArrayList.
     */

class QueryHandlerTest {

    //Fields
    private QueryHandler qh = null;
    private QueryHandler qhDB = null;

    /**
     * Sets up test method.
     * Set up the local list, and tiny-database.
     */

    @BeforeEach
    void setUp() {

            List<Website> sitesDB = FileHelper.parseFile("data/enwiki-tiny.txt");
            Index idxDB = new InvertedIndexHashMap();
            idxDB.build(sitesDB);
            qhDB = new QueryHandler(idxDB);

        List<Website> sites = new ArrayList<>();
        sites.add(new Website("1.com",       "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("2.com",       "example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("3.com",       "example3", Arrays.asList("word3", "word4", "word5")));
        sites.add(new Website("denmark.com", "example4", Arrays.asList("denmark", "germany", "berlin", "denmark")));
        sites.add(new Website("king.com",    "example5", Arrays.asList("king", "denmark")));
        sites.add(new Website("url4.com",    "example6", Arrays.asList("Denmark", "Germany", "Berlin")));
        sites.add(new Website("uRl5.com",    "example7", Arrays.asList("germany", "alps")));
        sites.add(new Website("uRl6.com",    "example8", Arrays.asList("queen")));
        sites.add(new Website("uRl7.com",    "example9", Arrays.asList("queen")));
        sites.add(new Website("uRl8.com",    "example10", Arrays.asList("president", "america", "state")));
        sites.add(new Website("uRl8.com",    "example11", Arrays.asList("æØå", "@@@", "$§!€#")));
        sites.add(new Website("uRl9.com",    "example12", Arrays.asList("chancellor", "germany")));
        sites.add(new Website(".com",        "example13", Arrays.asList( "denmark")));
        sites.add(new Website("URL10.com",   "NotInUnicode", Arrays.asList("你")));  // Tests for characters not part of UTF-8
        sites.add(new Website("empty.com",   "Empty", Arrays.asList("")));

        Index idx = new InvertedIndexHashMap();
        idx.build(sites);
        qh = new QueryHandler(idx);
    }

    @AfterEach
    void tearDown() {
        qhDB = null;
        qh = null;
    }

        /**
         * Test for single word
         *  - Special characters
         *  - Test whether a given word matches the title or URL
         *  Also test that a given title/URL will return the right word from a given index point in the ArrayList
         * Test both on local created list and tiny-database
         */

    @Test
    void testSingleWord() {

        //Tests on tiny-database.
        assertEquals(4, qhDB.getMatchingWebsites("denmark").size());                   //Tests for single word
        assertEquals(4, qhDB.getMatchingWebsites("copenhagen").size());                //Tests for single word


        //Tests on local list.
        assertEquals(1, qh.getMatchingWebsites("word1").size());                       //Tests for single word
        assertEquals(2, qh.getMatchingWebsites("word2").size());                       //Tests for single word
        assertEquals(3, qh.getMatchingWebsites("denmark").size());                     //Tests for single word
        assertEquals(3, qh.getMatchingWebsites("germany").size());                     //Tests for single word
        assertEquals(1, qh.getMatchingWebsites("Germany").size());                     //Tests for single word - Uppercase letters
        assertEquals(1, qh.getMatchingWebsites("$§!€#").size());                       //Tests for single word - Special characters
        assertEquals(0, qh.getMatchingWebsites("$§!€a#").size());                      //Tests for single word - Special characters
        assertEquals(1, qh.getMatchingWebsites("æØå").size());                         //Tests for single word - Special characters
    }

        /**
         * Test for if a word at a given index returns the expected title.
         * Test both on local created list and tiny-database
         */

    @Test
        void testForTitle(){

        //Tests on tiny-database.
        assertEquals("Denmark", qhDB.getMatchingWebsites("denmark").get(0).getTitle());                      //Tests words match a given title
        assertEquals("University of Copenhagen", qhDB.getMatchingWebsites("denmark").get(3).getTitle());     //Tests words match a given title
        assertEquals("IT University of Copenhagen", qhDB.getMatchingWebsites("denmark").get(1).getTitle());  //Tests words match a given title


        //Tests on local list.
        assertEquals("example1", qh.getMatchingWebsites("word1").get(0).getTitle());            //Tests words match a given title
        assertEquals ("example4", qh.getMatchingWebsites("denmark").get(0).getTitle());         //Tests words match a given title
        assertEquals("example5", qh.getMatchingWebsites("king").get(0).getTitle());             //Tests words match a given title
        assertNotEquals ("example7", qh.getMatchingWebsites("queen").get(1).getTitle());      //Tests words match a given title
    }

        /**
         * Test for if a word at a given index returns the expected URL.
         * Test both on local created list and tiny-database
         */

    @Test
    void testForURL(){

        //Tests on tiny-database.
        assertEquals("https://en.wikipedia.org/wiki/Denmark", qhDB.getMatchingWebsites("denmark").get(0).getUrl());       //Tests words match a given URL
        assertEquals("https://en.wikipedia.org/wiki/Denmark", qhDB.getMatchingWebsites("copenhagen").get(3).getUrl());    //Tests words match a given URL

        //Tests on local list.
        assertEquals("denmark.com", qh.getMatchingWebsites("denmark").get(0).getUrl());    //Tests words match a given URL
        assertEquals(".com",        qh.getMatchingWebsites("denmark").get(2).getUrl());    //Tests word matches a given URL
    }

        /**
         * Test for multiple words and "OR" statements.
         * Test both on local created list and tiny-database
         */

    @Test
    void testMultipleWords() {

        //Tests on tiny-database.
        assertEquals(4, qhDB.getMatchingWebsites("denmark OR germany").size());  //Tests with single "OR"
        assertEquals(5, qhDB.getMatchingWebsites("denmark OR japan").size());    //Tests with single "OR"
        assertEquals(1, qhDB.getMatchingWebsites("denmark germany").size());     //Tests with single "OR"

        //Tests on local list.
        assertEquals(5, qh.getMatchingWebsites("denmark OR germany").size());                      //Tests with single "OR"
        assertEquals(1, qh.getMatchingWebsites("denmark germany").size());                         //Tests multiple words
        assertEquals(1, qh.getMatchingWebsites("president america state").size());                 //Tests for lowercase letters
        assertEquals(0, qh.getMatchingWebsites("President America State").size());                 //Tests for uppercase letters
        assertEquals(0, qh.getMatchingWebsites("queen denmark").size());                           //Tests multiple words
        assertEquals(1, qh.getMatchingWebsites("queen denmark OR king denmark").size());           //Test multiple words with single "OR"
        assertEquals(3, qh.getMatchingWebsites("queen denmark OR king denmark OR queen").size());  //Tests multiple words and "OR" statments
        assertEquals(0, qh.getMatchingWebsites("Germany alps").size());                            //Test multiple words
        assertEquals(2, qh.getMatchingWebsites("Germany OR alps").size());                         //Tests with single "OR"
    }

        /**
         * Corner case tests.
         *  - Empty strings, lots of spaces, lots and "OR" statements and spaces.
         * Test both on local created list and tiny-database
         */

    @Test
    void testCornerCases() {

        //Tests on tiny-database
        assertEquals(4, qhDB.getMatchingWebsites("     denmark OR   alps").size());                       //Tests multiple spaces and "OR"
        assertEquals(0, qhDB.getMatchingWebsites("            ").size());                                 //Tests multiple spaces and "OR"
        assertEquals(4, qhDB.getMatchingWebsites("     denmark  OR OR OR OR Denmark    ").size());        //Tests multiple spaces and "OR"
        assertEquals(0, qhDB.getMatchingWebsites("").size());                                             //Tests multiple spaces

        //Tests on local list
        assertEquals(1, qh.getMatchingWebsites("你").size());                                                     //Tests for characters not in UTF-8
        assertEquals(2, qh.getMatchingWebsites("     Germany OR   alps").size());                                 //Tests multiple spaces and "OR"
        assertEquals(3, qh.getMatchingWebsites("word1 OR word2 OR word3 OR word4 OR word5").size());              //Tests single spaces and  multiple "OR's"
        assertEquals(3, qh.getMatchingWebsites("  word1   OR  word2 OR    word3 OR  word4   OR  word5").size());  //Tests multiple spaces and multiple "OR's"
        assertEquals(1, qh.getMatchingWebsites("denmark germany     ").size());                                   //Tests multiple spaces with two words
        assertEquals(1, qh.getMatchingWebsites("Germany           ").size());                                     //Tests multiple spaces
        assertEquals(1, qh.getMatchingWebsites("                    ").size());                                   //Tests String only of spaces
        assertEquals(1, qh.getMatchingWebsites("").size());                                                       //Tests empty String
        assertEquals(2, qh.getMatchingWebsites("Germany OR OR  alps").size());                                    //Tests for multiple "OR's"
    }
}