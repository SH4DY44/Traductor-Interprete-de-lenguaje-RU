import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class Main extends JFrame {
    private JTextArea codigoArea;
    private JTextArea resultadosArea;
    private JButton ejecutarBtn;
    private JButton limpiarBtn;
    private JButton guardarBtn;
    private JButton cargarBtn;

    public Main(){
        inicializarInterfaz();
    }

    public void inicializarInterfaz(){
        setTitle("Traductor de Lenguaje RU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Panel principal con div --> izq | der
        JSplitPane panelPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        //Panel izquierdo
        JPanel panelCodigo = new JPanel(new BorderLayout());
        panelCodigo.setBorder(BorderFactory.createTitledBorder("Codigo Ru"));

        codigoArea = new JTextArea(20, 40);
        codigoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        codigoArea.setText("Bienvenido a RU\n# Escribe el codigo aqui\n");
        JScrollPane scrollCodigo = new JScrollPane(codigoArea);
        panelCodigo.add(scrollCodigo, BorderLayout.CENTER);


        //Panel derecho
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));

        resultadosArea = new JTextArea(20, 40);
        resultadosArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        resultadosArea.setEditable(false);
        resultadosArea.setBackground(Color.BLACK);
        resultadosArea.setForeground((Color.GREEN));
        JScrollPane scrollResultados = new JScrollPane(resultadosArea);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);

        panelPrincipal.setLeftComponent(panelCodigo);
        panelPrincipal.setRightComponent(panelResultados);
        panelPrincipal.setDividerLocation(500);
        add(panelPrincipal, BorderLayout.CENTER);


        //Botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        ejecutarBtn = new JButton("Play");
        ejecutarBtn.setBackground(new Color(4, 125, 50));
        ejecutarBtn.setForeground(Color.WHITE);
        ejecutarBtn.addActionListener(new EjecutarListener());

        cargarBtn = new JButton("Cargar archivo");
        cargarBtn.addActionListener(new CargarListener());

        guardarBtn = new JButton("Guardar");
        guardarBtn.addActionListener(new GuardarListener());

        limpiarBtn = new JButton("Limpiar");
        limpiarBtn.addActionListener(e -> {
            codigoArea.setText("");
            resultadosArea.setText("");
        });

        panelBotones.add(ejecutarBtn);
        panelBotones.add(cargarBtn);
        panelBotones.add(guardarBtn);
        panelBotones.add(limpiarBtn);

        add(panelBotones, BorderLayout.SOUTH);
        //Configuras la ventana
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }

    private class EjecutarListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ejecutarCodigo();
        }
    }


    private class CargarListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            cargarArchivo();
        }
    }

    private class GuardarListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            guardarResultado();
        }
    }


    private void ejecutarCodigo(){
        String codigo  = codigoArea.getText();
        if(codigo.trim().isEmpty()){
            resultadosArea.setText("No hay codigo para ejecutar");
            return;
        }

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(baos));

            //CReamos el lexer y el parser
            CharStream input = CharStreams.fromString(codigo);
            RuLexer lexer = new RuLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            RuParser parser = new RuParser(tokens);

            //Vamosa hacer el manejo de errores
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener(){
               @Override
               public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                       int line, int charPositionInLine, String msg,
                                       RecognitionException e) {
                   throw new RuntimeException("Error de sintaxis en línea " + line +
                           ", posición " + charPositionInLine + ": " + msg);
               }
            });

            //Parseamos y ejecutamos
            ParseTree tree = parser.programa();
            MyVisitor visitor = new MyVisitor();
            visitor.visit(tree);

            //Restauramos
            System.setOut(originalOut);
            String salida = baos.toString();

            if(salida.isEmpty()){
                resultadosArea.setText("Codigo ejecutado correctamente");
            }else{
                resultadosArea.setText("Ejecucion exitosa\n\n" + salida);
            }

            //Mostramos variables
            StringBuilder variables = new StringBuilder("\n\n\nEstado de las variables\n");
            visitor.getVariables().forEach((k, v) ->
                    variables.append(k).append(" = ").append(v).append("\n"));

            if(visitor.getVariables().size()>0) {
                resultadosArea.append(variables.toString());
            }
        }catch (Exception ex){
            resultadosArea.setText("Error durante la ejecucion:\n" + ex.getMessage());
            ex.printStackTrace();

        }
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
            @Override
            public boolean accept(File f){
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".ru");
            }
            @Override
            public String getDescription(){
                return "Archivo Ru (*.ru)";
            }
        });

        int seleccion = fileChooser.showOpenDialog(this);
        if(seleccion == JFileChooser.APPROVE_OPTION){
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))){
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                codigoArea.setText(content.toString());
                resultadosArea.append("Archivo cargado: " + archivo.getName());
            }catch (IOException e){
                resultadosArea.setText("Error al cargar el archivo " + e.getMessage());
            }
        }
    }

    private void guardarResultado() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
            @Override
            public boolean accept(File f){
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            @Override
            public String getDescription(){
                return "Archivos de texto (*.txt)";
            }
        });
        int selecccion = fileChooser.showSaveDialog(this);
        if(selecccion == JFileChooser.APPROVE_OPTION){
            File archivo = fileChooser.getSelectedFile();
            if(!archivo.getName().endsWith(".txt")){
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            try (PrintWriter writer = new PrintWriter(archivo)){
                writer.println("Coddigo Ru");
                writer.println(codigoArea.getText());
                writer.println("Resultados");
                writer.println(resultadosArea.getText());
                resultadosArea.append("\nLOs resultados fueron guardados en: " + archivo.getName());
            }catch (IOException e){
                resultadosArea.setText("Error al guardar " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }catch (Exception e){
                e.printStackTrace();
            }
            new Main().setVisible(true);
        });
    }
}