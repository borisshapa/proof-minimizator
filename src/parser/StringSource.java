package parser;

public class StringSource implements StatementSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data + '\0';
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public StatementException error(final String message) {
        return new StatementException(pos + ": " + message);
    }
}
