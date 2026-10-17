3.3 Vista de Dominio / Objetos (Diagrama de Clases UML)

[AQUÍ INSERTA LA IMAGEN EXPORTADA DE DRAW.IO]

Leyenda del Diagrama:
Cajas de Clases UML: Representan las entidades del modelo de dominio mapeadas mediante el ORM (Spring Data JPA / Hibernate) en MariaDB.
Atributos con estereotipos: Indican las claves primarias («PK»), claves foráneas («FK») y restricciones únicas («UK»), junto con sus tipos de datos Java.
Líneas de Asociación: Muestran las relaciones entre entidades anotadas con sus multiplicidades (1 a 0..* y 1 a 1..*) y dirección de navegación.
Campo `activo : boolean`: Implementa el borrado lógico en todas las entidades de negocio.

Párrafo de Lectura (30 segundos):
La vista de dominio especifica el modelo de datos relacional persistido en MariaDB y mapeado por el ORM Spring Data JPA para atender las Historias de Usuario primarias. La entidad `Usuario` gestiona las credenciales de acceso (cédula y hash bcrypt del PIN) e identifica a cada empleado. Un `Usuario` posee de 0 a varios (`0..*`) registros de la entidad `Desprendible`, garantizando el aislamiento de datos por usuario (multitenancy a nivel de registro). Cada `Desprendible` almacena el periodo quincenal y la referencia al archivo PDF, conteniendo a su vez de 1 a varios (`1..*`) desgloses de la entidad `DetallePago` con los rubros devengados y deducidos. Todas las entidades incorporan la propiedad `activo` para asegurar el borrado lógico exigido en la arquitectura.