package operations.binary;


import expression.Expression;

public class Or extends AbstractBinaryOperator {
    public Or(Expression arg1, Expression arg2) {
        super(arg1, arg2, "|");
    }
}
