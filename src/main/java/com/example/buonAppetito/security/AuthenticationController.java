package com.example.buonAppetito.security;

import com.example.buonAppetito.exceptions.ComuneNotFoundException;
import com.example.buonAppetito.exceptions.ErrorResponse;
import com.example.buonAppetito.exceptions.UserNotConformedException;
import com.example.buonAppetito.request.AuthenticationRequest;
import com.example.buonAppetito.request.RegistrationRequest;
import com.example.buonAppetito.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (ComuneNotFoundException e){
            return new ResponseEntity<>(new ErrorResponse("ComuneNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        } catch (UserNotConformedException e){
            return new ResponseEntity<>(new ErrorResponse("UserNotConfirmedException", e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout/{id}")
    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    public void logout(HttpServletRequest httpRequest, @PathVariable Long id){
        authenticationService.logout(httpRequest, id);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam Long id, @RequestParam String token){
        if (authenticationService.confirmRegistration(id,token)){
            return new ResponseEntity<>(new GenericResponse("Conferma avvenuta con successo"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("NotConfirmedException", "OPS! Qualcosa è andato storto con la conferma del tuo account!"), HttpStatus.BAD_REQUEST);
    }
}
