-- db/migrations/001_esquema_inicial.sql
-- Creación física del esquema de base de datos para el Portal de Nómina (MariaDB / MySQL)

-- 1. Tabla de Usuarios (Credenciales del Portal)
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'empleado' CHECK (rol IN ('empleado', 'admin')),
    activo BOOLEAN NOT NULL DEFAULT TRUE, -- Borrado lógico
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Tabla de Desprendibles de Nómina (Capa de Réplica / Extracción en Solo Lectura)
CREATE TABLE desprendibles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    periodo_quincena VARCHAR(20) NOT NULL, -- Ej: '2026-10-Q1'
    fecha_pago DATE NOT NULL,
    monto_neto DECIMAL(12, 2) NOT NULL CHECK (monto_neto >= 0),
    ruta_pdf VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'disponible' CHECK (estado IN ('disponible', 'consultado', 'archivado')),
    activo BOOLEAN NOT NULL DEFAULT TRUE, -- Borrado lógico
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_desprendibles_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT uq_desprendible_periodo UNIQUE (usuario_id, periodo_quincena)
);

-- 3. Tabla de Registro de Auditoría de Accesos (Privacidad / Habeas Data)
CREATE TABLE auditoria_accesos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    accion VARCHAR(50) NOT NULL, -- Ej: 'LOGIN_EXITOSO', 'CONSULTA_PDF'
    ip_origen VARCHAR(45) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- 4. Índices Justificados para Optimización de Rendimiento
-- Índice en la cédula para acelerar las peticiones de autenticación (LOGIN)
CREATE INDEX idx_usuarios_cedula ON usuarios(cedula);

-- Índice compuesto en desprendibles para agilizar las búsquedas quincenales por empleado
CREATE INDEX idx_desprendibles_usuario_periodo ON desprendibles(usuario_id, periodo_quincena);