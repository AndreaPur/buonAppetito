package com.example.buonAppetito.security;

import com.example.buonAppetito.entities.Comune;
import com.example.buonAppetito.entities.TokenBlackList;
import com.example.buonAppetito.entities.Utente;
import com.example.buonAppetito.enums.Role;
import com.example.buonAppetito.exceptions.ComuneNotFoundException;
import com.example.buonAppetito.exceptions.UserNotConformedException;
import com.example.buonAppetito.repositories.ComuneRepository;
import com.example.buonAppetito.repositories.UtenteRepository;
import com.example.buonAppetito.request.AuthenticationRequest;
import com.example.buonAppetito.response.AuthenticationResponse;
import com.example.buonAppetito.request.RegistrationRequest;
import com.example.buonAppetito.services.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenBlackListService tokenBlackListService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ComuneRepository comuneRepository;

    public AuthenticationResponse register (RegistrationRequest request) throws ComuneNotFoundException {
        Comune comune = comuneRepository.findComuneByNome(request.getNomeComune());
        if(comune == null) {
            throw new ComuneNotFoundException();
        }
        var user = Utente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .comune(comune)
                .indirizzo(request.getIndirizzo())
                .telefono(request.getTelefono())
                .dataNascita(request.getDataNascita())
                .role(Role.TOCONFIRM)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var jwtToken = jwtService.generateToken(user);
        user.setRegistrationToken(jwtToken);
        utenteRepository.saveAndFlush(user);
        javaMailSender.send(createConfirmationMail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest authenticationRequest) throws UserNotConformedException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));
        var user = utenteRepository.findUtenteByEmail(authenticationRequest.getEmail());
        if(user.getRole().equals(Role.TOCONFIRM)){
            throw new UserNotConformedException();
        }
        var jwtToken = jwtService.generateToken(user);
        if(tokenBlackListService.tokenNotValidFromUtenteById(user.getId()).contains(jwtToken)){
            String email = jwtService.extractUsername(jwtToken);
            UserDetails userDetails = utenteRepository.findUtenteByEmail(email);
            String newToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(newToken).build();
        }
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void logout(HttpServletRequest httpRequest, Long id){
        String token = extractTokenFromRequest(httpRequest);
        TokenBlackList tokenBlackList = TokenBlackList.builder()
                .utente(utenteRepository.getReferenceById(id))
                .token(token)
                .build();
        tokenBlackListService.createTokenBlackList(tokenBlackList);
    }

    public String extractTokenFromRequest (HttpServletRequest request){
        String autherizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(autherizationHeader) && autherizationHeader.startsWith("Bearer ")){
            return autherizationHeader.substring(7);
        }
        return null;
    }

    public boolean confirmRegistration(Long id, String token){
        Utente utente = utenteRepository.getReferenceById(id);
        if (utente.getRegistrationToken().equals(token)) {
            utente.setRole(Role.UTENTE);
            utenteRepository.saveAndFlush(utente);
            return true;
        }
        return false;
    }

    public SimpleMailMessage createConfirmationMail (Utente utente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(utente.getEmail());
        message.setSubject("CONFERMA REGISTRAZIONE");
        String url = "http://localhost:8080/auth/confirm?id=" + utente.getId() + "&token=" + utente.getRegistrationToken();
        message.setText("Clicca qui per confermare la registrazione: " + url);
        return message;
    }
}
