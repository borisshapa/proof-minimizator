package operations.unary;

import expression.Expression;

import java.util.Objects;

public class AbstractUnaryOperator implements Expression{
    private final Expression arg;
    String op;

    AbstractUnaryOperator(Expression arg, String op) {
        this.arg = arg;
        this.op = op;
    }

    @Override
    public String toString() {
        return op + arg.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractUnaryOperator)) {
            return false;
        }
        if (this == obj) {
            return false;
        }

        AbstractUnaryOperator unOp = (AbstractUnaryOperator) obj;
        return arg.equals(unOp.arg) && op.equals(unOp.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, arg);
    }

    public Expression getArg() {
        return arg;
    }
}
