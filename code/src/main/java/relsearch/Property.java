package relsearch;

//import org.jetbrains.annotations.NotNull;

public class Property implements Comparable<Property>{
    int property;
    double score;
    Property(int id){
        this.property = id;
    }
    Property(int id, double score){
        this.property = id;
        this.score = score;
    }
    public int getProperty() {
        return property;
    }



    @Override
    public int compareTo( Property o) {
        return (int)Math.signum(o.score - this.score);
    }

    @Override
    public String toString() {
        return "property:"+property+"  score:"+score;
    }
}
