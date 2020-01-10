package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class ReadFile {

    private String path;
    private String stopWordsFilePath;
    private String corpusPath;
    private String [] corpusFilesPaths;
    private int lastFileRead;


    public ReadFile(String pathToDirectory) {
        path = pathToDirectory;
        lastFileRead = 0;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()){
                this.stopWordsFilePath = file.getAbsolutePath();
            }
            if (file.isDirectory()){
                this.corpusPath = file.getAbsolutePath();
            }
        }
        File[] listOfCorpusDirectories = new File(corpusPath).listFiles();
        corpusFilesPaths = new String [listOfCorpusDirectories.length];
        for (int i=0; i<listOfCorpusDirectories.length; i++){
            corpusFilesPaths[i] = listOfCorpusDirectories[i].getAbsolutePath();
        }
        for (int i=0; i<listOfCorpusDirectories.length; i++){
            File [] file = new File (corpusFilesPaths[i]).listFiles();
            corpusFilesPaths[i] = file[0].getAbsolutePath();
        }

    }

    public int filesNum (){
        return corpusFilesPaths.length;
    }

    public boolean hasNextFile (){
        if (lastFileRead >= corpusFilesPaths.length){
            return false;
        }
        return true;
    }


    public ArrayList <Document> readNext() throws IOException {
        String fileText = fileToString (corpusFilesPaths[lastFileRead]);
        ArrayList <Document> docsInFile = getFileDocs(fileText);
        lastFileRead ++;

        return docsInFile;
    }

    public HashSet<String> getStopWords() throws IOException {
        HashSet<String> stopWords = new HashSet<>();
        File file = new File(stopWordsFilePath);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = null;

        while ( (line = br.readLine()) != null ){
            stopWords.add(line);
        }

        return stopWords;
    }

    public static String fileToString (String filePath) throws IOException {
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

    private ArrayList<Document> getFileDocs (String fileText) {
        ArrayList<Document> fileDocs = new ArrayList<>();
        String [] docsString = fileText.split("<DOC>"); //split the text to the documentsData
        for (int i = 1; i < docsString.length ; i++){
            Document doc = stringToDocument(docsString[i]);
            fileDocs.add(doc);
        }
        return fileDocs;
    }

    private Document stringToDocument (String docString){
        Document doc = new Document();
        String [] openDocNo = docString.split("<DOCNO>"); // remove <DOCNO> tag
        String [] closeDocNo = openDocNo[1].split("</DOCNO>");// remove </DOCNO> tag
        doc.setDocNo(closeDocNo[0]); //between <DOC> and </DOCNO>
        //date?
        String [] openTitle = docString.split("<TI>"); // remove <TI> tag
        if (openTitle.length>1){
            String [] closeTitle = openTitle[1].split("</TI>");// remove </TI> tag
            doc.setTitle(closeTitle[0]); // between <TI> ant </TI>
        }
        String [] openText = docString.split("<TEXT>"); // remove <TEXT> tag
        if (openText.length>1){
            String [] closeText = openText[1].split("</TEXT>");// remove </TEXT> tag
            doc.setText(closeText[0]); // between <TEXT> ant </TEXT>

        }

        return doc;
    }


}
