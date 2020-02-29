package searchengine;

import java.util.ArrayList;
import java.util.List;

    /**
     * The TFIDFScore class implements the Score Interface. A TF-IDF Score will be calculated based on the
     * logarithm of the size of the database divided by the number of websites that the search word
     * occurs in and multiplied by the TF Score.
     */

public class TFIDFScore implements Score {

    /**
     * The getScore method calculates a score for a website and the word in the search query.
     * This implementation lowers the value of common words.
     * First d and n are defined. The logarithm of d divided by n, defined as logResult, is then calculated.
     * Then, logResult is multiplied with the tfScore, which is called from the class TFScore.
     *
     * @param string - this is the word searched for
     * @param website - this is the website object found when searched
     * @param index - this is the index used in the search
     * @return - returns the calculated tf-idf score as a double
     */

    @Override
    public double getScore(String string, Website website, Index index) {

        //idf = log(d/n), d = size of the database of websites, n = no. of websites the search word occurs on
        //TFIDFScore = TF * IDF

        double d; //find d
        d = index.size(); // sizeOfList is called to retrieve the size of all websites in the database.

        double n; //find n
        List<Website> allWebsitesContainingWord = new ArrayList<>();
        allWebsitesContainingWord.addAll((index.lookup(string))); //a new ArrayList is created to retrieve the all websites the search word occurs on.
        n = allWebsitesContainingWord.size(); // size() is called on a new ArrayList, in order to set the number of all websites as a double.

        if (n == 0) { //if n equals 0, the program will crash
            return 0;
        } else {
            double logResult = Math.log(d / n);

            TFScore tf = new TFScore();
            double tfScore = tf.getScore(string, website, index); //tfScore is initiated in which we call the getScore method from the TFScore class.
            double tfidfScore = logResult * tfScore;

            return tfidfScore;
        }
    }
}
