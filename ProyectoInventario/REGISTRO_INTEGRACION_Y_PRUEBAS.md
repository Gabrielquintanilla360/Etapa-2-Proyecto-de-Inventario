REGISTRO DE INTEGRACIÓN Y PRUEBAS

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
# 7. Pruebas de funcionamiento en el nuevo equipo

Se realizaron pruebas de ejecución del Sistema de Gestión de Inventario después de configurar el entorno de desarrollo en un nuevo equipo.

Durante las pruebas se verificaron los siguientes aspectos:

- **Inicio de sesión:** se comprobó que la ventana de acceso se mostrara correctamente y permitiera ingresar al sistema.
- **Menú principal:** se verificó el acceso al menú principal después de iniciar sesión.
- **Gestión de productos:** se comprobó que el módulo pudiera abrirse correctamente.
- **Control de existencias y reportes:** se verificó el acceso al módulo correspondiente.

Como resultado, se confirmó que la aplicación podía ejecutarse correctamente en el nuevo equipo y que era posible acceder a los módulos principales del sistema.

Estas pruebas permitieron comprobar la correcta configuración del entorno y la ejecución general del programa.