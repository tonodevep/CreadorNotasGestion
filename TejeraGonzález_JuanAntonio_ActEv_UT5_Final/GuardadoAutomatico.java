import java.util.Timer; // Importa la clase Timer para programar tareas recurrentes
import java.util.TimerTask; // Importa TimerTask para definir la tarea que se ejecutará
import java.util.Date; // Importa la clase Date para obtener la fecha y hora actuales


public class GuardadoAutomatico {
    private Timer timer; // Temporizador para manejar tareas periódicas
    private Notas notas; // Referencia a la instancia de la clase Notas

    // Constructor que inicializa el guardado automático
    public GuardadoAutomatico(Notas notas) {
        this.notas = notas; // Asocia la instancia de Notas al objeto actual
        timer = new Timer(); // Crea el temporizador
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                guardarNotas(); // Define la tarea que se ejecutará cada vez
            }
        }, 0, 300000); // Programa la tarea para ejecutarse cada 5 minutos (300,000 ms)
    }

    // Método para guardar notas
    private void guardarNotas() {
        if (notas != null) { //Verifica si la instancia de Notas no es nula
            notas.guardarNotasEnArchivo(); // Llama al método para guardar las notas en un archivo
            System.out.println("Notas guardadas automáticamente: " + new Date()); // Imprime un mensaje con la fecha y hora actuales
        }
    }

    // Método para detener el guardado automático
    public void detenerGuardadoAutomatico() {
        timer.cancel(); // Detiene el temporizador
        System.out.println("Guardado automático detenido."); // Imprime un mensaje indicando que se ha detenido
    }
}

