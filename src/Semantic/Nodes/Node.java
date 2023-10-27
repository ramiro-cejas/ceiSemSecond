package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

//This interface will provide the methods that all the nodes in the AST trees will have
public interface Node {
    public void check(SymbolTable symbolTable) throws SemanticException;
    public Token getType();
    void setParentBlock(NodeBlock nodeBlock);
    public Token getToken();
}
