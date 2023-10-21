package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public interface Sentence {
    void check(SymbolTable ts, ConcreteClass currentClass, ConcreteMethod currentMethod) throws SemanticException;
}
