package partial.function.lib;

import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import partial.function.lib.AbstractFlow.*;


public class ReceiveFlowBuilder {
    private PartialFunction<Object, BoxedUnit> statements = null;

    protected void addStatement(PartialFunction<Object, BoxedUnit> statement) {
        if (statements == null)
            statements = statement;
        else
            statements = statements.orElse(statement);
    }

    /**
     * Build a {@link scala.PartialFunction} from this builder. After this call
     * the builder will be reset.
     *
     * @return a PartialFunction for this builder.
     */
    public ReceiveFlow build() {
        PartialFunction<Object, BoxedUnit> empty = CaseStatement.empty();

        if (statements == null)
            return new AbstractFlow.ReceiveFlow(empty);
        else
            return new ReceiveFlow(statements.orElse(empty)); // FIXME why no new Receive(statements)?
    }

    /**
     * Return a new {@link ReceiveFlowBuilder} with no case statements. They can be
     * added later as the returned {@link ReceiveFlowBuilder} is a mutable object.
     *
     * @return a builder with no case statements
     */
    public static ReceiveFlowBuilder create() {
        return new ReceiveFlowBuilder();
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param type
     *          a type to match the argument against
     * @param apply
     *          an action to apply to the argument if the type matches
     * @return a builder with the case statement added
     */
    public <P> ReceiveFlowBuilder match(final Class<P> type, final FI.UnitApply<P> apply) {
        return matchUnchecked(type, apply);
    }

    /**
     * Add a new case statement to this builder without compile time type check.
     * Should normally not be used, but when matching on class with generic type
     * argument it can be useful, e.g. <code>List.class</code> and
     * <code>(List&lt;String&gt; list) -> {}</code>.
     *
     * @param type
     *          a type to match the argument against
     * @param apply
     *          an action to apply to the argument if the type matches
     * @return a builder with the case statement added
     */
    @SuppressWarnings("unchecked")
    public ReceiveFlowBuilder matchUnchecked(final Class<?> type, final FI.UnitApply<?> apply) {

        FI.Predicate predicate = new FI.Predicate() {
            @Override
            public boolean defined(Object o) {
                return type.isInstance(o);
            }
        };

        addStatement(new UnitCaseStatement<Object, Object>(predicate, (FI.UnitApply<Object>) apply));

        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param type
     *          a type to match the argument against
     * @param predicate
     *          a predicate that will be evaluated on the argument if the type
     *          matches
     * @param apply
     *          an action to apply to the argument if the type matches and the
     *          predicate returns true
     * @return a builder with the case statement added
     */
    public <P> ReceiveFlowBuilder match(final Class<P> type, final FI.TypedPredicate<P> predicate,
                                    final FI.UnitApply<P> apply) {
        return matchUnchecked(type, predicate, apply);
    }

    /**
     * Add a new case statement to this builder without compile time type check.
     * Should normally not be used, but when matching on class with generic type
     * argument it can be useful, e.g. <code>List.class</code> and
     * <code>(List&lt;String&gt; list) -> {}</code>.
     *
     * @param type
     *          a type to match the argument against
     * @param predicate
     *          a predicate that will be evaluated on the argument if the type
     *          matches
     * @param apply
     *          an action to apply to the argument if the type matches and the
     *          predicate returns true
     * @return a builder with the case statement added
     */
    @SuppressWarnings("unchecked")
    public <P> ReceiveFlowBuilder matchUnchecked(final Class<?> type, final FI.TypedPredicate<?> predicate,
                                             final FI.UnitApply<P> apply) {
        FI.Predicate fiPredicate = new FI.Predicate() {
            @Override
            public boolean defined(Object o) {
                if (!type.isInstance(o))
                    return false;
                else
                    return ((FI.TypedPredicate<Object>) predicate).defined(o);
            }
        };

        addStatement(new UnitCaseStatement<Object, Object>(fiPredicate, (FI.UnitApply<Object>) apply));

        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param object
     *          the object to compare equals with
     * @param apply
     *          an action to apply to the argument if the object compares equal
     * @return a builder with the case statement added
     */
    public <P> ReceiveFlowBuilder matchEquals(final P object, final FI.UnitApply<P> apply) {
        addStatement(new UnitCaseStatement<Object, P>(new FI.Predicate() {
            @Override
            public boolean defined(Object o) {
                return object.equals(o);
            }
        }, apply));
        return this;
    }

    /**
     * Add a new case statement to this builder.
     *
     * @param object
     *          the object to compare equals with
     * @param predicate
     *          a predicate that will be evaluated on the argument if the object
     *          compares equal
     * @param apply
     *          an action to apply to the argument if the object compares equal
     * @return a builder with the case statement added
     */
    public <P> ReceiveFlowBuilder matchEquals(final P object, final FI.TypedPredicate<P> predicate,
                                          final FI.UnitApply<P> apply) {
        addStatement(new UnitCaseStatement<Object, P>(new FI.Predicate() {
            @Override
            public boolean defined(Object o) {
                if (!object.equals(o))
                    return false;
                else {
                    @SuppressWarnings("unchecked")
                    P p = (P) o;
                    return predicate.defined(p);
                }
            }
        }, apply));
        return this;
    }

    /**
     * Add a new case statement to this builder, that matches any argument.
     *
     * @param apply
     *          an action to apply to the argument
     * @return a builder with the case statement added
     */
    public ReceiveFlowBuilder matchAny(final FI.UnitApply<Object> apply) {
        addStatement(new UnitCaseStatement<Object, Object>(new FI.Predicate() {
            @Override
            public boolean defined(Object o) {
                return true;
            }
        }, apply));
        return this;
    }
    static interface Predicate {
        /**
         * The predicate to evaluate.
         *
         * @param o  an instance that the predicate is evaluated on.
         * @return  the result of the predicate
         */
        public boolean defined(Object o);
    }

}
