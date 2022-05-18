package com.ibm.academia.apirest.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ibm.academia.apirest.commons.models.entity.Producto;



@FeignClient(name = "rest-productos")
public interface ProductoClienteRest {
	
	@GetMapping("/producto/listar")
	public List<Producto> listarProductos();
	
	@GetMapping("/producto/detalle/productoId/{productoId}")
	public Producto verDetalleProducto(@PathVariable Long productoId);
	
	@PostMapping("/producto/crear")
	public Producto crearProducto(@RequestBody Producto producto);
	
	@PutMapping("/producto/editar/productoId/{productoId}")
	public Producto actualizarProducto(@RequestBody Producto producto, @PathVariable Long productoId);
	
	@DeleteMapping("/producto/eliminar/productoId/{productoId}")
	public void eliminarProducto(@PathVariable Long productoId);
}