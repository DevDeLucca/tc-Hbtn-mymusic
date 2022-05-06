package com.ciandt.summit.bootcamp2022.controller;

import com.ciandt.summit.bootcamp2022.entity.Musicas;
import com.ciandt.summit.bootcamp2022.entity.PlaylistMusicas;
import com.ciandt.summit.bootcamp2022.entity.PlaylistMusicasKey;
import com.ciandt.summit.bootcamp2022.entity.Playlists;
import com.ciandt.summit.bootcamp2022.service.MusicasService;
import com.ciandt.summit.bootcamp2022.service.PlaylistsMusicasService;
import com.ciandt.summit.bootcamp2022.service.PlaylistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistsController {

    @Autowired
    private PlaylistsMusicasService playlistsMusicasService;

    @Autowired
    private PlaylistsService playlistsService;

    @Autowired
    private MusicasService musicasService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{playlistId}/musicas")
    public ResponseEntity<?> adicionarMusica(@PathVariable("playlistId") String playlistId, @Valid @RequestBody Musicas musicaId) {

        try {
            Optional<Playlists> playlists = playlistsService.buscarPlaylist(playlistId);
            Optional<Musicas> musicas = musicasService.buscarMusicaPorId(musicaId.getId());

            if (playlists.isPresent() && musicas.isPresent()) {
                PlaylistMusicasKey playlistMusicasKey = new PlaylistMusicasKey(playlistId, musicaId.getId());
                PlaylistMusicas playlistMusicas = new PlaylistMusicas(playlistMusicasKey);
                playlistsMusicasService.salvarPlayList(playlistMusicas);
                return ResponseEntity.status(HttpStatus.CREATED).body("");
            }

            return ResponseEntity.badRequest().build();

        } catch (Exception e) {

            throw new RuntimeException("Erro ao adicionar música.");
        }
    }

    @DeleteMapping(value = "{playlistId}/musicas/{musicaId}")
    public ResponseEntity<?> removerMusica(
            @PathVariable("playlistId") String playlistId,
            @PathVariable("musicaId") String musicaId) {

        Optional<Playlists> playlist = playlistsService.buscarPlaylist(playlistId);
        Optional<Musicas> musica = musicasService.buscarMusicaPorId(musicaId);

        if (!playlist.isPresent() && !musica.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist e Música não encontradas.");

        } else if (!playlist.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist não encontrada.");

        } else if (!musica.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Música não encontrada.");

        } else if (playlist.isPresent() && musica.isPresent()) {
            PlaylistMusicasKey playlistMusicasKey = new PlaylistMusicasKey(playlistId, musicaId);
            PlaylistMusicas playlistMusicas = new PlaylistMusicas(playlistMusicasKey);

            playlistsMusicasService.deletarMusicaPlaylist(playlistMusicas);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Música removida da Playlist.");
        }

        return null;
    }
}

