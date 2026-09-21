import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculadora Avanzada en Java Swing.
 * 
 * CARACTERÍSTICAS PRINCIPALES:
 * 1. Diseño con GridBagLayout: Control total sobre la cuadrícula y expansión de componentes.
 * 2. Evaluación de Expresiones: Soporta operaciones combinadas, jerarquía de operadores,
 *    paréntesis anidados y funciones trigonométricas en grados (DEG) o radianes (RAD).
 */
public class Calculadora extends JFrame {

    // ==========================================
    // --- COMPONENTES DE LA INTERFAZ GRÁFICA ---
    // ==========================================
    private JLabel lblOperacion;     // Muestra la expresión que introduce el usuario (ej. "sin(30)+5")
    private JLabel lblResultado;     // Muestra el resultado final calculated o el estado de "Error"
    private JRadioButton rbGrados;   // Opción para evaluar funciones trigonométricas en Grados (DEG)
    private JRadioButton rbRadianes; // Opción para evaluar funciones trigonométricas en Radianes (RAD)
    private ButtonGroup bgUnidades; // Agrupa los radio buttons para asegurar selección única (exclusión mutua)

    // ==========================================
    // --- VARIABLES DE ESTADO Y CONTROL --------
    // ==========================================
    private String expresionActual = "";       // Cadena de texto que acumula la entrada del usuario
    private boolean resultadoMostrado = false; // Flag para determinar si el texto actual es un resultado previo

    /**
     * Constructor principal: Configura la ventana y construye la interfaz sin usar JPanels.
     */
    public Calculadora() {
        // --- 1. CONFIGURACIÓN DE LA VENTANA PRINCIPAL (JFrame) ---
        setTitle("Calculadora Avanzada - Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 620);              // Tamaño fijo adecuado para el contenido
        setLocationRelativeTo(null);   // Centra la ventana automáticamente en la pantalla
        setResizable(false);            // Inhabilita el redimensionado para proteger la distribución

        // Definición del color de fondo estilo "Dark Mode"
        Color colorFondo = new Color(30, 30, 46);
        
        // Aplicamos el fondo y un margen (padding) directo al ContentPane del JFrame
        getContentPane().setBackground(colorFondo);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- 2. CONFIGURACIÓN DEL LAYOUT GENERAL ---
        // Asignamos GridBagLayout directamente a la ventana para gestionar la alineación en rejilla
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // 'fill = BOTH' hace que los componentes se expandan vertical y horizontalmente para llenar su celda
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4); // Margen de 4px alrededor de cada componente
        gbc.weightx = 1.0; // Distribuye el espacio horizontal sobrante por igual entre columnas
        gbc.weighty = 1.0; // Distribuye el espacio vertical sobrante por igual entre filas

        // Colores temáticos para el texto de las pantallas
        Color colorTextoOperacion = new Color(166, 173, 200);
        Color colorTextoResultado = new Color(245, 245, 247);

        // --- 3. CREACIÓN Y CONFIGURACIÓN DE PANTALLAS (JLabels) ---
        
        // PANTALLA SUPERIOR: Muestra la expresión que el usuario va construyendo
        lblOperacion = new JLabel(" ");
        lblOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblOperacion.setForeground(colorTextoOperacion);
        lblOperacion.setBackground(new Color(24, 24, 37));
        lblOperacion.setOpaque(true); // NOTA: En JLabel es IMPRESCINDIBLE setOpaque(true) para que pinte el fondo
        lblOperacion.setHorizontalAlignment(SwingConstants.RIGHT); // Alineación del texto a la derecha
        lblOperacion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding interno del texto

        // Posicionamiento en el Grid (Fila 0, Ocupa las 4 columnas)
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 4;
        add(lblOperacion, gbc); // Se añade directamente al JFrame

        // PANTALLA INFERIOR: Muestra el resultado de la evaluación o valor inicial "0"
        lblResultado = new JLabel("0");
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblResultado.setForeground(colorTextoResultado);
        lblResultado.setBackground(new Color(24, 24, 37));
        lblResultado.setOpaque(true); // Obligatorio para renderizar el color de fondo
        lblResultado.setHorizontalAlignment(SwingConstants.RIGHT);
        lblResultado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Posicionamiento en el Grid (Fila 1, Ocupa las 4 columnas)
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.gridwidth = 4;
        add(lblResultado, gbc); // Se añade directamente al JFrame

        // --- 4. SELECTOR DE UNIDADES TRIGONOMÉTRICAS (JRadioButtons) ---
        rbGrados = new JRadioButton("Grados (DEG)", true); // Seleccionado por defecto
        rbRadianes = new JRadioButton("Radianes (RAD)");

        Font fontRadio = new Font("Segoe UI", Font.BOLD, 12);
        
        rbGrados.setFont(fontRadio);
        rbGrados.setForeground(Color.WHITE);
        rbGrados.setOpaque(false); // Fondo transparente para ver el fondo del JFrame
        rbGrados.setFocusPainted(false);
        rbGrados.setHorizontalAlignment(SwingConstants.CENTER);

        rbRadianes.setFont(fontRadio);
        rbRadianes.setForeground(Color.WHITE);
        rbRadianes.setOpaque(false);
        rbRadianes.setFocusPainted(false);
        rbRadianes.setHorizontalAlignment(SwingConstants.CENTER);

        // El ButtonGroup garantiza que al seleccionar uno se deseleccione el otro
        bgUnidades = new ButtonGroup();
        bgUnidades.add(rbGrados);
        bgUnidades.add(rbRadianes);

        // Posicionamiento en la Fila 2 (Divididos en 2 columnas cada uno)
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.gridwidth = 2;
        add(rbGrados, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        add(rbRadianes, gbc);

        // --- 5. CREACIÓN Y DISTRIBUCIÓN DE BOTONES ---
        // Paleta de colores categorizada para la botonera
        Color btnNumeroColor   = new Color(69, 71, 90);   // Gris oscuro
        Color btnOperadorColor = new Color(137, 180, 250); // Azul
        Color btnTrigColor     = new Color(203, 166, 247); // Morado
        Color btnBorrarColor   = new Color(243, 139, 168); // Rojo / Rosa
        Color btnIgualColor    = new Color(166, 227, 161); // Verde

        // Fila 3: Funciones trigonométricas básicas y Reset
        agregarBoton("sin", 0, 3, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton("cos", 1, 3, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton("tan", 2, 3, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton("C",   3, 3, 1, 1, btnBorrarColor, Color.WHITE, gbc);

        // Fila 4: Paréntesis, Constante Pi y División
        agregarBoton("(", 0, 4, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton(")", 1, 4, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton("π", 2, 4, 1, 1, btnTrigColor, Color.BLACK, gbc);
        agregarBoton("/", 3, 4, 1, 1, btnOperadorColor, Color.BLACK, gbc);

        // Fila 5: Teclado numérico (7-9) y Multiplicación
        agregarBoton("7", 0, 5, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("8", 1, 5, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("9", 2, 5, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("*", 3, 5, 1, 1, btnOperadorColor, Color.BLACK, gbc);

        // Fila 6: Teclado numérico (4-6) y Resta
        agregarBoton("4", 0, 6, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("5", 1, 6, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("6", 2, 6, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("-", 3, 6, 1, 1, btnOperadorColor, Color.BLACK, gbc);

        // Fila 7: Teclado numérico (1-3) y Suma
        agregarBoton("1", 0, 7, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("2", 1, 7, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("3", 2, 7, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("+", 3, 7, 1, 1, btnOperadorColor, Color.BLACK, gbc);

        // Fila 8: Cero, Punto decimal y Botón de Igual
        // NOTA: El botón '=' utiliza gridwidth = 2 (Uso avanzado del grid)
        agregarBoton("0", 0, 8, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton(".", 1, 8, 1, 1, btnNumeroColor, Color.WHITE, gbc);
        agregarBoton("=", 2, 8, 2, 1, btnIgualColor, Color.BLACK, gbc);
    }

    /**
     * Método auxiliar para instanciar, personalizar y posicionar un JButton directamente en el JFrame.
     */
    private void agregarBoton(String texto, int x, int y, int ancho, int alto,
                              Color colorFondo, Color colorTexto, GridBagConstraints gbc) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setBackground(colorFondo);
        btn.setForeground(colorTexto);
        btn.setFocusPainted(false); // Elimina el recuadro de enfoque al hacer clic
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Asignación de evento mediante expresión lambda
        btn.addActionListener(e -> procesarEvento(e));

        // Configuración de coordenadas dentro de la rejilla
        gbc.gridx = x;          // Columna de inicio
        gbc.gridy = y;          // Fila de inicio
        gbc.gridwidth = ancho;  // Celdas horizontales que abarca
        gbc.gridheight = alto;  // Celdas verticales que abarca

        add(btn, gbc); // Añade el botón directamente a la ventana
    }

    // ==========================================
    // --- CONTROLADOR DE EVENTOS (INTERACCIÓN) -
    // ==========================================
    private void procesarEvento(ActionEvent e) {
        String comando = e.getActionCommand(); // Obtiene el texto del botón presionado

        if (comando.equals("C")) {
            // BORRADO TOTAL: Restablece variables y textos a su estado original
            expresionActual = "";
            lblOperacion.setText(" ");
            lblResultado.setText("0");
            resultadoMostrado = false;
        } else if (comando.equals("=")) {
            // EVALUACIÓN: Procesa la expresión si no está vacía
            if (!expresionActual.isEmpty()) {
                evaluarOperacion();
            }
        } else {
            // MANEJO DE ENTRADA CONTINUA DE TEXTO
            if (resultadoMostrado) {
                // Si el usuario presiona un número o función tras obtener un resultado, inicia nueva operación
                if ("0123456789πsin'cos'tan(".contains(comando)) {
                    expresionActual = "";
                } else if ("+-*/".contains(comando)) {
                    // Si presiona un operador (+, -, *, /), continúa calculando sobre el resultado anterior
                    expresionActual = lblResultado.getText();
                }
                resultadoMostrado = false;
            }

            // Si es una función trigonométrica, añade automáticamente el paréntesis de apertura '('
            if (comando.equals("sin") || comando.equals("cos") || comando.equals("tan")) {
                expresionActual += comando + "(";
            } else {
                expresionActual += comando; // Concatena el número o símbolo presionado
            }

            // Actualiza la pantalla superior con la expresión modificada
            lblOperacion.setText(expresionActual);
        }
    }

    // ==========================================
    // --- MOTOR DE CÁLCULO Y EVALUACIÓN --------
    // ==========================================

    /**
     * Llama al cálculo matemático, da formato al resultado en el JLabel e interpreta errores.
     */
    private void evaluarOperacion() {
        try {
            double resultado = calcularExpresion(expresionActual);

            // Muestra números enteros sin decimales ".0" innecesarios
            if (resultado == (long) resultado) {
                lblResultado.setText(String.format("%d", (long) resultado));
            } else {
                // Limita a 6 decimales y elimina los ceros sobrantes al final
                lblResultado.setText(String.format("%.6f", resultado).replaceAll("0+$", "").replaceAll(",$", "."));
            }

            resultadoMostrado = true; // Marca que la pantalla actual muestra un resultado final
        } catch (Exception ex) {
            lblResultado.setText("Error"); // Ante cualquier fallo de sintaxis o división por cero
        }
    }

    /**
     * Prepara la cadena reemplazando símbolos especiales e inicia el tokenizado y evaluación.
     */
    private double calcularExpresion(String expr) throws Exception {
        expr = expr.replace("π", String.valueOf(Math.PI)); // Sustituye el símbolo de Pi por su valor numérico
        expr = expr.replace("-(", "0-(");                  // Convierte el signo negativo unario frente a '(' en resta explícita

        List<String> tokens = tokenizar(expr);
        return evaluarTokens(tokens);
    }

    /**
     * ANALIZADOR LÉXICO (Tokenizer):
     * Convierte la cadena completa en una lista de 'tokens' (números, operadores, paréntesis y funciones).
     */
    private List<String> tokenizar(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numero = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            // Determina si un '-' es un signo negativo del número o el operador de resta
            boolean esSgnNegativo = (c == '-') &&
                    (i + 1 < expr.length() && (Character.isDigit(expr.charAt(i + 1)) || expr.charAt(i + 1) == '.')) &&
                    (i == 0 || "+-*/(".indexOf(expr.charAt(i - 1)) != -1);

            if (Character.isDigit(c) || c == '.' || esSgnNegativo) {
                numero.append(c); // Agrupa los caracteres que forman una misma cifra numérica
            } else if (Character.isLetter(c)) {
                // Agrupa letras continuas para identificar nombres de funciones (sin, cos, tan)
                StringBuilder func = new StringBuilder();
                while (i < expr.length() && Character.isLetter(expr.charAt(i))) {
                    func.append(expr.charAt(i));
                    i++;
                }
                i--; // Retrocede un índice tras completar el bucle de la palabra
                tokens.add(func.toString());
            } else {
                // Si teníamos un número almacenado en el StringBuilder, lo volcamos a la lista
                if (numero.length() > 0) {
                    tokens.add(numero.toString());
                    numero.setLength(0);
                }
                if (c != ' ') {
                    tokens.add(String.valueOf(c)); // Añade operadores (+, -, *, /) y paréntesis como tokens independientes
                }
            }
        }
        // Guarda el último número si quedó pendiente al final de la cadena
        if (numero.length() > 0) {
            tokens.add(numero.toString());
        }
        return tokens;
    }

    /**
     * EVALUADOR CON PRIORIDAD DE PARÉNTESIS:
     * Resuelve los paréntesis de DENTRO hacia AFUERA recursivamente.
     */
    private double evaluarTokens(List<String> tokens) throws Exception {
        // Bucle que busca y resuelve agrupaciones en paréntesis mientras existan
        while (tokens.contains("(")) {
            // Encuentra la posición del paréntesis de apertura '(' más interno
            int ultimoApertura = -1;
            for (int i = 0; i < tokens.size(); i++) {
                if (tokens.get(i).equals("(")) {
                    ultimoApertura = i;
                }
            }

            // Encuentra el primer paréntesis de cierre ')' posterior a esa apertura
            int primerCierre = -1;
            for (int i = ultimoApertura + 1; i < tokens.size(); i++) {
                if (tokens.get(i).equals(")")) {
                    primerCierre = i;
                    break;
                }
            }

            // Si hay un '(' sin un ')' que lo cierre, se cancela la evaluación
            if (primerCierre == -1) {
                throw new Exception("Error: Paréntesis no cerrado");
            }

            // Extrae la sublista de tokens contenidos dentro de ese paréntesis
            List<String> subTokens = new ArrayList<>(tokens.subList(ultimoApertura + 1, primerCierre));
            double valorSub = evaluarTokensSinParentesis(subTokens); // Calcula la sub-expresión

            // Comprueba si el paréntesis provenía de una función trigonométrica: ej. sin(...)
            boolean esFuncion = false;
            String funcion = "";
            if (ultimoApertura > 0) {
                String anterior = tokens.get(ultimoApertura - 1);
                if (anterior.equals("sin") || anterior.equals("cos") || anterior.equals("tan")) {
                    esFuncion = true;
                    funcion = anterior;
                }
            }

            if (esFuncion) {
                // Si el selector de Grados (DEG) está activo, convierte el ángulo a Radianes para Math
                if (rbGrados.isSelected()) {
                    valorSub = Math.toRadians(valorSub);
                }

                // Aplica la función trigonométrica correspondiente
                double resTrig = 0;
                switch (funcion) {
                    case "sin": resTrig = Math.sin(valorSub); break;
                    case "cos": resTrig = Math.cos(valorSub); break;
                    case "tan": resTrig = Math.tan(valorSub); break;
                }

                // Reemplaza desde la palabra clave de la función hasta el ')' por el valor numérico obtenido
                for (int k = primerCierre; k >= ultimoApertura - 1; k--) {
                    tokens.remove(k);
                }
                tokens.add(ultimoApertura - 1, String.valueOf(resTrig));
            } else {
                // Si eran paréntesis de agrupamiento normal, reemplaza desde '(' hasta ')' por el valor numérico
                for (int k = primerCierre; k >= ultimoApertura; k--) {
                    tokens.remove(k);
                }
                tokens.add(ultimoApertura, String.valueOf(valorSub));
            }
        }

        // Cuando ya no quedan paréntesis, se realiza la evaluación aritmética estándar
        return evaluarTokensSinParentesis(tokens);
    }

    /**
     * JERARQUÍA DE OPERADORES (Expresión plana):
     * 1ª Pasada: Multiplicaciones y Divisiones (*, /)
     * 2ª Pasada: Sumas y Restas (+, -)
     */
    private double evaluarTokensSinParentesis(List<String> tokens) throws Exception {
        if (tokens.isEmpty()) return 0;

        // --- PASO 1: Resolver Multiplicación (*) y División (/) ---
        List<String> sinMultDiv = new ArrayList<>();
        int i = 0;

        while (i < tokens.size()) {
            String token = tokens.get(i);

            if (token.equals("*") || token.equals("/")) {
                if (sinMultDiv.isEmpty()) throw new Exception("Error de sintaxis");
                double izq = Double.parseDouble(sinMultDiv.remove(sinMultDiv.size() - 1));
                i++;
                if (i >= tokens.size()) throw new Exception("Error de sintaxis");
                double der = Double.parseDouble(tokens.get(i));

                // Aplica el operador
                double res = token.equals("*") ? izq * der : izq / der;
                sinMultDiv.add(String.valueOf(res)); // Inserta el resultado parcial
            } else {
                sinMultDiv.add(token);
            }
            i++;
        }

        // --- PASO 2: Resolver Suma (+) y Resta (-) secuencialmente ---
        if (sinMultDiv.isEmpty()) return 0;
        double total = Double.parseDouble(sinMultDiv.get(0));

        for (int j = 1; j < sinMultDiv.size(); j += 2) {
            if (j + 1 >= sinMultDiv.size()) throw new Exception("Error de sintaxis");
            String op = sinMultDiv.get(j);
            double sig = Double.parseDouble(sinMultDiv.get(j + 1));

            if (op.equals("+")) total += sig;
            else if (op.equals("-")) total -= sig;
        }

        return total;
    }

    // ==========================================
    // --- PUNTO DE ENTRADA MAIN ----------------
    // ==========================================
    public static void main(String[] args) {
        // Ejecuta la creación del JFrame en el Event Dispatch Thread (EDT) de Swing para evitar problemas de hilos
        SwingUtilities.invokeLater(() -> {
            new Calculadora().setVisible(true);
        });
    }
}