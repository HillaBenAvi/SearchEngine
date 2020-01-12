package ViewModel;

import Model.ProcessManager;
import Model.Term;
import Model.SearchManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class MyViewModel {

    private ProcessManager processManager;
    private SearchManager searchManager;

    public MyViewModel(String corpusPath, String indexesPath, boolean stem){
        processManager = new ProcessManager(corpusPath, indexesPath, stem);

    }

    public void startIndexing (boolean stem) throws IOException, ClassNotFoundException {
        processManager.manage(stem);
    }


    public Hashtable<String, Term> getDictionary (){
        return processManager.getDictionary();
    }

    public ArrayList<String> getDictionarySortedKeys (){
        return processManager.getDictionarySortedList();
    }

    public void loadDictionary () throws IOException {
        processManager.loadDictionaryFromFile();
    }

    public void search (String query, boolean isFile, boolean semanticModel, String indexesPath, String resultsPath, boolean stem){
        searchManager = new SearchManager(indexesPath, processManager.getDictionary(), processManager.getDocuments());
        searchManager.search(query, isFile, semanticModel, resultsPath, stem);
    }


}
