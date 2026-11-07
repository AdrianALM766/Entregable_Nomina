package com.nomina.portalnomina.controller;

import com.nomina.portalnomina.model.Usuario;
import com.nomina.portalnomina.service.UsuarioService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioGuardado = usuarioService.registrarUsuario(usuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // Manejo del error 409 Conflict si intentas registrar la misma cédula
            Map<String, String> error = new HashMap<>();
            error.put("error", "Conflicto: La cédula ingresada ya se encuentra registrada.");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/consultar/{cedula}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable String cedula) {
        try {
            Usuario usuario = usuarioService.consultarPorCedula(cedula);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Manejo del error 404 Not Found si la cédula no existe
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

}