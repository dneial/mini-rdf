package qengine.structures;
import java.util.HashMap;
import java.util.Map;


public class Dictionnary {

	private Map<String, Long> dictionnary = new HashMap<String, Long>();

    private Long entries = 0L;

    public Long get(String s){
        return dictionnary.get(s);
    }

    public void put(String s){
        if(!dictionnary.containsKey(s)) {
            dictionnary.put(s, entries);
            entries++;
        }
    }

    public Long size(){
        return entries;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, Long> entry : dictionnary.entrySet()){
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

}
