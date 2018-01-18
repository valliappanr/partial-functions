package partial.function;

import partial.function.lib.Flow;

public class State {
    private Flow flow;
    public State(Flow flow) {
        this.flow = flow;
    }
    public void executeBehaviour(Object v1) {
        flow.receive().apply(v1);
    }
}
