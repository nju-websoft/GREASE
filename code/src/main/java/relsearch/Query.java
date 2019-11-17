package relsearch;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C), 2019, Anonymous.
 * FileName: Main.java
 *
 * Query class
 * @author  Anonymous
 * @Date  2019
 * @version 1.10
 */


public class Query {
    int queryId;
    int exampleNum;
    ArrayList<ExamplePair> examplePairs;

    Query(int q, int numberExample, ArrayList<Integer> pairs){
        assert pairs.size() == numberExample*2;
        queryId = q;
        exampleNum = numberExample;
        examplePairs = new ArrayList<ExamplePair>();
        for(int i=0; i<numberExample;i++){
            examplePairs.add(new ExamplePair(pairs.get(i*2),pairs.get(i*2+1)));
        }
    }
    Query(int q, int numberExample, List<ExamplePair> pairs){
        assert pairs.size() == numberExample;
        queryId = q;
        exampleNum = numberExample;
        examplePairs = new ArrayList<ExamplePair>(pairs);
    }

    public int getQueryId() {
        return queryId;
    }

    public int getExampleNum() {
        return exampleNum;
    }

    public ArrayList<ExamplePair> getExamplePairs() {
        return examplePairs;
    }

    public void print(){
        System.out.println("queryId:"+"\t"+queryId);
        System.out.println("exampleNum:"+"\t"+exampleNum);
        for(ExamplePair ep: examplePairs){
            ep.print();
        }
    }

    public static void main(String[] args){
        ArrayList<Integer> array = new ArrayList<Integer>();
        for(int i=0; i<10;i++){
            array.add(i);
        }
        Query query = new Query(0,5,array);
        query.print();
    }
}
