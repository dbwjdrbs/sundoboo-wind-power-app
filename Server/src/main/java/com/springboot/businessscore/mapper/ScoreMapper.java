package com.springboot.businessscore.mapper;


import com.springboot.business.entity.Business;
import com.springboot.businessscore.dto.ScorePostDto;
import com.springboot.businessscore.entity.Score;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

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
}
