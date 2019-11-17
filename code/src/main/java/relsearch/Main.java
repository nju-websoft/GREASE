package relsearch;

/**
 * Copyright (C), 2019, Anonymous.
 * FileName: Main.java
 *
 * Grease entrance of program
 * @author  Anonymous
 * @Date  2019
 * @version 1.10
 */

import datareader.Graph;
import datareader.TextReader;
import datareader.TwoLink;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    static TextReader myReader = new TextReader(); // optionally changed to SqlReader
    static Graph graph;
    static String graphTable;
    static String propertyTable;
    static String databaseName;
    static String typeTabel;
    static HashMap<Integer,TypePropMap> typePropInfo;
    static OntologyTree ontology;
    static HashMap<TwoLink,Long> twoLinkLongHashMap;
    static Map<Integer,Integer> entity2type;
    static Map<Integer,HashSet<Integer>> entity2prop;
//    static ArrayList<Query> queryList;
    static HashMap<Integer,Integer> typeCount;
    static double tao = 1;
    static double mu = 1;
    static double power = 1;
    static double rate_miss = 2;
    static double baseNumber = 1; //Math.E;
    static int beta = 2;  // decay factor
    static int MAX_LENGTH = 3;
    static int K_TOP = 3;
    static int exampleNum=3;
    static double TOTAL_PATH_COUNT = 2.398692697230165E15;    //37434464042L;  3348444425400; 901960429536721800
    static double SUM_PROPERTY_COUNT = 18746174;
    static double BIG_DOUBLE = 100000000;              // product with probability to avoid underflow
    static int naive = 0;
    static int plus = 5;
    static boolean printPropertyScore = false;
//    static String PASSWORD="123";


    /**
     * load all data
     */
    public static void initialize(){
//        graph = myReader.getGraph(databaseName,tableName); //toygraph,"objtriples"
        entity2prop = myReader.getPropMap(databaseName,"entity2property.txt");
        System.out.println("got the table:"+ "entity2property.txt");
        entity2type = myReader.getMinType(databaseName,typeTabel);
        System.out.println("got the table:"+ typeTabel);
        typeCount = myReader.getTypeCount(databaseName,"type_count.txt");
        ontology = new OntologyTree(14,0,-1,myReader.getOntology(databaseName,"ontology.txt"));
        twoLinkLongHashMap = myReader.getTwoLinkCount(databaseName,"bilink.txt");
        typePropInfo = new HashMap<Integer, TypePropMap>();
        for(int type:OntologyTree.OntTree.keySet()){
            if(!typePropInfo.containsKey(type)){
                typePropInfo.put(type, new TypePropMap(type));
            }
        }
        myReader.getTypePropMap(databaseName,"type_property_count.txt",typePropInfo);
//        System.out.println("getSumPropOfType(14): "+getSumPropOfType(14));
        graph = myReader.getGraphDouble(databaseName,graphTable); //toygraph,"objtriples"
    }

    /**
     * set parameter
     *
     * @param maxLength  length bound
     * @param kTop use top k ranked facets
     * @param database select database
     * @param table triple table
     * @param tableProperty entity's property table
     * @return
     */
    public static void config(int maxLength, int kTop,String database, String table,String tableProperty){
        MAX_LENGTH = maxLength;
        K_TOP = kTop;
        graphTable = table;
        propertyTable = tableProperty;
        databaseName = database;
        typeTabel = "entity2type.txt";
        if(database.equals("yago")){
            TOTAL_PATH_COUNT = 200;
            SUM_PROPERTY_COUNT = 100; //77749670 12430700
            typeTabel = "entity2type_empty";
        }
        if(database.equals("data/DemoGraph")){
            TOTAL_PATH_COUNT = 200;
            SUM_PROPERTY_COUNT = 100; //77749670 12430700
            typeTabel = "entity2type.txt";
        }
        System.out.println("MAX_LENGTH:"+MAX_LENGTH);
        System.out.println("K_TOP:"+K_TOP);
        System.out.println("databaseName:"+databaseName);
        System.out.println("entity2property table:"+propertyTable);
        System.out.println("graphTable:"+graphTable);
        System.out.println("TOTAL_PATH_COUNT:"+ TOTAL_PATH_COUNT);
        System.out.println("SUM_PROPERTY_COUNT:" + SUM_PROPERTY_COUNT);

        System.out.println("initialization finished!");

    }


    public static void timestamp(){
        System.out.println(new SimpleDateFormat("MM-dd | HH:mm:ss:SSS").format(new Date()));
    }
    public static void timestamp(String info){
        System.out.println(new SimpleDateFormat("MM-dd | HH:mm:ss:SSS").format(new Date()));
        System.out.println(info);
    }

    /**
     * search path between two node of one pair
     *
     * @param pair example pair
     * @return path list
     */
    public static ArrayList<ArrayList<Integer>> searchPaths(ExamplePair pair){
        if(graph.getKeySet().contains(pair.source)&&graph.getKeySet().contains(pair.target)){
            return graph.biSearchPath(pair.getSource(),pair.getTarget(),MAX_LENGTH);
        }
        return new ArrayList<ArrayList<Integer>>();
    }

    /**
     * convert a list paths to a list of relation path
     *
     * @param paths a list of paths(containing entity)
     * @return a list of relation paths
     */
    public static Map<RelationPath,Integer> getRelationPath(ArrayList<ArrayList<Integer>> paths){
        Map<RelationPath,Integer> pathMap = new HashMap<RelationPath, Integer>();
        for (ArrayList<Integer> path: paths){
            RelationPath relationPath = new RelationPath(path);   // relation path
            if (!pathMap.containsKey(relationPath)){
                pathMap.put(relationPath,0);
            }
            pathMap.put(relationPath, pathMap.get(relationPath)+1);
        }
        return pathMap;
    }

//    @NotNull
    /**
     * sort the relation path by the significance
     *
     * @param relationPathMap  map relation path to it count
     * @param examplePairs the given example pairs
     * @return ranked relation path
     */
    public static ArrayList<RelationPath> relation_Rank(List<Map<RelationPath,Integer>> relationPathMap,ArrayList<ExamplePair> examplePairs){
        Set<RelationPath> relationPathKeys = new HashSet<RelationPath>();
        HashMap<RelationPath,Integer> relationPathCount = new HashMap<>();
        for(Map<RelationPath,Integer> rpMap : relationPathMap){
            relationPathKeys.addAll(rpMap.keySet());
            System.out.println("relationPathKeys.size: "+relationPathKeys.size());
        }
        for(RelationPath rp:relationPathKeys){
            relationPathCount.put(rp,0);
            for(Map<RelationPath,Integer> rpMap : relationPathMap){
                if(rpMap.containsKey(rp)){
                    relationPathCount.put(rp,relationPathCount.get(rp)+1);
                }
            }
            if(relationPathCount.get(rp)==exampleNum){
                System.out.println(exampleNum + " exampleNum rp:"+rp.getRelationPath());
            }
        }
        ArrayList<RelationPath> relationPathList = new ArrayList< RelationPath>(relationPathKeys);
        for(RelationPath rp:relationPathList){
            if(naive!=0){
                rp.setScoreNaive(relationPathCount);
            }else {
                rp.setScore(relationPathMap,getRpFrequent(rp.getRelationPath()),examplePairs,relationPathCount);      // set relation path score according to the frequency of rp in every example pair
            }
        }
        Collections.sort(relationPathList);
        if (relationPathList.size() <= K_TOP){
            return  new ArrayList<RelationPath>(relationPathList.subList(0,relationPathList.size()));
        }
        return  new ArrayList<RelationPath>(relationPathList.subList(0,K_TOP));
    }

    /**
     * use language model to estimate the frequency of relation path
     *
     * @param rp relation path
     * @return estimated frequency
     */
    public static double getRpFrequent(List<Integer> rp){                // need to be implemented
        if(rp.size()==1){
            return twoLinkLongHashMap.get(new TwoLink(rp.get(0),0));
        }else if(rp.size()==2){
            return twoLinkLongHashMap.get(new TwoLink(rp.get(0),rp.get(1)));
        }else if(rp.size()==3){
            double approx1 = getRpFrequent(rp.subList(0,2))*getRpFrequent(rp.subList(1,3))/getRpFrequent(rp.subList(1,2));
            double approx2 = getRpFrequent(rp.subList(1,3))*getRpFrequent(rp.subList(0,2))/getRpFrequent(rp.subList(0,1));
            return approx1/2+approx2/2;
        }else{
            assert rp.size()==4;
            double approx1 = getRpFrequent(rp.subList(0,3))*getRpFrequent(rp.subList(2,4))/getRpFrequent(rp.subList(2,3));
            double approx2 = getRpFrequent(rp.subList(1,4))*getRpFrequent(rp.subList(0,2))/getRpFrequent(rp.subList(0,1));
            return approx1/2+approx2/2;
        }
    }

    /**
     * get the candidates using the ranked relation path and property
     *
     * @param source query entity
     * @param rpTopK top relation path
     * @param propertyScore top property
     * @return candidates list
     */
    public static ArrayList<Candidate> getCandList(int source, ArrayList<RelationPath> rpTopK,HashMap<Integer,Double> propertyScore ){
//        HashMap<Integer,Double> cand2Feature = new HashMap<Integer, Double>();
        HashMap<Integer,Candidate> candidateHashMap = new HashMap<>();
        HashMap<Integer,HashMap<RelationPath,Integer>> candidate2RelationPathSet = new HashMap<>();
        ArrayList<Candidate> candList = new ArrayList<Candidate>();
        timestamp("get candidate  begin ..");
        for(int i=0;i<rpTopK.size();i++){
            RelationPath rp = rpTopK.get(i);
            ArrayList<ArrayList<Integer>> pathsFollowRp = graph.getNodeByRp(source,rp.getRelationPath());
            System.out.println(source+" pathsFollowRp : "+pathsFollowRp.size());
            for(ArrayList<Integer> path:pathsFollowRp){
                int candidate = path.get(path.size()-1);
                if(!candidate2RelationPathSet.containsKey(candidate)){
                    candidate2RelationPathSet.put(candidate,new HashMap<>());
                }
                if(!candidate2RelationPathSet.get(candidate).containsKey(rp)){
                    candidate2RelationPathSet.get(candidate).put(rp,0);
                }
                candidate2RelationPathSet.get(candidate).put(rp,candidate2RelationPathSet.get(candidate).get(rp)+1);
//                candidate2RelationPathSet.get(candidate).add(rp);
                if(!candidateHashMap.containsKey(candidate)){
                    candidateHashMap.put(candidate,new Candidate(candidate));
                }
            }
        }
        ArrayList<Property> propertyArrayList = new ArrayList<>();
        for(int prop: propertyScore.keySet()){
            propertyArrayList.add(new Property(prop,propertyScore.get(prop)));
        }
        Collections.sort(propertyArrayList);
        List<Property> topProperty = propertyArrayList.subList(0,Math.min(propertyArrayList.size(),Main.K_TOP));
        for(Property prop: topProperty){
            System.out.println(prop);
        }
        double rpLogScore;
        double propScore;
        for(RelationPath rp: rpTopK) {
//            rpLogScore = rp.getLogScore() - rpTopK.get(rpTopK.size() - 1).getLogScore() + 1;
            rpLogScore = rp.getScore();
            System.out.println("rp:" + rp + " score:" + rpLogScore);
        }
        for(Property prop: topProperty){
            int property = prop.getProperty();
//            propScore = Math.log10(propertyScore.get(property)) - Math.log10(propertyScore.get(topProperty.get(topProperty.size()-1).getProperty()))+1;
            propScore = propertyScore.get(property);
            System.out.println("property:"+prop+" score"+propScore);
        }
        int rpCount = 0;
        for(int candidateKey:candidateHashMap.keySet()){
            double rpScore = 0.0;
            for(RelationPath rp: rpTopK){
//                rpLogScore = rp.getLogScore() - rpTopK.get(rpTopK.size() - 1).getLogScore() + 1;
                rpLogScore = rp.getScore();
                if(candidate2RelationPathSet.get(candidateKey).containsKey(rp)){
                    rpCount =  candidate2RelationPathSet.get(candidateKey).get(rp);
                    if(rpCount>Main.plus){
                        rpCount = Main.plus;
                    }
                    rpScore = rpScore + rpCount * rpLogScore;
                }else{
//                    rpScore = rpScore * 1/Main.TOTAL_PATH_COUNT;
                }
            }


            int type_cand = 14;
            if(entity2type.containsKey(candidateKey)){
                type_cand = entity2type.get(candidateKey);
            }
            double apScore = 0.0;
            for(Property prop: topProperty){
                int property = prop.getProperty();
//                propScore = Math.log10(propertyScore.get(property)) - Math.log10(propertyScore.get(topProperty.get(topProperty.size()-1).getProperty()))+1;
                propScore = propertyScore.get(property);
                if(entity2prop.containsKey(candidateKey)&&entity2prop.get(candidateKey).contains(property)){
//                    System.out.println("with such property candidateKey: "+candidateKey+" property: "+ prop);
                    apScore = apScore + propScore;
                }else {
//                    double probHaveProp = (double)getTypePropNum(14,property)/Main.SUM_PROPERTY_COUNT;
//                    propertyCount = (probHaveProp*rate_miss);
//                    apScore = apScore * probHaveProp * propertyScore.get(property)  ; // * Main.rate_miss;
                }
            }
//            double apLogScore = Math.log(apScore)/Math.log(propertyScore.get(topProperty.get(topProperty.size()-1).getProperty()));
//            candidateHashMap.get(candidateKey).addScore(rpScore,0);
//            candidateHashMap.get(candidateKey).addScore(0,apScore);
            candidateHashMap.get(candidateKey).setScore(rpScore,apScore);
        }
        for(Integer candidateKey:candidateHashMap.keySet()){
            candList.add(candidateHashMap.get(candidateKey));
        }
        timestamp("get candidate finished.");
//        System.out.println("**********************************");
//        for(int prop:propertyScore.keySet()){
//            System.out.println("property:"+prop+" score:"+propertyScore.get(prop));
//        }
//        System.out.println("**********************************");
        return candList;
    }

    /**
     * get number of property of some type
     *
     * @param type entity type
     * @param prop property
     * @return count
     */
    public static int getTypePropNum(int type, int prop){
        int count = 0;
        for(int t:OntologyTree.OntTree.get(type).descendant){
            count += typePropInfo.get(t).getPropNum(prop);
//            System.out.println("descendant:"+t+"count:"+typePropInfo.get(t).getPropNum(prop));
        }
        if(count == 0){     //smooth
            count = 1;
        }
        return count;
    }

    /**
     * get total number of all properties of given type
     *
     * @param type entity type
     * @return sum of count
     */
    public static int getSumPropOfType(int type){
        int count = 0;
        for(int t:OntologyTree.OntTree.get(type).descendant){
            count += typePropInfo.get(t).getTotalProp();
//            System.out.println("descendant:"+t+"count:"+typePropInfo.get(t).getTotalProp());
        }
        return count;
    }

    /**
     * get score of property given examples
     *
     * @param targetSet example entities
     * @param propertySet properties
     * @return map of property to score
     */
    public static HashMap<Integer,Double> getPropertyScore(ArrayList<Integer> targetSet, HashSet<Integer> propertySet ) throws Exception{
        HashMap<Integer,Double> propertyScore = new HashMap<>();
        HashMap<Integer,Integer> propertyCount = new HashMap<Integer,Integer>();
        double pScore;
        int lca = OntologyTree.getNodeLCA(targetSet);
        for (int property:propertySet){
            propertyCount.put(property,0);
            for (int target: targetSet){
                if(entity2prop.containsKey(target)&&entity2prop.get(target).contains(property)){
                    propertyCount.put(property,propertyCount.get(property)+1);
                }
            }
        }
        for (int property:propertySet){
            pScore = (double)getTypePropNum(lca,property)* Math.pow(Main.SUM_PROPERTY_COUNT,exampleNum+1)/getSumPropOfType(lca);
//            assert getTypePropNum(lca,property)>=propertyCount.get(property);
            if(getTypePropNum(lca,property) < propertyCount.get(property)){
                System.out.println("lca:"+lca+"property:"+property);
                for(int target: targetSet){
                    int type = 14;
                    if(entity2type.containsKey(target)){
                        type = entity2type.get(target);
                    }
                    System.out.println("target:"+target+" type:"+type);
                }
            }else{
                for(int target: targetSet){
                    if(entity2prop.containsKey(target)&&entity2prop.get(target).contains(property)){
                        pScore = pScore*1/getTypePropNum(lca,property);
                    }else {
                        double probHaveProp = (double)getTypePropNum(lca,property)/getSumPropOfType(lca);
                        pScore = pScore*probHaveProp/getTypePropNum(lca,property)*Main.mu;
                    }
                }
                propertyScore.put(property,pScore);
                if(Double.isNaN(pScore)){
                    System.out.println("NaN! :"+property+" value:"+pScore);
                }
            }
        }

//        for(int property:propertySet){
//            System.out.println("property: "+property+" "+"count: "+propertyCount.get(property)+" property score: "+propertyScore.get(property));
//        }
        return propertyScore;
    }

    /**
     * perform relevance search using the given example pairs
     *
     * @param query contains a query entity and some example paris
     * @throws IOException  when the query entity is not in the graph
     * @return ranked candidates list
     */
    public static ArrayList<Candidate> relevanceSearch(Query query) throws Exception{
        ArrayList<Double> ndcg = new ArrayList<>();
        if(!graph.getKeySet().contains(query.getQueryId())){
            ndcg.add(0.0);
            ndcg.add(0.0);
            ndcg.add(0.0);
            ndcg.add(-1.0);
            throw new IOException("the graph doesn't contain the query entity");
        }

        long startTime = System.currentTimeMillis();
        List<List<ArrayList<Integer>>> paths= new ArrayList<List<ArrayList<Integer>>>();
        List<Map<RelationPath,Integer>> relationFreq = new ArrayList<Map<RelationPath, Integer>>();
        ArrayList<Integer> targets = new ArrayList<>();
        HashSet<Integer> propertySet = new HashSet<>();
        for (ExamplePair pair:query.getExamplePairs()){
            targets.add(pair.target);
            if(entity2prop.containsKey(pair.getTarget())){
                propertySet.addAll(entity2prop.get(pair.getTarget()));
            }
            ArrayList<ArrayList<Integer>> pathOfPair= searchPaths(pair);
            paths.add(pathOfPair);
            relationFreq.add(getRelationPath(pathOfPair));
        }
        ArrayList<RelationPath> topK_relationPath = relation_Rank(relationFreq,query.getExamplePairs());
        for (RelationPath rp:topK_relationPath){
            System.out.println(rp);
        }
        ArrayList<ArrayList<ArrayList<Sequence>>> filterPaths = new ArrayList<ArrayList<ArrayList<Sequence>>>();
        for(int i=0;i<topK_relationPath.size();i++){
            filterPaths.add(new ArrayList<ArrayList<Sequence>>());
            for (int j=0;j<query.exampleNum;j++){
                filterPaths.get(i).add(new ArrayList<Sequence>());
                for(ArrayList<Integer> path:paths.get(j)){
                    if(topK_relationPath.get(i).confirm(path)){
                        filterPaths.get(i).get(j).add(new Sequence(path));
                    }
                }
            }
        }
        HashMap<Integer,Double> propertyScore = getPropertyScore(targets,propertySet);
//        timestamp("get candidate begin ..");
        if(Main.printPropertyScore){
            for(int property:propertyScore.keySet()){
                System.out.println("property:"+property+" score:"+propertyScore.get(property));
            }
        }
        ArrayList<Candidate> candidates = getCandList(query.getQueryId(),topK_relationPath,propertyScore);
        ArrayList<Candidate> candidatesCopy = new ArrayList<>(candidates);
//        timestamp("get candidate finished.");
        Collections.sort(candidates);
        long endTime = System.currentTimeMillis();
        timestamp("sort candidate by rp*ap finished.");
        System.out.println(candidates.subList(0,Math.min(candidates.size(),10)));
        System.out.println("using time: "+(endTime - startTime)+"ms");
        // =========================================================================
//        System.out.println("NDCG@5 :"+qa.getNDCG(candidates,5));
//        System.out.println("NDCG@10:"+qa.getNDCG(candidates,10));
//        System.out.println("NDCG@20:"+qa.getNDCG(candidates,20));
//        ndcg.add(qa.getNDCG(candidates,5));
//        ndcg.add(qa.getNDCG(candidates,10));
//        ndcg.add(qa.getNDCG(candidates,20));
//        ndcg.add((double)(endTime-startTime)/1000);   // ms
//        Comparator<Candidate> byRpScore = (Candidate cand1,Candidate cand2)->(int)(Math.signum(cand2.rpScore-cand1.rpScore));
////        Collections.reverse(candidates);
//        Collections.sort(candidatesCopy,byRpScore);
//        timestamp("sort candidate by rp finished.");
//        System.out.println(candidatesCopy.subList(0,Math.min(candidatesCopy.size(),20)));
//        System.out.println("NDCG@5 :"+qa.getNDCG(candidatesCopy,5));
//        System.out.println("NDCG@10:"+qa.getNDCG(candidatesCopy,10));
//        System.out.println("NDCG@20:"+qa.getNDCG(candidatesCopy,20));
//        ndcg.add(qa.getNDCG(candidatesCopy,5));
//        ndcg.add(qa.getNDCG(candidatesCopy,10));
//        ndcg.add(qa.getNDCG(candidatesCopy,20));
//        timestamp("pipline_v2_output query finished.");
        return candidates;

    }

    public static void main(String[] args){
        //system initialization
        config(3,3,"data/DemoGraph","triple.txt","entity2property.txt");
        timestamp();
        initialize();
        timestamp();

        // input the example pairs
        ArrayList<Integer> examples = new ArrayList<>();
        examples.add(7);
        examples.add(5);
        examples.add(13);
        examples.add(14);

        //construct query
        Query q = new Query(10,2,examples);

        // relevance search by example
        try {
            relevanceSearch(q);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
