import javax.swing.*; // Importa las clases de Swing para crear la interfaz gráfica
import java.awt.*; // Importa las clases de AWT para el diseño de la interfaz
import java.awt.event.*; // Importa las clases para manejar eventos
import java.io.*; // Importa las clases para manejar entrada/salida
import java.security.MessageDigest; // Importa la clase para el hash de contraseñas
import java.security.NoSuchAlgorithmException; // Importa la excepción para algoritmos no encontrados
import java.util.HashMap; // Importa la clase HashMap para almacenar credenciales

public class PantallaInicio {
    private JFrame ventanaInicio; // Ventana principal de la aplicación
    private JTextField campoCorreo; // Campo para ingresar el correo electrónico
    private JPasswordField campoContrasena; // Campo para ingresar la contraseña
    private HashMap<String, String> credencialesUsuarios; // Mapa para almacenar correos y contraseñas (en hash)

    public PantallaInicio() {
        // Configuración de la ventana
        ventanaInicio = new JFrame("Inicio de Sesión");
        ventanaInicio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaInicio.setSize(300, 200);
        ventanaInicio.setLayout(new GridLayout(3, 2));

        credencialesUsuarios = new HashMap<>(); // Inicializa el mapa de credenciales
        cargarCredenciales();

        // Agrega componentes a la ventana
        ventanaInicio.add(new JLabel("Correo electrónico:"));
        campoCorreo = new JTextField();
        ventanaInicio.add(campoCorreo);

        ventanaInicio.add(new JLabel("Contraseña:"));
        campoContrasena = new JPasswordField();
        ventanaInicio.add(campoContrasena);

        // Botones para iniciar sesión y registrarse
        JButton botonIniciarSesion = new JButton("Iniciar Sesión");
        JButton botonRegistrar = new JButton("Registrarse");
        ventanaInicio.add(botonIniciarSesion);
        ventanaInicio.add(botonRegistrar);

        // Eventos para los botones
        botonIniciarSesion.addActionListener(e -> iniciarSesion());
        botonRegistrar.addActionListener(e -> registrarUsuario());

        ventanaInicio.setVisible(true); // Muestra la ventana
    }

    private void cargarCredenciales() {
        // Carga las credenciales de los usuarios desde un archivo
        try (BufferedReader lector = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = lector.readLine()) != null) { // Lee cada línea del archivo
                String[] partes = linea.split(":"); // Divide la línea en partes
                if (partes.length >= 2) { // Verifica que haya al menos correo y contraseña
                    
                    String correo = partes[0].contains("@") ? partes[0] : partes[1]; // Obtiene el correo
                    String contrasenaHashed = partes[partes.length - 1]; // Obtiene la contraseña hasheada
                    credencialesUsuarios.put(correo, contrasenaHashed); // Almacena en el mapa
                }
            }
        } catch (IOException e) {
            System.out.println("Aún no hay usuarios registrados.");
        }
    }

    private void guardarCredenciales(String correo, String contrasenaHashed) {
        // Guarda las credenciales de un nuevo usuario en el archivo
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            escritor.write(correo + ":" + contrasenaHashed); // Escribe el correo y la contraseña hasheada
            escritor.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hashearContrasena(String contrasena) {
        // Hashea la contraseña usando SHA-256
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytesHashed = md.digest(contrasena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytesHashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña.");
        }
    }

    private void iniciarSesion() {
        // Método para iniciar sesión
        String correo = campoCorreo.getText();
        String contrasena = new String(campoContrasena.getPassword());
        String contrasenaHashed = hashearContrasena(contrasena);

        // Verifica si las credenciales son correctas
        if (credencialesUsuarios.containsKey(correo) && credencialesUsuarios.get(correo).equals(contrasenaHashed)) {
            ventanaInicio.dispose();
            new Notas(correo); // Carga la aplicación de notas para el usuario
        } else {
            JOptionPane.showMessageDialog(ventanaInicio, "Credenciales incorrectas.");
        }
    }

    private void registrarUsuario() {
        // Método para registrar un nuevo usuario
        String correo = campoCorreo.getText();
        String contrasena = new String(campoContrasena.getPassword());

        // Verifica si los campos están vacíos
        if (correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(ventanaInicio, "Los campos no pueden estar vacíos.");
        } else if (!correo.contains("@")) {
            JOptionPane.showMessageDialog(ventanaInicio, "Introduce un correo válido.");
        } else if (contrasena.length() < 6) {
            JOptionPane.showMessageDialog(ventanaInicio, "La contraseña debe tener al menos 6 caracteres.");
        } else if (credencialesUsuarios.containsKey(correo)) {
            JOptionPane.showMessageDialog(ventanaInicio, "El usuario ya está registrado.");
        } else {
            String contrasenaHashed = hashearContrasena(contrasena);
            guardarCredenciales(correo, contrasenaHashed);
            credencialesUsuarios.put(correo, contrasenaHashed);
            JOptionPane.showMessageDialog(ventanaInicio, "Usuario registrado exitosamente.");
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            String passwordHashed = hashearContrasena(contrasena);
            escritor.write(correo + ":" + passwordHashed);
            escritor.newLine();
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito. Será redirigido al inicio de sesión.");
            
            // Cierra la ventana actual y abre PantallaInicio
            SwingUtilities.invokeLater(() -> {
                Component registrarButton = null;
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(registrarButton);
                frame.dispose();
                new PantallaInicio();
            });
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el usuario.");
        }
    }
    

    // Inicia la aplicación creando una nueva instancia de PantallaInicio
    public static void main(String[] args) {
        new PantallaInicio();
    }
}

