package Model;

import java.io.File;
import java.util.ArrayList;

public class ReadQuery {

    private String path;

    public ReadQuery (String path){
        this.path= path;
    }

    public ArrayList<Query> readQueriesFromFile (String path){
        ArrayList<Query> queries = new ArrayList<>();
        String text = "";
        try{
            text = ReadFile.fileToString(path);
            queries = getQueriesFromText(text);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return queries;
    }

    private ArrayList<Query> getQueriesFromText(String text) {
        ArrayList<Query> queries = new ArrayList<>();

        String [] queriesTexts = text.split("<top>");
        for(int i=1; i<queriesTexts.length; i++){
            String [] removeTop = queriesTexts[i].split("</top>"); //remove </top>
            String [] removeNarr = removeTop[0].split("<narr>");
            String narr = removeNarr[1]; //after the narr tag
            String [] removeDescription = removeNarr[0].split("<desc>");
            String description = removeDescription [1];
            String [] removeTitle = removeDescription[0].split("<title>");
            String title = removeTitle[1];
            String [] removeNum = removeTitle[0].split("Number:");
            String num = removeNum[1];
            while (num.charAt(0) == ' '){
                num = num.substring(1);
            }

            Query query = new Query(num, title, description, narr);
            queries.add(query);

        }
        return queries;
    }


}
