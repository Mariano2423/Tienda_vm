/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.service;

import com.tienda.domain.Role;
import com.tienda.repository.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    //Se crea un objeto de manera unica (solo una instancia)para todo el proyecto y de manera automatica
    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        var lista = roleRepository.findAll();
        
        return lista;
    }

    @Transactional(readOnly = true)
    public Role getRole(Role role) {

        return roleRepository.findById(role.getRol()).orElse(null);
    }

    @Transactional
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Transactional
    public boolean delete(Role role) {
        try {
            roleRepository.delete(role);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
