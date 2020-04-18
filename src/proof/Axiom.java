package proof;

import expression.Expression;

public class Axiom extends ProofLine {
    int ax;

    public Axiom(Expression expression, int ax) {
        super(expression);
        this.ax = ax;
    }

    public String toString(int index) {
        return toString(index, "Ax. sch. " + ax);
    }
}
