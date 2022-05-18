package com.ibm.academia.apirest.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.academia.apirest.exception.NotFoundException;
import com.ibm.academia.apirest.models.entites.Item;
import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/item")
public class ItemController {

	private final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	@Qualifier("servicefeign")
	private ItemService itemService;

	@Autowired
	private CircuitBreakerFactory cbFactory;

	@GetMapping("/listar")
	public ResponseEntity<?> listar() {

		List<Item> items = itemService.buscarTodos();
		if (items.isEmpty())
			throw new NotFoundException("No existen items en la bd.");

		return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
	}

	@GetMapping("/detalle/productoId/{productoId}/cantidad/{cantidad}")
	public ResponseEntity<?> verDetalleItem(@PathVariable Long productoId, @PathVariable Integer cantidad) {

		Item item = cbFactory.create("items").run(() -> itemService.buscarPorId(productoId, cantidad),
				e -> metodoAlternativo(productoId, cantidad, e));

		return new ResponseEntity<Item>(item, HttpStatus.OK);

	}

	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
	@GetMapping("/detalle2/productoId/{productoId}/cantidad/{cantidad}")
	public CompletableFuture<Item> verDetalleItemAnotaciones(@PathVariable Long productoId, @PathVariable Integer cantidad)
	{
		return CompletableFuture.supplyAsync(() -> itemService.buscarPorId(productoId, cantidad)); 
	}
	
	@CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
	@TimeLimiter(name = "items")
	@GetMapping("/detalle3/productoId/{productoId}/cantidad/{cantidad}")
	public CompletableFuture<Item> verDetalleItemAnotaciones2(@PathVariable Long productoId, @PathVariable Integer cantidad) {

		return CompletableFuture.supplyAsync(() -> itemService.buscarPorId(productoId, cantidad));
	}
	
	/**
	 * Endpoint para crear un producto desde item
	 * @param producto
	 * @return
	 * @author RAJA - 30-12-2021
	 */
	@PostMapping("/crear")
	public ResponseEntity<?> crearProducto(@RequestBody Producto producto) 
	{
		Producto productoGuardado = itemService.guardarProducto(producto); 
		return new ResponseEntity<Producto>(productoGuardado, HttpStatus.CREATED); 
	}
	
	/**
	 * Endpoint para modificar un producto desde item 
	 * @param producto
	 * @param productoId
	 * @return
	 * @author RAJA - 30-12-2021
	 */
	@PutMapping("/editar/productoId/{productoId}")
	public ResponseEntity<?> editarProducto(@RequestBody Producto producto, @PathVariable Long productoId) 
	{
		Producto productoActualizado = itemService.actualizarProducto(producto, productoId); 
		return new ResponseEntity<Producto>(productoActualizado, HttpStatus.OK);
	}
	
	/**
	 * Endpoint para eliminar un producto desde item
	 * @param productoId
	 * @return
	 * @author RAJA - 30-12-2021
	 */
	@DeleteMapping("/eliminar/productoId/{productoId}")
	public ResponseEntity<?> eliminar(@PathVariable Long productoId) 
	{
		itemService.eliminarProductoPorId(productoId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	public Item metodoAlternativo(Long productoId, Integer cantidad, Throwable e)
	{
		log.info(e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();

		item.setCantidad(cantidad);
		producto.setId(productoId);
		producto.setNombre("Camara Sony");
		producto.setPrecio(500.00);
		item.setProducto(producto);

		return item;
	}

	public CompletableFuture<Item> metodoAlternativo2(Long productoId, Integer cantidad, Throwable e) {
		log.info(e.getMessage());

		Item item = new Item();
		Producto producto = new Producto();

		item.setCantidad(cantidad);
		producto.setId(productoId);
		producto.setNombre("Camara Samsung Galaxy");
		producto.setPrecio(1500.00);
		item.setProducto(producto);

		return CompletableFuture.supplyAsync(() -> item);
	}
}
