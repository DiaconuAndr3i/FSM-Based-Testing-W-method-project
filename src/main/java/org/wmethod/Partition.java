package org.wmethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partition <InputSymbol, OutputType> {
    // List of states
    private List<State<InputSymbol, OutputType>> states;
    // States that are handled in order to get first table for constructing partitions
    private List<State<InputSymbol, OutputType>> rootPartitions;
    // Partitions list
    private List<List<State<InputSymbol, OutputType>>> p;
    private void createRootPartitions(){
        rootPartitions = new ArrayList<>();

        // Sorting ascending based on id state
        states.sort(Comparator.comparingInt((State::getIdCurrentState)));

        // Build table partition based on outputs
        AtomicInteger idPartition = new AtomicInteger(-1);
        StringBuilder partitionDelimiter = new StringBuilder();
        states.forEach(state -> {
            StringBuilder str = new StringBuilder();
            state.getOutputs().forEach((key, value) -> str.append(value.toString()));
            if(!partitionDelimiter.toString().equals(str.toString())){
                partitionDelimiter.setLength(0);
                partitionDelimiter.append(str);
                idPartition.set(GenerateIncreasingId.getId());
            }
            state.setPartitionId(idPartition.get());
            rootPartitions.add(state);
        });
        GenerateIncreasingId.resetId();
    }

    // Get state by id
    private State<InputSymbol, OutputType> getPartitionStateByState(Integer idState){
        return this.rootPartitions
                .stream()
                .filter(state -> Objects.equals(state.getIdCurrentState(), idState))
                .findFirst()
                .orElse(new State<>());
    }

    // Updating partition id for next states based on current state id
    private void updateNextStatesPartitionId(){
        this.rootPartitions.forEach(state ->
                state.getNextStates().forEach((symbol, pair) ->
                        pair.setValue(this.getPartitionStateByState(pair.getKey()).getPartitionId())));
    }

    public List<List<State<InputSymbol, OutputType>>> createPartitions(){
        this.createRootPartitions();
        p = new ArrayList<>();

        this.updateNextStatesPartitionId();

        AtomicInteger numberOfPartitionsBetweenTwoSteps = new AtomicInteger(-1);
        AtomicInteger idPartition = new AtomicInteger(-2);


        // Building partitions at every step
        while (numberOfPartitionsBetweenTwoSteps.get() != idPartition.get()) {
            List<State<InputSymbol, OutputType>> copy = new ArrayList<>();
            for (State<InputSymbol, OutputType> state : rootPartitions) {
                copy.add(new State<>(state));
            }
            p.add(copy);
            numberOfPartitionsBetweenTwoSteps.set(idPartition.get());

            StringBuilder partitionDelimiter = new StringBuilder();
            this.rootPartitions.forEach(state -> {
                StringBuilder str = new StringBuilder();

                state.getNextStates().forEach((symbol, pair) -> str.append(pair.getValue().toString()));

                if (!partitionDelimiter.toString().equals(str.toString())) {
                    partitionDelimiter.setLength(0);
                    partitionDelimiter.append(str);
                    idPartition.set(GenerateIncreasingId.getId());
                }
                state.setPartitionId(idPartition.get());
            });
            this.updateNextStatesPartitionId();
            GenerateIncreasingId.resetId();
        }

        return p;
    }
}
