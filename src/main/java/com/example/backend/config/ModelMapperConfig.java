package com.example.backend.config;

import com.example.backend.dto.LibroDTO;
import com.example.backend.model.Categoria;
import com.example.backend.model.Editorial;
import com.example.backend.model.Libro;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        // Configuración para Libro a LibroDTO
        TypeMap<Libro, LibroDTO> libroToLibroDtoMap = modelMapper.createTypeMap(Libro.class, LibroDTO.class);

        // Convertidor personalizado para categorías
        Converter<Set<Categoria>, Set<Long>> categoriaToIdConverter = context -> {
            if (context.getSource() == null) {
                return new HashSet<>();
            }
            return context.getSource().stream()
                    .map(Categoria::getId)
                    .collect(Collectors.toSet());
        };

        // Convertidor para conversión inversa de IDs a Categorias
        Converter<Set<Long>, Set<Categoria>> idToCategoriaConverter = context -> {
            if (context.getSource() == null) {
                return new HashSet<>();
            }
            return context.getSource().stream()
                    .map(id -> {
                        Categoria categoria = new Categoria();
                        categoria.setId(id);
                        return categoria;
                    })
                    .collect(Collectors.toSet());
        };

        // Mapeo de categorías en Libro a LibroDTO
        libroToLibroDtoMap.addMappings(mapper ->
                mapper.using(categoriaToIdConverter)
                        .map(Libro::getCategorias, LibroDTO::setCategorias)
        );

        // Configuración para LibroDTO a Libro
        TypeMap<LibroDTO, Libro> libroDtoToLibroMap = modelMapper.createTypeMap(LibroDTO.class, Libro.class);

        // Mapeo de categorías en LibroDTO a Libro
        libroDtoToLibroMap.addMappings(mapper ->
                mapper.using(idToCategoriaConverter)
                        .map(LibroDTO::getCategorias, Libro::setCategorias)
        );

        // Mapeo de Editorial
        libroToLibroDtoMap.addMapping(
                src -> src.getEditorial().getId(),
                (dest, value) -> dest.setEditorialId((Long) value)
        );

        libroDtoToLibroMap.addMapping(
                LibroDTO::getEditorialId,
                (dest, value) -> {
                    Editorial editorial = new Editorial();
                    editorial.setId((Long) value);
                    dest.setEditorial(editorial);
                }
        );


        return modelMapper;
    }
}
