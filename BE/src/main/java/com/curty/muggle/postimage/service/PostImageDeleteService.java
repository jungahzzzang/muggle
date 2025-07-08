package com.curty.muggle.postimage.service;

import com.curty.muggle.postimage.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostImageDeleteService {
    private final PostImageRepository postImageRepository;

    /**
     * 여러 이미지 파일을 DB에서 삭제하는 메서드
     *
     * @param postIds 삭제할 이미지들의 고유 ID 목록
     */
    @Transactional
    public void delete(List<String> postIds) {
        postImageRepository.deleteAllById(
                postIds.stream().map(
                        UUID::fromString
                ).collect(Collectors.toList())
        );

        // TODO: GCP에서도 삭제를 해야하는지 미결정
    }
}
