package kr.or.connect.guestbook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

//컨트롤러 클래스
@Controller
public class GuestbookController {
  
    //서비스 객체 주입
    @Autowired
    GuestbookService guestbookService;
    
    //@PathVariable은 URL경로에 변수를 넣는것 이다. RESTful API에서 사용한다. ex)127.0.0.1:8080/abcd/abcd
    //@RequestParam은 URL 파라미터로 값을 넘기는 방식이다. ex)127.0.0.1:8080?a=b&c=d
     
    @GetMapping(path="/list")         // 페이지네이션을 위한 strat 파라미터. 디폴트 값은 0.
    public String list(@RequestParam(name="start", required=false, defaultValue="0") int start,
                       ModelMap model) {
        
        // start로 시작하는 방명록 목록 구하기
        List<Guestbook> list = guestbookService.getGuestbooks(start);
        
        // 전체 페이지수 구하기
        int count = guestbookService.getCount();
        int pageCount = count / GuestbookService.LIMIT;
        if(count % GuestbookService.LIMIT > 0)
            pageCount++;
        
        // 페이지 수만큼 start의 값을 리스트로 저장
        // 예를 들면 페이지수가 3이면
        // 0, 5, 10 이렇게 저장된다.
        // list?start=0 , list?start=5, list?start=10 으로 링크가 걸린다.
        List<Integer> pageStartList = new ArrayList<>();
        for(int i = 0; i < pageCount; i++) {
            pageStartList.add(i * GuestbookService.LIMIT);
        }
        
        model.addAttribute("list", list);
        model.addAttribute("count", count);
        model.addAttribute("pageStartList", pageStartList);
        
        return "list";
    }
    
    @PostMapping(path="/write")
    public String write(@ModelAttribute Guestbook guestbook,
                        HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        System.out.println("clientIp : " + clientIp);
        guestbookService.addGuestbook(guestbook, clientIp);
        return "redirect:list";
    }
}