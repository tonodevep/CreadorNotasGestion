import javax.swing.*; // Importa las clases necesarias para crear interfaces gráficas
import java.awt.event.ActionEvent; // Para manejar eventos de acciones
import java.awt.event.ActionListener; // Para definir el comportamiento al hacer clic en un botón
import java.io.BufferedReader; // Para leer archivos de texto
import java.io.BufferedWriter; // Para escribir en archivos de texto
import java.io.FileReader; // Para leer archivos línea por línea
import java.io.FileWriter; // Para escribir en archivos
import java.io.IOException; // Para manejar errores relacionados con E/S
import java.security.MessageDigest; // Para realizar el hash de contraseñas
import java.security.NoSuchAlgorithmException; // Para manejar errores de algoritmos de hash
import java.util.regex.Pattern; // Para validar correos electrónicos usando expresiones regulares

public class RegistroUsuario {
    private JTextField nombreUsuarioField; // Campo de texto para ingresar nombre de usuario
    private JTextField emailField; // Campo de texto para ingresar correo electrónico
    private JPasswordField passwordField; // Campo de texto para ingresar la contraseña (oculta)
    private JFrame frame; // Ventana principal de la aplicación
    private JButton registrarButton; // Botón para registrar al usuario

    public RegistroUsuario() {
        JFrame frame = new JFrame("Registro de Usuario"); // Crea la ventana
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana

        // Crear campos y botón
        nombreUsuarioField = new JTextField(20); // Campo para el nombre de usuario
        emailField = new JTextField(20); // Campo para el correo electrónico
        passwordField = new JPasswordField(20); // Campo para la contraseña
        registrarButton = new JButton("Registrar"); // Botón de registro
 
        // Define la acción al hacer clic en el botón de registro
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registrarUsuario(); // Intenta registrar al usuario
                } catch (IOException e1) {
                    e1.printStackTrace(); // Muestra cualquier error de entrada/salida
                }
            }
        });

        // Crea un panel para organizar los elementos en la ventana
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre de Usuario:")); // Etiqueta para el nombre de usuario
        panel.add(nombreUsuarioField); // Campo de texto para el nombre de usuario
        panel.add(new JLabel("Correo Electrónico:")); // Etiqueta para el correo electrónico
        panel.add(emailField); // Campo de texto para el correo electrónico
        panel.add(new JLabel("Contraseña:")); // Etiqueta para la contraseña
        panel.add(passwordField); // Campo de texto para la contraseña
        panel.add(registrarButton); // Botón de registro

        frame.add(panel); // Añade el panel a la ventana
        frame.pack(); // Ajusta el tamaño de la ventana
        frame.setVisible(true); // Muestra la ventana
    }

    private String hashearContrasena(String contrasena) { // Aplica un algoritmo SHA-256 para crear un hash seguro de la contraseña
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytesHashed = md.digest(contrasena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytesHashed) {
                sb.append(String.format("%02x", b)); // Convierte los bytes en formato hexadecimal
            }
            return sb.toString(); // Devuelve la contraseña hasheada
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña."); // Maneja errores si el algoritmo no está disponible
        }
    }

    
    private void registrarUsuario() throws IOException {
        String nombreUsuario = nombreUsuarioField.getText().trim(); // Obtiene el nombre de usuario ingresado
        String email = emailField.getText().trim(); // Obtiene el correo electrónico ingresado
        String password = new String(passwordField.getPassword()).trim(); // Obtiene la contraseña ingresada

        // Validaciones de entrada
        if (nombreUsuario.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios."); // Muestra un mensaje de error
            return;
        }

        if (!validarEmail(email)) {
            JOptionPane.showMessageDialog(null, "El correo electrónico no es válido."); // Verifica que el correo tenga el formato correcto
            return;
        }

        if (!validarContrasena(password)) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres."); // Requiere una contraseña mínima
            return;
        }

        // Guarda los datos del usuario en el archivo 'usuarios.txt'
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("usuarios.txt", true))) {
            String passwordHashed = hashearContrasena(password); // Hashea la contraseña
            escritor.write(email + ":" + passwordHashed); // Escribe el correo y la contraseña hasheada en el archivo
            escritor.newLine();
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito."); // Mensaje de éxito
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el usuario."); // Manejo de errores 
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito."); // Mensaje alternativo de éxito
            frame.dispose(); // Cierra la ventana actual
            new PantallaInicio(); // Abre la pantalla de inicio
        }
    }
        

    // Validación de correo electrónico
    private boolean validarEmail(String email) {
        // Usa una expresión regular para verificar el formato del correo electrónico
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches(); // Devuelve true si el correo es válido
    }

    private boolean correoYaRegistrado(String email) {
        // Verifica si el correo ya existe en el archivo 'usuarios.txt'
        try (BufferedReader lector = new BufferedReader(new FileReader("usuarios.txt"))) {
            return lector.lines().anyMatch(linea -> linea.split(":")[0].equals(email)); // Busca coincidencias en el archivo
        } catch (IOException e) {
            return false; // Si hay algún error, asume que el correo no está registrado
        }
    }

   // Validación de contraseña
   private boolean validarContrasena(String password) {
    return password.length() >= 6; // Sólo verifica que la contraseña tenga al menos 6 caracteres
}


    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        new RegistroUsuario(); // Inicia el registro de usuarios
    }
}

