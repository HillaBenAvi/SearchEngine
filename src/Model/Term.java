package Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Term {
    private String term;
    private int df;
    private int appears;
    private int locationInPosting;

    public Term(String term) {
        this.term = term;
        df = 0;
        appears = 0;
        locationInPosting = -1;
    }

    public void setAppears(int appears) {
        this.appears = appears;
    }

    public void addAppears(int add) {
        this.appears = this.appears + add;
    }

    public int getAppears() {
        return appears;
    }

    public void addAppearence(int tf) {
        df++;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }

    public void addDf (int add){
        df = df + add;
    }

    public void setLocationInPosting (int location){
        locationInPosting = location;
    }

    public int getLocationInPosting (){
        return locationInPosting;
    }
}