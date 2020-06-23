package kr.or.connect.guestbook.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

// Rest 컨트롤러이다.
@RestController
@RequestMapping(path = "/guestbooks") // 이렇게 해주면 잎에 "/guestbooks"을 붙여야함.
public class GuestbookApiController {

  // 서비스 객체 주입
  @Autowired
  GuestbookService guestbookService;

  // guest book 리스트 반환
  // 반환형이 Map 객체이다. 디스패처 서블릿은 json 메세지 컨버터를 내부적으로 사용해서 List Map객체를 json으로 변환해서 전송한다.
  @GetMapping
  public Map<String, Object> list(
      @RequestParam(name = "start", required = false, defaultValue = "0") int start) {

    List<Guestbook> list = guestbookService.getGuestbooks(start);

    int count = guestbookService.getCount();
    int pageCount = count / GuestbookService.LIMIT;
    if (count % GuestbookService.LIMIT > 0)
      pageCount++;

    List<Integer> pageStartList = new ArrayList<>();
    for (int i = 0; i < pageCount; i++) {
      pageStartList.add(i * GuestbookService.LIMIT);
    }

    Map<String, Object> map = new HashMap<>(); // json으로 만들 데이터를 map 만들어준다.
    map.put("list", list);
    map.put("count", count);
    map.put("pageStartList", pageStartList);

    return map;
  }

  // 여기도 Guestbook 객체로 반환되는데, 이를 디스패처 서블릿이 json 메세지 컨버터를 내부적으로 사용해서 json으로 변환해서 전송한다.
  // geuest book 작성
  @PostMapping
  public Guestbook write(@RequestBody Guestbook guestbook, HttpServletRequest request) {

    String clientIp = request.getRemoteAddr();
    // id가 입력된 guestbook이 반환된다.
    Guestbook resultGuestbook = guestbookService.addGuestbook(guestbook, clientIp);
    return resultGuestbook;
  }


  // guest book 삭제
  @DeleteMapping("/{id}")
  // 결과 정보를 담은 map 객체로 반환되는데,이를 디스패처 서블릿이 json 메세지 컨버터를 내부적으로 사용해서 json으로 변환해서 전송한다.
  public Map<String, String> delete(@PathVariable(name = "id") Long id,
      HttpServletRequest request) {
    String clientIp = request.getRemoteAddr();

    int deleteCount = guestbookService.deleteGuestbook(id, clientIp);
    return Collections.singletonMap("success", deleteCount > 0 ? "true" : "false");
  }
}
