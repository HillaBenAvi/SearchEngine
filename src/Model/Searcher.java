package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.stream.Stream;

public class Searcher {

    private Ranker ranker;
    private String postingFilesPath;
    private Hashtable<String, Term> dictionary;
    HashSet<DocumentData> documents;

    public Searcher(Hashtable<String, Term> dictionary, HashSet<DocumentData> documents, String postingFilesPath) {
        this.dictionary = dictionary;
        this.ranker = new Ranker();
        this.postingFilesPath = postingFilesPath;
        this.documents = documents;
    }


    // gets the query and return an arrayList of the relevant documents (after rank)
    public ArrayList<String> search (Query query){
        QueryParser queryParser = new QueryParser(query, null);
        queryParser.parse();
        Hashtable<String, Integer> queryTerms = queryParser.getQueryTerms();

        // open a set with the query terms
        // add the semantic model here

        //Daniel
        Hashtable<String,Hashtable<String,Integer>> hashtableSendToRanker = new Hashtable<>();
        hashtableSendToRanker = getRelevantDocuments(queryTerms.keySet()); //get the relevant docs of all the terms in the query
        ranker.rank(hashtableSendToRanker); //send to the ranker for rank the docs
        ArrayList<String> relevantDocs = ranker.getRankedDocuments(); // get the ranked docs.

        //continue...

        return relevantDocs;
    }

    //find the 5 most common entities of a document
    public ArrayList<String> entitiesRecognize (String docId){

        return null;
    }

    //I took this function from the Ranker class
    /**
     * get the list of terms in the query
     * @param queryTerms
     * @return list
     */
    private  Hashtable<String,Hashtable<String,Integer>> getRelevantDocuments ( Set<String> queryTerms ){
        //listOfTerms- key: a term in the query,
        //             value: all the documents the term appears in, and number of appears in each one.

        Hashtable<String,Hashtable<String,Integer>> listOfTerms = new Hashtable<>();
        for(String term : queryTerms){
            Hashtable<String, Integer> termsWithNumOfAppearencesInEveryDoc = findRelevantDocsForTerm(term);
            listOfTerms.put(term, termsWithNumOfAppearencesInEveryDoc);
        }
        // TODO: 1/2/2020 - a function that convert the listOfTerms to the Hashtable of docs (with terms in the doc) ;

        return listOfTerms;
    }

    // TODO: 1/2/2020 add read file for the queries


    //check how can we get a sorted list of the same posting and find all the relevant docs of each term
    /**
     * @param term from the query
     * @return all the documents the term appears in.
     */
    private Hashtable<String, Integer> findRelevantDocsForTerm (String term){
        Hashtable <String, Integer> documents = new Hashtable();
        try{

            //read the relevant line from the posting file
            Stream<String> lines = Files.lines(Paths.get(findPostingPathForTerm(term)));
            String line = lines.skip(dictionary.get(term).getLocationInPosting()).findFirst().get();

            //parsing the line
            String [] separatedLine = line.split(";");
            for(int i=0; i<separatedLine.length; i++){
                System.out.println(separatedLine[i]);
            }
            for(int i=1; i<separatedLine.length; i++){
                String [] doc = separatedLine[i].split(",");
                String docNo = doc[0].substring(1);
                String appears = doc[1].substring(0,doc[1].length()-1);
                documents.put(docNo, Integer.parseInt(appears));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private String findPostingPathForTerm (String term){
        String fileSeparator = System.getProperty("file.separator");
        String filePath = postingFilesPath + fileSeparator;
        if (term.charAt(0)>='0' && term.charAt(0) <= '9'){
            return filePath + "NUMBERS.txt";
        }
        else if (term.charAt(0)=='a' || term.charAt(0) == 'b' || term.charAt(0) == 'A' || term.charAt(0) == 'B'){
            return filePath + "AB.txt";
        }
        else if (term.charAt(0)=='c' || term.charAt(0) == 'd' || term.charAt(0) == 'C' || term.charAt(0) == 'D'){
            return filePath + "CD.txt";
        }
        else if ((term.charAt(0)>='e' && term.charAt(0) <= 'h') || (term.charAt(0) >= 'E' && term.charAt(0) <= 'H')){
            return filePath + "EFGH.txt";
        }
        else if ((term.charAt(0)>='i' && term.charAt(0) <= 'l') || (term.charAt(0) >= 'I' && term.charAt(0) <= 'L')){
            return filePath + "IJKL.txt";
        }
        else if ((term.charAt(0)>='m' && term.charAt(0) <= 'o') || (term.charAt(0) >= 'M' && term.charAt(0) <= 'O')){
            return filePath + "MNO.txt";
        }
        else if (term.charAt(0)=='p' || term.charAt(0) == 'q' || term.charAt(0) == 'P' || term.charAt(0) == 'Q'){
            return filePath + "PQ.txt";
        }
        else if (term.charAt(0)=='r' || term.charAt(0) == 's' || term.charAt(0) == 'R' || term.charAt(0) == 'S'){
            return filePath + "RS.txt";
        }
        else if ((term.charAt(0)>='t' && term.charAt(0) <= 'z') || (term.charAt(0) >= 'T' && term.charAt(0) <= 'Z')){
            return filePath + "TUVWXYZ.txt";
        }
        return null;
    }


    private Hashtable<String, Hashtable<String, Integer>> termsListToDocsList
            ( Hashtable<String,Hashtable<String,Integer>> terms){

        Hashtable<String, Hashtable<String, Integer>> docs = new Hashtable<>();

        for (String term : terms.keySet()){



        }

        return docs;
    }




}
