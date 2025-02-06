package com.pofo.backend.domain.reply.service;

import com.pofo.backend.domain.inquiry.entity.Inquiry;
import com.pofo.backend.domain.inquiry.repository.InquiryRepository;
import com.pofo.backend.domain.reply.dto.request.ReplyCreateRequest;
import com.pofo.backend.domain.reply.dto.response.ReplyDetailResponse;
import com.pofo.backend.domain.reply.dto.request.ReplyUpdateRequest;
import com.pofo.backend.domain.reply.dto.response.ReplyCreateResponse;
import com.pofo.backend.domain.reply.dto.response.ReplyUpdateResponse;
import com.pofo.backend.domain.reply.entity.Reply;
import com.pofo.backend.domain.reply.exception.ReplyException;
import com.pofo.backend.domain.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final InquiryRepository inquiryRepository;

    @Transactional
    public ReplyCreateResponse create(Long id, ReplyCreateRequest replyCreateRequest) {

//        if (admin == null) {
//            throw new ReplyException("관리자 정보가 유효하지 않습니다.");
//        }

        Inquiry inquiry = this.inquiryRepository.findById(id)
                .orElseThrow(() -> new ReplyException("문의사항을 찾을 수 없습니다."));

        if (replyRepository.existsByInquiryId(id)) {
            throw new ReplyException("해당 문의에 답변이 이미 존재합니다.");
        }

        try {
            Reply reply = Reply.builder()
//                    .admin(admin)
                    .inquiry(inquiry)
                    .content(replyCreateRequest.getContent())
                    .build();

            this.replyRepository.save(reply);
            return new ReplyCreateResponse(reply.getId());
        } catch (Exception e) {
            throw new ReplyException("답변 생성 중 오류가 발생했습니다. 원인: " + e.getMessage());
        }
    }

    @Transactional
    public ReplyUpdateResponse update(Long replyId, Long inquiryId, ReplyUpdateRequest replyUpdateRequest) {

        Reply reply = this.replyRepository.findByIdAndInquiryId(replyId, inquiryId)
                .orElseThrow(() -> new ReplyException("해당 답변을 찾을 수 없습니다."));

        try {
            reply.update(replyUpdateRequest.getContent());
            return new ReplyUpdateResponse(reply.getId());
        } catch (Exception e) {
            throw new ReplyException("답변 수정 중 오류가 발생했습니다. 원인: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public ReplyDetailResponse findById(Long id) {

        Reply reply = this.replyRepository.findById(id)
                .orElseThrow(() -> new ReplyException("해당 답변을 찾을 수 없습니다."));

        return new ReplyDetailResponse(reply.getId(), reply.getContent());
    }

    @Transactional(readOnly = true)
    public Long count() {
        return this.replyRepository.count();
    }
}
