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
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static HashMap<Expression, Integer> getHypotheses(String hypStr, List<Expression> collect) {
        HashMap<Expression, Integer> result = new HashMap<>();
        String[] splitted = hypStr.split(",");
        if (splitted.length == 1 && splitted[0].isEmpty()) {
            return result;
        }
        for (int i = 0; i < splitted.length; i++) {
            Expression statement = Statement.parse(splitted[i]);
            result.put(statement, i + 1);
            collect.add(statement);
        }
        return result;
    }

    private static boolean collect(int index, List<ProofLine> proofLines, boolean[] result, Expression total) {
        ProofLine line = proofLines.get(index);
        if (line == null) {
            throw new StatementException("Proof is incorrect");
        }
        boolean isTotal1 = false;
        boolean isTotal2 = false;
        boolean isTotal = proofLines.get(index).getExpression().equals(total);
        if (isTotal) {
            Arrays.fill(result, false);
        }

        result[index] = true;
        if (line instanceof ModusPonens) {
            ModusPonens mp = (ModusPonens) line;
            int ind1 = mp.getLine1();
            int ind2 = mp.getLine2();

            isTotal1 = collect(ind1, proofLines, result, total);

            if (!isTotal1) {
                isTotal2 = collect(ind2, proofLines, result, total);
            }
        }
        return isTotal1 || isTotal2 || isTotal;
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
            HashMap<Expression, Integer> hypotheses = getHypotheses(statement.substring(0, deducibly), hypothesesList);
            List<Expression> proof = new ArrayList<>();
            while ((statement = scanner.readLine()) != null) {
                proof.add(Statement.parse(statement));
            }

            if (!proof.get(proof.size() - 1).equals(total)) {
                throw new StatementException("Proof is incorrect");
            }

            List<ProofLine> proofLines = Minimizator.minimize(hypotheses, proof);
            int start = proof.size() - 1;

            boolean[] choose = new boolean[proof.size()];
            Arrays.fill(choose, false);
            collect(start, proofLines, choose, total);

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
