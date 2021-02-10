package org.lost.service;

import org.lost.domain.LUser;
import org.lost.domain.NUser;
import org.lost.domain.RUser;
import org.lost.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void register(RUser rUser);

    boolean findByName(String name);

    boolean active(String code);

    User login(LUser user);
    User upload(MultipartFile file, String id, String password);

    User selectByPrimaryKey(String id, String password);

    boolean changeUser(NUser user);
}
