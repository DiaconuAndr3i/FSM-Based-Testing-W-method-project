package org.wmethod;

public class GenerateIncreasingId {

    private static Integer id = 0;
    public static Integer getId(){
        return ++id;
    }

    public static void resetId(){
        id = 0;
    }
}
