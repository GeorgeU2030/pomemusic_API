package com.pomegranate.pomemusic.dto;

public record ResponseUserDto(UserDto user, String token, String message) {}
