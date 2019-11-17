package datareader;

public class Tuple {
    int predicate;
    int so;

    public Tuple(int p, int so){
        this.predicate = p;
        this.so = so;
    }

    @Override
    public int hashCode() {
        int hashcode = (""+predicate+so).hashCode(); //this.predicate*this.so + this.predicate + this.so;
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        Tuple another = (Tuple)obj;
        return this.predicate==another.predicate&&this.so==another.so;
    }

    @Override
    public String toString() {
        return ""+predicate+"\t"+so;
    }

    public static void main(String[] args){
        Tuple t1 = new Tuple(3,5);
        Tuple t2 = new Tuple(3,5);
        System.out.println(t1.equals(t2));
        System.out.println(t1);
    }
}
