package com.curty.muggle.post.service;

import com.curty.muggle.common.component.EntityFinder;
import com.curty.muggle.common.component.PostValidator;
import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.dto.request.PostAddRequest;
import com.curty.muggle.post.dto.request.PostUpdateRequest;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.post.repository.PostRepository;
import com.curty.muggle.postimage.service.PostImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostAddService {
    private final EntityFinder entityFinder;
    private final PostImageUploadService postImageUploadService;
    private final PostRepository postRepository;
    private final PostValidator postValidator;

    /**
     * 게시글 등록 요청 DTO의 필드 유효성을 검사하는 내부 메서드
     * 제목, 내용, 카테고리의 유효성을 {@link PostValidator}를 통해 확인
     *
     * @param request 등록 요청 DTO ({@link PostUpdateRequest})
     */
    private void validate(PostAddRequest request) {
        postValidator.checkTitle(request.getTitle());
        postValidator.checkContent(request.getContent());
        postValidator.checkCategory(request.getCategory());
    }

    /**
     * 새로운 게시글을 추가하는 메서드로
     * 파일이 포함된 경우, GCS에 이미지를 업로드하고 파일 메타데이터를 데이터베이스에 저장한다.
     * 모든 파일 업로드 및 DB 저장이 트랜잭션 내에서 일관되게 처리된다.
     *
     * @param request  추가할 게시글의 데이터를 담은 {@link PostAddRequest} 객체
     * @param member 게시글을 작성하는 회원 {@link Member} 객체
     */
    @Transactional
    public void add(PostAddRequest request, Member member) {
        // 검증
        validate(request);

        // Post 객체 조회
        Post post = request.toEntity(member);

        // Post 저장
        Post savedPost = postRepository.save(post);

        // GCS에 이미지 업로드 + PostFile 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            postImageUploadService.upload(savedPost, request.getImages());
        }
    }
}
