package com.PostGreSQL.dto;

import java.util.List;

//public record MenuSelection(Long id, boolean checked, List<PageSelection> pages) {}
public record MenuSelection(Long id, String name, boolean checked, List<PageSelection> pages) {}
