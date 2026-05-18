package org.example.jobservice.repository;

import org.example.jobservice.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversationId IN :convIds AND m.senderId != :userId AND m.read = false")
    long countUnread(@Param("convIds") List<String> convIds, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.read = true WHERE m.conversationId = :convId AND m.senderId != :userId")
    void markConversationRead(@Param("convId") String convId, @Param("userId") Long userId);
}
