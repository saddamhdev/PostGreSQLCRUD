package com.PostGreSQL.dto;

import java.util.List;

public record TreePermissionRequest(Long userId, List<MenuSelection> menus) {}