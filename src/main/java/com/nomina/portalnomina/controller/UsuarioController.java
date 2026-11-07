package com.nomina.portalnomina.controller;

import com.nomina.portalnomina.model.Usuario;
import com.nomina.portalnomina.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuariosActivos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioGuardado = usuarioService.registrarUsuario(usuario);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
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
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/actualizar/{cedula}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String cedula, @RequestBody Map<String, Object> campos) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuarioParcial(cedula, campos);
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminar/{cedula}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String cedula) {
        try {
            usuarioService.eliminarUsuarioLogico(cedula);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Usuario inhabilitado (borrado lógico) correctamente.");
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}