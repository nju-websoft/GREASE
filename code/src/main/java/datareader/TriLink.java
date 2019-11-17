package datareader;

import java.util.ArrayList;

public class TriLink {
    int first;
    int second;
    int third;
    static final int OFFSET = 3480806;
    public TriLink(int start, int mid, int end){
        first = start;
        second = mid;
        third = end;
    }

    public TriLink(ArrayList<Integer> path){
        assert path.size()==3||path.size()==5||path.size()==7;
        first=path.get(1);
        if(path.size()==3){
            second=0;
            third=0;
        }else if (path.size()==5){
            second=path.get(3);
            third=0;
        }else {
            second=path.get(3);
            third=path.get(5);
        }
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    public void setLink(int pos, int value){
        assert pos==0||pos==1;
        if(pos==0){
            first=value;
        }else {
            second=value;
        }
    }

    @Override
    public int hashCode() {
        String str = " ";
        return (str+first+second+third).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.first==((TriLink)obj).getFirst()&&this.second==((TriLink)obj).getSecond()&&this.third==((TriLink)obj).getThird();
    }

    public String toString() {
        String str = "";
        return str+first+"#"+second+"#"+third;
    }

    public static void main(String[] args){
        ArrayList<Integer> path=new ArrayList<Integer>();
        path.add(1);
        path.add(2);
        path.add(3);
        TriLink tl = new TriLink(path);
        System.out.println(tl);
        path.add(4);
        path.add(5);
        TriLink tl2 = new TriLink(path);
        System.out.println(tl2);
        path.add(6);
        path.add(7);
        TriLink tl3 = new TriLink(path);
        System.out.println(tl3);
    }
}
