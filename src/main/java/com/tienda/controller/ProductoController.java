/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

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
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    

    @GetMapping("/listado")
    public String listado(Model model) {

        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        model.addAttribute("totalProductos", lista.size());
        
        var categorias=categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        
        return "/producto/listado";
    }

    @Autowired
    private FirebaseStorageService firebaseStorageService;
    @Autowired
    private MessageSource messageSource;

    @PostMapping("/guardar")
    public String guardar(Producto producto,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        if (!imagenFile.isEmpty()) {
            productoService.save(producto);
            String rutaImagen
                    = firebaseStorageService
                            .cargaImagen(
                                    imagenFile,
                                    "producto",
                                    producto.getIdProducto());
            producto.setRutaImagen(rutaImagen);
        }
        productoService.save(producto);
        redirectAttributes.addFlashAttribute("todoOk",messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(Producto producto,
            RedirectAttributes redirectAttributes) {
        producto = productoService.getProducto(producto);
        if(producto==null){
            redirectAttributes.addFlashAttribute("error",messageSource.getMessage("producto.error01", null, Locale.getDefault()));
        } else if(false){// se modifica en semana8
            redirectAttributes.addFlashAttribute("error",messageSource.getMessage("producto.error02", null, Locale.getDefault()));
        } else if (productoService.delete(producto)) {
            redirectAttributes.addFlashAttribute("todoOk",messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));
        }
        else{
            redirectAttributes.addFlashAttribute("error",messageSource.getMessage("producto.error03", null, Locale.getDefault()));
        }
        productoService.delete(producto);

        return "redirect:/producto/listado";
    }

    @PostMapping("/modificar")
    public String modificar(Producto producto, Model model) {
        producto = productoService.getProducto(producto);
        model.addAttribute("producto", producto);

        var categorias=categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        
        return "/producto/modifica";
    }
}
