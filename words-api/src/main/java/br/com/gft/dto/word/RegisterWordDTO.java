package br.com.gft.dto.word;

import java.util.Set;

import br.com.gft.entities.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWordDTO {

	private String name;
	private Set<Tag> tags;

}
