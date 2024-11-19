package com.example.backend.service;

import com.example.backend.dto.ReseniaDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.Libro;
import com.example.backend.model.Resenia;
import com.example.backend.model.Usuario;
import com.example.backend.repository.LibroRepository;
import com.example.backend.repository.ReseniaRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ReseniaServiceImpl implements ReseniaService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ReseniaRepository  reseniaRepository;

    @Override
    public Resenia crearResenia(ReseniaDTO reseniaDTO, Long idLibro, Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el ID: " + idUsuario));

        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + idLibro));

        Resenia resenia = Resenia.builder()
                .usuario(usuario)
                .libro(libro)
                .calificacion(reseniaDTO.getCalificacion())
                .comentario(reseniaDTO.getComentario())
                .fechaPublicacion(LocalDate.now())
                .build();

        return  reseniaRepository.save(resenia);
    }

    @Override
    public Resenia editarResenia(ReseniaDTO reseniaDTO, Long idResenia){
        Resenia resenia = reseniaRepository.findById(idResenia)
                .orElseThrow(() -> new ResourceNotFoundException("Resenia no encontrada con el ID: " + idResenia));

        resenia.setCalificacion(reseniaDTO.getCalificacion());
        resenia.setComentario(reseniaDTO.getComentario());
        resenia.setFechaPublicacion(LocalDate.now());

        return reseniaRepository.save(resenia);
    }

    @Override
    public void eliminarResenia(Long idResenia){
        Resenia resenia = reseniaRepository.findById(idResenia)
                .orElseThrow(() -> new ResourceNotFoundException("Resenia no encontrada con el ID: " + idResenia));

        reseniaRepository.deleteById(idResenia);
    }

    @Override
    public Set<Resenia> mostrarReseniaPorLibro(Long idLibro){
        Libro libro = libroRepository.findById(idLibro)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + idLibro));

        return reseniaRepository.findAllByLibroId(idLibro);
    }
}
