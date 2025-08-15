package com.tienda.controller;

import com.tienda.repository.ReporteRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private ReporteRepository reporteRepository;

    // Página principal de reportes (para que el link del menú funcione)
    @GetMapping("/principal")
    public String principal() {
        return "reportes/principal";
    }

    // --------- REPORTE 1: Inventario bajo (HTML) ----------
    @GetMapping("/inventario")
    public String inventario(
            @RequestParam(defaultValue = "10") int umbral,
            @RequestParam(required = false) Long idCategoria,
            Model model
    ) {
        List<Object[]> categorias = reporteRepository.categorias();
        List<Object[]> data = reporteRepository.inventarioBajo(umbral, idCategoria);

        double totalValor = 0.0;
        for (Object[] r : data) {
            if (r[5] != null) totalValor += ((Number) r[5]).doubleValue(); // valor
        }

        model.addAttribute("categorias", categorias);
        model.addAttribute("umbral", umbral);
        model.addAttribute("idCategoria", idCategoria);
        model.addAttribute("data", data);
        model.addAttribute("totalValor", totalValor);
        return "reportes/inventario";
    }

    // --------- REPORTE 1: Inventario bajo (CSV) ----------
    @GetMapping("/inventario/csv")
    public void inventarioCsv(
            @RequestParam(defaultValue = "10") int umbral,
            @RequestParam(required = false) Long idCategoria,
            HttpServletResponse response
    ) throws Exception {
        List<Object[]> data = reporteRepository.inventarioBajo(umbral, idCategoria);
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_bajo.csv");

        try (PrintWriter out = response.getWriter()) {
            out.println("ID,Producto,Categoria,Existencias,Precio,Valor");
            for (Object[] r : data) {
                Long id      = ((Number) r[0]).longValue();
                String prod  = (String) r[1];
                String cat   = (String) r[2];
                int exi      = ((Number) r[3]).intValue();
                double pre   = ((Number) r[4]).doubleValue();
                double valor = ((Number) r[5]).doubleValue();
                out.printf("%d,%s,%s,%d,%.2f,%.2f%n", id, esc(prod), esc(cat), exi, pre, valor);
            }
        }
    }

    // --------- REPORTE 2: Ventas por rango (HTML) ----------
    @GetMapping("/ventas")
    public String ventas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta,
            Model model
    ) {
        // Si no mandan fechas, por defecto últimos 30 días
        if (desde == null || hasta == null) {
            Calendar cal = Calendar.getInstance();
            hasta = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            desde = cal.getTime();
        }

        List<Object[]> data = reporteRepository.ventasPorRango(desde, hasta);

        long totalUnidades = 0L;
        double totalMonto = 0.0;
        for (Object[] r : data) {
            totalUnidades += ((Number) r[2]).longValue();   // unidades
            totalMonto    += ((Number) r[3]).doubleValue(); // total
        }

        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("data", data);
        model.addAttribute("totalUnidades", totalUnidades);
        model.addAttribute("totalMonto", totalMonto);
        return "reportes/ventas";
    }

    // --------- REPORTE 2: Ventas por rango (CSV) ----------
    @GetMapping("/ventas/csv")
    public void ventasCsv(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta,
            HttpServletResponse response
    ) throws Exception {
        List<Object[]> data = reporteRepository.ventasPorRango(desde, hasta);
        response.setContentType("text/csv; charset=UTF-8");
        String nombre = "ventas_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombre);

        try (PrintWriter out = response.getWriter()) {
            out.println("ID,Producto,Unidades,Total");
            for (Object[] r : data) {
                Long id       = ((Number) r[0]).longValue();
                String prod   = (String) r[1];
                long unidades = ((Number) r[2]).longValue();
                double total  = ((Number) r[3]).doubleValue();
                out.printf("%d,%s,%d,%.2f%n", id, esc(prod), unidades, total);
            }
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}