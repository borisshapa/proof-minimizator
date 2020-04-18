package parser;

import expression.Expression;
import expression.Variable;
import operations.binary.And;
import operations.binary.Implication;
import operations.binary.Or;
import operations.unary.Negate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;

public class Statement {

    public static Expression parse(final String source) {
        return parse(new StringSource(source));
    }

    public static Expression parse(StatementSource source) {
        return new StatementParser(source).parseStatement();
    }

    private static class StatementParser extends BaseParser {

        public StatementParser(StatementSource source) {
            super(source);
            nextChar();
        }

        public Expression parseStatement() {
            final Expression result = parseExpr();
            if (test('\0')) {
                return result;
            }
            throw error("End of statement expected");
        }

        private Expression parseExpr() {
            Expression result = parseDisj();
            if (test('-')) {
                expect('>');
                result = new Implication(result, parseExpr());
            }
            return result;
        }

        private Expression parseDisj() {
            Expression result = parseConj();
            while (test('|')) {
                result = new Or(result, parseConj());
            }
            return result;
        }

        private Expression parseConj() {
            Expression result = parseNeg();
            while(test('&')) {
                result = new And(result, parseNeg());
            }
            return result;
        }

        private Expression parseNeg() {
            Expression result;
            skipWhitespace();
            if (test('!')) {
                result = new Negate(parseNeg());
            } else if (test('(')) {
                result = parseExpr();
                expect(')');
            } else {
                result = parseVar();
            }
            skipWhitespace();
            return result;
        }

        private Variable parseVar() {
            StringBuilder result = new StringBuilder();
            if (!between('A', 'Z')) {
                throw error("Letter expected");
            }
            while (between('A', 'Z') || between('0', '9') || ch == '\'') {
                result.append(ch);
                nextChar();
            }
            return new Variable(result.toString());
        }

        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}
