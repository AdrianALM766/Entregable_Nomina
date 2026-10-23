package com.nomina.portalnomina.controller;

import com.nomina.portalnomina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        // Verifica la conexión a la base de datos MariaDB contando los registros
        long cantidadUsuarios = usuarioRepository.count();
        
        response.put("status", "UP");
        response.put("message", "Servidor Spring Boot de Nómina corriendo correctamente");
        response.put("database", "MariaDB conectada con éxito");
        response.put("total_usuarios", cantidadUsuarios);
        
        return response;
    }
}