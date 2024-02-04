package com.clover.youngchat.domain.friend.controller;

import com.clover.youngchat.domain.friend.dto.response.FriendAddRes;
import com.clover.youngchat.domain.friend.dto.response.FriendDeleteRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetListRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
import com.clover.youngchat.domain.friend.service.command.FriendCommandService;
import com.clover.youngchat.domain.friend.service.query.FriendQueryService;
import com.clover.youngchat.global.response.RestResponse;
import com.clover.youngchat.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendQueryService friendQueryService;
    private final FriendCommandService friendCommandService;

    @GetMapping
    public RestResponse<List<FriendGetListRes>> getFriendList(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(friendQueryService.getFriendList(userDetails.getUser()));
    }

    @GetMapping("/search")
    public RestResponse<List<FriendGetSearchListRes>> getFriendSearchList(
        String keyword,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            friendQueryService.getFriendSearchList(keyword, userDetails.getUser()));
    }

    @PostMapping("/{friendId}")
    public RestResponse<FriendAddRes> addFriend(@PathVariable Long friendId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            friendCommandService.addFriend(friendId, userDetails.getUser()));
    }

    @DeleteMapping("/{friendId}")
    public RestResponse<FriendDeleteRes> deleteFriend(@PathVariable Long friendId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            friendCommandService.deleteFriend(friendId, userDetails.getUser()));
    }
}
