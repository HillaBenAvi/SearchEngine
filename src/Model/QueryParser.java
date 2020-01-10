package Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class QueryParser extends AParser {
    private Hashtable<String, Integer> queryTerms;
    private Query query;
    private HashSet<String> stopWords;

    public QueryParser (Query _query, HashSet<String> _stopWords)
    {
        stopWords = _stopWords;
        query = _query;
    }

    @Override
    public void parse() {
        queryTerms = parseText(query.getTitle(), stopWords);
    }

    public Hashtable<String, Integer> getQueryTerms() {
        return queryTerms;
    }
}
