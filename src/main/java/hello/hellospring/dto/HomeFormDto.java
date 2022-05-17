package hello.hellospring.dto;

import hello.hellospring.domain.Game;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeFormDto {
    //private Long accountId;
    private List<Game> gameList;
}
