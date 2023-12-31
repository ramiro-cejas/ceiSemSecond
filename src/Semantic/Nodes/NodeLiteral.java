package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SemanticException;
import SecondSemantic.Semantic.SymbolTable;

public class NodeLiteral implements Node{

    public Token literal;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    private Token type;

    public NodeLiteral(Token literal, NodeBlock parentBlock) {
        this.literal = literal;
        this.parentBlock = parentBlock;
    }

    @Override
    public void check(SymbolTable symbolTable) throws SemanticException {
        if (alreadyChecked)
            return;
        if (literal.getName().equals("keyword_null"))
            type = new Token("keyword_null", "null", literal.getRow());
        else if (literal.getName().equals("keyword_true") || literal.getName().equals("keyword_false"))
            type = new Token("keyword_boolean", "boolean", literal.getRow());
        else if (literal.getName().equals("intLiteral"))
            type = new Token("keyword_int", "int", literal.getRow());
        else if (literal.getName().equals("floatLiteral"))
            type = new Token("keyword_float", "float", literal.getRow());
        else if (literal.getName().equals("strLiteral"))
            type = new Token("idClass", "String", literal.getRow());
        else
            type = new Token("keyword_char", "char", literal.getRow());
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

    @Override
    public Token getToken() {
        return literal;
    }
}
