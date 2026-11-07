package com.nomina.portalnomina.service;

import com.nomina.portalnomina.model.Usuario;
import com.nomina.portalnomina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        // Regla de negocio inicial: forzar que esté activo y asignar rol si viene vacío
        usuario.setActivo(true);
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("empleado");
        }
        
        // Guarda físicamente la fila en la tabla usuarios de MariaDB
        return usuarioRepository.save(usuario);
    }

    //Consultar con (GET)
    public Usuario consultarPorCedula(String cedula) {
        return usuarioRepository.findByCedulaAndActivoTrue(cedula)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cedula: "+ cedula));
    }
}