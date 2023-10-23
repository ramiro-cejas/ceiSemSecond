package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteAttribute;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SymbolTable;

public class NodeVarDeclaration implements Node{

    public Token type;
    public Token id;
    public Node expression;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;

    public NodeVarDeclaration(Token id, Node expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public void check(SymbolTable symbolTable) {
        if (alreadyChecked){
            return;
        }
        if (expression != null){
            expression.check(symbolTable);
            type = expression.getType();
            parentBlock.visibleVariables.add(new ConcreteAttribute(id, type, new Token("-", "-", -1)));
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        if (type == null){
            return new Token("void", "void", -1);
        }
        return type;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {
        this.parentBlock = nodeBlock;
    }

    public String toString(){
        return "NodeVarDeclaration: "+type.getLexeme()+" "+id.getLexeme();
    }
}
