/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    //Se crea un objeto de manera unica (solo una instancia)para todo el proyecto y de manera automatica
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        var lista = categoriaRepository.findAll();

        // Se valida si solo se desean las categorias activas...
        if (activo) {
            //Solo se quieren activas...
            lista.removeIf(c -> !c.isActivo());
        }

        return lista;
    }

    @Transactional(readOnly = true)
    public Categoria getCategoria(Categoria categoria) {

        return categoriaRepository.findById(categoria.getIdCategoria())
                .orElse(null);
    }

    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }
}
