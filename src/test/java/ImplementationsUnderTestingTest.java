import org.junit.jupiter.params.provider.Arguments;
import org.wmethod.*;
import org.wmethod.CorrectDesign;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

public class ImplementationsUnderTestingTest {

    private static final List<List<Character>> testingSetClass;
    private static final List<State<Character, Integer>> correctDesign;
    private static final List<State<Character, Integer>> transferErrorDesign;
    private static final List<State<Character, Integer>> transferErrorAndOperationErrorDesign;
    private static final List<State<Character, Integer>> iutWithOneMoreState1Design;
    private static final List<State<Character, Integer>> iutWithOneMoreState2Design;

    static {
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
        List<List<Character>> Z = characterizationSet.getCharacterizationSet();

        TransitionCoverSet<Character, Integer> transitionCoverSet = TransitionCoverSet.<Character, Integer> builder()
                .states(states)
                .build();
        List<List<Character>> P = transitionCoverSet.getP();
        TestingSet<Character> testingSet = TestingSet.<Character> builder()
                .P(P)
                .Z(Z)
                .build();

        testingSetClass = testingSet.getTestingSet();
        correctDesign = states;
        transferErrorDesign = TransferErrorDesign.getStates();
        transferErrorAndOperationErrorDesign = TransferErrorAndOperationErrorDesign.getStates();
        iutWithOneMoreState1Design = IUTWithOneMoreState1.getStates();
        iutWithOneMoreState2Design = IUTWithOneMoreState2.getStates();
    }

    @ParameterizedTest
    @MethodSource("providedInputsForTesting")
    void testingTransferError(List<Character> test){
        StringBuilder strForCorrectDesign = getStrOutputForDesign(correctDesign, test);
        StringBuilder strForTransferErrorDesign = getStrOutputForDesign(transferErrorDesign, test);
        System.out.println("Output for correct design: " + strForCorrectDesign);
        System.out.println("Output for transfer error design: " + strForTransferErrorDesign);
        assertEquals(strForCorrectDesign.toString(), strForTransferErrorDesign.toString());
    }

    @ParameterizedTest
    @MethodSource("providedInputsForTesting")
    void testingTransferErrorAndOperationErrorDesign(List<Character> test){
        StringBuilder strForCorrectDesign = getStrOutputForDesign(correctDesign, test);
        StringBuilder strForTransferErrorAndOperationErrorDesign = getStrOutputForDesign(transferErrorAndOperationErrorDesign, test);
        System.out.println("Output for correct design: " + strForCorrectDesign);
        System.out.println("Output for transfer error and operation error design: " + strForTransferErrorAndOperationErrorDesign);
        assertEquals(strForCorrectDesign.toString(), strForTransferErrorAndOperationErrorDesign.toString());
    }

    @ParameterizedTest
    @MethodSource("providedInputsForTesting")
    void testingIUTWithOneMoreState1(List<Character> test){
        StringBuilder strForCorrectDesign = getStrOutputForDesign(correctDesign, test);
        StringBuilder strForIUTWithOneMoreState1 = getStrOutputForDesign(iutWithOneMoreState1Design, test);
        System.out.println("Output for correct design: " + strForCorrectDesign);
        System.out.println("Output for transfer error and operation error design: " + strForIUTWithOneMoreState1);
        assertEquals(strForCorrectDesign.toString(), strForIUTWithOneMoreState1.toString());
    }

    @ParameterizedTest
    @MethodSource("providedInputsForTesting")
    void testingIUTWithOneMoreState2(List<Character> test){
        StringBuilder strForCorrectDesign = getStrOutputForDesign(correctDesign, test);
        StringBuilder strForIUTWithOneMoreState2 = getStrOutputForDesign(iutWithOneMoreState2Design, test);
        System.out.println("Output for correct design: " + strForCorrectDesign);
        System.out.println("Output for transfer error and operation error design: " + strForIUTWithOneMoreState2);
        assertEquals(strForCorrectDesign.toString(), strForIUTWithOneMoreState2.toString());
    }

    private static Stream<Arguments> providedInputsForTesting(){
        return testingSetClass.stream().map(Arguments::of);
    }

    private StringBuilder getStrOutputForDesign(List<State<Character, Integer>> design, List<Character> test){
        StringBuilder strForDesign = new StringBuilder();
        State<Character, Integer> state = design.get(0);
        for(Character character: test){
            if (state.getOutputs().get(character) != null){
                strForDesign.append(state.getOutputs().get(character));
                state = getStateById(state.getNextStates().get(character).getKey(), design);
            }
        }
        return strForDesign;
    }

    private State<Character, Integer> getStateById(Integer id, List<State<Character, Integer>> states){
        return states
                .stream()
                .filter(state -> Objects.equals(state.getIdCurrentState(), id))
                .findFirst()
                .orElse(new State<>());
    }
}
