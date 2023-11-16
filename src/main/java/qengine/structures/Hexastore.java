package qengine.structures;

public class Hexastore {
    private Index spx = new Index();
    private Index pox = new Index();
    private Index xos = new Index();
    private Index xpo = new Index();
    private Index osx = new Index();
    private Index xsp = new Index();

    public void put(Long s, Long p, Long o){
        spx.put(s, p, o);
        pox.put(p, o, s);
        xos.put(o, s, p);
        xpo.put(p, o, s);
        osx.put(o, s, p);
        xsp.put(s, p, o);
    }

    public Index getSpx() {
        return spx;
    }

    public Index getPox() {
        return pox;
    }

    public Index getXos() {
        return xos;
    }

    public Index getXpo() {
        return xpo;
    }

    public Index getOsx() {
        return osx;
    }

    public Index getXsp() {
        return xsp;
    }
}
