package com.PostGreSQL.dto;

import java.util.List;

//public record PageSelection(Long id, boolean checked, List<ComponentSelection> components) {}
public record PageSelection(Long id, String name, boolean checked, List<ComponentSelection> components) {}