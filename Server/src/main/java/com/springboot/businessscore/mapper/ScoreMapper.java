package com.springboot.businessscore.mapper;


import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.ScorePostDto;
import com.springboot.businessscore.dto.ScoreResponseDto;
import com.springboot.businessscore.entity.Score;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ScoreMapper {
    default Score scorePostDtoToScore(ScorePostDto scorePostDto){
        Business business = new Business();
        business.setBusinessesId(scorePostDto.getBusinessId());
        Score score = new Score();
        score.setBusinessScoreTitle(scorePostDto.getBusinessScoreTitle());
        score.setScoreList1(scorePostDto.getScoreList1());
        score.setScoreList2(scorePostDto.getScoreList2());
        score.setScoreList3(scorePostDto.getScoreList3());
        score.setScoreList4(scorePostDto.getScoreList4());
        score.setObserverName(scorePostDto.getObserverName());
        score.setModifiedAt(scorePostDto.getModifiedAt());

        return score;
    }

    default ScoreResponseDto scoreToScoreResponseDto(Score score){
        ScoreResponseDto responseDto = new ScoreResponseDto();
        responseDto.setBusinessId(score.getScoreId());
        responseDto.setBusinessScoreTitle(score.getBusinessScoreTitle());
        responseDto.setScoreList1(score.getScoreList1());
        responseDto.setScoreList2(score.getScoreList2());
        responseDto.setScoreList3(score.getScoreList3());
        responseDto.setScoreList4(score.getScoreList4());
        responseDto.setObserverName(score.getObserverName());
        responseDto.setCreatedAt(score.getCreatedAt());
        responseDto.setModifiedAt(score.getModifiedAt());
        return responseDto;
    }
    // 여러 score를 포함한 리스트이기 때문에 scores의 모든 요소(여기서는 각 score객체)를 다 한꺼번에 디티오로 변환하는 과정
    default List<ScoreResponseDto> scoreToScoresResponseDto(List<Score> scores){
        return scores.stream()
                .map(score -> scoreToScoreResponseDto(score)).collect(Collectors.toList());
    }
}
