package br.com.gft.dto;

import java.util.Set;

import br.com.gft.entities.Word;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

	private Long id;
	private String name;
	private Set<Word> words;

}
