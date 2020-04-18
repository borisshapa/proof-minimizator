package proof;

import expression.Expression;

import java.util.Map;

public class ModusPonens extends ProofLine {
    int line1, line2;

    public ModusPonens(Expression expression, int exp1, int exp2) {
        super(expression);
        this.line1 = exp1;
        this.line2 = exp2;
    }

    public int getLine1() {
        return line1;
    }

    public int getLine2() {
        return line2;
    }

    public String toString(int index) {
        return toString(index, String.format("M.P. %d, %d", line2, line1));
    }
}
