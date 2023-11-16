package qengine.structures;

public class Hexastore {
    private Index spo = new Index();
    private Index sop = new Index();
    private Index ops = new Index();
    private Index osp = new Index();
    private Index pos = new Index();
    private Index pso = new Index();

    public void put(Long s, Long p, Long o){
        spo.put(s, p, o);
        sop.put(s, o, p);
        ops.put(o, p, s);
        osp.put(o, s, p);
        pos.put(p, o, s);
        pso.put(p, s, o);
    }

    public Index getSpo() {
        return spo;
    }

    public Index getSop() {
        return sop;
    }

    public Index getOps() {
        return ops;
    }

    public Index getOsp() {
        return osp;
    }

    public Index getPos() {
        return pos;
    }

    public Index getPso() {
        return pso;
    }

    @Override
    public String toString() {
        return "Hexastore {" +
                "\nspo:\n" + spo +
                "\nsop:\n" + sop +
                "\nops:\n" + ops +
                "\nosp:\n" + osp +
                "\npos:\n" + pos +
                "\npso:\n" + pso +
                '}';
    }
}
