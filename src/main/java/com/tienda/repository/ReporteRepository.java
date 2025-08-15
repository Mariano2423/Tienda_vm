package com.tienda.repository;

import com.tienda.domain.Producto;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReporteRepository extends org.springframework.data.jpa.repository.JpaRepository<Producto, Long>  {

    // Para poblar el combo de categorías (id, descripcion)
    @Query(value = """
        SELECT c.id_categoria, c.descripcion
        FROM categoria c
        ORDER BY c.descripcion
        """, nativeQuery = true)
    List<Object[]> categorias();

    // REPORTE 1: Inventario bajo (umbral + categoría opcional)
    @Query(value = """
        SELECT 
            p.id_producto,             -- [0] Long
            p.descripcion,             -- [1] String
            c.descripcion,             -- [2] String (categoria)
            p.existencias,             -- [3] Integer
            p.precio,                  -- [4] Double
            (p.existencias * p.precio) -- [5] Double (valor)
        FROM producto p
        JOIN categoria c ON p.id_categoria = c.id_categoria
        WHERE p.activo = 1
          AND p.existencias <= :umbral
          AND (:idCategoria IS NULL OR c.id_categoria = :idCategoria)
        ORDER BY p.existencias ASC
        """, nativeQuery = true)
    List<Object[]> inventarioBajo(
            @Param("umbral") int umbral,
            @Param("idCategoria") Long idCategoria
    );

    // REPORTE 2: Ventas por rango de fechas (desde/hasta)
    @Query(value = """
        SELECT 
            v.id_producto,               -- [0] Long
            p.descripcion,               -- [1] String
            SUM(v.cantidad),             -- [2] Long
            SUM(v.cantidad*v.precio)     -- [3] Double
        FROM venta v
        JOIN factura f  ON v.id_factura  = f.id_factura
        JOIN producto p ON v.id_producto = p.id_producto
        WHERE f.fecha BETWEEN :desde AND :hasta
        GROUP BY v.id_producto, p.descripcion
        ORDER BY 4 DESC
        """, nativeQuery = true)
    List<Object[]> ventasPorRango(
            @Param("desde") Date desde,
            @Param("hasta") Date hasta
    );
}