package com.nomina.portalnomina.service;

import com.nomina.portalnomina.model.Usuario;
import com.nomina.portalnomina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setActivo(true);
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("empleado");
        }

        if (usuario.getPinHash() != null && !usuario.getPinHash().isEmpty()) {
            String pinCifrado = passwordEncoder.encode(usuario.getPinHash());
            usuario.setPinHash(pinCifrado);
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario consultarPorCedula(String cedula) {
        return usuarioRepository.findByCedulaAndActivoTrue(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo con cédula: " + cedula));
    }

    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    public Usuario actualizarUsuarioParcial(String cedula, Map<String, Object> camposAActualizar) {
        if (camposAActualizar == null || camposAActualizar.isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos un campo para actualizar.");
        }

        Usuario usuarioExistente = consultarPorCedula(cedula);

        camposAActualizar.forEach((clave, valor) -> {
            switch (clave) {
                case "nombre":
                    if (valor == null || ((String) valor).trim().isEmpty()) {
                        throw new IllegalArgumentException("El nombre no puede estar vacío.");
                    }
                    usuarioExistente.setNombre((String) valor);
                    break;
                case "rol":
                    if (valor == null || ((String) valor).trim().isEmpty()) {
                        throw new IllegalArgumentException("El rol no puede estar vacío.");
                    }
                    usuarioExistente.setRol((String) valor);
                    break;
                case "pinHash":
                    if (valor == null || ((String) valor).trim().isEmpty()) {
                        throw new IllegalArgumentException("El PIN no puede estar vacío.");
                    }
                    usuarioExistente.setPinHash(passwordEncoder.encode((String) valor));
                    break;
                default:
                    break;
            }
        });

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuarioLogico(String cedula) {
        Usuario usuario = consultarPorCedula(cedula);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String cedula, String pinPlano) {
        if (cedula == null || cedula.trim().isEmpty() || pinPlano == null || pinPlano.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar cédula y PIN.");
        }

        Usuario usuario = usuarioRepository.findByCedulaAndActivoTrue(cedula)
                .orElseThrow(() -> new SecurityException("Credenciales inválidas o usuario inactivo."));

        if (!passwordEncoder.matches(pinPlano, usuario.getPinHash())) {
            throw new SecurityException("Credenciales inválidas.");
        }

        return usuario;
    }
}