package com.gongspot.project.domain.order.service;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.GeneralException;

import io.portone.sdk.server.payment.Payment;
import io.portone.sdk.server.payment.PaymentClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortoneApiService {

    private PaymentClient iamportClient;

    @Value("${imp.api-key}")
    private String apiKey;

    @Value("${imp.store-id}")
    private String storeId;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new PaymentClient(secretKey,"https://api.portone.io", storeId);
    }

    /**
     * imp_uid를 사용하여 포트원 서버에서 결제 정보를 조회 & 금액 검증
     *
     * @param impUid 클라이언트로부터 전달받은 결제 고유번호
     * @param clientAmount 클라이언트가 요청한 결제 금액
     */
    public void verifyPayment(String impUid, Long clientAmount) {
        try {
            Payment payment = iamportClient.getPayment(impUid).join();

            if (payment instanceof Payment.Unrecognized) {
                log.error("Unrecognized payment type for impUid: {}", impUid);
                throw new GeneralException(ErrorStatus.PAYMENT_NOT_FOUND);
            }

            if (payment instanceof Payment.Recognized recognized) {
                Long portoneAmount = recognized.getAmount().getTotal();

                if (portoneAmount.compareTo(clientAmount) != 0) {
                    log.error("Payment amount mismatch. Portone amount: {}, Client amount: {}", portoneAmount, clientAmount);
                    throw new GeneralException(ErrorStatus.PAYMENT_AMOUNT_MISMATCH);
                }

                log.info("Payment verification successful for impUid: {}", impUid);
            }

        } catch (CompletionException e) {
            log.error("Failed to verify payment with impUid: {}", impUid, e);
            throw new GeneralException(ErrorStatus.PAYMENT_VERIFICATION_FAILED);
        }
    }

}