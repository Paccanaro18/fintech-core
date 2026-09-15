package com.paccanaro.fintech.usuario;
import com.paccanaro.fintech.conta.Conta;
import com.paccanaro.fintech.conta.ContaRepository;
import com.paccanaro.fintech.usuario.dto.CadastroRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;

    public UsuarioService(PasswordEncoder passwordEncoder,
                          UsuarioRepository usuarioRepository,
                          ContaRepository contaRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
    }
    @Transactional
    public Usuario cadastrar(CadastroRequest request) {
        if(usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email já Cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario = usuarioRepository.save(usuario);

        Conta conta = new Conta();
        conta.setUsuario(usuario);
        conta.setAgencia("0001");
        conta.setNumero(gerarNumeroConta());
        conta.setSaldo(BigDecimal.ZERO);
        contaRepository.save(conta);

        return usuario;
    }
    private String gerarNumeroConta() {
        long timestamp = System.currentTimeMillis();
        return String.valueOf(timestamp).substring(7);
    }
}
