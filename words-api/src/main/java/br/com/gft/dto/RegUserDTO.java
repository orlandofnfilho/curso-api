package br.com.gft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegUserDTO {

	private String nome;
	private String email;
	private String password;
	private Long perfilId;

}
