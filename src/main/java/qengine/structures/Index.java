package qengine.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Index {
    private Map<Long, Map<Long, ArrayList<Long>>> index = new HashMap<>();

    public ArrayList<Long> get(Long x, Long y){
        if(index.containsKey(x)){
            if(index.get(x).containsKey(y)){
                return index.get(x).get(y);
            }
        }
        return new ArrayList<>();
    }

    public void put(Long x, Long y, Long z){
        if(index.containsKey(x)){
            if(index.get(x).containsKey(y)){
                index.get(x).get(y).add(z);
            }else{
                ArrayList<Long> list = new ArrayList<>();
                list.add(z);
                index.get(x).put(y, list);
            }
        }else{
            ArrayList<Long> list = new ArrayList<>();
            list.add(z);
            Map<Long, ArrayList<Long>> map = new HashMap<>();
            map.put(y, list);
            index.put(x, map);
        }
    }

    public void putX(ArrayList<Long> x){

    }
}
