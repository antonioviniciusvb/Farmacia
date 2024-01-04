package com.generation.farmacia.util;

import java.util.Optional;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.UsuarioLogin;

public class HttpTest {

	public static <T1, T2> ResponseEntity<T2> executaRequisicao(TestRestTemplate testRestTemplate, T1 corpo, String uri,
			HttpMethod metodo, Class<T2> tipo) {

		var corpoRequisicao = corpo != null ? new HttpEntity<T1>(corpo) : null;

		return testRestTemplate.exchange(uri, metodo, corpoRequisicao, tipo);
	}

	public static <T> ResponseEntity<String> executaRequisicaoAuth(TestRestTemplate testRestTemplate, Optional<T> corpo,
			String uri, HttpMethod metodo, UsuarioLogin usuario) {

		var corpoRequisicao = corpo.isPresent() ? new HttpEntity<T>(corpo.get()) : null;

		return testRestTemplate.withBasicAuth(usuario.getUsuario(), usuario.getSenha()).exchange(uri, metodo,
				corpoRequisicao, String.class);
	}
}