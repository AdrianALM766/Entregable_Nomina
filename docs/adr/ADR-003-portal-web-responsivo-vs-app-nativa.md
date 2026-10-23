# ADR-003: Selección de Portal Web Responsivo (Web App) sobre Aplicación Móvil Nativa o WhatsApp API Oficial

* **Estatus:** Aceptado
* **Fecha:** 2026-10-24
* **Autores:** Estudiante (Arquitecto de Software)

---

## 1. Contexto

La empresa busca eliminar el proceso manual quincenal donde Recursos Humanos envía 350 PDFs individuales por WhatsApp[cite: 2, 3]. 

Los 350 empleados poseen dispositivos móviles de diversas marcas, sistemas operativos (Android / iOS) y gamas heterogéneas[cite: 2, 3]. Asimismo, la empresa manufacturera no vende software y posee una restricción administrativa de minimizar los costos operativos recurrentes de infraestructura[cite: 2, 3]. 

Se debe decidir la interfaz y canal tecnológico a través del cual los empleados consultarán y descargarán sus comprobantes de pago[cite: 2, 3].

---

## 2. Decisión

Se decide desarrollar un **Portal Web Responsivo (Progressive Web App / Web App)** construido sobre tecnologías estándar (**HTML5, Bootstrap 5 y JavaScript Vainilla**) expuesto mediante la API REST en Spring Boot[cite: 2, 3].

Se descarta formalmente el desarrollo de una **Aplicación Móvil Nativa (Android/iOS)** y la integración transaccional con la **API Oficial de WhatsApp Business (Meta)**[cite: 2, 3].

---

## 3. Consecuencias y Trade-offs

### Pros (Beneficios)
* **Cero Instalación y Multiplataforma:** Los empleados pueden acceder al portal desde cualquier navegador web (Chrome, Safari, Edge) en teléfonos, tablets o computadores de planta sin instalar nada de las tiendas (Play Store / App Store)[cite: 2].
* **Costo Operativo Cero en Mensajería:** Evita los costos recurrentes por mensaje/conversación que cobra Meta por la API de WhatsApp Business, cumpliendo la restricción de bajo costo[cite: 2, 3].
* **Mantenibilidad Simplificada:** Al haber un único código base web, las actualizaciones se reflejan de inmediato para los 350 usuarios sin requerir despliegues ni descargas móviles adicionales[cite: 2, 3].

### Contras (Compromisos / Trade-offs Aceptados)
* **Pérdida de Canal Directo en Chat:** Los empleados deben ingresar a una URL en lugar de recibir el documento directamente dentro del chat de WhatsApp como estaban acostumbrados de forma manual[cite: 2].
* **Dependencia de Conectividad a Internet/Intranet:** Requiere que el teléfono o equipo de planta tenga acceso a la red donde se aloja el portal para descargar el comprobante[cite: 2, 3].