//package com.clover.youngchat.global.security;
//
//import com.clover.youngchat.global.jwt.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.messaging.support.MessageHeaderAccessor;
//
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
//@RequiredArgsConstructor
//@Slf4j
//public class FilterChannelInterceptor implements ChannelInterceptor {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message,
//            StompHeaderAccessor.class);
//        if (headerAccessor == null) {
//            throw new AssertionError();
//        }
//
//        if (headerAccessor.getCommand() == StompCommand.CONNECT) {
//            String accessToken = String.valueOf(
//                headerAccessor.getNativeHeader("AccessToken").get(0));
//            accessToken = accessToken.split(" ")[1].trim();
//            try {
//                String email = jwtUtil.getUserInfoFromToken(accessToken);
//                headerAccessor.addNativeHeader("User", String.valueOf(email));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return MessageBuilder.createMessage(message.getPayload(),
//            headerAccessor.getMessageHeaders());
//    }
//}