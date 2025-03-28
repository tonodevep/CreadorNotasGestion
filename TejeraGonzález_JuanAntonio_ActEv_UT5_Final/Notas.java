import javax.swing.*; // Importa las clases necesarias para construir interfaces gráficas
import java.io.*; // Importa clases de E/S para leer y escribir archivos
import java.nio.file.*; // Importa clases para manipular rutas y directorios
import java.util.*; // Importa utilidades como listas y otros elementos
import java.awt.*; // Importa clases para diseño y componentes gráficos
import java.awt.event.*; // Importa clases para manejar eventos de usuario

public class Notas {
    private String directorioUsuario; // Ruta del directorio donde se almacenarán las notas
    private JFrame ventana; // Ventana principal de la aplicación
    private DefaultListModel<String> modeloLista = new DefaultListModel<>(); // Modelo para manejar la lista de notas
    private JList<String> listaNotas; // Componente visual para mostrar la lista de notas
    private JTextArea txtContenido; // Área de texto para escribir el contenido de las notas
    private JTextField txtBuscar; // Campo de búsqueda para localizar notas por título o contenido
    private GuardadoAutomatico guardadoAutomatico; // Instancia para realizar el guardado automático

    public Notas(String correoUsuario) {
        // Configura el directorio del usuario utilizando su correo electrónico
        this.directorioUsuario = "usuarios/" + sanitizarNombreDirectorio(correoUsuario);
        crearDirectorioUsuario(); // Crea el directorio si no existe
        configurarVentana(); // Configura la interfaz gráfica
        cargarNotas(); // Carga las notas desde el directorio
        guardadoAutomatico = new GuardadoAutomatico(this); // Inicia el guardado automático de notas
    }

    private String sanitizarNombreDirectorio(String nombre) {
        // Convierte caracteres no válidos del nombre de directorio en "_"
        return nombre.replaceAll("[^a-zA-Z0-9-_.]", "_");
    }

    private void crearDirectorioUsuario() {
        // Crea el directorio para almacenar las notas del usuario
        try {
            Files.createDirectories(Paths.get(directorioUsuario));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al crear directorio de usuario");  
        }
    }

    private void configurarVentana() {
        // Configura la ventana principal de la aplicación
        ventana = new JFrame("Notas Mejoradas");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 600);
        ventana.setLayout(new BorderLayout());

        // Configura el panel editor para escribir el contenido de las notas
        JPanel panelEditor = new JPanel(new BorderLayout());
        txtContenido = new JTextArea();
        panelEditor.add(new JScrollPane(txtContenido), BorderLayout.CENTER);

        // Configura el panel de la lista de notas y el campo de búsqueda  
        JPanel panelLista = new JPanel(new BorderLayout());
        listaNotas = new JList<>(modeloLista);
        txtBuscar = new JTextField();
        JButton btnBuscar = new JButton("Buscar");
        
        // Acciones de búsqueda al escribir texto o hacer clic en el botón
        btnBuscar.addActionListener(e -> buscarNota());
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                buscarNota();
            }
        });

        panelLista.add(new JScrollPane(listaNotas), BorderLayout.CENTER);
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(new JLabel("Buscar:"), BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);
        panelLista.add(panelBusqueda, BorderLayout.SOUTH);

        // Divide la ventana en dos partes: lista de notas y editor de texto
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLista, panelEditor);
        ventana.add(splitPane, BorderLayout.CENTER);

        // Configura los botones inferiores para las acciones principales
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        panelBotones.add(btnCerrarSesion);


        // Define las acciones de cada botón
        btnAgregar.addActionListener(e -> agregarNota());
        btnGuardar.addActionListener(e -> guardarNota());
        btnEliminar.addActionListener(e -> eliminarNota());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        ventana.add(panelBotones, BorderLayout.SOUTH);

        // Acciones de doble clic en la lista de notas para cargar una nota seleccionada
        listaNotas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    cargarNotaSeleccionada();
                }
            }
        });


        ventana.setVisible(true); // Muestra la ventana principal
    }

    // Métodos para agregar, guardar, eliminar, buscar y cargar notas
    private void agregarNota() {
        String titulo = JOptionPane.showInputDialog(ventana, "Título de la nota:");
        if (titulo != null && !titulo.trim().isEmpty()) {
            if (modeloLista.contains(titulo)) {
                JOptionPane.showMessageDialog(ventana, "Ya existe una nota con ese título");
            } else {
                modeloLista.addElement(titulo);
                guardarNotaEnArchivo(titulo, txtContenido.getText());
            }
        }
    }

    private void guardarNota() {
        String titulo = listaNotas.getSelectedValue();
        if (titulo != null) {
            guardarNotaEnArchivo(titulo, txtContenido.getText());
            JOptionPane.showMessageDialog(ventana, "Nota guardada correctamente");
        } else {
            JOptionPane.showMessageDialog(ventana, "Seleccione una nota para guardar");
        }
    }

    private void guardarNotaEnArchivo(String titulo, String contenido) {
        try {
            Files.write(Paths.get(directorioUsuario, titulo + ".txt"), 
                      contenido.getBytes());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(ventana, "Error al guardar la nota");
        }
    }

    private void eliminarNota() {
        String titulo = listaNotas.getSelectedValue();
        if (titulo != null) {
            int confirmacion = JOptionPane.showConfirmDialog(ventana, 
                "¿Eliminar la nota '" + titulo + "'?", "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    Files.deleteIfExists(Paths.get(directorioUsuario, titulo + ".txt"));
                    modeloLista.removeElement(titulo);
                    limpiarCampos();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(ventana, "Error al eliminar la nota");
                }
            }
        }
    }

    private void cargarNotas() {
        File dir = new File(directorioUsuario);
        File[] archivos = dir.listFiles((d, name) -> name.endsWith(".txt"));
        
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloLista.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }

    private void cargarNotaSeleccionada() {
        String titulo = listaNotas.getSelectedValue();
        if (titulo != null) {
            try {
                String contenido = new String(Files.readAllBytes(
                    Paths.get(directorioUsuario, titulo + ".txt")));
                txtContenido.setText(contenido);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(ventana, "Error al cargar la nota");
            }
        }
    }

    private void buscarNota() {
        String termino = txtBuscar.getText().trim().toLowerCase();
        modeloLista.clear();
        
        File dir = new File(directorioUsuario);
        File[] archivos = dir.listFiles((d, name) -> 
            name.toLowerCase().contains(termino) || 
            contieneTextoEnArchivo(d, name, termino));
        
        if (archivos != null) {
            for (File archivo : archivos) {
                modeloLista.addElement(archivo.getName().replace(".txt", ""));
            }
        }
    }

    private boolean contieneTextoEnArchivo(File dir, String nombreArchivo, String texto) {
        try {
            String contenido = new String(Files.readAllBytes(Paths.get(dir.getPath(), nombreArchivo))).toLowerCase();
            return contenido.contains(texto);
        } catch (IOException e) {
            return false;
        }
    }

    private void limpiarCampos() {
        txtContenido.setText("");
    }

    public void cerrarSesion() {
        guardadoAutomatico.detenerGuardadoAutomatico();
        ventana.dispose();
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notas("usuario@ejemplo.com"));  
    }

    public void guardarNotasEnArchivo() {
    for (int i = 0; i < modeloLista.size(); i++) {
        String titulo = modeloLista.get(i);
        String contenido = ""; 
        guardarNotaEnArchivo(titulo, contenido);
    }
}
}




