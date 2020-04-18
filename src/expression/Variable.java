package expression;

public class Variable implements Expression {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return var.equals(((Variable) obj).var);
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }
}
