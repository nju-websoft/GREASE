package relsearch;

import java.util.ArrayList;

public class Sequence {
    public ArrayList<Integer> sequence;
    public ArrayList<Integer> nodePath;

   public Sequence(ArrayList<Integer> path){
       sequence = new ArrayList<Integer>();
       nodePath = new ArrayList<Integer>();
       for(int i=0;i<path.size();i++){
           int label = path.get(i);
           sequence.add(label);
           if(i%2==0&&i!=0){
               nodePath.add(label);
           }
       }
   }

   public static void main(String[] args){
       ArrayList<Integer> path = new ArrayList<Integer>();
       path.add(0);
       path.add(1);
       path.add(2);
       path.add(3);
       path.add(4);
       Sequence seq = new Sequence(path);
       System.out.println(seq.nodePath);
   }
}
