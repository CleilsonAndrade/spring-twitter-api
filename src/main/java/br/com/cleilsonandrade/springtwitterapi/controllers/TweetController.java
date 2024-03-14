package br.com.cleilsonandrade.springtwitterapi.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.cleilsonandrade.springtwitterapi.controllers.dtos.CreateTweetDto;
import br.com.cleilsonandrade.springtwitterapi.entities.Role;
import br.com.cleilsonandrade.springtwitterapi.entities.Tweet;
import br.com.cleilsonandrade.springtwitterapi.repositories.TweetRepository;
import br.com.cleilsonandrade.springtwitterapi.repositories.UserRepository;

@RestController
@RequestMapping("/tweets")
public class TweetController {
  private final TweetRepository tweetRepository;
  private final UserRepository userRepository;

  public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
    this.tweetRepository = tweetRepository;
    this.userRepository = userRepository;
  }

  @PostMapping
  public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto,
      JwtAuthenticationToken token) {
    var user = userRepository.findById(UUID.fromString(token.getName()));

    var tweet = new Tweet();
    tweet.setUser(user.get());
    tweet.setContent(dto.content());

    tweetRepository.save(tweet);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token) {
    var user = userRepository.findById(UUID.fromString(token.getName()));
    var tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    var isAdmin = user.get().getRoles().stream()
        .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

    if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {
      tweetRepository.deleteById(tweetId);
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok().build();
  }
}
