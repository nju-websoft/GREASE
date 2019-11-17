package relsearch;

import java.util.HashMap;

public class TypePropMap {
    int typeId;
    int totalProp;
    HashMap<Integer,Integer> typePropCount;

    public TypePropMap(int type){
        typeId = type;
        totalProp = 0;
        typePropCount = new HashMap<Integer, Integer>();
    }

    public void addTypeProp(int prop){
        if(!typePropCount.containsKey(prop)){
            typePropCount.put(prop,0);
        }
        typePropCount.put(prop,typePropCount.get(prop)+1);
        totalProp++;
    }

    public int getTotalProp() {
        return totalProp;
    }

    public int getPropNum(int prop) {
        if(typePropCount.containsKey(prop)){
            return typePropCount.get(prop);
        }
        return 0;
    }

    public HashMap<Integer, Integer> getTypePropCount() {
        return typePropCount;
    }

    public void addTypePropCount(int prop,int count){
        typePropCount.put(prop,count);
        totalProp += count;
    }

}
