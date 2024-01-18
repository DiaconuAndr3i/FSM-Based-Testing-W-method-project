package org.wmethod;

import java.util.*;

public class Main {
    public static void main(String[] args) {
      List<State<Character, Integer>> states = CorrectDesign.getStates();


      Partition<Character, Integer> partitions = Partition.<Character, Integer> builder()
                .states(states)
                .build();

        List<List<State<Character, Integer>>> list = partitions.createPartitions();
        CharacterizationSet<Character, Integer> characterizationSet = CharacterizationSet.<Character, Integer> builder()
                .numberStates(5)
                .inputs(List.of('a', 'b'))
                .p(list)
                .states(states)
                .build();

        // Printing W set
        System.out.println("Printing W set");
        List<List<Character>> Z = characterizationSet.getCharacterizationSet();
        System.out.println(Z);

        TransitionCoverSet<Character, Integer> transitionCoverSet = TransitionCoverSet.<Character, Integer> builder()
                .states(states)
                .build();

        // Printing P set
        System.out.println("Printing P set");
        List<List<Character>> P = transitionCoverSet.getP();
        System.out.println(P);

        TestingSet<Character> testingSet = TestingSet.<Character> builder()
                .P(P)
                .Z(Z)
                .build();

        // Printing testing set
        System.out.println("Printing testing set");
        System.out.println(testingSet.getTestingSet());
    }
}