package datareader;

import relsearch.TypePropMap;

import java.util.HashMap;
import java.util.HashSet;

public abstract class DataReader {

    public abstract HashMap<Integer,HashSet<Integer>> getPropMap(String database, String table);
    public abstract HashMap<Integer,Integer> getMinType(String database, String table);
    public abstract HashMap<Integer,Integer> getTypeCount(String database, String table);
    public abstract HashMap<Integer,HashSet<Integer>> getOntology(String database,String table);
    public abstract HashMap<TwoLink,Long> getTwoLinkCount(String database, String table);
    public abstract void getTypePropMap(String database, String table, HashMap<Integer, TypePropMap> tpMap);
    public abstract Graph getGraphDouble(String database, String table);

}
