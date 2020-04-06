package kr.or.connect.guestbook.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.guestbook.dao.GuestbookDao;
import kr.or.connect.guestbook.dao.LogDao;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.dto.Log;
import kr.or.connect.guestbook.service.GuestbookService;

// Service Layer은 @Service 어노테이션을 붙인다.
@Service
public class GuestbookServiceImpl implements GuestbookService{
    
    //스프링 컨테이너가 알아서 guestbookDao 주입 시켜줌 
    @Autowired
    GuestbookDao guestbookDao; 
    
    // 스프링 컨테이너가 알아서 logDao 주입 시켜줌
    @Autowired
    LogDao logDao;  

    //게스트북 목록 가져오기
    @Override
    @Transactional //읽기만 하는 메소드는 @Transactional 어노테이션을 붙여주면 Read only 커넥션을 사용한다.
    public List<Guestbook> getGuestbooks(Integer start) {
        List<Guestbook> list = guestbookDao.selectAll(start, GuestbookService.LIMIT); //start부터 LIMIT(5개)만큼 가져온다.
        return list;
    }

    //게스트북 삭제
    @Override
    @Transactional(readOnly=false) //이 메소드는 ReadOnly가 아니므로 false 처리
    public int deleteGuestbook(Long id, String ip) {
        int deleteCount = guestbookDao.deleteById(id);
        Log log = new Log(); // 삭제 로그 남기기
        log.setIp(ip);
        log.setMethod("delete");
        log.setRegdate(new Date());
        logDao.insert(log);
        return deleteCount;
    }

    //게스트북 삽입
    @Override
    @Transactional(readOnly=false) //이 메소드는 ReadOnly가 아니므로 false 처리
    public Guestbook addGuestbook(Guestbook guestbook, String ip) {
        guestbook.setRegdate(new Date());
        Long id = guestbookDao.insert(guestbook);
        guestbook.setId(id);
        
        // 이 메서드는 @Transactional이 붙어있다. 즉! 이 메서드는 트랜잭션이다. 즉! 이 메서드 전체 로직은 나눌 수 없는 하나의 단위이다. 
        // 이 메서드의 모든 부분이 성공해야 DB에 반영이된다. 
        // 아래는 트랜잭션을 배우기 위한 강제 예외처리 코드 ( insert 이후 강제로 예외가 발생하게 한다. 윗 라인에서 insert가 정상적으로 될지라도 이 메서드는 트랜잭션이므로 DB에 반영이 안됨 즉, rollbakc 된다.)
        
        /*
        if(1 == 1)        
            throw new RuntimeException("test exception");
          */
        
        Log log = new Log(); // 삽입 로그 남기기
        log.setIp(ip);
        log.setMethod("insert");
        log.setRegdate(new Date());
        logDao.insert(log);
        
        return guestbook;
    }

    @Override
    public int getCount() {
        return guestbookDao.selectCount();
    }
    
    
}