package SecondSemantic.Semantic;

import SecondSemantic.Lexical.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ConcreteClass{
    public Token name;
    Token implementsName;
    Token extendsName;
    public HashMap<String, ConcreteMethod> methods;
    public HashMap<String, ConcreteAttribute> attributes;
    public ConcreteMethod currentMethod;
    SymbolTable symbolTable;
    public ConcreteMethod constructor;
    private boolean consolidated = false;
    public HashMap<String, ConcreteAttribute> hiddenAttributes = new HashMap<>();

    public ConcreteClass(Token token, SymbolTable symbolTable) {
        this.name = token;
        this.implementsName = new Token("", "-", -1);
        this.extendsName = new Token("", "Object", -1);
        methods = new HashMap<String, ConcreteMethod>();
        attributes = new HashMap<String, ConcreteAttribute>();
        this.symbolTable = symbolTable;
    }

    public void addConstructor(ConcreteMethod m) throws SemanticException {
        if (m.name.getLexeme().equals(name.getLexeme())){
            if (constructor == null){
                constructor = m;
            } else
                symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Constructor already defined in line "+ m.name.getRow()));
        } else
            symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Constructor name must be the same as the class name in line "+ m.name.getRow()));
    }

    public void addMethod(ConcreteMethod m) throws SemanticException {
        if (!methods.containsKey(m.name.getLexeme())) {
            methods.put(m.name.getLexeme(), m);
        } else {
            methods.remove(m.name.getLexeme());
            symbolTable.semExceptionHandler.show(new SemanticException(m.name, "Method " + m.name.getLexeme() + " already defined in line " + m.name.getRow()));
        }
    }

    public void addAttribute(ConcreteAttribute a) throws SemanticException {
        if (!attributes.containsKey(a.name.getLexeme()))
            if (a.type.getLexeme().equals("void")){
                symbolTable.semExceptionHandler.show(new SemanticException(a.name,"Attribute " + a.name.getLexeme() + " cannot be void in line "+ a.name.getRow()));
            }else {
                attributes.put(a.name.getLexeme(), a);
            }
        else{
            attributes.remove(a.name.getLexeme());
            symbolTable.semExceptionHandler.show(new SemanticException(a.name,"Attribute " + a.name.getLexeme() + " already defined in line "+ a.name.getRow()));
        }
    }

    public void check() throws SemanticException {

        if (!Objects.equals(extendsName.getLexeme(), "$") && !symbolTable.classes.containsKey(extendsName.getLexeme()) && !symbolTable.interfaces.containsKey(extendsName.getLexeme()))
            symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class extended " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));

        else if (!Objects.equals(implementsName.getLexeme(), "-") && !symbolTable.interfaces.containsKey(implementsName.getLexeme()))
            symbolTable.semExceptionHandler.show(new SemanticException(implementsName,"Interface implemented " + implementsName.getLexeme() + " not defined in line "+ implementsName.getRow()));

        else for (ConcreteMethod m : methods.values()){

            if (m.type.getName().equals("idClass")) {
                if (!symbolTable.classes.containsKey(m.type.getLexeme()) && !symbolTable.interfaces.containsKey(m.type.getLexeme())){
                    symbolTable.semExceptionHandler.show(new SemanticException(m.type, "Class or interface " + m.type.getLexeme() + " not defined in line " + m.type.getRow()));
                    break;
                }

            }

            m.check();
        }

        for (ConcreteAttribute a : attributes.values()){
            if (a.type.getName().equals("idClass")) {
                if (!symbolTable.classes.containsKey(a.type.getLexeme()) && !symbolTable.interfaces.containsKey(a.type.getLexeme()))
                    symbolTable.semExceptionHandler.show(new SemanticException(a.type,"Class or interface " + a.type.getLexeme() + " not defined in line "+ a.type.getRow()));
            }
        }
    }

    public void consolidate(ArrayList parentsList) throws SemanticException {
        //if im in the list, there is a cycle then symbolTable.semExceptionHandler.show(exception
        if (parentsList.contains(name.getLexeme()))
            symbolTable.semExceptionHandler.show(new SemanticException(name,"Cycle detected in class " + name.getLexeme() + " in line "+ name.getRow()));
        else {
            if (!consolidated){
            if (extendsName.getLexeme().equals("$")){}
            else if (extendsName.getLexeme().equals("Object")) inherit(extendsName.getLexeme());
            else {
                ConcreteClass parent = symbolTable.classes.get(extendsName.getLexeme());
                if (parent == null)
                    parent = symbolTable.interfaces.get(extendsName.getLexeme());
                if (parent == null)
                    symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class or interface " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));
                else {
                    parentsList.add(name.getLexeme());
                    parent.consolidate(parentsList);
                    inherit(extendsName.getLexeme());
                }
            }
            consolidated = true;
        }
        if (constructor == null){
            constructor = new ConcreteMethod(name, name, new Token("", "-", -1), symbolTable);
        }
        checkCorrectInheritance();}
    }

    private void checkCorrectInheritance() throws SemanticException {
        //check if all methods from the interfaces are implemented
        if (!implementsName.getLexeme().equals("-")){
            ConcreteClass parent = symbolTable.interfaces.get(implementsName.getLexeme());
            if (parent == null)
                symbolTable.semExceptionHandler.show(new SemanticException(implementsName,"Interface " + implementsName.getLexeme() + " not defined in line "+ implementsName.getRow()));
            else
                for (ConcreteMethod m : parent.methods.values()){
                    if (!methods.containsKey(m.name.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(m.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                    else{
                        //check if method is overriden, if the signature is the same, same type of return and same type and order of parameters then its ok
                        ConcreteMethod current = methods.get(m.name.getLexeme());
                        if (!current.type.getLexeme().equals(m.type.getLexeme()))
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        else if (current.parameters.size() != m.parameters.size())
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        //check if parameters are the same with the same order using parametersInOrder
                        else for (int i = 0; i < current.parametersInOrder.size(); i++){
                            if (!current.parametersInOrder.get(i).type.getLexeme().equals(m.parametersInOrder.get(i).type.getLexeme()))
                                symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " from interface " + implementsName.getLexeme() + " not implemented in line "+ m.name.getRow()));
                        }
                    }
                }
        }
    }

    private void inherit(String lexeme) throws SemanticException {
        ConcreteClass parent = symbolTable.classes.get(lexeme);
        if (parent == null)
            parent = symbolTable.interfaces.get(lexeme);
        if (parent == null)
            symbolTable.semExceptionHandler.show(new SemanticException(extendsName,"Class or interface " + extendsName.getLexeme() + " not defined in line "+ extendsName.getRow()));
        else{
            for (ConcreteAttribute a : parent.attributes.values()){
                if (!attributes.containsKey(a.name.getLexeme()))
                    attributes.put(a.name.getLexeme(), a);
                else{
                    //attributes overrides the parent's attributes but the parent's attributes are still there
                    hiddenAttributes.put(a.name.getLexeme(), a);
                }
            }
            for (ConcreteMethod m : parent.methods.values()){
                if (!methods.containsKey(m.name.getLexeme()))
                    methods.put(m.name.getLexeme(), m);
                else{
                    //check if method is overriden, if the signature is the same, same type of return and same type and order of parameters then its ok
                    ConcreteMethod current = methods.get(m.name.getLexeme());
                    //check if method has the same static modifier
                    if (!current.isStatic.getLexeme().equals(m.isStatic.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " need to be the same of the method declared in the parent "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (static or not)"));
                    if (!current.type.getLexeme().equals(m.type.getLexeme()))
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (return type)"));
                    else if (current.parameters.size() != m.parameters.size())
                        symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (number of parameters)"));
                    //check if parameters are the same with the same order using parametersInOrder
                    else for (int i = 0; i < current.parametersInOrder.size(); i++){
                        if (!current.parametersInOrder.get(i).type.getLexeme().equals(m.parametersInOrder.get(i).type.getLexeme()))
                            symbolTable.semExceptionHandler.show(new SemanticException(current.name,"Method " + m.name.getLexeme() + " already defined in the parent class "+ parent.name.getLexeme() + " in line "+ m.name.getRow() + " (type of parameters)"));
                    }
                }
            }
        }
    }

    public void setImplementsName(Token implementsName) {
        this.implementsName = implementsName;
    }

    public void setExtendsName(Token extendsName) {
        this.extendsName = extendsName;
    }

    public void checkNamesAndTypes() throws SemanticException {
        for (ConcreteMethod m : methods.values()){
            m.checkNamesAndTypes();
        }
    }
}