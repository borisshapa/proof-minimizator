import expression.Expression;
import minimizator.Minimizator;
import parser.Statement;
import parser.StatementException;
import proof.Axiom;
import proof.Hypothesis;
import proof.ModusPonens;
import proof.ProofLine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static HashMap<Expression, Integer> getHypotheses(String expr, int endOfHypotheses, List<Expression> collect) {
        HashMap<Expression, Integer> result = new HashMap<>();
        String[] splitted = expr.substring(0, endOfHypotheses).split(",");
        for (int i = 0; i < splitted.length; i++) {
            if (splitted[i].isEmpty()) {
                continue;
            }
            Expression statement = Statement.parse(splitted[i]);
            result.put(statement, i + 1);
            collect.add(statement);
        }
        return result;
    }

    private static void collect(int index, List<ProofLine> proofLines, boolean[] result) {
        ProofLine line = proofLines.get(index);
        if (line == null) {
            throw new StatementException("Proof is incorrect");
        }
        result[index] = true;
        if (line instanceof ModusPonens) {
            ModusPonens mp = (ModusPonens) line;
            int ind1 =mp.getLine1();
            int ind2 = mp.getLine2();

            if (!result[ind1]) {
                collect(mp.getLine1(), proofLines, result);
            }

            if (!result[ind2]) {
                collect(mp.getLine2(), proofLines, result);
            }
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
            String statement = scanner.readLine();

            int deducibly = statement.indexOf("|-");
            if (deducibly == -1) {
                throw new StatementException("|- expected");
            }
            Expression total = Statement.parse(statement.substring(deducibly + 2));

            List<Expression> hypothesesList = new ArrayList<>();
            HashMap<Expression, Integer> hypotheses = getHypotheses(statement, deducibly, hypothesesList);
            List<Expression> proof = new ArrayList<>();
            while ((statement = scanner.readLine()) != null) {
                proof.add(Statement.parse(statement));
            }

            List<ProofLine> proofLines = Minimizator.minimize(hypotheses, proof);
            int start = proof.lastIndexOf(total);

            boolean[] choose = new boolean[proof.size()];
            Arrays.fill(choose, false);
            collect(start, proofLines, choose);

            int index = 1;
            Map<ProofLine, Integer> indices = new HashMap<>();

            String firstLine = hypothesesList.stream()
                    .map(Objects::toString)
                    .collect(Collectors.joining(", "));
            if (!firstLine.isEmpty()) {
                firstLine += " ";
            }
            firstLine += "|- " + total.toString();
            System.out.println(firstLine);
            for (int i = 0; i < proof.size(); i++) {
                if (choose[i]) {
                    ProofLine line = proofLines.get(i);
                    if (line instanceof ModusPonens) {
                        ModusPonens mp = (ModusPonens) line;
                        int ind1 = indices.get(proofLines.get(mp.getLine1()));
                        int ind2 = indices.get(proofLines.get(mp.getLine2()));
                        System.out.println(new ModusPonens(proof.get(i), ind1, ind2).toString(index));
                    } else {
                        System.out.println(line.toString(index));
                    }
                    indices.put(line, index++);
                }
            }
        } catch (Exception e) {
            System.out.println("Proof is incorrect");
        }
    }
}
