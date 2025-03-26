# Creador de Notas Interactivo - README 📝

## Objetivo General 🎯
Extender la aplicación **Creador de Notas Interactivo** para permitir que los usuarios guarden sus notas de forma persistente, organizadas por usuario, y accedan a ellas mediante un sistema de inicio de sesión y registro con control de errores.

## Descripción del Reto 🚀
Hasta el momento, las notas solo existían durante la ejecución de la aplicación y se perdían al cerrarla. El desafío actual consiste en almacenar y recuperar las notas de cada usuario al iniciar sesión.

### Funcionalidades a Implementar 💻:

#### 1. **Pantalla de Inicio de Sesión 🔑:**
- Permite a los usuarios iniciar sesión utilizando su correo electrónico y contraseña.
- Si el usuario existe, sus notas se cargan y se muestra la aplicación de notas.
- Si el usuario no existe, aparece una opción para que se registre.
  
  **Manejo de Errores ⚠️:**
  - Mostrar mensajes de error si las credenciales son incorrectas.
  - Bloquear el acceso si el usuario no está registrado y no ha completado el proceso de registro.

#### 2. **Pantalla de Registro 📝:**
- Recoge los siguientes datos del usuario:
  - Nombre de usuario.
  - Correo electrónico (será el ID del usuario).
  - Contraseña (debe ser guardada con un hash, nunca en texto plano).
  
  **Validación de Datos ✅:**
  - Verificar que los campos no estén vacíos.
  - El correo debe tener un formato válido.
  - La contraseña debe tener una longitud mínima para garantizar seguridad.
  
- La información del usuario debe guardarse en un directorio estructurado.

#### 3. **Pantalla Principal (Creador de Notas) 🗒️:**
- Mantiene las funcionalidades previas:
  - Crear notas ✍️.
  - Eliminar notas 🗑️.
  - Limpiar todas las notas ✨.
  - Filtrar notas mediante un buscador 🔍.
  
- **Persistencia de Notas 💾:**
  - Las notas deben guardarse en archivos organizados por usuario, para que se mantengan incluso si la aplicación se cierra.
  - Al cerrar sesión, la aplicación debe guardar automáticamente el estado de las notas para que el usuario pueda recuperarlas en su próxima sesión.
  
- **Opciones de Implementación ⚙️:**
  - Guardar automáticamente las notas al hacer cambios.
  - Permitir la opción de guardar manualmente mediante un botón 💾.
  - Implementar otras funcionalidades que optimicen la experiencia del usuario.

## Autor ✨
Este proyecto fue desarrollado por Juan Antonio "Toño" Tejera González 👨‍💻👩‍💻. Puedes encontrarme en mi Github [![Web](https://img.shields.io/badge/GitHub-tonodevep-14a1f0?style=for-the-badge&logo=github&logoColor=white&labelColor=101010)](https://github.com/tonodevep/) 🚀

