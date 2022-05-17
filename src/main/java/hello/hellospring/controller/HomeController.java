package hello.hellospring.controller;

import hello.hellospring.domain.Game;
import hello.hellospring.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController
{
    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    // 메인 화면 템플릿
    @GetMapping("/")
    public String home() {
        return "redirect:/account/login";
    }

    // 메인 화면 템플릿
    @GetMapping("/home")
    public String main() {
        return "home_2";
    }

    // 계정 로그인 후 홈 화면에 유저의 게임 플레이 기록 목록 표시
    @GetMapping("/home/list")
    @ResponseBody
    public Map<String, Object> getUserGameList(Principal principal, @RequestParam("start") int start, @RequestParam("length") int length) {
        String curAccountName = principal.getName();
        Page<Game> gameListPage = homeService.getAccountGameListPage(curAccountName, start, length);

        Long gameCount = homeService.getAccountGameCount(curAccountName);

        Map<String, Object> data = new HashMap<>();

        // 게임 목록 정보(gameListPage)
        data.put("gameListPage", gameListPage);
        // 페이징 구성 정보(recordsTotal, recordsFiltered)
        data.put("recordsTotal", gameCount);
        data.put("recordsFiltered", gameCount);

        return data;
    }
}
