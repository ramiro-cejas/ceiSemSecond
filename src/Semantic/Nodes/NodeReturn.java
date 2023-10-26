package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.ConcreteClass;
import SecondSemantic.Semantic.ConcreteMethod;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeReturn implements Node{

    public Node expression;
    private Token type;
    private boolean alreadyChecked = false;
    private NodeBlock parentBlock;

    public NodeReturn(Node expression) {
        this.expression = expression;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked){
            return;
        }
        //if im in the constructor method then i dont need to check for return
        if (parentBlock.currentMethod == parentBlock.currentClass.constructor){
            alreadyChecked = true;
            return;
        }
        if (expression != null){
            expression.check(symbolTable);
            type = expression.getType();
        }
        else{
            type = new Token("keyword_void", "void", -1);
        }
        if (!type.getLexeme().equals("null")) {
            if (type.getName().equals("idClass")) {
                // type is subtype of the return type declared in the method
                ConcreteClass classOfReturn = symbolTable.classes.get(type.getLexeme());
                if (classOfReturn == null) {
                    classOfReturn = symbolTable.interfaces.get(type.getLexeme());
                }
                if (classOfReturn == null) {
                    symbolTable.semExceptionHandler.show(new SemanticException(type, "Class or interface " + type.getLexeme() + " not found"));
                }

                //now get the type of return declared in the method
                ConcreteClass classOfMethod = symbolTable.classes.get(parentBlock.currentMethod.type.getLexeme());
                if (classOfMethod == null) {
                    classOfMethod = symbolTable.interfaces.get(parentBlock.currentMethod.type.getLexeme());
                }
                if (classOfMethod == null) {
                    symbolTable.semExceptionHandler.show(new SemanticException(parentBlock.currentMethod.type, "Class or interface " + parentBlock.currentMethod.type.getLexeme() + " not found"));
                }

                //now we should have the concretes class, now check if the return type is a subtype of the method type
                if (!classOfReturn.isSubTypeOf(classOfMethod)) {
                    symbolTable.semExceptionHandler.show(new SemanticException(type, "Class " + type.getLexeme() + " is not a subtype of " + parentBlock.currentMethod.type.getLexeme() + " in line " + type.getRow()));
                }

            } else if (!type.getLexeme().equals(parentBlock.currentMethod.type.getLexeme())) {
                symbolTable.semExceptionHandler.show(new SemanticException(parentBlock.currentMethod.type, "No return found with type " + parentBlock.currentMethod.type.getLexeme() + " in line " + parentBlock.currentMethod.type.getRow()));
            }
        }
        alreadyChecked = true;
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
