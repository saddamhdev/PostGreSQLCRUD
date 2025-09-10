package com.PostGreSQL.dto;

import java.util.List;

public record TreePermissionResponse(Long userId, String userName,
                                     String email, List<MenuSelection> menus) {}

