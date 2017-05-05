package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import java.util.ArrayList;

/**
 * Created by nagaprasad on 5/5/17.
 */
public class CyclicArrayList {
    final private ArrayList<String> arrayList;
    final private int size;

    CyclicArrayList (int size) {
        arrayList = new ArrayList<>(size);
        this.size = size;
    }

    public boolean add(String string) {
        if (arrayList.size() >= size) {
            arrayList.remove(0);
        }
        return arrayList.add(string);
    }

    public ArrayList<String> getRef() {
        return arrayList;
    }
}
