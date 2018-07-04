package com.example.tony.consoleapplication;

import java.util.ArrayList;
import java.util.List;

public class ParentStore {
    private String mParentStore;
    private List<ListItem> mEnglishStore;

    public ParentStore(String storename, List<ListItem> englishname) {
        mParentStore = storename;
        mEnglishStore = englishname;
    }
    public void setParentStore(String storename) {
        mParentStore = storename;
    }
    public void setEnglishStore(List<ListItem> englishname) {
        mEnglishStore = englishname;
    }

    public String getParentStore() {
        return this.mParentStore;
    }

    public List<ListItem> getEnglishStore() {
        return this.mEnglishStore;
    }
}

