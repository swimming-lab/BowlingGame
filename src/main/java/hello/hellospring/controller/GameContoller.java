package hello.hellospring.controller;

import hello.hellospring.domain.Game;
import hello.hellospring.dto.GameFormDto;
import hello.hellospring.service.AccountService;
import hello.hellospring.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class GameContoller {
    private final GameService gameService;
    private final AccountService accountService;

    @Autowired
    public GameContoller(GameService gameService, AccountService accountService) {
        this.gameService = gameService;
        this.accountService = accountService;
    }

    // 게임 시작 시 초기 세팅
    @GetMapping("/game/start")
    public String startGame(Model model, Principal principal) {
        String curAccountName = principal.getName();
        Game curGame = gameService.startGame(curAccountName);

        GameFormDto gameFormDto = new GameFormDto();
        gameFormDto.setGameId(curGame.getGameId());

        model.addAttribute("gameFormDto", gameFormDto);

        return "redirect:/game/play?gameId=" + gameFormDto.getGameId();
    }

    // 게임 ID 값으로 로드 후 게임 진행
    @GetMapping("/game/play")
    public String playGame(Model model, @RequestParam("gameId") Long gameId) {
        GameFormDto gameFormDto = gameService.getGameResult(gameId);
        model.addAttribute("gameFormDto", gameFormDto);

        return "bowling/BowlingGame_2";
    }

    @PostMapping("/game/save")
    public @ResponseBody ResponseEntity game(@RequestBody @Valid GameFormDto gameFormDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String> (sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Game game;
        try {
            game = gameService.saveGame(gameFormDto);
        } catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Game>(game, HttpStatus.OK);
    }

    // 완료된 게임의 결과 화면
    @GetMapping("/game/result")
    public String getResult(Model model, @RequestParam("gameId") Long gameId) {
        GameFormDto gameFormDto = gameService.getGameResult(gameId);
        model.addAttribute("gameFormDto", gameFormDto);

        return "bowling/result_2";
    }
}