/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.service;

import com.tienda.domain.Rol;
import com.tienda.repository.RolRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    //Se crea un objeto de manera unica (solo una instancia)para todo el proyecto y de manera automatica
    @Autowired
    private RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<Rol> getRols() {
        var lista = rolRepository.findAll();
        
        return lista;
    }

    @Transactional(readOnly = true)
    public Rol getRol(Rol rol) {

        return rolRepository.findById(rol.getIdRol()).orElse(null);
    }

    @Transactional
    public void save(Rol rol) {
        rolRepository.save(rol);
    }

    @Transactional
    public boolean delete(Rol rol) {
        try {
            rolRepository.delete(rol);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
