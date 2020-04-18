package parser;

public interface StatementSource {
    boolean hasNext();
    char next();
    StatementException error(final String message);
}
