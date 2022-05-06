package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.service.MusicasService;
import com.ciandt.summit.bootcamp2022.service.impl.MusicasServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v1/music")
public class MusicController {

    private Map<String, Object> response = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(MusicasServiceImpl.class);

    @Autowired
    private MusicasService musicasService;

    @GetMapping
    public ResponseEntity<?> encontrarTodasMusicasPorArtista(
            @Size(min = 3, message = "Digite ao menos 3 caracteres")
            @RequestParam(value = "nomeArtista", defaultValue = "nomeArtista") String nomeArtista) {

        response.clear();
        response.put("musicas", musicasService.buscarTodasMusicasPorNomeArtista(nomeArtista));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

