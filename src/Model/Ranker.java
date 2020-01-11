package Model;

import javafx.util.Pair;

import java.util.*;

public class Ranker {
    private ArrayList<String> rankedDocuments; // new-Daniel

    public Ranker (){ //new-Daniel
        rankedDocuments = new ArrayList<>();
    }

    //this function gets all the terms of the query and the relevant docs for each term,
    // ranks the docs and returns the 50 most relevant docs.
    public  List <Pair<String,Double>> rank(Hashtable<DocumentData, Hashtable<Term, Integer>> docsWithTheirTerms, int numOfDocs, double avgDocsLength) {
        LinkedList <Pair<String,Double>> rankedDocs = new LinkedList<>();
        for ( DocumentData docData: docsWithTheirTerms.keySet()){
            double bm25 = BM25(docData, docsWithTheirTerms.get(docData), numOfDocs, avgDocsLength);
            Double score = bm25 ;
            rankedDocs.add(new Pair(docData.getDocID(), score));
        }
        return getTopRanked(rankedDocs);
    }

    private List<Pair<String, Double>> getTopRanked(LinkedList <Pair<String,Double>> rankedDocs) {
        rankedDocs.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int returnListSize = Math.min(50, rankedDocs.size());
        return rankedDocs.subList(0, returnListSize);
    }

    /**
     * @param doc
     * @param termsInDoc - hashtable with terms and number of appearences in the doc
     * @return the score of the document to the specific query
     **/
    private double BM25 (DocumentData doc, Hashtable <Term , Integer> termsInDoc, int numOfDocs, double avgDocdLength){

        ArrayList <Term> termsInDocList = new ArrayList<>();
        termsInDocList.addAll(termsInDoc.keySet());
        double score =0;
        double b =0.75, k1= 1.6;

        for (Term term: termsInDocList ){
            double idf = term.calculateIDF(numOfDocs);
            double termScore = idf * ( ((termsInDoc.get(term)*(k1+1))) / (termsInDoc.get(term) + k1 * (1 - b + ( b * (doc.getLength()/ avgDocdLength) ) ) ));
            score = score + termScore;
        }

        return score;
    }



}
