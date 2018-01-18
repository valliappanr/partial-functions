package partial.function;

import partial.function.lib.AbstractFlow;
import partial.function.lib.ReceiveFlowBuilder;


import static partial.function.lib.UnitMatch.match;

public class SampleFlowImpl extends AbstractFlow {

    @Override
    public ReceiveFlow createReceiveFlow() {
        return ReceiveFlowBuilder.create().match(
                String.class, data -> {
                    System.out.println("data:" + data);
                }
        ).match(Order.class, order -> {
            System.out.println("order" + order);
        }).build();
    }
}