# ADR-002: Estrategia de autenticación de operarios mediante Cédula, PIN hasheado (bcrypt) y Tokens JWT

* **Estatus:** Aceptado
* **Fecha:** 2026-10-24
* **Autores:** Estudiante (Arquitecto de Software)

---

## 1. Contexto

Los 350 empleados de la planta industrial necesitan un mecanismo de acceso sencillo y de baja fricción para consultar sus desprendibles de pago desde sus dispositivos móviles o computadores de la planta[cite: 2, 3]. 

Muchos operarios no cuentan con correo electrónico corporativo activo ni memorizan contraseñas complejas con caracteres especiales[cite: 2]. Sin embargo, la información salarial resguardada es de naturaleza estrictamente confidencial (cumplimiento de Ley 1581 de 2012 / Habeas Data)[cite: 2, 3]. Se requiere definir el mecanismo de autenticación y manejo de sesiones de la API REST que garantice la privacidad sin complicar la usabilidad de los trabajadores de planta[cite: 2, 3].

---

## 2. Decisión

Se decide implementar un **Esquema de Autenticación de Cédula + PIN de 4 dígitos** respaldado por **Tokens de Sesión Stateless (JSON Web Tokens - JWT)** y almacenamiento de claves hasheadas con **bcrypt**[cite: 2, 3].

1. **Primer Ingreso:** El empleado ingresa su Cédula; el portal valida contra la lista sincronizada de la nómina y le permite crear un PIN numérico personal de 4 dígitos[cite: 2].
2. **Resguardo de Credenciales:** El PIN jamás se almacena en texto plano en la base de datos (MariaDB); se guarda aplicando la función hash bcrypt con factor de costo[cite: 2, 3].
3. **Manejo de Sesión:** Tras validar Cédula y PIN, el Backend (Spring Boot) emite un JWT firmado digitalmente con un tiempo de expiración corto (ej. 15 minutos), el cual debe enviarse en la cabecera HTTP `Authorization: Bearer <token>` para cada consulta de PDF[cite: 2, 3].

---

## 3. Consecuencias y Trade-offs

### Pros (Beneficios)
* **Alta Usabilidad para Planta:** Facilita el ingreso rápido de los operarios ingresando un código numérico sencillo (PIN) en pantalla sin requerir cuentas de correo[cite: 2].
* **Arquitectura REST sin Estado (Stateless):** El servidor no almacena sesiones en memoria (HttpSession), mejorando la escalabilidad y eficiencia de respuesta[cite: 2, 3].
* **Seguridad y Confidencialidad (AC-01):** Garantiza que si la base de datos es vulnerada, los PINs no puedan ser reconstruidos (bcrypt)[cite: 2, 3]. Cualquier petición sin token válido es rechazada inmediatamente con HTTP 401[cite: 2, 3].

### Contras (Compromisos / Trade-offs Aceptados)
* **Menor entropía de clave:** Un PIN de 4 dígitos tiene una combinación menor que una contraseña alfa-numérica compleja[cite: 2]. Para mitigar este riesgo, el sistema implementa bloqueo automático de la cuenta tras 5 intentos fallidos consecutivos[cite: 2].
* **Gestión de Revocación de Tokens:** Al ser tokens *stateless*, revocar un JWT antes de su expiración requiere mantener una lista negra (*blacklist*) en caché/BD si un usuario decide cerrar sesión explícitamente[cite: 2, 3].