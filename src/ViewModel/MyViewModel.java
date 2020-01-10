package ViewModel;

import Model.ProcessManager;
import Model.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class MyViewModel {

    private ProcessManager manager;

    public MyViewModel(String corpusPath, String indexesPath, boolean stem){
        manager = new ProcessManager(corpusPath, indexesPath, stem);
    }

    public void startIndexing () throws IOException, ClassNotFoundException {
        manager.manage();
    }


    public Hashtable<String, Term> getDictionary (){
        return manager.getDictionary();
    }

    public ArrayList<String> getDictionarySortedKeys (){
        return manager.getDictionarySortedList();
    }

    public void loadDictionary () throws IOException {
        manager.loadDictionaryFromFile();
    }

}
