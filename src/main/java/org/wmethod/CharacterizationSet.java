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
public class CharacterizationSet<InputSymbol, OutputType> {
    private List<List<State<InputSymbol, OutputType>>> p;
    private List<State<InputSymbol, OutputType>> states;
    private Integer numberStates;
    private List<List<InputSymbol>> characterizationSet;
    private List<List<InputSymbol>> w;
    private List<InputSymbol> inputs;

    private void distinguishingSequenceForAllStates(){
        characterizationSet = new ArrayList<>();
        for(int i = 0; i < numberStates - 1; i++){
            for(int j = i + 1; j < numberStates; j++){
                AtomicInteger r = new AtomicInteger(-1);
                for(int k = p.size() - 1; k >= 0; k--){
                    // Trying to find index r regarding the last table where p_i and p_j are not in the same group
                   if(Objects.equals(p.get(k).get(i).getPartitionId(), p.get(k).get(j).getPartitionId())){
                       r.set(k);
                       break;
                   }
                }
                // if we're managing to find that r index, starting to build distinguishing sequence between current pair of states
                if(r.get() != -1){
                    List<InputSymbol> localItem = new ArrayList<>();
                    AtomicInteger nextStatePi = new AtomicInteger(-1);
                    AtomicInteger nextStatePj = new AtomicInteger(-1);
                    while(r.get() >= 0){
                        State<InputSymbol, OutputType> currentStatePi;
                        State<InputSymbol, OutputType> currentStatePj;
                        if (nextStatePj.get() != -1 && nextStatePj.get() != -1){
                             currentStatePi = p.get(r.get()).get(nextStatePi.get());
                             currentStatePj = p.get(r.get()).get(nextStatePj.get());
                        }
                        else {
                            currentStatePi = p.get(r.get()).get(i);
                            currentStatePj = p.get(r.get()).get(j);
                        }
                        InputSymbol symbol = null;
                        for(InputSymbol item: inputs){
                            Integer groupStatePi = currentStatePi.getNextStates().get(item).getValue();
                            Integer groupStatePj = currentStatePj.getNextStates().get(item).getValue();
                            if (!Objects.equals(groupStatePi, groupStatePj)){
                                symbol = item;
                                nextStatePi.set(currentStatePi.getNextStates().get(item).getKey() - 1);
                                nextStatePj.set(currentStatePj.getNextStates().get(item).getKey() - 1);
                                break;
                            }
                        }
                        localItem.add(symbol);
                        r.set(r.get()-1);
                    }
                    localItem.add(getDistinguishingSymbolBetweenTwoStates(nextStatePi.get(), nextStatePj.get()));
                    characterizationSet.add(localItem);
                }
                else {
                    characterizationSet.add(new ArrayList<>(List.of(Objects.requireNonNull(getDistinguishingSymbolBetweenTwoStates(i, j)))));
                }
            }
        }
    }


    // Identifying the input symbol that distinguish the two states
    private InputSymbol getDistinguishingSymbolBetweenTwoStates(Integer state1, Integer state2){
        for (InputSymbol item: inputs){
            OutputType outputStatePi = states.get(state1).getOutputs().get(item);
            OutputType outputStatePj = states.get(state2).getOutputs().get(item);
            if(!outputStatePi.equals(outputStatePj)){
                return item;
            }
        }
        return null;
    }


    // Obtaining the set W, eliminating duplicates
    public List<List<InputSymbol>> getCharacterizationSet(){
        this.distinguishingSequenceForAllStates();

        Map<String, List<InputSymbol>> dictionary = new HashMap<>();

        for(List<InputSymbol> listInputsSymbol: characterizationSet){
            StringBuilder str = new StringBuilder();
            listInputsSymbol.forEach(inputSymbol -> str.append(inputSymbol.toString()));
            if(!dictionary.containsKey(str.toString())){
                dictionary.put(str.toString(), listInputsSymbol);
            }
        }

        w = new ArrayList<>();

        dictionary.forEach((key, value) -> w.add(value));

        return w;
    }
}


