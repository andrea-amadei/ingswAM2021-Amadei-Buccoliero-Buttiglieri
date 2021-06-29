package it.polimi.ingsw.common.utils;

public class Triplet <F, S, T> extends Pair<F, S>{
    private final T third;

    public Triplet(F first, S second, T third){
        super(first, second);
        this.third = third;
    }

    public T getThird(){
        return third;
    }
}
