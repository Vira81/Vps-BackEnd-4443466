package com.VidaPlus.ProjetoBackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.VidaPlus.ProjetoBackend.dto.EmailDto;
import com.VidaPlus.ProjetoBackend.dto.SenhaAlterarDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioCadastroDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioPerfilDto;
import com.VidaPlus.ProjetoBackend.dto.UsuarioSaidaDto;
import com.VidaPlus.ProjetoBackend.entity.PessoaEntity;
import com.VidaPlus.ProjetoBackend.entity.UsuarioEntity;
import com.VidaPlus.ProjetoBackend.entity.enums.PerfilUsuario;
import com.VidaPlus.ProjetoBackend.entity.enums.StatusUsuario;
import com.VidaPlus.ProjetoBackend.exception.AlteracaoIndevida;
import com.VidaPlus.ProjetoBackend.exception.EmailJaCadastradoException;
import com.VidaPlus.ProjetoBackend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class UsuarioService {
	//private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioLogadoService login;

	/**
	 * O usuario altera o email usado para logar O token antigo será invalido, logar
	 * usando o email novo.
	 */
	public ResponseEntity<?> atualizarEmail(EmailDto dto) {
		UsuarioEntity usuario = buscarId(login.getUsuarioLogado().getId());

		if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
			if (usuarioRepository.existsByEmail(dto.getEmail())) {
				throw new EmailJaCadastradoException("E-mail já está em uso");
			}
			usuario.setEmail(dto.getEmail());
		}

		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Email atualizado com sucesso");
	}

	// Buscar Usuario por Id
	public UsuarioEntity buscarId(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}

	/**
	 * Buscar Usuario por Id Usado para controlar a saida de dados sensiveis
	 */
	public UsuarioSaidaDto buscarPorId(Long id) {
		UsuarioEntity usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
		return new UsuarioSaidaDto(usuario);
	}

	/**
	 * Lista todos os usuarios usando o Dto de saida
	 */
	@Transactional
	public List<UsuarioSaidaDto> listarTodosUsuarios() {
		List<UsuarioEntity> usuarios = usuarioRepository.findAllSaida();
		return usuarios.stream().map(UsuarioSaidaDto::new).toList();
	}

	/**
	 * Atualiza a senha do usuario
	 * 
	 */
	public ResponseEntity<?> atualizarSenha(SenhaAlterarDto dto) {

		UsuarioEntity usuario = buscarId(login.getUsuarioLogado().getId());
		// Compara a senha atual com a senha informada
		if (passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
			usuario.setSenhaHash(passwordEncoder.encode(dto.getNovaSenha()));
		} else {
			throw new AlteracaoIndevida("A senha atual está incorreta!");
		}

		usuarioRepository.save(usuario);
		return ResponseEntity.ok("Senha atualizada com sucesso");
	}


	/**
	 * Marca o usuario como Inativo
	 * TODO: Desativar Token
	 */
	public void desativarUsuario() {
		UsuarioEntity usuario = buscarId(login.getUsuarioLogado().getId());
		usuario.setStatus(StatusUsuario.INATIVO);
	}
	
	// TODO: Desativar o Token, quando o usuario sair
	// TODO: Controller
	public void logOut() {
		
	}
	
	/**
	 * Buscar por email (exato)
	 * TODO: criar exception
	 */
	public UsuarioSaidaDto buscarPorEmail(String email) {
	    UsuarioEntity usuario = usuarioRepository.findByEmail(email)
	        .orElseThrow(() -> new AlteracaoIndevida("Usuário não encontrado"));
	    return new UsuarioSaidaDto(usuario);
	}
	
	/**
	 * Busca por uma parte do nome do usuario
	 */
	public List<UsuarioSaidaDto> buscarPorNome(String nome) {
	    return usuarioRepository.findByPessoaNomeContainingIgnoreCase(nome)
	        .stream()
	        .map(UsuarioSaidaDto::new)
	        .toList();
	}

	/**
	 * Cria um novo usuario
	 */
	public UsuarioEntity cadastrarNovoUsuario(UsuarioCadastroDto dto) {
		if (usuarioRepository.existsByEmail(dto.getEmail())) {
			throw new EmailJaCadastradoException("O e-mail informado já está cadastrado.");
		}

		// Perfil de Paciente e Status Pendente
		UsuarioEntity novoUsuario = UsuarioEntity.builder().email(dto.getEmail())
				.senhaHash(passwordEncoder.encode(dto.getSenha())).perfil(PerfilUsuario.PACIENTE)
				.status(StatusUsuario.PENDENTE).pessoa(new PessoaEntity()).build();

		return usuarioRepository.save(novoUsuario);
	}
	
	/**
	 * Alterar o Status e Perfil do usuario, buscando por CPF
	 * 
	 * Poderia ser separado...
	 * TODO: Exception
	 *  
	 **/
	public UsuarioSaidaDto atualizarPerfilPorCpf(UsuarioPerfilDto dto) {

	    // Buscar o usuário pelo CPF 
	    UsuarioEntity usuario = usuarioRepository.findByPessoaCpf(dto.getCpf())
	        .orElseThrow(() -> new AlteracaoIndevida("Usuário com esse CPF não foi encontrado"));

	    if (dto.getPerfil() != null) {
	        usuario.setPerfil(dto.getPerfil());
	    }

	    if (dto.getStatus() != null) {
	        usuario.setStatus(dto.getStatus());
	    }
	    
	    if (dto.getPerfil() == null && dto.getStatus() == null) {
	    	throw new AlteracaoIndevida("Nenhum dado foi informado");
	    }
	    
	    usuarioRepository.save(usuario);
	    return new UsuarioSaidaDto(usuario);
	}


}
