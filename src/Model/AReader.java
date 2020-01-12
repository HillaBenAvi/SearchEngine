package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public abstract class AReader {

    protected String stopWordsFilePath;

    public HashSet<String> getStopWords(){
        HashSet<String> stopWords = new HashSet<>();
        File file = new File(stopWordsFilePath);
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = null;

            while ( (line = br.readLine()) != null ){
                stopWords.add(line);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return stopWords;
    }

    public String fileToString (String filePath) throws IOException {
        StringBuilder fileText = new StringBuilder(" ");
        File file = new File(filePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null){
            fileText.append(line + " ");
            line = br.readLine();
        }

        return fileText.toString();
    }
}
