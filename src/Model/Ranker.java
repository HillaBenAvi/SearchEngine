package Model;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.Hashtable;

public class Ranker {
    private ArrayList<String> rankedDocuments; // new-Daniel

    public Ranker (){ //new-Daniel
        rankedDocuments = new ArrayList<>();
    }

    //this function gets all the terms of the query and the relevant docs for each term,
    // ranks the docs and returns the 50 most relevant docs.
    // new-Daniel
    public void rank(Hashtable<String, Hashtable<String, Integer>> termsWithTheirDocs) {


    }

    /**
     *
     * @param doc
     * @param queryTerms - hashtable with terms and number of appearences in the doc
     * @return the score of the document to the specific query
     **/
    private double BM25 (DocumentData doc, Hashtable<String,Integer> queryTerms){


        return 0;
    }


    public ArrayList<String> getRankedDocuments() {
        return rankedDocuments;
    }
}
