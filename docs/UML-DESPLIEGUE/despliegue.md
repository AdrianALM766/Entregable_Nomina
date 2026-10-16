3.5 Vista de Despliegue (Diagrama UML de Despliegue)

[AQUÍ PEGAS LA IMAGEN EXPORTADA DE DRAW.IO]

Leyenda y Especificación de Despliegue:
• Nodos de Ejecución: Representan los entornos físicos y contenedorizados del sistema (Host Local / VM, Navegador Web del Cliente, Contenedor Backend Spring Boot, Contenedor MariaDB y Nodo Legado Delphi).
• Artefactos: Corresponden a los componentes ejecutables y esquemas de datos (Frontend Web HTML5/Bootstrap, API REST JAR, Base de Datos del Portal nomina_db y Base de Datos Legada).
• Puertos y Protocolos Expuestos:
  - Puerto HTTP :8080: Canal de comunicación cliente-servidor para peticiones REST en formato JSON.
  - Puerto TCP :3306: Conexión relacional JDBC entre el Backend y la base de datos MariaDB.
  - Conexión Read-Only (JDBC): Enlace de consulta desacoplado hacia la base de datos del sistema en Delphi sin permisos de escritura.

Variables de Entorno Sensibles (Configuración del Sistema):
Para dar cumplimiento a los lineamientos de seguridad y evitar la exposición de credenciales en el repositorio de código fuente:
- SERVER_PORT: Puerto de escucha del servicio Spring Boot.
- SPRING_DATASOURCE_URL: Cadena de conexión JDBC hacia MariaDB.
- SPRING_DATASOURCE_USERNAME / SPRING_DATASOURCE_PASSWORD: Credenciales de acceso a la BD del portal.
- JWT_SECRET: Clave secreta para la firma y verificación de tokens de sesión.
- JWT_EXPIRATION: Tiempo de expiración del token emitido.
- MARIADB_ROOT_PASSWORD / MARIADB_DATABASE / MARIADB_USER / MARIADB_PASSWORD: Variables de inicialización del contenedor MariaDB.

Párrafo de Lectura (30 segundos):
La vista de despliegue detalla la topología de nodos y contenedores que soporta el portal de nómina. El cliente accede desde su navegador al Frontend Web responsivo, enviando peticiones HTTP JSON al puerto 8080 de la API REST desarrollada en Spring Boot 3 / Java 21. La aplicación persiste usuarios y credenciales en el puerto 3306 del contenedor MariaDB. Para cumplir la restricción técnica del cliente, el backend establece una conexión JDBC independiente en modo estrictamente lectura (Read-Only) hacia la base de datos del sistema legado en Delphi, permitiendo la extracción de comprobantes quincenales sin arriesgar ni modificar el núcleo transaccional original.