package com.example.holisticbabehelpcenter.repository;

import com.example.holisticbabehelpcenter.model.ExchangedMessages;
import com.example.holisticbabehelpcenter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangedMessageRepository extends JpaRepository<ExchangedMessages, Long> {
    List<ExchangedMessages> findBySenderAndReceiverOrderBySentTimeAsc(User sender, User receiver);
    List<ExchangedMessages> findByReceiverAndSenderOrderBySentTimeAsc(User receiver, User sender);



}
