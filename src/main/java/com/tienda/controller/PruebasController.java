/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import com.tienda.service.FirebaseStorageService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pruebas")
public class PruebasController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(Model model) {

        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        return "/pruebas/listado";
    }

    @GetMapping("/listado/{idCategoria}")
    public String listado(Categoria categoria, Model model) {
        categoria = categoriaService.getCategoria(categoria);

        var lista = categoria.getProductos();
        model.addAttribute("productos", lista);

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        return "/pruebas/listado";
    }

    @GetMapping("/listado2")
    public String listado2(Model model) {

        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);

        return "/pruebas/listado2";
    }

    @PostMapping("/query1")
    public String query1(@RequestParam() double precioInf,
            @RequestParam() double precioSup,
            Model model) {
        var lista = productoService.consultaAmpliada(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("productoInf", precioInf);
        model.addAttribute("productoSup", precioSup);
        return "/pruebas/listado2";
    }

    @PostMapping("/query2")
    public String query2(@RequestParam() double precioInf,
            @RequestParam() double precioSup,
            Model model) {
        var lista = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("productoInf", precioInf);
        model.addAttribute("productoSup", precioSup);
        return "/pruebas/listado2";
    }

    @PostMapping("/query3")
    public String query3(@RequestParam() double precioInf,
            @RequestParam() double precioSup,
            Model model) {
        var lista = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("productoInf", precioInf);
        model.addAttribute("productoSup", precioSup);
        return "/pruebas/listado2";
    }

    @PostMapping("/queryInventario")
    public String consultarPorInventario(
            @RequestParam("maxCantidad") int maxCantidad,
            Model model) {

        var productos = productoService.getProductosConExistenciasMenoresA(maxCantidad);
        var categorias = categoriaService.getCategorias(true);

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        return "/pruebas/listado2";
    }
}
