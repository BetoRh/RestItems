package com.ibm.academia.apirest.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;

import com.ibm.academia.apirest.clients.ProductoClienteRest;
import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.models.entites.Item;



@Service("servicefeign")
@EntityScan({"com.ibm.academia.apirest.commons.models.entity"})
public class ItemServiceFeing implements ItemService {

	@Autowired
	private ProductoClienteRest clientefeing;
	
	@Override
	public List<Item> buscarTodos() {
		
		return clientefeing.listarProductos()
				.stream()
				.map(p -> new Item(p, 1))
				.collect(Collectors.toList());
	}

	@Override
	public Item buscarPorId(Long productoId, Integer cantidad) {
		
		return new Item(clientefeing.verDetalleProducto(productoId), cantidad);
	}

	@Override
	public Producto guardarProducto(Producto producto) {
		
		return clientefeing.crearProducto(producto);
	}

	@Override
	public Producto actualizarProducto(Producto producto, Long productId) {
		
		return clientefeing.actualizarProducto(producto, productId);
	}

	@Override
	public void eliminarProductoPorId(Long productoId) {
		
		clientefeing.eliminarProducto(productoId);
	}

}
