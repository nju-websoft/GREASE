package relsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Candidate implements Comparable<Candidate>{
    int id;
//    ArrayList<PathFeature> pathFeats;
    double score = 0.0;
    double rpScore;
    double apScore;
//    double banlance = 0.01;


//    public Candidate(int id, ArrayList<PathFeature> pathFeaturesf){
//        this.id = id;
//        this.pathFeats = pathFeaturesf;
//        for(PathFeature pf: pathFeats){
//            score += pf.getScorePp()*pf.getScoreRp();
//            rpScore +=pf.getScoreRp();
//        }
//    }

    public Candidate(int id){
        this.id = id;
        score = 0.0;
        rpScore = 0.0;
        apScore = 0.0;
    }

    public Candidate(int id, double score){
        this.id = id;
        this.score = score;
    }
    private Candidate(int id, double score, double scoreRp){
        this.id = id;
        this.score = score;
        this.rpScore = scoreRp;
    }
    public void addScore(double scoreRp,double scoreAp){
        rpScore += scoreRp;
        apScore += scoreAp;
        score = rpScore+apScore;
    }
    public void setScore(double scoreRp,double scoreAp){
        rpScore = scoreRp;
        apScore = scoreAp;
        score = rpScore+Main.rate_miss*apScore;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public double getApScore() {
        return apScore;
    }

    public double getRpScore() {
        return rpScore;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id==((Candidate)obj).getId();
    }

    public int compareTo(Candidate o){

//        BigInteger other = new BigInteger(Double.doubleToLongBits());
//        Double.doubleToLongBits()
//        BigDecimal bd1 = new BigDecimal(o.score);
//        BigDecimal bd2 = new BigDecimal(this.score);
//        return bd1.subtract(bd2).signum();
//        return (int)o.score - (int)this.score;
        return (int)Math.signum(o.score-this.score) ;
//        if((o.score - this.score)<0){
//            return -1;
//        }else if((o.score - this.score)>0){
//            return 1;
//        }else{
//            return 0;
//        }
    }

    @Override
    public String toString() {
        return "id:"+id+"/"+"score:"+score+"/"+"rpscore:"+rpScore;
    }
    public static void main(String[] args){
        ArrayList<Candidate> cands = new ArrayList<Candidate>();
        Candidate a = new Candidate(1,343,3.0);
        Candidate b = new Candidate(2,68,4.0);
        Candidate c = new Candidate(3,486,5.0);
        cands.add(a);
        cands.add(b);
        cands.add(c);
        System.out.println(cands);
        Collections.sort(cands);
        System.out.println(cands);
        Comparator<Candidate> byRpScore = (Candidate cand1,Candidate cand2)->(int)(Math.signum(cand2.rpScore-cand1.rpScore));
        Collections.sort(cands,byRpScore);
        System.out.println(cands);
    }

}
