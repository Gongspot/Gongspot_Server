package com.gongspot.project.domain.order.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortoneApiService {

    private IamportClient iamportClient;

    @Value("${imp.api-key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    /**
     * imp_uid를 사용하여 포트원 서버에서 결제 정보를 조회 & 금액 검증
     *
     * @param impUid 클라이언트로부터 전달받은 결제 고유번호
     * @param clientAmount 클라이언트가 요청한 결제 금액
     */
    public void verifyPayment(String impUid, BigDecimal clientAmount) {
        try {
            Payment payment = iamportClient.paymentByImpUid(impUid).getResponse();

            if (payment == null) {
                log.error("Payment not found for impUid: {}", impUid);
                throw new GeneralException(ErrorStatus.PAYMENT_NOT_FOUND);
            }

            // 서버 DB에 저장된 금액(clientAmount)과 아임포트 서버의 실제 결제 금액(payment.getAmount()) 비교
            BigDecimal portoneAmount = payment.getAmount();
            if (portoneAmount.compareTo(clientAmount) != 0) {
                log.error("Payment amount mismatch. Portone amount: {}, Client amount: {}", portoneAmount, clientAmount);
                throw new GeneralException(ErrorStatus.PAYMENT_AMOUNT_MISMATCH);
            }

            log.info("Payment verification successful for impUid: {}", impUid);

        } catch (IamportResponseException | IOException e) {
            log.error("Failed to verify payment with impUid: {}", impUid, e);
            throw new GeneralException(ErrorStatus.PAYMENT_VERIFICATION_FAILED);
        }
    }
}
