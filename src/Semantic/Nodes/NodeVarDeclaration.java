package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.*;

public class NodeVarDeclaration implements Node{

    public Token type;
    public Token id;
    public Node expression;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;

    public NodeVarDeclaration(Token id, Node expression, NodeBlock parentBlock) {
        this.id = id;
        this.expression = expression;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        if (expression != null){
            System.out.println("Checking expression of variable " + id.getLexeme() + " in class " + parentBlock.currentClass.name.getLexeme() + " in method " + parentBlock.currentMethod.name.getLexeme());
            expression.check(symbolTable);
            type = expression.getType();
            System.out.println("Expression type: " + type.getLexeme());
            parentBlock.localVariables.add(new ConcreteAttribute(id, type, new Token("-", "-", -1)));
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
