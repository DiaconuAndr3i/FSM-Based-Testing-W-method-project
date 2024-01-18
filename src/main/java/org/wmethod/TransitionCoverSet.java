package org.wmethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransitionCoverSet<InputSymbol, OutputType> {
    // Set of states
    private List<State<InputSymbol, OutputType>> states;
    // Root tree without the possible cycles in its own state
    private List<State<InputSymbol, OutputType>> rootTree;
    private HashMap<Integer, Boolean> visitedArray;
    // P set
    private List<List<InputSymbol>> transitionCoverSet;

    private State<InputSymbol, OutputType> getStateById(Integer id){
        return states
                .stream()
                .filter(state -> Objects.equals(state.getIdCurrentState(), id))
                .findFirst()
                .orElse(new State<>());
    }

    // Simple recursive DFS for getting rootTree
    private void generateRootTree(State<InputSymbol, OutputType> state){
        visitedArray.put(state.getIdCurrentState(), Boolean.TRUE);

        rootTree.add(state);
        for (Map.Entry<InputSymbol, Pair<Integer, Integer>> entry : state.getNextStates().entrySet()) {
            if (!visitedArray.get(entry.getValue().getKey())) {
                generateRootTree(getStateById(entry.getValue().getKey()));
            }
        }
    }

    // Constructing P set
    public List<List<InputSymbol>> getP(){
        visitedArray = new HashMap<>();
        rootTree = new ArrayList<>();
        states.forEach(state -> visitedArray.put(state.getIdCurrentState(), Boolean.FALSE));
        generateRootTree(states.get(0));


        transitionCoverSet = new ArrayList<>();

        List<InputSymbol> list = new ArrayList<>();

        Map<Integer, Boolean> map = new HashMap<>();
        rootTree.forEach(state -> map.put(state.getIdCurrentState(), Boolean.FALSE));

        for (State<InputSymbol, OutputType> state: rootTree){
            map.put(state.getIdCurrentState(), Boolean.TRUE);
            InputSymbol symbolForUpdating = null;
            for (Map.Entry<InputSymbol, Pair<Integer, Integer>> entry : state.getNextStates().entrySet()) {
                if (!map.get(entry.getValue().getKey())) {
                    symbolForUpdating = entry.getKey();
                }
                List<InputSymbol> localList = new ArrayList<>(list);
                localList.add(entry.getKey());
                transitionCoverSet.add(localList);
            }
            list.add(symbolForUpdating);
        }

        transitionCoverSet.add(new ArrayList<>());

        return transitionCoverSet;
    }
}
