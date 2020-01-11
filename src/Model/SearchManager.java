package Model;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class SearchManager {

    private Hashtable<String, Term> dictionary;
    private Hashtable<String, DocumentData> documents;
    private String postingPath;
    private Searcher searcher;

    public SearchManager(String postingPath){
        this.postingPath = postingPath;
        loadDictionary();
        loadDocuments();
        this.searcher = new Searcher(dictionary, documents, postingPath);
    }

    private void loadDictionary (){
        dictionary =new Hashtable<>();
        try{
            String fileSeparator = System.getProperty("file.separator");
            String filePath =  postingPath + fileSeparator + "Dictionary.txt";
            FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null){
                String [] splittedLine = line.split("=");
                Term term = new Term (splittedLine[0]);
                term.setDf(Integer.parseInt(splittedLine[1]));
                term.setAppears(Integer.parseInt(splittedLine[2]));
                term.setLocationInPosting(Integer.parseInt(splittedLine[3]));
                dictionary.put(splittedLine[0], term);
                line = br.readLine();
            }
        }
        catch(Exception e){
            System.out.println("loadDictionary- search manager");
            e.printStackTrace();
        }

    }

    private void loadDocuments () {
        documents =new Hashtable<>();
        try{
            String fileSeparator = System.getProperty("file.separator");
            String filePath =  postingPath + fileSeparator + "Documents.txt";
            FileReader fr = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null){
                String [] splitLine = line.split(";");
                DocumentData docData = new DocumentData (splitLine[0]);
                docData.setLength(Integer.parseInt(splitLine[1]));
                docData.setCommonWordName(splitLine[2]);
                docData.setMostCommonWord(Integer.parseInt(splitLine[3]));
                documents.put(docData.getDocID(), docData);
                line = br.readLine();
            }
        }
        catch(Exception e){
            System.out.println("exception in loadDocuments- search manager");
            e.printStackTrace();
        }

    }

    //the function will receive the query/path to query file and call the searcher to find the relevant documents by
    //calling the searcher.
    public Hashtable<String, List <Pair<String,Double>>> search (String query, boolean semanticModel) throws IOException {

        String fileSeparator = System.getProperty("file.separator");
        String stopWordsPath =  postingPath + fileSeparator + "StopWords.txt";

        ArrayList<Query> queries = new ArrayList<>();
        ReadQuery reader = new ReadQuery();
        reader.setStopWordsPath(stopWordsPath);
        HashSet<String> stopWords = reader.getStopWords();
        if (query.contains("/")){ // query is a path to file

            queries = reader.readQueriesFromFile(query);
        }
        else {
            Query queryToAdd = new Query ("0" , query, "", "");
            queries.add(queryToAdd);
        }
        Hashtable<String, List <Pair<String,Double>>> results = new Hashtable<>();
        for (Query q : queries){
            List <Pair<String,Double>> relevantDocuments = searcher.search(q , semanticModel, stopWords);
            results.put(q.getQueryId(), relevantDocuments);
        }

        return results;

    }

}


















