package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.SubscriptionDTO;
import com.nighthawk.aetha_backend.entity.AuthUser;
import com.nighthawk.aetha_backend.entity.Novel;
import com.nighthawk.aetha_backend.entity.Subscription;
import com.nighthawk.aetha_backend.entity.SubscriptionTiers;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import com.nighthawk.aetha_backend.repository.NovelRepository;
import com.nighthawk.aetha_backend.repository.SubscriptionRepository;
import com.nighthawk.aetha_backend.repository.SubscriptionTiersRepository;
import com.nighthawk.aetha_backend.utils.VarList;
import com.nighthawk.aetha_backend.utils.predefined.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SubscriptionService {

    private final SubscriptionTiersRepository _subscriptionTiersRepository;
    private final SubscriptionRepository _subscriptionRepository;
    private final AuthUserRepository _authUserRepository;
    private final NovelRepository _novelRepository;
    private final ResponseDTO responseDTO;

    @Autowired
    public SubscriptionService(
            SubscriptionTiersRepository subscriptionTiersRepository,
            SubscriptionRepository subscriptionRepository,
            AuthUserRepository authUserRepository,
            NovelRepository novelRepository,
            ResponseDTO responseDTO
    ) {
        _subscriptionTiersRepository = subscriptionTiersRepository;
        _subscriptionRepository = subscriptionRepository;
        _authUserRepository = authUserRepository;
        _novelRepository = novelRepository;
        this.responseDTO = responseDTO;
    }


    //? Create a new subscription
    public ResponseDTO createSubscription(UserDetails userDetails, SubscriptionDTO subscriptionDTO) {

        try {
            AuthUser user = _authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found")); //? Get the user from the authentication
            Novel novel = _novelRepository.findById(subscriptionDTO.getNovelId()).orElseThrow(() -> new NoSuchElementException("Novel not found")); //? Get the novel from the request

            //? Check for already existing subscriptions
            int previousSubscriptionCount = _subscriptionRepository.countByUserAndNovel(user, novel);

            if(previousSubscriptionCount > 0) {
                responseDTO.setCode(VarList.RSP_ERROR);
                responseDTO.setMessage("Subscription already exists");
                responseDTO.setContent("User already has a subscription for the novel");
            } else {
                //? Check for payment success confirmation
                boolean paymentSuccess = true; //? Assume payment was successful

                if(paymentSuccess) {

                    Subscription subscription = new Subscription();

                    SubscriptionTiers subscriptionTier = _subscriptionTiersRepository.findByNovel(novel).orElseThrow(() -> new NoSuchElementException("Subscription Tiers not found for this Novel"));

                    subscription.setUser(user);
                    subscription.setNovel(novel);
                    subscription.setAmount(subscriptionDTO.getAmount());
                    subscription.setSubscriptionTier(subscriptionTier);
                    subscription.setStartedData(java.time.LocalDate.now());

                    int months = switch (subscriptionDTO.getSubscriptionTier()) {
                        case 1 ->
                                Math.round((float) subscriptionTier.getTier1_duration() / 4); //? Duration was in weeks
                        case 2 ->
                                Math.round((float) subscriptionTier.getTier2_duration() / 4); //? Duration was in weeks
                        case 3 ->
                                Math.round((float) subscriptionTier.getTier3_duration() / 4); //? Duration was in weeks
                        default -> 0;
                    };

                    subscription.setEndDate(java.time.LocalDate.now().plusMonths(months));
                    subscription.setStatus(SubscriptionStatus.ACTIVE);

                    responseDTO.setCode(VarList.RSP_SUCCESS);
                    responseDTO.setMessage("Subscription created successfully");
                    responseDTO.setContent(_subscriptionRepository.save(subscription));
                } else {
                    responseDTO.setCode(VarList.RSP_ERROR);
                    responseDTO.setMessage("Payment Error");
                    responseDTO.setContent("Payment was not successful");
                }
            }

        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error Occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO getCurrentSubscriptions(UserDetails userDetails) {

        try {
            AuthUser user = _authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            List<Subscription> subscriptions = _subscriptionRepository.findByUser(user);

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(subscriptions);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error Occurred");
            responseDTO.setContent(e.getMessage());
        }

        return responseDTO;
    }

    public ResponseDTO checkSubscription(UserDetails userDetails, String novelId) {

        try {

            AuthUser user = _authUserRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));
            Novel novel = _novelRepository.findById(novelId).orElseThrow(() -> new NoSuchElementException("Novel not found"));

            Subscription subscription = _subscriptionRepository.findByUserAndNovel(user, novel).orElseThrow(() -> new NoSuchElementException("Subscription not found"));

            if (LocalDate.now().isAfter(subscription.getEndDate())) {
                throw new IllegalStateException("Subscription expired");
            }

            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Success");
            responseDTO.setContent(true);

        } catch (NoSuchElementException e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Subscription not found");
            responseDTO.setContent(e.getMessage());
        } catch (IllegalStateException e) {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Subscription Expired");
            responseDTO.setContent(false);
        } catch (Exception e) {
            responseDTO.setCode(VarList.RSP_ERROR);
            responseDTO.setMessage("Error Occurred");
            responseDTO.setContent(false);
        }

        return responseDTO;
    }
}
