package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SymbolTable;

public class EmptyNode implements Node{
    public Token semicolon;

    public Token type = new Token("keyword_void", "void", -1);

    public EmptyNode(Token semicolon) {
        this.semicolon = semicolon;
    }

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

    @Override
    public Token getToken() {
        return semicolon;
    }
}
