5.2 Script SQL DDL / Migraciones

Justificación de la Estructura DDL e Integridad Referencial:
• Claves Primarias (PK) y AUTO_INCREMENT: Todas las tablas incorporan su respectivo identificador único secuencial id.
• Claves Foráneas (FK) con REFERENCES: La tabla desprendibles vincula a usuarios mediante fk_desprendibles_usuario, previniendo huérfanos de nómina.
• Restricciones CHECK:
  - CHECK (rol IN ('empleado', 'admin')): Garantiza que los roles asignados pertenezcan estrictamente a los perfiles permitidos en el sistema.
  - CHECK (monto_neto >= 0): Valida que los valores financieros de pago sean consistentes y no contengan saldos negativos no válidos.
• Restricciones UNIQUE:
  - UNIQUE (cedula): Impide la creación duplicada de cuentas para un mismo número de identificación.
  - CONSTRAINT uq_desprendible_periodo UNIQUE (usuario_id, periodo_quincena): Asegura que exista un único comprobante registrado por empleado para cada periodo quincenal.
• Borrado Lógico (activo): Implementado en las entidades principales mediante la columna activo BOOLEAN NOT NULL DEFAULT TRUE. Las eliminaciones o desactivaciones de cuentas/comprobantes se realizan mediante actualizaciones lógicas (UPDATE usuarios SET activo = false), preservando la integridad de la auditoría y los registros históricos de pagos sin ejecutar borrados físicos (DELETE).
• Índices Justificados (CREATE INDEX):
  - idx_usuarios_cedula: Optimiza el tiempo de respuesta en las operaciones de login por cédula (< 2 segundos).
  - idx_desprendibles_usuario_periodo: Acelera las consultas simultáneas en picos quincenales cuando los 350 empleados ingresan a descargar su PDF.