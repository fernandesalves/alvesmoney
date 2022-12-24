package com.alvesmoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesmoney.api.event.RecursoCriadoEvent;
import com.alvesmoney.api.model.Categoria;
import com.alvesmoney.api.repository.CategoriaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository cr;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping
	public List<Categoria> pesquisarCategorias() {

		return cr.findAll();
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> encontrarCategoriaPorCodigo(@PathVariable Long codigo) {

		Categoria resultado = this.cr.findById(codigo).orElse(null);

		return resultado != null ? ResponseEntity.ok(resultado) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody Categoria categoria,
			HttpServletResponse response) {

		Categoria novaCategoria = this.cr.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaCategoria.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
	}
}
