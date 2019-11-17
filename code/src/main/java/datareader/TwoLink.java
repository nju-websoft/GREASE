package datareader;

import java.util.ArrayList;

public class TwoLink {
    int first;
    int second;
    static final int OFFSET = 9000000;
    public TwoLink(int h, int t){
        first = h;
        second = t;
    }

    public TwoLink(ArrayList<Integer> path){
        assert path.size()==3||path.size()==5;
        first=path.get(1);
        if(path.size()==3){
            second=0;
        }else{
            second=path.get(3);
        }
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public void setLink(int pos,int value){
        assert pos==0||pos==1;
        if(pos==0){
            first=value;
        }else {
            second=value;
        }
    }

    @Override
    public int hashCode() {
        return (second+1000-OFFSET)*2000+first+1000-OFFSET;
    }

    @Override
    public boolean equals(Object obj) {
        return this.first==((TwoLink)obj).getFirst()&&this.second==((TwoLink)obj).getSecond();
    }

    @Override
    public String toString() {
        return ""+first+"#"+second;
    }

    public static void main(String[] args){
        ArrayList<Integer> path=new ArrayList<Integer>();
        path.add(1);
        path.add(2);
        path.add(3);
        TwoLink tl = new TwoLink(path);
        System.out.println(tl);
        path.add(4);
        path.add(5);
        TwoLink tl2 = new TwoLink(path);
        System.out.println(tl2);
    }

}
