package partial.function;

public class PartialFlowSample {
    public static void main(String[] args) {
        State state = new State(new SampleFlowImpl());
        state.executeBehaviour(new Order("01", "chocolate"));
        state.executeBehaviour("test");
    }
}
