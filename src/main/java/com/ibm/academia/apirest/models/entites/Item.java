package com.ibm.academia.apirest.models.entites;

import java.io.Serializable;

import com.ibm.academia.apirest.commons.models.entity.Producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable{

	private Producto producto;
	private Integer cantidad;
	
	public Double getTotal() {
		
		return producto.getPrecio() * cantidad;
	}
	
	
	private static final long serialVersionUID = -6283527613284334391L;

}
