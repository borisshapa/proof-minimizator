package proof;

import expression.Expression;

public class Hypothesis extends ProofLine {
    int hypothesis;

    public Hypothesis(Expression expression, int hypothesis) {
        super(expression);
        this.hypothesis = hypothesis;
    }

    public String toString(int index) {
        return toString(index, "Hypothesis " + hypothesis);
    }
}
