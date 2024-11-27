package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.entity.SubscriptionTiers;
import com.nighthawk.aetha_backend.repository.SubscriptionTiersRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class SubscriptionTiersService {

    private final SubscriptionTiersRepository _subscriptionTiersRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionTiersService.class);
    private final ResponseDTO responseDTO;

    @Autowired
    public SubscriptionTiersService(
            SubscriptionTiersRepository subscriptionTiersRepository,
            ResponseDTO responseDTO) {
        _subscriptionTiersRepository = subscriptionTiersRepository;
        this.responseDTO = responseDTO;
    }

    public boolean createSubscriptionTiers(SubscriptionTiers subscriptionTiers) {
        try {

            //? Check whether there are any existing subscription tiers for the current novel
            int subscriptionTierCount = _subscriptionTiersRepository.countByNovel(subscriptionTiers.getNovel());

            if(subscriptionTierCount != 0) {
                logger.warn("Subscription tiers already exists for the novel : {}", subscriptionTiers.getNovel().getId());
                return false;
            }

            _subscriptionTiersRepository.save(subscriptionTiers);
            return true;
        } catch (Exception e) {
            logger.error("Error while creating subscription tiers : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateSubscriptionTiers(SubscriptionTiers subscriptionTiers, String subscriptionTiersId) {
        try {
            SubscriptionTiers previousSubscriptionTiers = _subscriptionTiersRepository.findById(subscriptionTiersId)
                    .orElseThrow(() -> new NoSuchElementException("Subscription tiers not found"));

            if(subscriptionTiers.getId() == null) {
                logger.warn("Subscription tiers id is null, setting the previous id : {}", previousSubscriptionTiers.getId());
                subscriptionTiers.setId(previousSubscriptionTiers.getId());
            }

            _subscriptionTiersRepository.save(subscriptionTiers);
            return true;
        } catch (Exception e) {
            logger.error("Error while updating subscription tiers : {}", e.getMessage());
            return false;
        }
    }

    public boolean deleteSubscriptionTier(String subscriptionTiersId) {
        try {
            _subscriptionTiersRepository.deleteById(subscriptionTiersId);
            return true;
        } catch (Exception e) {
            logger.error("Error while deleting subscription tiers : {}", e.getMessage());
            return false;
        }
    }

    public ResponseDTO getSubscriptionTiersForNovel(String novelId) {
        try {
            SubscriptionTiers subscriptionTiers = _subscriptionTiersRepository.findByNovelId(novelId)
                    .orElseThrow(() -> new NoSuchElementException("Subscription tiers not found"));

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(subscriptionTiers);
        } catch (Exception e) {
            logger.error("Error while getting subscription tiers : {}", e.getMessage());

            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO updateSubscriptionTiersForNovel(String novelId, SubscriptionTiers tiers) {
        System.out.println(tiers);

        try {
            _subscriptionTiersRepository.save(tiers);
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(null);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Failure");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }
}
