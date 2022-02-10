package com.ssafy.arttab.like;

import com.ssafy.arttab.artwork.Artwork;
import com.ssafy.arttab.artwork.ArtworkRepository;
import com.ssafy.arttab.like.dto.LikeRequestDto;
import com.ssafy.arttab.member.domain.Member;
import com.ssafy.arttab.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ArtworkRepository artworkRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public void InsertLike(LikeRequestDto requestDto) {
        Artwork artwork = artworkRepository.findById(requestDto.getArtworkId())
                .orElseThrow(() -> new
                        IllegalArgumentException("insertlike artwork error id=" + requestDto.getArtworkId()));
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new
                        IllegalArgumentException("insertlike member error id=" + requestDto.getMemberId()));
        likeRepository.save(requestDto.toEntity(artwork, member)).getId();
    }

    public void delete(LikeRequestDto requestDto) {
        Artwork artwork = artworkRepository.findById(requestDto.getArtworkId())
                .orElseThrow(() -> new
                        IllegalArgumentException("deleteLike artwork error id=" + requestDto.getArtworkId()));
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new
                        IllegalArgumentException("deleteLike member error id=" + requestDto.getMemberId()));
        likeRepository.delete(requestDto.toEntity(artwork, member));
    }

}
