package com.nomina.portalnomina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 20, message = "La cédula no debe superar los 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no debe superar los 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El PIN es obligatorio")
    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    @Column(nullable = false, length = 20)
    private String rol = "empleado";

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}