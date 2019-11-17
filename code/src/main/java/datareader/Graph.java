package datareader;

import relsearch.Main;

import java.util.*;


public class Graph {
    private HashMap< Integer,LinkedEdges> Graph;
    private HashSet<Integer> KeySet;
    private HashSet<Integer> EdgeSet;
    private long LinkCounts;

    public Graph(){
        this.Graph = new HashMap< Integer,LinkedEdges>();
        this.KeySet = new HashSet<Integer>();
        this.EdgeSet = new HashSet<Integer>();
        this.LinkCounts = 0;
    }

    public Graph(int maxId){
        this.Graph = new HashMap< Integer,LinkedEdges>();
        this.KeySet = new HashSet<Integer>();
        this.EdgeSet = new HashSet<Integer>();
        this.LinkCounts = 0;
        for (int i=1;i<=maxId;i++){
            Graph.put(i,new LinkedEdges());
        }
    }


    private void addoutEdge(Triple triple){
        if (!this.Graph.containsKey(triple.getSubject())){
            Edge link = new Edge(triple,true);
            LinkedEdges linkedges = new LinkedEdges(link);
            Graph.put(triple.getSubject(),linkedges);
        }else {
            Edge link = new Edge(triple,true);
            Graph.get(triple.getSubject()).addEdge(link);
        }
        this.KeySet.add(triple.getSubject());
        this.EdgeSet.add(triple.getPredicate());
        this.LinkCounts++;
    }


    private void addinEdge(Triple triple){
        if (!this.Graph.containsKey(triple.getObject())){
            Edge link = new Edge(triple,false);
            LinkedEdges linkedges = new LinkedEdges(link);
            Graph.put(triple.getObject(),linkedges);
        }else {
            Edge link = new Edge(triple,false);
            Graph.get(triple.getObject()).addEdge(link);
        }
        this.KeySet.add(triple.getObject());
        this.EdgeSet.add(triple.getPredicate());
        this.LinkCounts++;
    }


    public void addEdge(Triple triple){
        this.addinEdge(triple);
        this.addoutEdge(triple);
    }
    public void addEdgeOut(Triple triple){
        if (!this.Graph.containsKey(triple.getSubject())){
            Edge link = new Edge(triple);
            LinkedEdges linkedges = new LinkedEdges(link);
            Graph.put(triple.getSubject(),linkedges);
        }else{
            Edge link = new Edge(triple);
            Graph.get(triple.getSubject()).addLink(link);
        }
        this.EdgeSet.add(Math.abs(triple.getPredicate()));
        this.KeySet.add(triple.getSubject());
        this.LinkCounts++;
//        if(triple.getPredicate()>0){
//            addoutEdge(triple);
//        }
    }

    public int size(){
        return this.Graph.size();
    }

    public HashSet<Integer> getKeySet(){
//        this.KeySet = (HashSet<Integer>) Graph.keySet();
        return this.KeySet;
    }

    public HashSet<Integer> getEdgeSet(){
        return this.EdgeSet;
    }

    public long getLinkCounts(){
        return this.LinkCounts;
    }

    public LinkedEdges getLinkedEdge(int key){
        return this.Graph.get(key);
    }

    public long printGraphinfo(){
        System.out.println("number of     keys:"+this.KeySet.size());
        System.out.println("number of edgetype:"+this.EdgeSet.size());
        System.out.println("number of    links:"+this.LinkCounts);
        System.out.println("number of  triples:"+this.LinkCounts/2);
        return this.LinkCounts;
    }

    public ArrayList<ArrayList<Integer>> getPath(int s, int t, int depth){
        assert this.KeySet.contains(s);
        assert this.KeySet.contains(t);
        ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> currents = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> nexts = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> start = new ArrayList<Integer>();
        start.add(s);
        currents.add(start);
        int count = 0;
        while (depth > 0){
            for(ArrayList<Integer> path: currents){
                int head = path.get(path.size()-1);
//                System.out.println("count:"+count+"label:"+sqlreader.getLabel("dbpedia",head));
                if(count%100000 == 0){
                    System.out.println("count:"+count);
                }
                count++;
                ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                if(linkededgeArrayList.size()>100000){
                    System.out.println("id:"+head+"size:"+linkededgeArrayList.size());
                }
                for(Edge link: this.Graph.get(head).getLinkedges()){
                    if (link.getNode()==t){
                        ArrayList<Integer> path_gold = new ArrayList<Integer>(path);
                        path_gold.add(link.getSignedEdge());
                        path_gold.add(link.getNode());
                        paths.add(path_gold);
                    }else if (!path.contains(link.getNode())){
                        ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                        path_new.add(link.getSignedEdge());
                        path_new.add(link.getNode());
                        nexts.add(path_new);
                    }
                }
            }
            currents = new ArrayList<ArrayList<Integer>>(nexts);
            nexts = new ArrayList<ArrayList<Integer>>();
            depth--;
        }
        return paths;
    }

    public ArrayList<ArrayList<Integer>> biSearchPath(int s, int t,int depth){
        assert this.KeySet.contains(s);
        assert this.KeySet.contains(t);
        int lradius = depth/2;
        if (depth%2 == 1){
            lradius++;
        }
        int rradius = depth - lradius;
        assert lradius >= rradius && (lradius+rradius)==depth;
        Map<Integer, ArrayList<ArrayList<Integer>>> semipath = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
        ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> currents = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> nexts = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> start = new ArrayList<Integer>();
        start.add(s);
        currents.add(start);
        int count = 0;
        int Lrad = lradius;
        while (lradius > 0){
            for(ArrayList<Integer> path: currents){
                int head = path.get(path.size()-1);
                count++;
                ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                for(Edge link: linkededgeArrayList){
                    if (link.getNode()==t){
                        ArrayList<Integer> path_gold = new ArrayList<Integer>(path);
                        path_gold.add(link.getSignedEdge());
                        path_gold.add(link.getNode());
                        paths.add(path_gold);
                    }else if (!path.contains(link.getNode())){
                        ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                        path_new.add(link.getSignedEdge());
                        path_new.add(link.getNode());
                        nexts.add(path_new);
                        if(path_new.size()==(2*Lrad+1)){
                            if(!semipath.containsKey(link.getNode())) {
                                semipath.put(link.getNode(),new ArrayList<ArrayList<Integer>>());
                            }
                            semipath.get(link.getNode()).add(path_new);
                        }
                    }
                }
            }
            currents = new ArrayList<ArrayList<Integer>>(nexts);
            nexts = new ArrayList<ArrayList<Integer>>();
            lradius--;
        }
        lradius = depth-rradius;
        currents = new ArrayList<ArrayList<Integer>>();
        nexts = new ArrayList<ArrayList<Integer>>();
        start = new ArrayList<Integer>();
        start.add(t);
        currents.add(start);
        while (rradius > 0){
            for(ArrayList<Integer> path: currents){
                int head = path.get(path.size()-1);
                if(count%100000 == 0){
                    System.out.println("count:"+count);
                }
                count++;
                ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                for(Edge link: linkededgeArrayList){
                    if (link.getNode()!=s&&!path.contains((link.getNode()))){
                        if(semipath.containsKey(link.getNode())){
                            ArrayList<Integer> path_left;  //  left path
                            ArrayList<Integer> path_right; // right path
                            for(ArrayList<Integer> semip:semipath.get(link.getNode())){
                                path_left = new ArrayList<Integer>(semip);
                                path_right = new ArrayList<Integer>(path);
                                path_right.add(link.getSignedEdge());
                                Collections.reverse(path_right);
                                for(int i=0;i<path_right.size();i++){
                                    if(i%2==0){
                                        path_right.set(i,-path_right.get(i));
                                    }
                                }
                                if(path_left.get(path_left.size()-3)!=(int)path_right.get(1)){
                                    //Integer can not compare use ==
//                                        if(path_right.size()==4){
//                                            System.out.println("left:" + path_left.get(path_left.size()-3));
//                                            System.out.println("right:" + path_right.get(1));
//                                        }
                                    path_left.addAll(path_right);
                                    paths.add(path_left);
                                }
                            }
                        }
                        ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                        path_new.add(link.getSignedEdge());
                        path_new.add(link.getNode());
                        nexts.add(path_new);
                    }
                }
            }
            currents = new ArrayList<ArrayList<Integer>>(nexts);
            nexts = new ArrayList<ArrayList<Integer>>();
            rradius--;
        }
        return paths;
    }

    public ArrayList<ArrayList<Integer>> getNodeByRp(int source, ArrayList<Integer> rp){
//        System.out.println("relation path:"+rp);
        int depth =  0;
        assert this.KeySet.contains(source);
        ArrayList<ArrayList<Integer>> currents = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> nexts = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> start = new ArrayList<Integer>();
        start.add(source);
        currents.add(start);
        while (depth < rp.size()){
//            System.out.println("depth:"+depth);
            for(ArrayList<Integer> path: currents){
                int head = path.get(path.size()-1);
                ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                for(Edge link: linkededgeArrayList){
                    if(link.getSignedEdge() == rp.get(depth) && !path.contains(link.getNode())){
//                        System.out.println("link:"+link.getSignedEdge());
//                        System.out.println("path:"+path);
                        ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                        path_new.add(link.getSignedEdge());
                        path_new.add(link.getNode());
                        nexts.add(path_new);
//                        System.out.println("path_new:"+path_new);
                    }
                }
            }
            currents = new ArrayList<ArrayList<Integer>>(nexts);
            nexts = new ArrayList<ArrayList<Integer>>();
            depth++;
        }
        return currents;
    }

    public HashMap<TwoLink,Long> getTwoLinkMap(){
        HashMap<TwoLink,Long> twoLinkMap = new HashMap<TwoLink, Long>();
        int count = 0;
        int count_always = 0;
//        Random rand = new Random();
        System.out.println("Graph.keySet.size:"+Graph.keySet().size());
        for(int node:Graph.keySet()){
            count_always++;
//            if(rand.nextInt(5)!=0){
//                continue;
//            }
            int depth = 0;
            if(count_always%10000==0){
                Main.timestamp("count:"+count+" always:"+count_always);
            }


            ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> currents = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> nexts = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> start = new ArrayList<Integer>();
            start.add(node);
            currents.add(start);
            while (depth < 2){
                for(ArrayList<Integer> path: currents){
                    int head = path.get(path.size()-1);
                    ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                    for(Edge link: linkededgeArrayList){
                        if (link.getNode()==node){
                            ArrayList<Integer> path_gold = new ArrayList<Integer>(path);
                            path_gold.add(link.getSignedEdge());
                            path_gold.add(link.getNode());
                            TwoLink tl = new TwoLink(path_gold);
//                            if(path_gold.size()>2){
//                                System.out.println("path too long:"+tl);
//                            }
                            if(!twoLinkMap.containsKey(tl)){
                                twoLinkMap.put(tl,0L);
                            }
                            twoLinkMap.put(tl,twoLinkMap.get(tl)+1);
                        }else if (!path.contains(link.getNode())){
                            ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                            path_new.add(link.getSignedEdge());
                            path_new.add(link.getNode());
                            nexts.add(path_new);
//                            if(node == 16){
//                                System.out.println(path_new);
//                            }
                            TwoLink tl = new TwoLink(path_new);
//                            if(path_new.size()>2){
//                                System.out.println("path too long:"+tl);
//                            }
                            if(!twoLinkMap.containsKey(tl)){
                                twoLinkMap.put(tl,0L);
                            }
                            twoLinkMap.put(tl,twoLinkMap.get(tl)+1);
//                            if(tl.first == 4 && tl.second == 2){
//                                System.out.println("node: "+node);
//                            }
                        }
                    }
                }
                currents = new ArrayList<ArrayList<Integer>>(nexts);
                nexts = new ArrayList<ArrayList<Integer>>();
                depth++;
            }
//            if(count==20000){
//                break;
//            }
            count++;
        }
        return twoLinkMap;
    }

    public HashMap<Integer, LinkedEdges> getGraph() {
        return Graph;
    }
    public HashMap<TriLink,Integer> getTriLinkMap(){
        HashMap<TriLink,Integer> triLinkMap = new HashMap<TriLink, Integer>();
        int count = 0;
        System.out.println("Graph.keySet.size:"+Graph.keySet().size());
        for(int node:Graph.keySet()){
            int depth = 0;
            if(count%1000==0){
                Main.timestamp("count:"+count);
            }
            ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> currents = new ArrayList<ArrayList<Integer>>();
            ArrayList<ArrayList<Integer>> nexts = new ArrayList<ArrayList<Integer>>();
            ArrayList<Integer> start = new ArrayList<Integer>();
            start.add(node);
            currents.add(start);
            while (depth < 3){
                for(ArrayList<Integer> path: currents){
                    int head = path.get(path.size()-1);
                    ArrayList<Edge> linkededgeArrayList = this.Graph.get(head).getLinkedges();
                    for(Edge link: linkededgeArrayList){
                        if (link.getNode()==node&&path.size()==5){
                            ArrayList<Integer> path_gold = new ArrayList<Integer>(path);
                            path_gold.add(link.getSignedEdge());
                            path_gold.add(link.getNode());
                            TriLink tl = new TriLink(path_gold);
                            if(!triLinkMap.containsKey(tl)){
                                triLinkMap.put(tl,0);
                            }
                            triLinkMap.put(tl,triLinkMap.get(tl)+1);
                        }else if (!path.contains(link.getNode())){
                            ArrayList<Integer> path_new = new ArrayList<Integer>(path);
                            path_new.add(link.getSignedEdge());
                            path_new.add(link.getNode());
                            nexts.add(path_new);
                            if(path_new.size()==7){
                                TriLink tl = new TriLink(path_new);
                                if(!triLinkMap.containsKey(tl)){
                                    triLinkMap.put(tl,0);
                                }
                                triLinkMap.put(tl,triLinkMap.get(tl)+1);
                            }
                        }
                    }
                }
                currents = new ArrayList<ArrayList<Integer>>(nexts);
                nexts = new ArrayList<ArrayList<Integer>>();
                depth++;
            }
//            if(count == 10000){
//                return triLinkMap;
//            }
            count++;
        }
        return triLinkMap;
    }
}
