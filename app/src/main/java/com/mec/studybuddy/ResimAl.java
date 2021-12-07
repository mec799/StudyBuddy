package com.mec.studybuddy;

public class ResimAl {
    private String gonderiResmi ;
    private String gonderen ;
    private String gonderiId ;

    public ResimAl() {
    }

    public ResimAl(String gonderiResmi, String gonderen, String gonderiId) {
        this.gonderiResmi = gonderiResmi;
        this.gonderen = gonderen;
        this.gonderiId = gonderiId;
    }

    public String getGonderiResmi() {
        return gonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        this.gonderiResmi = gonderiResmi;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getGonderiId() {
        return gonderiId;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiId = gonderiId;
    }
}
