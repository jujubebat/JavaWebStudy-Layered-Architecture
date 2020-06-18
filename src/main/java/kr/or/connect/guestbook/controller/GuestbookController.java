package kr.or.connect.guestbook.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.connect.guestbook.argumentresolver.HeaderInfo;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

// 컨트롤러 클래스
@Controller
public class GuestbookController {

  // 서비스 객체 주입
  @Autowired
  GuestbookService guestbookService;

  // @PathVariable은 URL경로에 변수를 넣는것 이다. RESTful API에서 사용한다. ex)127.0.0.1:8080/abcd/abcd
  // @RequestParam은 URL 파라미터로 값을 넘기는 방식이다. ex)127.0.0.1:8080?a=b&c=d

  /*
   * @GetMapping(path = "/list") // 페이지네이션을 위한 strat 파라미터. 디폴트 값은 0. public String
   * list(@RequestParam(name = "start", required = false, defaultValue = "0") int start, ModelMap
   * model, HttpServletRequest request, HttpServletResponse response) {
   * 
   * String value = null; boolean find = false; Cookie[] cookies = request.getCookies(); // 쿠키의 배열을
   * 얻어온다.
   * 
   * if (cookies != null) { // 쿠키가 없으면 null을 반환하므로 처리한다. for (Cookie cookie : cookies) { //쿠키 배열을
   * 돌면서 쿠키의 이름이 "count"인 값을 가져온다. if ("count".equals(cookie.getName())) { find = true; value =
   * cookie.getValue(); } } }
   * 
   * if (!find) { // 쿠키가 없다면. value = "1"; // 쿠키는 문자열이다. } else { // 쿠키를 찾았다면. try { int i =
   * Integer.parseInt(value); // 문자열 쿠키값을 int 형으로 바꿔준다. value = Integer.toString(++i); // 사이트 방문횟수를
   * 증가 시킨다. } catch (Exception ex) { value = "1"; } }
   * 
   * Cookie cookie = new Cookie("count", value); // 새로운 정보에 대한 쿠키를 만든다. cookie.setMaxAge(60 * 60 *
   * 24 * 365); // 쿠키 유지 시간 설정 :1년 동안 유지. cookie.setPath("/"); // /list/ 경로 이하에 모두 쿠키 적용.
   * response.addCookie(cookie); // 새로운 정보에 대한 쿠키를 만들어서 보낸다. 클라이언트는 새로운 쿠키를 옛날 쿠키와 교체한다.
   * 
   * 
   * // start로 시작하는 방명록 목록 구하기 List<Guestbook> list = guestbookService.getGuestbooks(start);
   * 
   * // 전체 페이지수 구하기 int count = guestbookService.getCount(); int pageCount = count /
   * GuestbookService.LIMIT; if (count % GuestbookService.LIMIT > 0) pageCount++;
   * 
   * // 페이지 수만큼 start의 값을 리스트로 저장 // 예를 들면 페이지수가 3이면 // 0, 5, 10 이렇게 저장된다. // list?start=0 ,
   * list?start=5, list?start=10 으로 링크가 걸린다. List<Integer> pageStartList = new ArrayList<>(); for
   * (int i = 0; i < pageCount; i++) { pageStartList.add(i * GuestbookService.LIMIT); }
   * 
   * model.addAttribute("list", list); model.addAttribute("count", count);
   * model.addAttribute("pageStartList", pageStartList); model.addAttribute("cookieCount", value);
   * // jsp에게 전달하기 위해서 쿠키 값을 model에 담아 전송한다.
   * 
   * return "list"; }
   */

  /*
   * Spring MVC가 제공하는 CookieValue를 사용하는 경우.
   */
  @GetMapping(path="/list")
	public String list(@RequestParam(name="start", required=false, defaultValue="0") int start,
					   ModelMap model, @CookieValue(value="count", defaultValue="1", required=true) String value,
					   HttpServletResponse response,
					   HeaderInfo headerInfo) {
		System.out.println("-----------------------------------------------------");
		System.out.println(headerInfo.get("user-agent"));
		System.out.println("-----------------------------------------------------");
		
    try {
      int i = Integer.parseInt(value); // 문자열 쿠키값을 int 형으로 바꿔준다.
      value = Integer.toString(++i); // 사이트 방문횟수를 증가 시킨다.
    } catch (Exception ex) {
      value = "1";
    }


    Cookie cookie = new Cookie("count", value); // 새로운 정보에 대한 쿠키를 만든다.
    cookie.setMaxAge(60 * 60 * 24 * 365); // 쿠키 유지 시간 설정 :1년 동안 유지.
    cookie.setPath("/"); // /list/ 경로 이하에 모두 쿠키 적용.
    response.addCookie(cookie); // 새로운 정보에 대한 쿠키를 만들어서 보낸다. 클라이언트는 새로운 쿠키를 옛날 쿠키와 교체한다.


    // start로 시작하는 방명록 목록 구하기
    List<Guestbook> list = guestbookService.getGuestbooks(start);

    // 전체 페이지수 구하기
    int count = guestbookService.getCount();
    int pageCount = count / GuestbookService.LIMIT;
    if (count % GuestbookService.LIMIT > 0)
      pageCount++;

    // 페이지 수만큼 start의 값을 리스트로 저장
    // 예를 들면 페이지수가 3이면
    // 0, 5, 10 이렇게 저장된다.
    // list?start=0 , list?start=5, list?start=10 으로 링크가 걸린다.
    List<Integer> pageStartList = new ArrayList<>();
    for (int i = 0; i < pageCount; i++) {
      pageStartList.add(i * GuestbookService.LIMIT);
    }

    model.addAttribute("list", list);
    model.addAttribute("count", count);
    model.addAttribute("pageStartList", pageStartList);
    model.addAttribute("cookieCount", value); // jsp에게 전달하기 위해서 쿠키 값을 model에 담아 전송한다.

    return "list";
  }

  @PostMapping(path = "/write")
  public String write(@ModelAttribute Guestbook guestbook, HttpServletRequest request) {
    String clientIp = request.getRemoteAddr();
    System.out.println("clientIp : " + clientIp);
    guestbookService.addGuestbook(guestbook, clientIp);
    return "redirect:list";
  }

  @GetMapping(path = "/delete")
  public String delete(@RequestParam(name = "id", required = true) Long id,
      @SessionAttribute("isAdmin") String isAdmin, HttpServletRequest request,
      RedirectAttributes redirectAttr) {
    if (isAdmin == null || !"true".equals(isAdmin)) { // 세션값이 true가 아닐 경우
      redirectAttr.addFlashAttribute("errorMessage", "로그인을 하지 않았습니다.");
      return "redirect:loginform";
    }
    String clientIp = request.getRemoteAddr();
    guestbookService.deleteGuestbook(id, clientIp);
    return "redirect:list";
  }

}
