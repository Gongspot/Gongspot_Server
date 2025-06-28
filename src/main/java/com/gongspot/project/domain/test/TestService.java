package com.gongspot.project.domain.test;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String getSuccessData() {
        // 비즈니스 로직 예시
        return "서비스 단에서 처리한 데이터";
    }

    public void throwError() {
        // 예외 발생 로직
        throw new GeneralException(ErrorStatus._BAD_REQUEST);
    }
}
