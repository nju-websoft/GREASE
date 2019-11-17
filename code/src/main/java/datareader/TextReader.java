package datareader;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import relsearch.OntologyTree;
import relsearch.TypePropMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TextReader extends DataReader{
    public HashMap<Integer,HashSet<Integer>> getPropMap(String database, String table){
        HashMap<Integer,HashSet<Integer>> entity2prop = new HashMap<Integer, HashSet<Integer>>();
        List<String> lines = this.getLines(database+"/"+table);
        System.out.println("got table");
        if(lines != null){
            for(String line: lines){
                int entity = Integer.parseInt(line.split(" ")[0]);
                int prop = Integer.parseInt(line.split(" ")[1]);
                if(!entity2prop.containsKey(entity)){
                    entity2prop.put(entity,new HashSet<Integer>());
                }
                entity2prop.get(entity).add(prop);
            }
        }
        return entity2prop;
    }
    public HashMap<Integer,Integer> getMinType(String database, String table){
        HashMap<Integer,Integer> entity2type = new HashMap<Integer, Integer>();
        List<String> lines = this.getLines(database+"/"+table);
        if(lines != null){
            for(String line: lines){
                int entity = Integer.parseInt(line.split(" ")[0]);
                int type = Integer.parseInt(line.split(" ")[1]);
                entity2type.put(entity,type);
            }
        }
        return entity2type;
    }
    public HashMap<Integer,Integer> getTypeCount(String database, String table){
        HashMap<Integer,Integer> type2count = new HashMap<Integer, Integer>();
        List<String> lines = this.getLines(database+"/"+table);
        if(lines != null){
            for(String line: lines){
                int type = Integer.parseInt(line.split(" ")[0]);
                int count = Integer.parseInt(line.split(" ")[1]);
                type2count.put(type,count);
            }
        }
        return type2count;
    }
    public HashMap<Integer,HashSet<Integer>> getOntology(String database,String table){
        HashMap<Integer,HashSet<Integer>> parent2child = new HashMap<Integer, HashSet<Integer>>();
        List<String> lines = this.getLines(database+"/"+table);
        final long startTime = System.nanoTime();
        if(lines != null){

            for (String line:lines){
                int child = Integer.parseInt(line.split(" ")[0]);
                int parent = Integer.parseInt(line.split(" ")[1]);
                if(!parent2child.containsKey(parent)){
                    parent2child.put(parent,new HashSet<Integer>());
                }
                parent2child.get(parent).add(child);
            }

        }
        final long duration = System.nanoTime() - startTime;
        System.out.println("get Ontology duration:"+duration/1000000000);
        return parent2child;
    }
    public HashMap<TwoLink,Long> getTwoLinkCount(String database, String table){
        HashMap<TwoLink,Long> twoLinkLongHashMap = new HashMap<TwoLink, Long>();
        List<String> lines = this.getLines(database+"/"+table);
        if(lines != null){
            for(String line: lines){
                String link = line.split(" ")[0];
                int first = Integer.parseInt(link.split("#")[0]);
                int second =  Integer.parseInt(link.split("#")[1]);
                TwoLink tl = new TwoLink(first,second);
                long count = Long.parseLong(line.split(" ")[1]);
                twoLinkLongHashMap.put(tl,count);
            }
        }
        return twoLinkLongHashMap;
    }
    public void getTypePropMap(String database, String table, HashMap<Integer, TypePropMap> tpMap){
        List<String> lines = this.getLines(database+"/"+table);
        System.out.println("got table");
        if(lines != null){
            for(String line: lines){
                int type = Integer.parseInt(line.split(" ")[0]);
                int prop = Integer.parseInt(line.split(" ")[1]);
                int count = Integer.parseInt(line.split(" ")[2]);
                if(!tpMap.containsKey(type)){
                    tpMap.put(type,new TypePropMap(type));
                }
                tpMap.get(type).addTypePropCount(prop,count);
            }
        }
        return;
    }
    public Graph getGraphDouble(String database, String table){
        Graph g = new Graph();
        List<String> lines = getLines(database+"/"+table);
        for(String line: lines){
            String sub = line.split(" ")[0];
            String pre = line.split(" ")[1];
            String obj = line.split(" ")[2];
//            System.out.println(sub+" "+pre+" "+obj);
            int subject = Integer.parseInt(sub);
            int predicate = Integer.parseInt(pre)+1000;  // because predicate start from 0
            int object = Integer.parseInt(obj);
            Triple triple = new Triple(subject,predicate,object);
            Triple triple_inv = new Triple(object, -predicate, subject);
            g.addEdgeOut(triple);
            g.addEdgeOut(triple_inv);
        }
        return g;
    }

    public List<String> getLines(String filename){
        List<String> lines = new ArrayList<String>();
        try{
            lines = Files.readAllLines(Paths.get(filename));
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return lines;
    }

    public void indexProperty(Graph graph){
        int count = 0;
        int idCount = 0;
        HashMap<Tuple,Integer> tuple2prop = new HashMap<>();
        for(int node:graph.getKeySet()){
            for(Edge e:graph.getLinkedEdge(node).getLinkedges()){
//                System.out.println(e);
                if(e.getSignedEdge()>0){
                    Tuple tuple = new Tuple(e.getSignedEdge(),e.getNode());
                    if(!tuple2prop.containsKey(tuple)){
                        tuple2prop.put(tuple,idCount);
                        idCount++;
                    }
                    count++;
                    System.out.println(node+" "+tuple2prop.get(tuple));
                }
            }
        }
        System.out.println("count of property: "+ count);
        System.out.println("idCount: "+ idCount);
        System.out.println("size of tuple2prop: "+ tuple2prop.size());

    }

    public void indexTypePropertyCount(HashMap<Integer,HashSet<Integer>> entity2prop, HashMap<Integer,Integer> entity2type){
        HashMap<Integer,HashMap<Integer,Integer>> typePropCount = new HashMap<>();
        for(int entity:entity2prop.keySet()){
            for(int property:entity2prop.get(entity)){
                int type = entity2type.get(entity);
                if(!typePropCount.containsKey(type)){
                    typePropCount.put(type,new HashMap<>());
                }
                if(!typePropCount.get(type).containsKey(property)){
                    typePropCount.get(type).put(property,0);
                }
                typePropCount.get(type).put(property,typePropCount.get(type).get(property)+1);
            }
        }
        int sum = 0;
        for(int type:typePropCount.keySet()){
            for(int property:typePropCount.get(type).keySet()){
                System.out.println(type+ " "+property+" "+typePropCount.get(type).get(property));
                sum += typePropCount.get(type).get(property);
            }
        }
        System.out.println("sum of property: "+sum);
    }



    public static void main(String[] args){
        TextReader tr = new TextReader();
        String filename = "data/DemoGraph/entity_map.txt";
        List<String> lines = tr.getLines(filename);
        Map<String,String> id2uri = new HashMap<>();
        for(String line:lines){
            id2uri.put(line.split(" ")[0],line.split(" ")[1]);
        }

        filename = "data/DemoGraph/predicate_map.txt";
        lines = tr.getLines(filename);
        Map<String,String> id2pre = new HashMap<>();
        for(String line:lines){
            id2pre.put(line.split(" ")[0],line.split(" ")[1]);
        }

        filename = "data/DemoGraph/triple.txt";
        lines = tr.getLines(filename);
        System.out.println(lines.size());
        for(String line:lines){
            String sub = line.split(" ")[0];
            String pre = line.split(" ")[1];
            String obj = line.split(" ")[2];
            System.out.println(id2uri.get(sub)+" "+id2pre.get(pre)+" "+id2uri.get(obj));
        }

        Graph g = tr.getGraphDouble("data/DemoGraph","triple.txt");
        g.printGraphinfo();

        System.out.println(g.biSearchPath(5,6,2));
        tr.indexProperty(g);

        HashMap<Integer,HashSet<Integer>> entity2prop = tr.getPropMap("data/DemoGraph","entity2property.txt");
        System.out.println(entity2prop.get(0));
        System.out.println(entity2prop.get(5));
        System.out.println(entity2prop.get(9));

        HashMap<Integer,Integer> entity2type = tr.getMinType("data/DemoGraph","entity2type.txt");
        System.out.println(entity2type.get(0));
        System.out.println(entity2type.get(5));
        System.out.println(entity2type.get(9));

        HashMap<Integer,Integer> type2count = tr.getTypeCount("data/DemoGraph","type_count.txt");
        System.out.println(type2count);

//        HashMap<TwoLink,Long> twoLinkMap = g.getTwoLinkMap();
//        System.out.println(twoLinkMap.size());
//        for(TwoLink tl:twoLinkMap.keySet()){
//            System.out.println(tl+" "+twoLinkMap.get(tl));
//            TwoLink tl_inv = new TwoLink(-tl.second,-tl.first);
//            if(tl.second == 0)
//                tl_inv = new TwoLink(-tl.first,0);
//            if(twoLinkMap.get(tl) != twoLinkMap.get(tl_inv)){
//                System.out.println(tl);
//                System.out.println(twoLinkMap.get(tl));
//                System.out.println(twoLinkMap.get(tl_inv));
//            }
//        }

        HashMap<TwoLink,Long> twoLinkLongHashMap = tr.getTwoLinkCount("data/DemoGraph","bilink.txt");
        System.out.println(twoLinkLongHashMap.size());

        HashMap<Integer,HashSet<Integer>> ontology = tr.getOntology("data/DemoGraph","ontology.txt");
        for(int key:ontology.keySet()){
            System.out.println(key+" "+ontology.get(key));
        }

        tr.indexTypePropertyCount(entity2prop,entity2type);

        HashMap<Integer, TypePropMap> typePropInfo = new HashMap<Integer, TypePropMap>();
        for(int type: OntologyTree.OntTree.keySet()){
            if(!typePropInfo.containsKey(type)){
                typePropInfo.put(type, new TypePropMap(type));
            }
        }
        tr.getTypePropMap("data/DemoGraph","type_property_count.txt",typePropInfo);
    }
}
