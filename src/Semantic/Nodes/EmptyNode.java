package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SymbolTable;

public class EmptyNode implements Node{

    public EmptyNode() {}

    @Override
    public void check(SymbolTable symbolTable) {
        //do nothing
    }

    @Override
    public Token getType() {
        //do nothing
        return null;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        //do nothing
    }
}
