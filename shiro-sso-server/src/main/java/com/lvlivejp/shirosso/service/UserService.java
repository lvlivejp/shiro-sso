package com.lvlivejp.shirosso.service;

import com.lvlivejp.shirosso.core.base.BaseResult;
import com.lvlivejp.shirosso.model.TUser;

public interface UserService {
    BaseResult save(TUser tUser);
}
