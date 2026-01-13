package com.dndplatform.notificationservice.view.model.vm;

public record UserRegisteredEventViewModel(
        Long userId,
        String email,
        String username
) {
}