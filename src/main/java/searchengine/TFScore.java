package searchengine;


import java.util.List;

    /**
     * TFScore contains one method to calculate the Term Frequency.
     */

public class TFScore implements Score {

    /**
     * getScore counts the number of times a single word
     * in the query string occurs on a single website matching the word;
     * also called the Term Frequency (TF). The method returns a double as the count
     * to use, when calculating the TFIDFScore.
     *
     * @param string  The string is a single word from the query.
     * @param website The website is a single website object.
     * @param index   The index is the index data structure.
     * @return the Term Frequency.
     */

    public double getScore(String string, Website website, Index index) { //call this method from the getMatchingWebsites in QueryHandler

        List<String> wordList = website.getWords(); //call getWords to create word list
        double scoreResult = 0;

        for (String word : wordList) { //for each word in the list
            if (word.equals(string)) { //if word equals the string from the parameters
                scoreResult++;
            }
        }
        return scoreResult;
    }
}