package operations.binary;


import expression.Expression;

import java.util.Objects;

public class AbstractBinaryOperator implements Expression {
    private Expression arg1, arg2;
    String op;

    public AbstractBinaryOperator(Expression arg1, Expression arg2, String op) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.op = op;
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", arg1.toString(), op, arg2.toString());
    }

    public Expression getLeft() {
        return arg1;
    }

    public Expression getRight() {
        return arg2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractBinaryOperator)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        AbstractBinaryOperator binOp = (AbstractBinaryOperator) obj;
        return arg1.equals(binOp.arg1) && arg2.equals(binOp.arg2) && op.equals(binOp.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arg1, op, arg2);
    }
}
