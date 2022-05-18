package com.ibm.academia.apirest.services;

import java.util.List;

import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.models.entites.Item;


public interface ItemService {
	
	public List<Item> buscarTodos();
	
	public Item buscarPorId(Long productoId, Integer cantidad);
	public Producto guardarProducto(Producto producto);
	public Producto actualizarProducto(Producto producto, Long productId);
	public void eliminarProductoPorId(Long productoId);

}
