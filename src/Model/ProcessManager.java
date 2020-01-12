package Model;

import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class ProcessManager {

    private ReadFile readFile;
    private MergeDictionaries mergeDictionaries;
    private Stemmer stemmer;
    private Indexer indexer;
    private Hashtable<String, Term> dictionary;
    private boolean toStem;

    public HashSet<DocumentData> documents;

    static int BATCH_SIZE=50000;

    public ProcessManager(String sourcePath, String postingPath, boolean stem){
        readFile = new ReadFile(sourcePath);
        mergeDictionaries = new MergeDictionaries();
        stemmer = new Stemmer();
        indexer = new Indexer(postingPath, stem);
        documents = new HashSet<>();
        toStem = stem;
    }

    public void manage (boolean stem) throws IOException, ClassNotFoundException {
        toStem = stem;
        ArrayList <Document> docsToParse = new ArrayList<>();
        ArrayList<Pair< String, Hashtable<String, Integer>>> docsAfterParsing;
        HashSet<String> stopWords = readFile.getStopWords();

        while (readFile.hasNextFile()){
            if (docsToParse.size() >= BATCH_SIZE ){
                AParser docsParser = new DocsParser(docsToParse, stopWords);
                docsParser.parse();
                docsAfterParsing = ((DocsParser) docsParser).getListParsedDoc();
                addDocuments(docsAfterParsing);
                Hashtable<String, LinkedList<Pair<String, Integer>>> dicAfterStemOrMerge;
                if(toStem){
                    dicAfterStemOrMerge = stemmer.stemDictionary(mergeDictionaries.getMergedDictionary());
                }
                else{
                    dicAfterStemOrMerge = mergeDictionaries.getMergedDictionary();
                }
                indexer.indexing(dicAfterStemOrMerge, toStem);
                docsToParse = new ArrayList<>();
            }
            else {
                docsToParse.addAll(readFile.readNext());
            }
        }
        //last batch
        AParser docsParser = new DocsParser(docsToParse, stopWords);
        docsParser.parse();
        docsAfterParsing = ((DocsParser) docsParser).getListParsedDoc();
        addDocuments(docsAfterParsing);
        Hashtable<String, LinkedList<Pair<String, Integer>>> dicAfterStemOrMerge;
        if(toStem){
            dicAfterStemOrMerge = stemmer.stemDictionary(mergeDictionaries.getMergedDictionary());
        }
        else{
            dicAfterStemOrMerge = mergeDictionaries.getMergedDictionary();
        }

        indexer.indexing(dicAfterStemOrMerge, toStem);
        indexer.tempFilesToPostingFiles();
        indexer.createDocumentsAndDictionaryFiles(documents);
        indexer.createStopWordsFile(stopWords);
        loadDictionaryFromFile ();
    }



    private void addDocuments (ArrayList<Pair< String, Hashtable<String, Integer>>> docsToAdd){
        mergeDictionaries = new MergeDictionaries();
        for ( Pair<String, Hashtable<String, Integer>> pair: docsToAdd) {
            mergeDictionaries.merge(pair.getKey(), pair.getValue());
            DocumentData docData = new DocumentData(pair.getKey());
            Hashtable<String, Integer> termsInDoc = pair.getValue();
            docData.setDocID( pair.getKey());
            docData.setUniqueWords(termsInDoc.size());
            int length = 0;
            int maxCommonWord = 0;
            String commonWord = "";
            for(String term : termsInDoc.keySet()){
                if(termsInDoc.get(term) > maxCommonWord){
                    maxCommonWord = termsInDoc.get(term);
                    commonWord = term;
                }
                length = length + termsInDoc.get(term);
            }
            docData.setLength(length);
            docData.setMostCommonWord(maxCommonWord);
            docData.setCommonWordName(commonWord);
            documents.add(docData);
        }
    }

    public Hashtable<String, Term> getDictionary (){
        this.dictionary = indexer.getDictionary();
        return indexer.getDictionary();
    }

    public Hashtable<String, DocumentData> getDocuments (){
        return indexer.getDocuments();
    }



    public ArrayList <String> getDictionarySortedList (){

        return indexer.getSortedKeys(dictionary.keySet());
    }

    public void loadDictionaryFromFile () throws IOException {
        indexer.loadDictionaryFromFile();
        indexer.loadDocumentsFromFile();
    }


}
