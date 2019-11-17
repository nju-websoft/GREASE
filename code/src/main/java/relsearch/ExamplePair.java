package relsearch;

public class ExamplePair {
    int source;
    int target;

    ExamplePair(int s, int t){
        source = s;
        target = t;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    public void print(){
        System.out.println(source+"\t"+target);
    }

    @Override
    public int hashCode() {
        return (""+source+target).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.source==((ExamplePair) obj).source&&this.target==((ExamplePair) obj).target;
    }
}
