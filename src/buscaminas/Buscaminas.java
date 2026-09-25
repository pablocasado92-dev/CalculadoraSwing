package buscaminas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buscaminas extends JFrame {

    // Configuración del tablero (Constantes: filas, columnas, minas)
    private final int FILAS = 8;
    private final int COLUMNAS = 8;
    private final int MINAS = 10;

    // Componentes de la interfaz (Paneles, Labels, Botón de carita)
    private JPanel panelSuperior;
    private JPanel panelTablero;
    private JLabel labelMinas;
    private JLabel labelTiempo;
    private JButton btnCarita;

    // Matriz de botones para el tablero
    private JButton[][] botones;

    public Buscaminas() {
        // Configuración de la ventana principal (Título, operación de cierre, layout general)
        setTitle("Buscaminas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // 1. Inicializar componentes superiores (marcadores/carita)
        inicializarPanelSuperior();

        // 2. Inicializar panel del tablero y matriz de botones
        inicializarPanelTablero();

        // 3. Ajustar tamaño de ventana y centrar
        pack();
        setLocationRelativeTo(null);
    }

    private void inicializarPanelSuperior() {
        // Crear panel con FlowLayout
        panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Crear e integrar los JLabels y el JButton central
        labelMinas = new JLabel("010"); // TODO: Actualizar dinámicamente
        btnCarita = new JButton("🙂");
        btnCarita.addActionListener(e -> reiniciarJuego());
        labelTiempo = new JLabel("000"); // TODO: Conectar a un Timer
        
        panelSuperior.add(labelMinas);
        panelSuperior.add(btnCarita);
        panelSuperior.add(labelTiempo);

        // Añadir el panel al norte de la ventana principal
        add(panelSuperior, BorderLayout.NORTH);
    }

    private void inicializarPanelTablero() {
        panelTablero = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        botones = new JButton[FILAS][COLUMNAS];

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                // Configurar posición en la rejilla GridBagLayout
                gbc.gridx = c;
                gbc.gridy = f;
                gbc.fill = GridBagConstraints.BOTH;

                // Crear botón
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(30, 30));
                
                // Listener para el clic
                final int fila = f;
                final int col = c;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        alHacerClicEnCasilla(fila, col);
                    }
                });

                // Guardar referencia en la matriz e insertar en el panel
                botones[f][c] = btn;
                panelTablero.add(btn, gbc);
            }
        }

        add(panelTablero, BorderLayout.CENTER);
    }

    // --- MÉTODOS DE LÓGICA / MANEJO DE EVENTOS ---

    private void alHacerClicEnCasilla(int fila, int columna) {
        // Manejar el clic en una casilla determinada
    }

    private void reiniciarJuego() {
        // Resetear el estado del tablero
    }

    public static void main(String[] args) {
        // Ejecutar la aplicación en el hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            new Buscaminas().setVisible(true);
        });
    }
}