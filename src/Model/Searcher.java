package Model;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import com.medallia.word2vec.Word2VecModel;
import javafx.util.Pair;



public class Searcher {

    private Ranker ranker;
    private String postingFilesPath;
    private Hashtable<String, Term> dictionary;
    private Hashtable<String, DocumentData> documents;

    public Searcher(Hashtable<String, Term> dictionary, Hashtable<String,DocumentData> documents, String postingFilesPath) {
        this.dictionary = dictionary;
        this.ranker = new Ranker();
        this.postingFilesPath = postingFilesPath;
        this.documents = documents;
    }


    // gets the query and return an arrayList of the relevant documents (after rank)
    public List <Pair<String,Double>> search (Query query, boolean semanticModel, HashSet<String> stopWords, boolean stem){
        QueryParser queryParser = new QueryParser(query, stopWords);
        queryParser.parse();
        Pair<ArrayList<String>, ArrayList<String>> queryStrings = queryParser.getQueryTerms();

        if (semanticModel){
            ArrayList<String> expandedTitle = expandQuery(queryStrings.getKey());
            ArrayList<String> expandedDescription= expandQuery(queryStrings.getValue());
            queryStrings = new Pair (expandedTitle, expandedDescription);
        }

        // get all the relevant docs ordered by the terms
        Hashtable<String,Hashtable<String, Pair<Integer, Boolean >>> allRelevantDocsOrderedByTerms = getRelevantDocuments(queryStrings.getKey(), queryStrings.getValue(), stem);

        //order all the relevant docs by documents
        Hashtable<DocumentData, Hashtable<Term, Pair<Integer, Boolean>>> allRelevantDocsOrderedByDoc = termsListToDocsList(allRelevantDocsOrderedByTerms);

        double avgDocLength = calculateDocsAvqLength();

        //send to the ranker for rank the docs, get the 50 most relevant.
        List <Pair<String,Double>> relevantRankedDocs = ranker.rank(allRelevantDocsOrderedByDoc, documents.size(), avgDocLength);

        return relevantRankedDocs;
    }



    private double calculateDocsAvqLength() {
        int totalLength = 0;
        for (String doc: documents.keySet()) {
            DocumentData docData = documents.get(doc);
            totalLength = totalLength + docData.getLength();
        }
        return totalLength/documents.size();
    }

    //find the 5 most common entities of a document
    public ArrayList<String> entitiesRecognize (String docId){

        return null;
    }

    //I took this function from the Ranker class
    /**
     * get the list of terms in the query
     * @param
     * @return list
     */
    private  Hashtable<String,Hashtable<String, Pair<Integer, Boolean >>> getRelevantDocuments ( ArrayList<String> queryTitleTerms,  ArrayList<String> queryDescriptionTerms, boolean stem ){
        //listOfTerms- key: a term in the query,
        //             value: all the documents the term appears in, and number of appears in each one.

        Hashtable<String,Hashtable<String, Pair<Integer, Boolean >>> listOfTerms = new Hashtable<>();
        for(String term : queryTitleTerms){
            if (dictionary.containsKey(term)){
                Hashtable<String, Pair< Integer, Boolean >> termsWithNumOfAppearencesInEveryDoc = findRelevantDocsForTerm(term, true, stem);
                listOfTerms.put(term, termsWithNumOfAppearencesInEveryDoc);
            }

        }

        for(String term : queryDescriptionTerms){
            if (dictionary.containsKey(term)){
                Hashtable<String, Pair< Integer, Boolean >> termsWithNumOfAppearencesInEveryDoc = findRelevantDocsForTerm(term, false, stem);
                listOfTerms.put(term, termsWithNumOfAppearencesInEveryDoc);
            }

        }

        return listOfTerms;
    }



    //check how can we get a sorted list of the same posting and find all the relevant docs of each term
    /**
     * @param term from the query
     * @return all the documents the term appears in.
     */
    private Hashtable<String, Pair<Integer, Boolean >> findRelevantDocsForTerm (String term, boolean isInTitle, boolean stem){
        Hashtable<String, Pair<Integer, Boolean >> documents = new Hashtable();

        try{
            //read the relevant line from the posting file
            Stream<String> lines = Files.lines(Paths.get(findPostingPathForTerm(term, stem)));
            String line = lines.skip(dictionary.get(term).getLocationInPosting()).findFirst().get();

            //parsing the line
            String [] separatedLine = line.split(";");
            for(int i=1; i<separatedLine.length; i++){
                String [] doc = separatedLine[i].split(",");
                String docNo = doc[0].substring(1);
                String appears = doc[1].substring(0,doc[1].length()-1);
                documents.put(docNo, new Pair (Integer.parseInt(appears), isInTitle));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return documents;

    }

    private String findPostingPathForTerm (String term, boolean stem){
        String fileSeparator = System.getProperty("file.separator");
        String filePath = postingFilesPath + fileSeparator;
        if (!stem) {
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
            else if ((term.charAt(0)>='t' && term.charAt(0) <= 'z') || (term.charAt(0) >= 'T' && term.charAt(0) <= 'Z')) {
                return filePath + "TUVWXYZ.txt";
            }
        }
        else{
            if (term.charAt(0)>='0' && term.charAt(0) <= '9'){
                return filePath + "SNUMBERS.txt";
            }
            else if (term.charAt(0)=='a' || term.charAt(0) == 'b' || term.charAt(0) == 'A' || term.charAt(0) == 'B'){
                return filePath + "SAB.txt";
            }
            else if (term.charAt(0)=='c' || term.charAt(0) == 'd' || term.charAt(0) == 'C' || term.charAt(0) == 'D'){
                return filePath + "SCD.txt";
            }
            else if ((term.charAt(0)>='e' && term.charAt(0) <= 'h') || (term.charAt(0) >= 'E' && term.charAt(0) <= 'H')){
                return filePath + "SEFGH.txt";
            }
            else if ((term.charAt(0)>='i' && term.charAt(0) <= 'l') || (term.charAt(0) >= 'I' && term.charAt(0) <= 'L')){
                return filePath + "SIJKL.txt";
            }
            else if ((term.charAt(0)>='m' && term.charAt(0) <= 'o') || (term.charAt(0) >= 'M' && term.charAt(0) <= 'O')){
                return filePath + "SMNO.txt";
            }
            else if (term.charAt(0)=='p' || term.charAt(0) == 'q' || term.charAt(0) == 'P' || term.charAt(0) == 'Q'){
                return filePath + "SPQ.txt";
            }
            else if (term.charAt(0)=='r' || term.charAt(0) == 's' || term.charAt(0) == 'R' || term.charAt(0) == 'S'){
                return filePath + "SRS.txt";
            }
            else if ((term.charAt(0)>='t' && term.charAt(0) <= 'z') || (term.charAt(0) >= 'T' && term.charAt(0) <= 'Z')) {
                return filePath + "STUVWXYZ.txt";
            }
        }
        return null;
    }


    private Hashtable<DocumentData, Hashtable<Term, Pair<Integer, Boolean >>> termsListToDocsList
            ( Hashtable<String,Hashtable<String,Pair<Integer, Boolean >>> terms){
        // terms is the output hashtable of getRelevantDocuments
        //hashTable of all the relevant docs. each doc has the terms from the query that appears in the doc.
        Hashtable<DocumentData, Hashtable<Term, Pair<Integer, Boolean >>> docsList = new Hashtable<>();

        for (String term : terms.keySet()){
            for (String docNo: terms.get(term).keySet()){ //all the documents the term appears in
                DocumentData docData = documents.get(docNo);
                Pair appearsAndIsInTitle = terms.get(term).get(docNo);
                if (!docsList.containsKey(docData)) {
                    docsList.put(docData, new Hashtable<>());
                }
                if (dictionary.containsKey(term.toUpperCase())){
                    docsList.get(docData).put(dictionary.get(term.toUpperCase()), appearsAndIsInTitle);
                }
                else if (dictionary.containsKey(term.toLowerCase())){
                    docsList.get(docData).put(dictionary.get(term.toLowerCase()), appearsAndIsInTitle);
                }
                //if the dictionary doesn't contain the term, it will not be in the hashtable.
            }
        }
        return docsList;
    }


    //function that implements the semantic model.
    //receive a list that contains the words in the query and return a list of the expanded query
    private ArrayList<String> expandQuery (ArrayList<String> query){
        ArrayList<String> toReturn=new ArrayList<>();
        toReturn.addAll(query);
        try {
            for (int i = 0; i < query.size(); i++) {
                Word2VecModel model = Word2VecModel.fromTextFile(new File( "d:\\documents\\users\\hillabe\\Downloads\\word2vec.c.output.model.txt"));
                com.medallia.word2vec.Searcher searcher = model.forSearch();
                int num = 3;
                List<com.medallia.word2vec.Searcher.Match> matches = searcher.getMatches(query.get(i), num);
                for (com.medallia.word2vec.Searcher.Match match : matches) {
                    match.match();
                }
                for (int j = 1; j < matches.size(); j++) {
                    String temp=matches.get(j).toString();
                    String[] arr=temp.split("\\[");
                    arr[1]=arr[1].substring(0,arr[1].length()-1);
                    // if(Double.parseDouble(arr[1])>0.95)
                    toReturn.add(arr[0]);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;

    }

    private Hashtable<String,Term> stringsToTerms (ArrayList<String> stringsList){
        Hashtable<String, Term> termsList = new Hashtable<>();

        for(String str : stringsList ){
            if (dictionary.containsKey(str.toLowerCase())){
                termsList.put(str.toLowerCase(), dictionary.get(str.toLowerCase()));
            }
            else if (dictionary.containsKey(str.toUpperCase())){
                termsList.put(str.toUpperCase(), dictionary.get(str.toUpperCase()));
            }
            //if the word doesn't appear in the dictionary- it will not be calculated.
        }

        return termsList;
    }


}
