package datareader;

import java.util.ArrayList;

public class Edge {
    private int edge;
    private int node;
    private int weight;
    private boolean out;

    public Edge(Triple triple, boolean out){
        this.edge = triple.getPredicate();
        this.out = out;
        this.weight = 1;
        if (out)
            this.node = triple.getObject();
        else
            this.node = triple.getSubject();
    }
    public Edge(Triple triple){
        if(triple.getPredicate()<0){
            this.edge = -triple.getPredicate();
            this.out = false;
        }else {
            this.edge = triple.getPredicate();
            this.out = true;
        }
        this.weight = 1;
        this.node = triple.getObject();
//        if (out)
//            this.node = triple.getObject();
//        else
//            this.node = triple.getSubject();
    }
    public int getNode(){
        return  this.node;
    }
    public int getEdge(){
        return  this.edge;
    }
    public boolean getDirection(){
        return this.out;
    }
    public int getSignedEdge(){
        if(!out)
            return -this.edge;
        else
            return this.edge;
    }
    public void incrementWeight(){
        this.weight++;
    }
    public int getWeight(){
        return this.weight;
    }
    public String toString(){
        return getSignedEdge()+" "+node;
    }

    @Override
    public boolean equals(Object obj) {
        Edge other = (Edge)obj;
        return (this.edge==other.edge&&this.node==other.node&&this.out==other.out);
    }
    @Override
    public int hashCode(){
        String str = "edge"+edge+node+out;
        return str.hashCode();
    }

    public static void main(String[] args){
        ArrayList<Edge> links = new ArrayList<Edge>();
        Edge link1 = new Edge(new Triple(1,2,3),true);
        Edge link2 = new Edge(new Triple(1,2,3),true);

        link2.incrementWeight();
        links.add(link1);
        System.out.println(links.contains(link2));
        if (links.contains(link2)){
            int index = links.indexOf(link2);
            System.out.println(links.get(index).getWeight());
            links.get(index).incrementWeight();
            System.out.println(links.get(index).getWeight());
        }
        System.out.println(links.contains(link2));
        if (links.contains(link2)){
            int index = links.indexOf(link2);
            System.out.println(links.get(index).getWeight());
            links.get(index).incrementWeight();
            System.out.println(links.get(index).getWeight());
        }
    }
}
