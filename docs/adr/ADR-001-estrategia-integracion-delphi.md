# ADR-001: Estrategia de integración en solo lectura sobre el sistema legado en Delphi

* **Estatus:** Aceptado
* **Fecha:** 2026-10-24
* **Autores:** Estudiante (Arquitecto de Software)

---

## 1. Contexto

El sistema de nómina principal de la empresa opera sobre una aplicación legada desarrollada en Delphi con 18 años de antigüedad[cite: 2, 3]. El sistema carece de documentación técnica actualizada y no cuenta con soporte por parte del proveedor original ("caja negra")[cite: 2, 3]. Sin embargo, su operación en el cálculo de la nómina es críticamente estable[cite: 2, 3].

Se requiere implementar un portal web de autoconsulta que permita a los 350 empleados de la planta industrial acceder a sus desprendibles de pago quincenales en formato PDF[cite: 2, 3]. El riesgo principal identificado es que cualquier alteración o modificación directa sobre el código fuente o la base de datos del sistema en Delphi podría comprometer la integridad del proceso de pago de nómina de la empresa[cite: 2, 3].

Se deben evaluar las alternativas para exponer los datos de pago al nuevo portal garantizando la continuidad operativa del sistema legado[cite: 2, 3].

---

## 2. Decisión

Se decide implementar una **Estrategia de Integración Desacoplada en Modo Solo Lectura (Patrón Sidecar / Fachada Aislada)**. 

La nueva solución en Java 21 / Spring Boot se conectará de forma perimetral a la base de datos del sistema legado utilizando credenciales con permisos restringidos estrictamente a consultas (`GRANT SELECT`)[cite: 3]. Los datos extraídos serán replicados y mapeados mediante el ORM (Spring Data JPA) hacia la base de datos propia del portal (MariaDB)[cite: 3].

Bajo ninguna circunstancia se ejecutarán operaciones de escritura (`INSERT`, `UPDATE`, `DELETE`) ni se crearán procedimientos almacenados o *triggers* dentro de la base de datos del sistema en Delphi[cite: 3].

---

## 3. Consecuencias y Trade-offs

### Pros (Beneficios)
* **Tolerancia a Fallos y Protección del Núcleo:** Garantiza un 0% de riesgo de corrupción de datos en el sistema de nómina original, ya que el motor en Delphi opera aislado del tráfico de consultas web[cite: 2, 3].
* **Independencia Tecnológica:** Permite desarrollar el nuevo portal con un *stack* moderno (Java 21, Spring Boot, MariaDB, JWT) sin depender de la tecnología legada en Delphi[cite: 3].
* **Cumplimiento de Restricciones:** Respeta estrictamente la prohibición del cliente de no modificar el sistema antiguo[cite: 2, 3].

### Contras (Compromisos / Trade-offs Aceptados)
* **Duplicidad Controlada de Datos:** Se requiere espacio de almacenamiento adicional en MariaDB para alojar la réplica de los desprendibles e historiales de pago[cite: 3].
* **Latencia de Sincronización:** Existe una ventana de desfasaje temporal (eventual consistency) entre la generación de la nómina en Delphi y la finalización del proceso de replicación/extracción hacia el portal de autoconsulta[cite: 3].