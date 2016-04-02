package jovascript.parser;

import jovascript.parser.AST;
import scala.collection.JavaConversions$;
import scala.collection.Seq;
import scala.collection.Seq$;

public class JovascriptASTVisitor extends JovascriptBaseVisitor<AST> {
    @Override
    public AST visitProgram(JovascriptParser.ProgramContext ctx) {
        return new ProgramNode(Seq$.MODULE$.empty());
    }
}
