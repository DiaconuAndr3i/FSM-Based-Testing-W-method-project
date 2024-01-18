import org.wmethod.GenerateIncreasingId;
import org.wmethod.Pair;
import org.wmethod.State;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TransferErrorAndOperationErrorDesign {
    private static final List<State<Character, Integer>> states = new ArrayList<>();
    private static final Integer numberOfStates = 5;
    private static final  List<Integer> ids = new ArrayList<>();
    static {
        for(int i = 0; i < numberOfStates; i++){
            ids.add(GenerateIncreasingId.getId());
        }
        GenerateIncreasingId.resetId();


        // State 1
        Map<Character, Pair<Integer, Integer>> nextStates1 = new LinkedHashMap<>();
        nextStates1.put('a', new Pair<>(ids.get(0), null));
        nextStates1.put('b', new Pair<>(ids.get(3), null));
        Map<Character, Integer> outputsForCurrentState1 = new LinkedHashMap<>();
        outputsForCurrentState1.put('a', 0);
        outputsForCurrentState1.put('b', 1);
        State<Character, Integer> state1 = State.<Character, Integer> builder()
                .idCurrentState(ids.get(0))
                .nameCurrentState("q1")
                .nextStates(nextStates1)
                .outputs(outputsForCurrentState1)
                .build();

        // State 2
        Map<Character, Pair<Integer, Integer>> nextStates2 = new LinkedHashMap<>();
        // Transfer error for q2 state from q1 to q3
        nextStates2.put('a', new Pair<>(ids.get(2), null));
        nextStates2.put('b', new Pair<>(ids.get(4), null));
        Map<Character, Integer> outputsForCurrentState2 = new LinkedHashMap<>();
        outputsForCurrentState2.put('a', 0);
        outputsForCurrentState2.put('b', 1);
        State<Character, Integer> state2 = State.<Character, Integer> builder()
                .idCurrentState(ids.get(1))
                .nameCurrentState("q2")
                .nextStates(nextStates2)
                .outputs(outputsForCurrentState2)
                .build();


        // State 3
        Map<Character, Pair<Integer, Integer>> nextStates3 = new LinkedHashMap<>();
        nextStates3.put('a', new Pair<>(ids.get(4), null));
        nextStates3.put('b', new Pair<>(ids.get(0), null));
        Map<Character, Integer> outputsForCurrentState3 = new LinkedHashMap<>();
        outputsForCurrentState3.put('a', 0);
        outputsForCurrentState3.put('b', 1);
        State<Character, Integer> state3 = State.<Character, Integer> builder()
                .idCurrentState(ids.get(2))
                .nameCurrentState("q3")
                .nextStates(nextStates3)
                .outputs(outputsForCurrentState3)
                .build();


        // State 4
        Map<Character, Pair<Integer, Integer>> nextStates4 = new LinkedHashMap<>();
        nextStates4.put('a', new Pair<>(ids.get(2), null));
        nextStates4.put('b', new Pair<>(ids.get(3), null));
        Map<Character, Integer> outputsForCurrentState4 = new LinkedHashMap<>();
        outputsForCurrentState4.put('a', 1);
        outputsForCurrentState4.put('b', 1);
        State<Character, Integer> state4 = State.<Character, Integer> builder()
                .idCurrentState(ids.get(3))
                .nameCurrentState("q4")
                .nextStates(nextStates4)
                .outputs(outputsForCurrentState4)
                .build();


        // State 5
        Map<Character, Pair<Integer, Integer>> nextStates5 = new LinkedHashMap<>();
        nextStates5.put('a', new Pair<>(ids.get(1), null));
        nextStates5.put('b', new Pair<>(ids.get(4), null));
        Map<Character, Integer> outputsForCurrentState5 = new LinkedHashMap<>();
        outputsForCurrentState5.put('a', 1);
        // Here we have for b output 0 instead of 1
        outputsForCurrentState5.put('b', 0);
        State<Character, Integer> state5 = State.<Character, Integer> builder()
                .idCurrentState(ids.get(4))
                .nameCurrentState("q5")
                .nextStates(nextStates5)
                .outputs(outputsForCurrentState5)
                .build();


        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);
        states.add(state5);
    }

    public static List<State<Character, Integer>> getStates(){
        return states;
    }
}
