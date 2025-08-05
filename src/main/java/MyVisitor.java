import java.util.HashMap;
import java.util.Map;

public class MyVisitor extends RuBaseVisitor<Object> {
    //Tabla de simbolos para que podamos almacenar las variables y sus respectivos valores
    Map<String, Object> variables = new HashMap<>();

    @Override
    public Object visitPrograma(RuParser.ProgramaContext ctx){
       return visit(ctx.bloque());
    }

    @Override
    public Object visitBloque(RuParser.BloqueContext ctx){
        Object resultado = null;
        for(RuParser.SentenciaContext sentencia:ctx.sentencia()){
            resultado = visit(sentencia);
        }
        return resultado;
    }

    @Override
    public Object visitSentencia(RuParser.SentenciaContext ctx){
        return visitChildren(ctx);
    }

    @Override
    public Object visitAsignacion(RuParser.AsignacionContext ctx){
        String nombreVar = ctx.ID().getText();
        Object valor = visit(ctx.expr());
        variables.put(nombreVar, valor); //var hola = 4
        return valor; // 4
    }


    @Override
    public Object visitSentencia_if(RuParser.Sentencia_ifContext ctx){
        Object condicion = visit(ctx.bloque_condicional(0).expr());
        if (isTrue(condicion)){
            return visit(ctx.bloque_condicional(0).bloque_de_sentencia());
        }
        //ELSE IF
        for(int i=1; i <ctx.bloque_condicional().size(); i++){
            Object elseIfcondicion = visit(ctx.bloque_condicional(i).expr());
            if(isTrue(elseIfcondicion)){
                return visit(ctx.bloque_condicional(i).bloque_de_sentencia());
            }
        }
        //ELSE
        if (ctx.bloque_de_sentencia() != null){
            return visit(ctx.bloque_de_sentencia());
        }
        //Si no se cumple ninguna condicion
        return null;
    }

    @Override
    public Object visitBloque_de_sentencia(RuParser.Bloque_de_sentenciaContext ctx){
        if(ctx.bloque() != null){
            return visit(ctx.bloque());
        }else if(ctx.sentencia() != null){
            return visit(ctx.sentencia());
        }
        return null;
    }

    @Override
    public Object visitSentencia_while(RuParser.Sentencia_whileContext ctx){
        Object resultado = null;
        while(isTrue(visit(ctx.expr()))){
            resultado = visit(ctx.bloque_de_sentencia());
        }
        return resultado;
    }

    @Override
    public Object visitLog(RuParser.LogContext ctx){
        Object valor = visit(ctx.expr());
        System.out.println("LOG " +   valor);
        return valor;
    }

    @Override
    public Object visitImprimir(RuParser.ImprimirContext ctx){
        Object valor = visit(ctx.expr());
        System.out.println(valor);
        return valor;
    }

    @Override
    public Object visitMenosUnarioExpr(RuParser.MenosUnarioExprContext ctx){
        return -toDouble(visit(ctx.expr()));
    }

    @Override
    public Object visitNotExpr(RuParser.NotExprContext ctx){
        return !isTrue(visit(ctx.expr()));
    }

    @Override
    public Object visitMultiplicacionExpr(RuParser.MultiplicacionExprContext ctx){
        double izquierda = toDouble(visit(ctx.expr(0)));
        double derecha = toDouble(visit(ctx.expr(1)));

        switch(ctx.op.getType()){
            case RuParser.MULT:
                return izquierda * derecha;
            case RuParser.DIV:
                if(derecha == 0){
                    throw new RuntimeException("No se puede dividir por 0");
                }
                return izquierda / derecha;
            case RuParser.MOD:
                return izquierda % derecha;
            default:
                throw new RuntimeException("Operacion no permitida");
        }
    }

    @Override
    public Object visitAtomExpr(RuParser.AtomExprContext ctx){
        return visit(ctx.atomo());
    }

    @Override
    public Object visitOrExpr(RuParser.OrExprContext ctx){
        return isTrue(visit(ctx.expr(0))) || isTrue(visit(ctx.expr(1)));
    }

    @Override
    public Object visitPowExpr(RuParser.PowExprContext ctx){
        double izquierda = toDouble(visit(ctx.expr(0)));
        double derecha = toDouble(visit(ctx.expr(1)));
        return Math.pow(izquierda, derecha);
    }

    @Override
    public Object visitIgualdadExpr(RuParser.IgualdadExprContext ctx){
        Object izquierdo = visit(ctx.expr(0));
        Object derecho = visit(ctx.expr(1));
        if(ctx.op.getType() == RuParser.IGUAL){
            return izquierdo.equals(derecho);
        }else{
            return !izquierdo.equals(derecho);
        }
    }

    @Override
    public Object visitRelacionalExpr(RuParser.RelacionalExprContext ctx){
        double izquierda = toDouble(visit(ctx.expr(0)));
        double derecha = toDouble(visit(ctx.expr(1)));
        switch (ctx.op.getType()){
            case RuParser.MAYORQ:
                return izquierda > derecha;
            case RuParser.MENORQ:
                return izquierda < derecha;

            case RuParser.MENIG:
                return izquierda <= derecha;
            case RuParser.MAYIG:
                return izquierda >= derecha;
            default:
                throw new RuntimeException("Operador relacional que no me sé, pa'");
        }
    }

    @Override
    public Object visitAditivaExpr(RuParser.AditivaExprContext ctx){
        Object izq  = visit(ctx.expr(0));
        Object der = visit(ctx.expr(1));
        if(ctx.op.getType() == RuParser.MAS){
            if(izq instanceof String || der instanceof String){
                return izq.toString() + der.toString();
            }
            return toDouble(izq) + toDouble(der);

        }else{
            return toDouble(izq) - toDouble(der);
        }
    }


    @Override
    public Object visitAndExpr(RuParser.AndExprContext ctx){
        return isTrue(visit(ctx.expr(0))) && isTrue(visit(ctx.expr(1)));
    }

    @Override
    public Object visitParExpr(RuParser.ParExprContext ctx){
        return visit(ctx.expr());
    }

    @Override
    public Object visitNumberAtom(RuParser.NumberAtomContext ctx){
        if (ctx.INT() != null){
            return Integer.parseInt(ctx.INT().getText());
        }else
            return Double.parseDouble(ctx.getText());
    }

    @Override
    public Object visitBooleanAtom(RuParser.BooleanAtomContext ctx){
        return Boolean.parseBoolean(ctx.getText());
    }

    @Override
    public Object visitIdAtom(RuParser.IdAtomContext ctx){
        String nombreVar = ctx.getText();
        if(variables.containsKey(nombreVar)){
            return variables.get(nombreVar);
        }else{
            throw new RuntimeException("Variable " + nombreVar + " no definida");
        }
    }

    @Override
    public Object visitStringAtom(RuParser.StringAtomContext ctx){
        String texto = ctx.getText();
        return texto.substring(1, texto.length()-1); //
    }

    @Override
    public Object visitNilAtom(RuParser.NilAtomContext ctx){
        return null;
    }


    private boolean isTrue(Object obj){
        if (obj == null){
            return false;
        }

        if (obj instanceof Boolean){
            return (Boolean)obj;
        }

        if (obj instanceof Number){
            return ((Number)obj).doubleValue() != 0;
        }

        if (obj instanceof String){
            return !((String)obj).isEmpty();
        }

        return true;
    }

    private double toDouble(Object obj){
        if(obj instanceof Number){
            return ((Number)obj).doubleValue();
        }

        if(obj instanceof String){
           try {
               return Double.parseDouble((String)obj);
           } catch (NumberFormatException e) {
               throw new RuntimeException("No se pudo convertir el String a número");
           }
        }
        throw new RuntimeException("No se pudo convertir el objeto a número");
    }

    public Map<String, Object> getVariables() {
        return variables;

    }
}

