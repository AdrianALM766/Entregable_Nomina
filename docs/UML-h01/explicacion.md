3.4 Vista Dinámica (Diagrama de Secuencia UML)

[AQUÍ INSERTA LA IMAGEN EXPORTADA DE DRAW.IO]

Leyenda del Diagrama:
Actor y Líneas de Vida: Representan los objetos y componentes del patrón N-Capas que participan en el flujo de autenticación (FrontendWeb, AuthController, AuthService, UsuarioRepository y MariaDB).
Flechas Continuas (Mensajes Síncronos): Indican llamadas síncronas entre capas (HTTP POST, llamadas a métodos Java y consultas SQL SELECT).
Flechas Punteadas (Respuestas): Representan el retorno de datos, instancias de objetos o estados HTTP (200 OK, 401 Unauthorized).
Cuadro Delimitador alt (Fragmento Alternativo): Modela la bifurcación entre el camino de error (credenciales inválidas con respuesta HTTP 401) y el camino feliz (generación y entrega del token JWT).

Párrafo de Lectura (30 segundos):
La vista dinámica ilustra la ejecución del escenario primario HU-01 (Autenticación) atravesando todas las capas del patrón N-Capas. El Empleado envía sus credenciales desde el FrontendWeb al AuthController vía HTTP POST. El controlador delega la verificación al AuthService, el cual consulta la entidad Usuario en MariaDB a través del repositorio JPA aplicando el filtro de borrado lógico (activo = true). La capa de servicio valida la contraseña comparando el hash bcrypt del PIN. En el flujo alterno de error, la API responde un HTTP 401 Unauthorized rechazando el acceso no autorizado (AC-01). En el camino feliz, se emite un token JWT firmado y se retorna HTTP 200 OK al cliente para autorizar las consultas subsiguientes de desprendibles.