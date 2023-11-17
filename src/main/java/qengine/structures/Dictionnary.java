package qengine.structures;
import java.util.HashMap;
import java.util.Map;


public class Dictionnary {

	private Map<String, Long> dictionnary = new HashMap<String, Long>();
    private Map<Long,String > dicoInv = new HashMap<Long, String>();

    private Long entries = 0L;

    public Long get(String s){
        return dictionnary.get(s);
    }
    public String get(Long l){
        return dicoInv.get(l);
    }

    public void put(String s){
        if(!dictionnary.containsKey(s)) {
            dictionnary.put(s, entries);
        }
        if(!dicoInv.containsKey(entries)){
            dicoInv.put(entries, s);
        }
        entries++;
    }

    public Long size(){
        return entries;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, Long> entry : dictionnary.entrySet()){
            sb.append(entry.getKey()).append(" <-> ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
