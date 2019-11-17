package datareader;

import relsearch.TypePropMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SqlReader extends DataReader {
    public HashMap<Integer,HashSet<Integer>> getPropMap(String database, String table){
        return null;
    }
    public HashMap<Integer,Integer> getMinType(String database, String table){
        return null;
    }
    public HashMap<Integer,Integer> getTypeCount(String database, String table){
        return null;
    }
    public HashMap<Integer,HashSet<Integer>> getOntology(String database,String table){
        return null;
    }
    public HashMap<TwoLink,Long> getTwoLinkCount(String database, String table){
        return null;
    }
    public void getTypePropMap(String database, String table, HashMap<Integer, TypePropMap> tpMap){
        return ;
    }
    public Graph getGraphDouble(String database, String table){
        return null;
    }

    public List<String> getLines(String filename){
        return null;
    }

    public void indexProperty(Graph graph){
        return ;
    }

    public void indexTypePropertyCount(HashMap<Integer,HashSet<Integer>> entity2prop, HashMap<Integer,Integer> entity2type){
        return ;
    }
}
