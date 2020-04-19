package minimizator;

import expression.Expression;
import expression.Variable;
import operations.binary.AbstractBinaryOperator;
import operations.binary.And;
import operations.binary.Implication;
import operations.binary.Or;
import operations.unary.AbstractUnaryOperator;
import operations.unary.Negate;
import parser.Statement;
import parser.StatementException;
import proof.Axiom;
import proof.Hypothesis;
import proof.ModusPonens;
import proof.ProofLine;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

public class Minimizator {
    private static final Variable A = new Variable("A");
    private static final Variable B = new Variable("B");
    private static final Variable C = new Variable("C");

    private static Implication impl(Expression a, Expression b) {
        return new Implication(a, b);
    }

    private static And and(Expression a, Expression b) {
        return new And(a, b);
    }

    private static Or or(Expression a, Expression b) {
        return new Or(a, b);
    }

    private static Negate neg(Expression a) {
        return new Negate(a);
    }

    private static final List<Expression> AXIOMS = new ArrayList<>(Arrays.asList(
            impl(A, impl(B, A)),
            impl(impl(A, B), impl(impl(A, impl(B, C)), impl(A, C))),
            impl(A, impl(B, and(A, B))),
            impl(and(A, B), A),
            impl(and(A, B), B),
            impl(A, or(A, B)),
            impl(B, or(A, B)),
            impl(impl(A, C), impl(impl(B, C), impl(or(A, B), C))),
            impl(impl(A, B), impl(impl(A, neg(B)), neg(A))),
            impl(neg(neg(A)), A)
    ));


    public static List<ProofLine> minimize(Map<Expression, Integer> hypotheses, List<Expression> proof) {
        Map<Expression, ModusPonens> modusPonens = new HashMap<>();
        List<ProofLine> result = new ArrayList<>();
        Map<Expression, Integer> proved = new HashMap<>();
        Map<Expression, List<Line>> l2r = new HashMap<>();

        for (int i = 0; i < proof.size(); i++) {
            Expression expr = proof.get(i);

            int axiom = isAxiom(expr);
            Integer hyp = hypotheses.get(expr);
            ModusPonens mp = modusPonens.get(expr);

            ProofLine line = null;
            if (axiom != -1) {
                line = new Axiom(expr, axiom);
            } else if (hyp != null) {
                line = new Hypothesis(expr, hyp);
            } else if (mp != null) {
                line = new proof.ModusPonens(expr, mp.getFirst(), mp.getSecond());
            } else {
                throw new StatementException("Proof is incorrect");
//                result.add(null);
//                continue;
            }

            result.add(line);

            List<Line> r = l2r.get(expr);
            if (r != null) {
                for (Line x : r) {
                    modusPonens.put(x.getExpression(), new ModusPonens(i, x.getIndex()));
                }
                l2r.remove(expr);
            }

            if (!proved.containsKey(expr)) {
                proved.put(expr, i);
            }

            if (expr instanceof Implication) {
                Implication impl = (Implication) expr;
                Integer index = proved.get(impl.getLeft());
                if (index == null) {
                    List<Line> rList = l2r.get(impl.getLeft());
                    Line lineMP = new Line(impl.getRight(), i);
                    if (rList != null) {
                        rList.add(lineMP);
                    } else {
                        List<Line> newList = new ArrayList<>();
                        newList.add(lineMP);
                        l2r.put(impl.getLeft(), newList);
                    }
                } else {
                    modusPonens.put(impl.getRight(), new ModusPonens(index, i));
                }
            }
        }
        return result;
    }

    private static int isAxiom(Expression expr) {
        final Map<Variable, Expression> vars = new HashMap<>();
        for (int i = 0; i < AXIOMS.size(); i++) {
            vars.clear();
            if (checkAxiom(expr, AXIOMS.get(i), vars)) {
                return i + 1;
            }
        }
        return -1;
    }

    private static boolean checkAxiom(Expression expr, Expression axiom, Map<Variable, Expression> vars) {
        if (axiom instanceof Variable) {
            Expression variable = vars.get(axiom);
            if (variable == null) {
                vars.put((Variable) axiom, expr);
                return true;
            } else {
//                System.err.println(axiom + "    " + vars.get(axiom) + "    " + expr);
                return variable.equals(expr);
            }
        }
        if (expr.getClass() != axiom.getClass()) {
//            System.err.println(expr + "    " + axiom);
            return false;
        }
        if (expr instanceof AbstractBinaryOperator) {
            AbstractBinaryOperator binOp = (AbstractBinaryOperator) expr;
            AbstractBinaryOperator axiomOp = (AbstractBinaryOperator) axiom;
            return checkAxiom(binOp.getLeft(), axiomOp.getLeft(), vars)
                    && checkAxiom(binOp.getRight(), axiomOp.getRight(), vars);
        }
        if (expr instanceof AbstractUnaryOperator) {
            return checkAxiom(((AbstractUnaryOperator) expr).getArg(), ((AbstractUnaryOperator) axiom).getArg(), vars);
        }
        return false;
    }

    private static class ModusPonens {
        private final int first;
        private final int second;

        ModusPonens(int first, int second) {
            this.first = first;
            this.second = second;
        }

        public int getFirst() {
            return first;
        }

        public int getSecond() {
            return second;
        }
    }

    private static class Line {
        private final Expression expression;
        private final int index;

        public Line(Expression expression, int index) {
            this.expression = expression;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public Expression getExpression() {
            return expression;
        }
    }
}
