public enum OperationMode {
    Write("WRITE",1),
    Read("READ", 0);
    private final String _value;
    private final int _mark;

    public String getValue() { return _value; }
    public int getMark() {return _mark; }

    private  OperationMode(String value, int mark) {
        _value = value;
        _mark = mark;
    }
    public static OperationMode getMode(String value) {
        if (value.equalsIgnoreCase(Write.getValue()))
            return Write;
        else if (value.equalsIgnoreCase(Read.getValue()))
            return Read;
         else
            return null;

    }
}
