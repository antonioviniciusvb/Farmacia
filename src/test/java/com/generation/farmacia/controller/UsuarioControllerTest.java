package com.generation.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioLogin;
import com.generation.farmacia.repository.UsuarioRepository;
import com.generation.farmacia.service.UsuarioService;
import com.generation.farmacia.util.HttpTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

	private Usuario usuarioTeste = new Usuario(0L, "Antonio Bandeira", "antonio_bandeira@genesis16bit.com.br",
			"123456789", "");

	private UsuarioLogin usuarioRoot = new UsuarioLogin(0L, "Root", "root@genesis16bit.com", "rootroot", "");

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {

		usuarioRepository.deleteAll();

		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@genesis16bit.com", "rootroot", ""));
	}

	@BeforeEach
	void reset() {

		usuarioTeste.setId(0L);
		usuarioTeste.setNome("Antonio Bandeira");
		usuarioTeste.setUsuario("antonio_bandeira@genesis16bit.com.br");
		usuarioTeste.setSenha("123456789");
		usuarioTeste.setFoto("");

		var usuario = usuarioRepository.findByUsuario(usuarioTeste.getUsuario());

		if (usuario.isPresent())
			usuarioRepository.deleteById(usuario.get().getId());
	}

	@Test
	@DisplayName("Deve Cadastar um Usuário")
	public void deveCriarUmUsuario() {

		usuarioTeste.setUsuario("antonio_bandeira@genesis16bit.com.br");
		usuarioTeste.setSenha("4ntonio3");

		assertEquals(HttpStatus.CREATED, HttpTest.executaRequisicao(testRestTemplate, usuarioTeste,
				"/usuarios/cadastrar", HttpMethod.POST, Usuario.class).getStatusCode());
	}

	@Test
	@DisplayName("Não deve cadastrar usuário já existente")
	public void naoDeveCadastrarUsuarioExistente() {

		String uri = "/usuarios/cadastrar";

		usuarioService.cadastrarUsuario(usuarioTeste);

		assertEquals(HttpStatus.BAD_REQUEST,
				HttpTest.executaRequisicao(testRestTemplate, usuarioTeste, uri, HttpMethod.POST, Usuario.class)
						.getStatusCode());
	}

	@Test
	@DisplayName("Deve Atualizar usuário")
	public void deveAtualizarUsuario() {

		String uri = "/usuarios/atualizar";

		usuarioTeste.setUsuario("usuarioteste@email.com");

		Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioTeste);

		usuarioTeste.setId(usuarioCadastrado.get().getId());
		usuarioTeste.setNome("Produção - Atualizado");
		usuarioTeste.setUsuario("administracao@genesis16bit.com");

		assertEquals(HttpStatus.OK, HttpTest
				.executaRequisicaoAuth(testRestTemplate, Optional.of(usuarioTeste), uri, HttpMethod.PUT, usuarioRoot)
				.getStatusCode());

	}

	@Test
	@DisplayName("Deve Listar todos os usuários cadastrados")
	public void deveListarTodosUsuarios() {

		usuarioService
				.cadastrarUsuario(new Usuario(0L, "Administrativo", "rebeca@genesis16bit.com", "administrac4o", ""));

		usuarioService
				.cadastrarUsuario(new Usuario(0L, "Administrativo", "luis@genesis16bit.com", "administrac4o", ""));

		assertEquals(HttpStatus.OK, HttpTest
				.executaRequisicaoAuth(testRestTemplate, Optional.empty(), "/usuarios/all", HttpMethod.GET, usuarioRoot)
				.getStatusCode());

	}

	@Test
	@DisplayName("Deve logar usuário")
	public void deveAutenticarUsuario() {

		String uri = "/usuarios/logar";

		UsuarioLogin estoque = new UsuarioLogin(0L, "Estoque", "estoque@genesis16bit.com", "estoq123", "");

		// Cadastrando...
		HttpTest.executaRequisicao(testRestTemplate, estoque, "/usuarios/cadastrar", HttpMethod.POST,
				UsuarioLogin.class).getStatusCode();

		// Logando...
		var resposta = HttpTest.executaRequisicaoAuth(testRestTemplate, Optional.of(estoque), uri, HttpMethod.POST,
				estoque);

		System.out.println(resposta.getBody());

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

	@Test
	@DisplayName("Deve Procurar usuário por Id")
	public void deveProcurarUsuarioPorId() {

		//Buscando o root
		String uri = "/usuarios/1";

		// Cadastrando...
		HttpTest.executaRequisicao(testRestTemplate, usuarioTeste, "/usuarios/cadastrar", HttpMethod.POST,
				UsuarioLogin.class).getStatusCode();

		var resposta = HttpTest.executaRequisicaoAuth(testRestTemplate, Optional.empty(), uri, HttpMethod.GET,
				usuarioRoot);

		System.out.println(resposta.getBody());

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	@DisplayName("Não deve Encontrar o usuário")
	public void naoDeveProcurarUsuarioPorId() {

		// Usando id 2 para buscar o usuário cadastrado, pois o 1 é o root
		String uri = "/usuarios/200";

		var resposta = HttpTest.executaRequisicaoAuth(testRestTemplate, Optional.of(usuarioRoot), uri, HttpMethod.GET,
				usuarioRoot);

		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());

	}

//	@Test
//	@DisplayName("Não deve logar usuário")
//	public void naoDeveLogarUsuario() {
//
//		String uri = "/usuarios/logar";
//
//		UsuarioLogin estoque = new UsuarioLogin(0L, "Estoque", "estoque@genesis16bit.com", "estoq123", "");
//
//		//Cadastrando...
//		HttpTest.executaRequisicao(testRestTemplate, estoque, "/usuarios/cadastrar", HttpMethod.POST,
//				UsuarioLogin.class).getStatusCode();
//
//		// Logando...
//		var resposta = HttpTest.executaRequisicaoAuth(testRestTemplate, Optional.of(estoque), uri, HttpMethod.POST,
//				estoque);
//
//		estoque.setSenha("senhaalterada");
//
//		assertEquals(HttpStatus.FORBIDDEN, resposta.getStatusCode());
//
//	}

}
