package SecondSemantic.Semantic.Nodes;

import SecondSemantic.Lexical.Token;
import SecondSemantic.Semantic.SymbolTable;

public class NodeLiteral implements Node{

    public Token literal;
    public NodeBlock parentBlock;
    private boolean alreadyChecked = false;
    private Token type;

    public NodeLiteral(Token literal, NodeBlock parentBlock) {
        this.literal = literal;
        this.parentBlock = parentBlock;

        if (literal.getName().equals("keyword_null"))
            type = new Token("null", "null", literal.getRow());
        else if (literal.getName().equals("keyword_true") || literal.getName().equals("keyword_false"))
            type = new Token("boolean", "boolean", literal.getRow());
        else if (literal.getName().equals("integer_literal"))
            type = new Token("int", "int", literal.getRow());
        else if (literal.getName().equals("float_literal"))
            type = new Token("float", "float", literal.getRow());
        else if (literal.getName().equals("string_literal"))
            type = new Token("string", "string", literal.getRow());
        else
            type = new Token("char", "char", literal.getRow());
    }

    @Override
    public void check(SymbolTable symbolTable) {
        if (!alreadyChecked){
            if (literal.getName().equals("keyword_null"))
                type = new Token("null", "null", literal.getRow());
            else if (literal.getName().equals("keyword_true") || literal.getName().equals("keyword_false"))
                type = new Token("boolean", "boolean", literal.getRow());
            else if (literal.getName().equals("integer_literal"))
                type = new Token("int", "int", literal.getRow());
            else if (literal.getName().equals("float_literal"))
                type = new Token("float", "float", literal.getRow());
            else if (literal.getName().equals("string_literal"))
                type = new Token("string", "string", literal.getRow());
            else
                type = new Token("char", "char", literal.getRow());
        }
        alreadyChecked = true;
    }

    @Override
    public Token getType() {
        return null;
    }

    @Override
    public void setParentBlock(NodeBlock nodeBlock) {

    }
}
