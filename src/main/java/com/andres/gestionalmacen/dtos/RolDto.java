package com.andres.gestionalmacen.dtos;
/**
 * Clase que representa un rol.
 * 
 * @author Andrés
 */
public class RolDto {
	 private Long id;
	 private String nombre;  

	 public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
