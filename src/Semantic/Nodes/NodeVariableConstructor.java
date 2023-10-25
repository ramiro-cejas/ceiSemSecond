package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableConstructor extends NodeVariable{

    public NodeVariableConstructor(Token name, NodeBlock parentBlock, Token type) {
        super(name, parentBlock);
        this.type = type;
    }

    public void check(SymbolTable symbolTable){

    }
}
