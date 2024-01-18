package org.wmethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestingSet<InputSymbol> {
    List<List<InputSymbol>> P;
    // Will be W if we assume that number of states in the correct design is the same with number of states in the given design
    List<List<InputSymbol>> Z;

    // Cartesian product between characterization set and transition cover set
    public List<List<InputSymbol>> getTestingSet(){
        List<List<InputSymbol>> testingSet = new ArrayList<>();
        for (List<InputSymbol> listInP: P){
            for (List<InputSymbol> listInZ: Z){
                List<InputSymbol> list = new ArrayList<>();
                list.addAll(listInP);
                list.addAll(listInZ);
                testingSet.add(list);
            }
        }
        return testingSet;
    }

}
