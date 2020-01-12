package Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class QueryParser extends AParser {
    private Hashtable<String, Integer> queryTitleTerms;
    private Hashtable<String, Integer> queryDescriptionTerms;
    private Query query;
    private HashSet<String> stopWords;

    public QueryParser (Query _query, HashSet<String> _stopWords)
    {
        stopWords = _stopWords;
        query = _query;
    }

    @Override
    public void parse() {
        queryTitleTerms = parseText(query.getTitle().toLowerCase(), stopWords);
        queryDescriptionTerms = parseText(query.getDescription().toLowerCase(), stopWords);
    }

    //return pair of two lists- the first is the terms in the title and the second is the terms in the description
    public Pair<ArrayList<String>,ArrayList<String>> getQueryTerms() {
        ArrayList<String> titleTermsList = new ArrayList<>();
        titleTermsList.addAll(queryTitleTerms.keySet());
        ArrayList<String> descriptionTermsList = new ArrayList<>();
        descriptionTermsList.addAll(queryDescriptionTerms.keySet());
        return new Pair<>(titleTermsList, descriptionTermsList);
    }
}
