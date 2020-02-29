package searchengine;

import java.util.*;
import java.util.stream.Collectors;

    /**
     * This class is responsible for answering queries to our search engine.
     */

public class QueryHandler {

    /**
     * The index the QueryHandler uses for answering queries.
     */

    private Index idx = null;

    /**
     * The constructor
     *
     * @param idx The index used by the QueryHandler.
     */

    public QueryHandler(Index idx) {
        this.idx = idx;
    }

    /**
     * getMachingWebsites answers queries of the type
     * "subquery1 OR subquery2 OR subquery3 ...". A "subquery"
     * has the form "word1 word2 word3 ...". A website matches
     * a subquery if all the words occur on the website. A website
     * matches the whole query, if it matches at least one subquery.
     * The TFScore and the TFIDFScore is used to calculate a score for
     * each website in order to rank the websites in the search result getting the most relevant first.
     * To return a ranked list the method invoke the sorter method converting the resultsScoreMap to a ranked list of websites.
     *
     * @param line the line is the query string
     * @return the list of websites that matches the query in a ranked order
     */

    public List<Website> getMatchingWebsites(String line) {
        Set<Website> resultsIntermediate = new HashSet<>(); //using a set to store websites that match the query with no duplicates
        Map<Website, Double> scoreMap = new HashMap<>(); //using a HashMap to store websites that matches a subquery paired with the value of their (TF/TFIDF)Score (in Double)
        Score scoreMechanism = new TFIDFScore(); //choose between TFScore and TFIDFScore
        Map<Website, Double> resultsScoreMap = new HashMap<>();//using a HasMap to store all websites matching the query paired with the value of their final Scores (in Double)
        line = line.trim().replaceAll("\\s+", " ");
        String[] splittedQuery = line.split("\\s" + "(OR )+");

        for (String queryString : splittedQuery) {
            scoreMap.clear();
            resultsIntermediate.clear();
            String[] splittedQuery2 = queryString.split("\\s+");

            resultsIntermediate.addAll(idx.lookup(splittedQuery2[0]));
            for (int i = 1; i < splittedQuery2.length; i++) {
                resultsIntermediate.retainAll(idx.lookup(splittedQuery2[i]));
            }

            for (Website w : resultsIntermediate) {
                double tempScore;
                double scoreResult = 0;
                for (String searchWord : splittedQuery2) {
                    double score = scoreMechanism.getScore(searchWord, w, idx);
                    scoreResult = scoreResult + score;
                }

                if (resultsScoreMap.containsKey(w)) { //if a website in the two scoremaps is the same, then choose the greatest mapped value for that website
                    tempScore = resultsScoreMap.get(w);
                    if (tempScore < scoreResult) {
                        scoreMap.put(w, scoreResult);
                    } else {
                        scoreMap.put(w, tempScore);
                    }
                } else {
                    scoreMap.put(w, scoreResult);
                }
            }

            resultsScoreMap.putAll(scoreMap);
        }

        List<Website> resultsSorted = sorter(resultsScoreMap);

        return resultsSorted;
    }

    /**
     * the sorter method is converting the resultsScoreMap to a sorted list
     * of the matching websites such that the website with the highest score
     * is recieved as the first one in the searchresult.
     *
     * @param resultsScoreMap the map with the matching websites and their final scores
     * @return a sorted list of websites according to scores
     */

    public List<Website> sorter(Map<Website, Double> resultsScoreMap) {
        return resultsScoreMap.entrySet().stream()
                .sorted((x, y) -> y.getValue().compareTo(x.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}