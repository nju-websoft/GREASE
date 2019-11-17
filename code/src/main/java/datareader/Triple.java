package datareader;

public class Triple {
    private int subject;
    private int predicate;
    private int object;

    public Triple(int subject, int predicate, int object){
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }
    public Triple(int node1, int edge, int node2, boolean out){
        if (out){
            this.subject = node1;
            this.predicate = edge;
            this.object = node2;
        }else {
            this.subject = node2;
            this.predicate = edge;
            this.object = node1;
        }

    }

    public int getObject() {
        return object;
    }
    public int getSubject(){
        return subject;
    }
    public int getPredicate(){
        return predicate;
    }
    public void reverse(){
        this.predicate = -this.predicate;
        int subj = this.subject;
        this.subject = this.object;
        this.object = subj;
    }

    @Override
    public int hashCode() {
        return (""+subject+predicate+object).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.subject==((Triple)obj).getSubject()&&this.predicate==((Triple)obj).getPredicate()&&this.object==((Triple)obj).getObject();
    }
}
