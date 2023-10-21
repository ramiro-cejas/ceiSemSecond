package SecondSemantic.Semantic;

public class SemExceptionHandler {
    SymbolTable symbolTable;
    public SemExceptionHandler(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    public void show(SemanticException e) {
        symbolTable.errors.add(e);
    }
}
