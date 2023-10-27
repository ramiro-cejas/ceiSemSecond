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
            if (type.getLexeme().equals("null"))
                symbolTable.semExceptionHandler.show(new SemanticException(id, "Cannot assign null to a variable"));

            if (type.getLexeme().equals("void"))
                symbolTable.semExceptionHandler.show(new SemanticException(id, "Cannot assign void to a variable"));

            //check if already exists a variable with the same name
            if (parentBlock.getVisible(id.getLexeme()) != null){
                throw new SemanticException(id,"Variable " + id.getLexeme() + " already declared");
            }
            System.out.println("Adding variable " + id.getLexeme() + " to the local variables of the method " + parentBlock.currentMethod.name.getLexeme());
            parentBlock.localVariables.add(new ConcreteAttribute(id, type, new Token("keyword_static", "static", -1)));
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

    @Override
    public Token getToken() {
        return id;
    }

    public String toString(){
        return "NodeVarDeclaration: "+type.getLexeme()+" "+id.getLexeme();
    }
}
