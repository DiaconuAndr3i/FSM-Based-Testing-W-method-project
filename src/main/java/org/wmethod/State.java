package org.wmethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class State <InputSymbol, OutputType> {
    // Id state
    private Integer idCurrentState;
    // Name state like 'q1' for example
    private String nameCurrentState;
    // Id for partition that is needed when starting partitioning process
    private Integer partitionId;
    //First item from pair is id for next state and second is id partition for that state
    private Map<InputSymbol, Pair<Integer, Integer>> nextStates;
    // Output for each input
    private Map<InputSymbol, OutputType> outputs;

    //Copy constructor for state for deeply copying

    public State(State<InputSymbol, OutputType> state){
        this.idCurrentState = state.getIdCurrentState();
        this.nameCurrentState = state.getNameCurrentState();
        this.partitionId = state.partitionId;

        this.nextStates = new HashMap<>();
        this.outputs = new HashMap<>();

        for (Map.Entry<InputSymbol, Pair<Integer, Integer>> entry : state.nextStates.entrySet()) {
            this.nextStates.put(entry.getKey(), new Pair<>(entry.getValue().getKey(), entry.getValue().getValue()));
        }

        this.outputs.putAll(state.outputs);
    }
}
