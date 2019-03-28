package com.lvlivejp.shirosso.service;

import com.lvlivejp.shirosso.model.TPermission;

import java.util.List;

public interface PermissionService {

    List<TPermission> findAll();

    void insert(TPermission tPermission);
}
