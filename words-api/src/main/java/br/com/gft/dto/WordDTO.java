package br.com.gft.dto;

import br.com.gft.entities.Word;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WordDTO {

	private Long id;
	private String word;

	public WordDTO(Word obj) {
		this.id = obj.getId();
		this.word = obj.getWord();
	}
}
