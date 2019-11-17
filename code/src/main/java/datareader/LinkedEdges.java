package datareader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class LinkedEdges {
    private ArrayList<Edge> linkedges;

    public LinkedEdges(){
        this.linkedges = new ArrayList<Edge>();
    }
    public LinkedEdges(Edge link){
        this.linkedges = new ArrayList<Edge>();
        this.linkedges.add(link);
//        linkedges.contains();
    }
    public void addEdge(Edge link){
        if(this.linkedges.contains(link)){
            int index = this.linkedges.indexOf(link);
            this.linkedges.get(index).incrementWeight();
        }else {
            this.linkedges.add(link);
        }

    }
    public void addLink(Edge link){
        this.linkedges.add(link);
    }
    public int getSize(){
        return this.linkedges.size();
    }
    public HashSet<Integer> getNodeSet(){
        HashSet<Integer> NodeSet = new HashSet<Integer>();
        for (Edge link: this.linkedges){
            NodeSet.add(link.getNode());
        }
        return NodeSet;
    }
    public HashSet<Integer> getEdgeSet(){
        HashSet<Integer> EdgeSet = new HashSet<Integer>();
        for (Edge link: this.linkedges){
            EdgeSet.add(link.getEdge());
        }
        return EdgeSet;
    }

    public ArrayList<Edge> getLinkedges() {
        return linkedges;
    }

    public static void main(String[] args){
        LinkedEdges le = new LinkedEdges();
        System.out.println(le.getSize());
    }

    public Iterator<Edge> iterator(){
        return this.linkedges.iterator();
    }

}
