package br.com.gft.config;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String life_span;
	private String temperament;
	private String origin;

}
