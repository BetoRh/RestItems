package com.ibm.academia.apirest.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.models.entites.Item;


@Service("serviceRestTemplate")
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private RestTemplate clienteRest;

	@Override
	@Transactional(readOnly = true)
	public List<Item> buscarTodos() {

		List<Producto> productos = Arrays.asList(clienteRest.getForObject("http://rest-productos/producto/listar", Producto[].class));	
		return productos
				.stream()
				.map(p -> new Item(p,1))
				.collect(Collectors.toList());
	}

	@Override
	public Item buscarPorId(Long productoId, Integer cantidad) 
	{
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("productoId", productoId.toString());
		
		Producto producto = clienteRest.getForObject("http://rest-productos/producto/detalle/productoId/{productoId}", Producto.class, variables);
		return new Item(producto, cantidad);
	}
	
	@Override
	public Producto guardarProducto(Producto producto) 
	{
		HttpEntity<Producto> request = new HttpEntity<Producto>(producto);
		
		ResponseEntity<Producto> response = clienteRest.exchange("http://rest-productos/producto/crear", HttpMethod.POST, request, Producto.class);
		Producto productoResponse = response.getBody();
		
		return productoResponse;
	}

	@Override
	public void eliminarProductoPorId(Long productoId) 
	{
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("productoId", productoId.toString());
		
		clienteRest.delete("http://rest-productos/producto/eliminar/productoId/{productoId}", variables);
	}

	@Override
	public Producto actualizarProducto(Producto producto, Long productoId) 
	{
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("productoId", productoId.toString());
		
		HttpEntity<Producto> request = new HttpEntity<Producto>(producto);
		
		ResponseEntity<Producto> response = clienteRest.exchange("http://rest-productos/producto/editar/productoId/{productoId}", HttpMethod.PUT, request, Producto.class, variables);
		
		return response.getBody();
	}
	
	
	

}
