/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tienda.repository;

import com.tienda.domain.Constante;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConstanteRepository extends JpaRepository<Constante,Long>{
    public Constante findByAtributo (String atributo);
}
