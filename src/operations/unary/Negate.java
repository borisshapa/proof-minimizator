package operations.unary;


import expression.Expression;

public class Negate extends AbstractUnaryOperator {
    public Negate(Expression arg) {
        super(arg, "!");
    }
}
