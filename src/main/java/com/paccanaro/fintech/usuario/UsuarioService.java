package com.paccanaro.fintech.usuario;
import com.paccanaro.fintech.config.JwtService;
import com.paccanaro.fintech.conta.Conta;
import com.paccanaro.fintech.conta.ContaRepository;
import com.paccanaro.fintech.usuario.dto.CadastroRequest;
import com.paccanaro.fintech.usuario.dto.LoginRequest;
import com.paccanaro.fintech.usuario.dto.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final ContaRepository contaRepository;
    private final JwtService jwtService;

    public UsuarioService(PasswordEncoder passwordEncoder,
                          UsuarioRepository usuarioRepository,
                          ContaRepository contaRepository,
                          JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
        this.jwtService = jwtService;
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

    public LoginResponse login(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senhas invalidos"));

        if (!passwordEncoder.matches(loginRequest.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Email ou senha invalidos");
        }
        String token = jwtService.gerarToken(usuario.getEmail());
        return new LoginResponse(token);

    }

}
