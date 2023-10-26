package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVariableThis extends NodeVariable{

    public ConcreteClass thisClass;
    public NodeBlock parentBlock;
    private Token type;

    public NodeVariableThis(Token thisTok, ConcreteClass thisClass, NodeBlock parentBlock) {
        super(thisTok, parentBlock);
        this.thisClass = thisClass;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        System.out.println("Checking this");
        type = thisClass.name;
        if (childChain != null){
            System.out.println("The partial type is: "+ type.getLexeme());
            childChain.check(symbolTable);
            type = childChain.getType();
        }
        System.out.println("Type of this: " + type.getName());
    }

    @Override
    public Token getType() {
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }
}
