package br.com.gft.config;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.gft.entities.Perfil;
import br.com.gft.entities.User;
import br.com.gft.repositories.PerfilRepository;
import br.com.gft.repositories.UserRepository;

@Component
@Transactional
public class StartDb implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PerfilRepository perfilRepository;
	
	
	public String gerar(int len){
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(chars.charAt(rnd.nextInt(chars.length())));
		return sb.toString();
	}
	
	@Override
	public void run(String... args) throws Exception {

		Perfil p1 = new Perfil(null, "ADMIN");
		Perfil p2 = new Perfil(null, "USUARIO");
		
		Test obj = new RestTemplate().getForObject("https://api.thedogapi.com/v1/breeds/{q}", Test.class,
				1);
			
	System.out.println(obj);
	

	String name = "Aff";
	
	Test[] obj2 = new RestTemplate().getForObject("https://api.thedogapi.com/v1/breeds/search?q={name}", Test[].class,
			name);
		
     
	List<Test> obj3 = List.of(obj2);
	
	System.out.println("getForObject1: "+Arrays.asList(obj2));
	obj3.forEach(System.out::println);
	
	
	
	Test obj4 = obj3.get(0);
	
	System.out.println("getForObject2: "+obj4);

       

    String nome = this.gerar(6);
    System.out.println("RandomCode: "+nome);
	
	
	HttpHeaders headers = new HttpHeaders();
	
	String name2 = "Affenpinscher";
    
	headers.set("x-api-key", "ce3c5fd-feb4-4bae-bfb6-b6eb22f57724");
	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
	
	ResponseEntity<List<Test>> response = new RestTemplate().exchange(
			"https://api.thedogapi.com/v1/breeds/search?q="+name2, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {});
	
	
	System.out.println("Exchange1: "+response.getBody());
	System.out.println("Tamanho: "+response.getBody().size());
	System.out.println("Exchange Headers: "+response.getHeaders());
	
	String someString = "123@,.";
	boolean isNumeric = someString.chars().allMatch( Character::isDigit );
	System.out.println(someString.length());
	System.out.println(isNumeric);
	
	
	
	
	

		if (perfilRepository.findAll().isEmpty()) {
			perfilRepository.saveAll(List.of(p1, p2));

		}

		User u1 = new User(null, "Admin", "admin@gft.com", new BCryptPasswordEncoder().encode("Gft@1234"), p1);
		User u2 = new User(null, "Usuário", "usuario@gft.com", new BCryptPasswordEncoder().encode("Gft@1234"), p2);

		if (userRepository.findAll().isEmpty()) {
			userRepository.saveAll(List.of(u1, u2));
		}

	}

}
