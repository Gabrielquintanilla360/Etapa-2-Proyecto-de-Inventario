#REGISTRO DE INTEGRACIÓN Y PRUEBAS

# 1. Configuración del entorno de desarrollo

Se realizó la configuración del proyecto Sistema de Gestión de Inventario en un nuevo equipo de trabajo, utilizando las siguientes herramientas:

- Java JDK 21.
- Apache NetBeans IDE 22.
- MongoDB Java Driver 5.14.0.
- GitHub Desktop para la gestión del repositorio.

# 2. Integración del proyecto

Se verificó la estructura del proyecto y la organización de sus paquetes principales:

- Conexion
- Dao
- Modelo
- Vista

Se incorporaron las bibliotecas necesarias para trabajar con MongoDB:

- bson-5.14.0.jar
- mongodb-driver-core-5.14.0.jar
- mongodb-driver-sync-5.14.0.jar

Posteriormente, se comprobó que NetBeans reconociera las bibliotecas y permitiera compilar el proyecto correctamente.

# 3. Verificación de compilación

Se ejecutó el proceso Clean and Build en NetBeans, obteniendo el resultado BUILD SUCCESSFUL, lo que confirmó que el proyecto podía compilarse sin errores.

# 4. Pruebas de ejecución

Se ejecutó la aplicación y se comprobó que la ventana de inicio de sesión se mostrara correctamente.

También se verificó el acceso al sistema mediante las credenciales iniciales configuradas en el proyecto.

# 5. Resultado de la integración

La configuración del entorno de desarrollo y la incorporación de las bibliotecas permitieron abrir, compilar y ejecutar correctamente el proyecto en un nuevo equipo de trabajo.

# 6. Actividades pendientes

- Verificar el funcionamiento de los módulos del sistema.
- Comprobar la conexión y persistencia de información en MongoDB.
- Realizar pruebas finales de integración.
- Mantener organizado el repositorio y registrar los cambios mediante GitHub.