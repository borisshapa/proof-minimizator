package operations.binary;


import expression.Expression;

public class Implication extends AbstractBinaryOperator {
    public Implication(Expression arg1, Expression arg2) {
        super(arg1, arg2, "->");
    }
}
