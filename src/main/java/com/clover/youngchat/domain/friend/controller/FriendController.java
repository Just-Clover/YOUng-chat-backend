package com.clover.youngchat.domain.friend.controller;

import com.clover.youngchat.domain.friend.dto.response.FriendAddRes;
import com.clover.youngchat.domain.friend.dto.response.FriendDeleteRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetListRes;
import com.clover.youngchat.domain.friend.dto.response.FriendGetSearchListRes;
import com.clover.youngchat.domain.friend.service.FriendService;
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

    private final FriendService friendService;

    @GetMapping
    public RestResponse<List<FriendGetListRes>> getFriendList(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(friendService.getFriendList(userDetails.getUser()));
    }

    @GetMapping("/search/{keyword}")
    public RestResponse<List<FriendGetSearchListRes>> getFriendSearchList(
        @PathVariable String keyword,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(
            friendService.getFriendSearchList(keyword, userDetails.getUser()));
    }

    @PostMapping("/{friendId}")
    public RestResponse<FriendAddRes> addFriend(@PathVariable Long friendId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(friendService.addFriend(friendId, userDetails.getUser()));
    }

    @DeleteMapping("/{friendId}")
    public RestResponse<FriendDeleteRes> deleteFriend(@PathVariable Long friendId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return RestResponse.success(friendService.deleteFriend(friendId, userDetails.getUser()));
    }
}
