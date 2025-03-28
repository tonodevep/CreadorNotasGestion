import java.io.File; // Importa la clase File para manipular archivos
import javax.swing.JOptionPane; // Importa JOptionPane para mostrar cuadros de diálogo

public class Main {
    public static void main(String[] args) {
        try {
            File archivoUsuarios = new File("usuarios.txt"); // Crea un objeto File que representa el archivo 'usuarios.txt'
            // Verifica si el archivo existe y no está vacío
            if (archivoUsuarios.exists() && archivoUsuarios.length() > 0) {
                new PantallaInicio(); // Si el archivo existe y tiene contenido, inicia la pantalla de inicio
            } else {
                new RegistroUsuario(); // Si el archivo no existe o está vacío, abre el registro de usuario
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error crítico: " + e.getMessage()); // Captura cualquier excepción y muestra un mensaje de error crítico
        }
    }
}

