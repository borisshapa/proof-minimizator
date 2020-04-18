package operations.binary;


import expression.Expression;

public class And extends AbstractBinaryOperator {
    public And(Expression arg1, Expression arg2) {
        super(arg1, arg2, "&");
    }
}
