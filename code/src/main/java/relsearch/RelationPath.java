package relsearch;

import java.util.*;

public class RelationPath implements Comparable<RelationPath>{
    private ArrayList<Integer> relationPath;
    private double score;
//    private double logScore;
    private int rpCount;
    public RelationPath(ArrayList<Integer> path){
        assert path.size()%2==1 && path.size()>2;
        relationPath = new ArrayList<Integer>();
        for (int i=0;i<path.size();i++){  // convert path to relation path
            if(i%2!=0){
                relationPath.add(path.get(i));
            }
        }
        score = 0;
    }

    public boolean confirm(List<Integer> path){    //judge whether a path confirm to a relationpath
        if(path.size() == relationPath.size()*2+1){
            for (int i=0;i<relationPath.size();i++){  // convert path to relation path
                    if (!path.get(i*2+1).equals(relationPath.get(i))){
                        return false;
                    }
            }
            return true;
        }
        return false;
    }

    public int getLength(){
        return relationPath.size();
    }

    public ArrayList<Integer> getRelationPath() {
        return relationPath;
    }

    @Override
    public int hashCode() {
        return relationPath.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ArrayList<Integer> rpAnother = ((RelationPath)obj).getRelationPath();
        if (getLength()==rpAnother.size()){
            for (int i=0; i<getLength();i++){
                if (!relationPath.get(i).equals(rpAnother.get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return relationPath.toString()+"\t"+score;
    }

    public int compareTo(RelationPath o) {
        if (o.score == this.score){
            return this.relationPath.size() - o.relationPath.size();
        }else if((o.score - this.score)<0){
            return -1;
        }else{
            return 1;
        }
    }

    public void setScore(List<Map<RelationPath,Integer>> relationPathMap) {
        for(Map<RelationPath,Integer> mapRp:relationPathMap){
            if(mapRp.containsKey(this) && mapRp.get(this)>0){
                this.score++;
            }
        }
    }

    public void setScore(List<Map<RelationPath,Integer>> relationPathMap,double rpFreq,ArrayList<ExamplePair> examplePairs) {   // not consider the deficiency of relation
        this.score = Main.BIG_DOUBLE/Main.TOTAL_PATH_COUNT*rpFreq;
        int index = 0;
        for(Map<RelationPath,Integer> mapRp:relationPathMap){
            double rpCount;                          // should smooth later
            if(mapRp.containsKey(this)){
                rpCount = (double)mapRp.get(this);
            }else {
                int source = examplePairs.get(index).getSource();
                int target = examplePairs.get(index).getTarget();
                int typeSoure = 14;
                int typeTarget = 14;
                if(Main.entity2type.containsKey(source)){
                    typeSoure = Main.entity2type.get(source);
                }
                if(Main.entity2type.containsKey(target)){
                    typeTarget = Main.entity2type.get(target);
                }
                double probHaveRp = rpFreq/Math.pow((Main.typeCount.get(typeSoure)*Main.typeCount.get(typeTarget)),Main.power);
                rpCount = probHaveRp*Math.pow(0.97,getLength())*Main.tao;
            }
            score = score*rpCount/rpFreq;
            index++;
        }
    }


    public void setScore(List<Map<RelationPath,Integer>> relationPathMap,double rpFreq,ArrayList<ExamplePair> examplePairs,HashMap<RelationPath,Integer> relationPathCounter) {   // not consider the deficiency of relation
        this.rpCount = relationPathCounter.get(this);
        this.score = Main.BIG_DOUBLE/Main.TOTAL_PATH_COUNT*rpFreq;
        int index = 0;
        for(Map<RelationPath,Integer> mapRp:relationPathMap){
            double rpCount;                          // should smooth later
            int source = examplePairs.get(index).getSource();
            int target = examplePairs.get(index).getTarget();
            int typeSoure = 14;
            int typeTarget = 14;
            if(Main.entity2type.containsKey(source)){
                typeSoure = Main.entity2type.get(source);
            }
            if(Main.entity2type.containsKey(target)){
                typeTarget = Main.entity2type.get(target);
            }
            if(mapRp.containsKey(this)){
                rpCount = (double)mapRp.get(this);
            }else {
                double pairCount = (double)Main.typeCount.get(typeSoure)*(double)Main.typeCount.get(typeTarget);
                double probHaveRp =  rpFreq/(Math.pow(pairCount,Main.power));
//                System.out.println("pairCount:"+pairCount+" typeSoure:"+typeSoure+" typeSoure:"+typeTarget);
//                System.out.println("probHaveRp:"+probHaveRp);
//                rpCount = probHaveRp*(1.0-Math.pow(1-Main.rate_miss,getLength()))*Main.tao*Math.pow(Main.baseNumber,relationPathCounter.get(this)-1);
                rpCount = probHaveRp*Main.tao*Math.pow(Main.baseNumber,relationPathCounter.get(this)-1);

            }
            score = score*rpCount/rpFreq;
//            System.out.println("rpCount: "+rpCount+" rpFreq: "+rpFreq+" score: "+score);
            index++;
        }
        score = score*Math.exp(-Main.beta*relationPath.size())*Math.exp(Main.beta*2);
//        System.out.println("set score: "+score);
    }
    public void setScoreNaive(HashMap<RelationPath,Integer> relationPathCounter) {   // not consider the deficiency of relation
        this.rpCount = relationPathCounter.get(this);
        this.score = this.rpCount;
    }

    public void setScore(float score){
        this.score = score;
    }

    public double getScore(){
        return score;
    }
    public double getLogScore(){
        return Math.log10(score);
    }

    public static void demo(){
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);

        RelationPath rp1 = new RelationPath(array); //2 4;8
        array.add(6);
        array.add(7);
        RelationPath rp2 = new RelationPath(array); // 2 4 6;

        rp1.setScore(8);
        rp2.setScore(10);

        ArrayList<RelationPath> rpList = new ArrayList<RelationPath>();
        rpList.add(rp1);
        rpList.add(rp2);

        Collections.sort(rpList);
        System.out.println(rpList.get(0).getRelationPath().toString());
        System.out.println(rpList.get(1).getRelationPath().toString());
    }

    public static void main(String[] args){
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);

        RelationPath rp1 = new RelationPath(array); //2 4;8
        RelationPath rp2 = new RelationPath(array); // 2 4 6;
        array.add(6);
        array.add(7);
        RelationPath rp3 = new RelationPath(array); // 2 4 6;

        Set<RelationPath> rpSet = new HashSet<RelationPath>();
        rpSet.add(rp1);
        rpSet.add(rp2);
        rpSet.add(rp3);
        System.out.println(rpSet.size());

    }
}
