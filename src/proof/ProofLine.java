package proof;

import expression.Expression;

public abstract class ProofLine {
    Expression expression;

    protected ProofLine(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    protected String toString(int index, String type) {
        return String.format("[%d. %s] %s", index, type, expression.toString());
    }

    public abstract String toString(int index);
}
