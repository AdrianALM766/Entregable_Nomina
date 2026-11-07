package com.nomina.portalnomina.service;

import com.nomina.portalnomina.model.Usuario;
import com.nomina.portalnomina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setActivo(true);
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("empleado");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario consultarPorCedula(String cedula) {
        return usuarioRepository.findByCedulaAndActivoTrue(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con cédula: " + cedula));
    }

    //metodo que lista los usuarios activos de la bd
    public List<Usuario> listarUsuariosActivos() {
    return usuarioRepository.findByActivoTrue();
}

    // Método para actualización parcial de datos (PATCH)
    public Usuario actualizarUsuarioParcial(String cedula, Map<String, Object> camposAActualizar) {
        // 1. Buscar el usuario existente
        Usuario usuarioExistente = consultarPorCedula(cedula);

        // 2. Iterar sobre las llaves enviadas y actualizar solo los campos permitidos
        camposAActualizar.forEach((clave, valor) -> {
            switch (clave) {
                case "nombre":
                    if (valor != null) usuarioExistente.setNombre((String) valor);
                    break;
                case "rol":
                    if (valor != null) usuarioExistente.setRol((String) valor);
                    break;
                case "pinHash":
                    if (valor != null) usuarioExistente.setPinHash((String) valor);
                    break;
                // Ignorar cedula, id o createdAt para proteger la integridad del registro
                default:
                    break;
            }
        });

        // 3. Guardar los cambios en MariaDB
        return usuarioRepository.save(usuarioExistente);
    }

    // Nuevo método para Borrado Lógico (DELETE)
    public void eliminarUsuarioLogico(String cedula) {
        Usuario usuario = consultarPorCedula(cedula);
        usuario.setActivo(false); // Desactivar el registro en lugar de borrar la fila física
        usuarioRepository.save(usuario);
    }
}