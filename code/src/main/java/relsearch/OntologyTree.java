package relsearch;

//import datareader.MysqlReader;

import java.util.*;

public class OntologyTree {
    int classId;
    int treeLevel;
    int parentId;
    HashSet<Integer> ancestor;
    HashSet<Integer> children;
    HashSet<Integer> descendant;
    static int maxLevel=-1;
    public static HashMap<Integer,OntologyTree> OntTree=new HashMap<Integer, OntologyTree>();
    public OntologyTree(int id, int level, int parent, HashMap<Integer,HashSet<Integer>> parent2child){
        classId = id;
        treeLevel = level;
        parentId = parent;
        children = new HashSet<Integer>();
        descendant = new HashSet<Integer>();
        ancestor = new HashSet<Integer>();
        descendant.add(classId);
        ancestor.add(classId);

        OntTree.put(classId,this);

        if(parent==-1){
            ancestor.add(classId);
        }else{
            ancestor.addAll(OntTree.get(parent).ancestor);
        }

        if(parent2child.containsKey(classId)){
            for(int child:parent2child.get(classId)){
                children.add(child);
                OntologyTree childOnt = new OntologyTree(child,treeLevel+1,classId,parent2child);
                descendant.addAll(childOnt.descendant);
            }
        }
        if(treeLevel>maxLevel){
            maxLevel=treeLevel;
        }
    }
    public int getTreeLevel() {
        return treeLevel;
    }

    public int getParentId() {
        return parentId;
    }

    public static int getNodeLCA(Collection<Integer> nodeSet){
        HashSet<Integer> commonParents =new HashSet<Integer>();
        commonParents.addAll(OntologyTree.OntTree.get(14).descendant);
        for(int node:nodeSet){
            int type = 14;
            if(Main.entity2type.containsKey(node)){
                type = Main.entity2type.get(node);
            }
            commonParents.retainAll(OntTree.get(type).ancestor);
        }
        int maxLevel = -1;
        int lca = -1;
        for(int cp:commonParents){
            if(OntTree.get(cp).getTreeLevel()>maxLevel){
                lca = cp;
                maxLevel = OntTree.get(lca).treeLevel;
            }
        }
        return lca;
    }

    public static int getTypeLCA(Set<Integer> typeSet){
        HashSet<Integer> commonParents =new HashSet<Integer>();
        commonParents.addAll(OntologyTree.OntTree.get(14).descendant);
        for(int type:typeSet){
            commonParents.retainAll(OntTree.get(type).ancestor);
        }
        int maxLevel = -1;
        int lca = -1;
        for(int cp:commonParents){
            if(OntTree.get(cp).getTreeLevel()>maxLevel){
                lca = cp;
                maxLevel = OntTree.get(lca).treeLevel;
            }
        }
        return lca;
    }

    public static int getTypeMin(Collection<Integer> typeSet){
        int typeMin = 14;
        int level = -1;
        for(int type:typeSet){
            if(OntTree.get(type).getTreeLevel()>level){
                typeMin = type;
                level = OntTree.get(type).getTreeLevel();
            }
        }
        return typeMin;
    }



    public static void main(String[] args){
//        String host = "localhost";
//        String database = "dbpedia";
//        String user = "root";
//        String passwd = "123456";
//        MysqlReader myReader = new MysqlReader(host,database,user,passwd);
//        HashMap<Integer,HashSet<Integer>> parent2child = myReader.getOntology("dbpedia","ontology_min");
//        OntologyTree onT = new OntologyTree(14,0,-1,parent2child);
//        System.out.println(OntTree.size());
//        System.out.println(OntTree.get(14).children);
//        System.out.println(OntTree.get(14).parentId);
//        System.out.println(OntTree.get(14).descendant.size());
//        System.out.println(OntologyTree.maxLevel);
//        ArrayList<Integer> types = new ArrayList<>();
//        types.add(3481461);
//        types.add(3481459);
//        types.add(3481458);
//        types.add(3481454);
//        types.add(14);
//        System.out.println(OntologyTree.getTypeMin(types));

//        HashSet<Integer> integerHashSet = new HashSet<Integer>();
//        integerHashSet.add(21);
//        integerHashSet.add(43);
//
//        System.out.println("lca:"+getTypeLCA(integerHashSet));
//        System.out.println(OntTree.get(14).descendant.size());
//        System.out.println(OntTree.get(24).descendant); //父类
//        System.out.println(OntTree.get(10).children);   //子类
//
//        HashMap<Integer,Integer> type2count = myReader.getTypeCount("dbpedia","type2count");
//        HashMap<Integer,Integer> typeCount = new HashMap<>();
//        for (int type:OntTree.keySet()){
//            int count = 0;
//            for(int subType:OntTree.get(type).descendant){
//                if(type2count.containsKey(subType)){
//                    count += type2count.get(subType);
//                }
//            }
//            typeCount.put(type,count);
//        }
//        System.out.println(typeCount.get(14));
//        myReader.writeTypeCount(typeCount);


    }
}
