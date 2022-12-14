package com.example.exceptionprac.domain;

import com.example.exceptionprac.dto.AccountDto;
import com.querydsl.core.types.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // JPA 관련 테스트 설정만 로드 (기본적으로 @Transactional을 포함) > 테스트 시, 실제 DB가 아닌 테스트 DB로 테스트 진행(@Entity 적용된 클래스 스캔 후, 스프링 데이터 JPA 저장소 구성)
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private QUsers qUsers = QUsers.users;

    @Test
    public void findByEmail_test() {
        // 실행과 동시에 작성되는 SQL문이 없기 때문에 User 객체 저장하는 부분 별도 추가
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        // String email = "test@test.com";
        Users user = userRepository.findByEmail(Email.of(dto.getEmail().getEmail()));
        assertThat(user.getEmail().getEmail()).isEqualTo(dto.getEmail().getEmail());
    }

    @Test
    public void findById_test() {
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        Optional<Users> optionalUser = userRepository.findById(1L);
        Users user = optionalUser.get();
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    public void isExistedEmail_test() {
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        // String email = "test@test.com";
        boolean existsByEmail = userRepository.existsByEmail(Email.of(dto.getEmail().getEmail()));
        assertThat(existsByEmail).isTrue();
    }

    @Test
    public void findRecentlyRegistered_test() {
        List<Users> users = userRepository.findRecentlyRegistered(10);
        assertThat(users.size()).isLessThan(11);
    }

    // 이메일 존재 여부 테스트
    @Test
    public void predicate_test_01() {
        // given
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        Predicate predicate = qUsers.email.eq(Email.of("test@test.com"));

        // when
        boolean exists = userRepository.exists(predicate);

        // then
        assertThat(exists).isTrue();
    }

    // 유저명 존재 여부 테스트
    @Test
    public void predicate_test_02() {
        // given
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        Predicate predicate = qUsers.username.eq("soon"); // eq(): 동일성 체크 메서드

        // when
        boolean exists = userRepository.exists(predicate);

        // then
        assertThat(exists).isTrue();

    }

    @Test
    public void predicate_test_03() {
        // given
        AccountDto.SignUpReq dto = buildSignUpReq();
        userRepository.save(dto.toEntity());

        Predicate predicate = qUsers.email.email.like("test%"); // like("str%"): str 검색 메서드

        // when
        long count = userRepository.count(predicate);

        // then
        assertThat(count).isGreaterThan(0); // isGreaterThan(int): int보다 큰지 체크 (숫자 비교 메서드)
    }

    public AccountDto.SignUpReq buildSignUpReq() {
        return AccountDto.SignUpReq.builder()
                .email(buildEmail())
                .username("soon")
                .password("soon1234")
                .build();
    }

    public Email buildEmail() {
        return Email.builder()
                .email("test@test.com")
                .build();
    }

}