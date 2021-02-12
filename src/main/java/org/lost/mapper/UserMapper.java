package org.lost.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.lost.domain.LUser;
import org.lost.domain.NUser;
import org.lost.domain.User;
import org.springframework.stereotype.Repository;


@Mapper
public interface UserMapper {

    @Select("select name from user where id = '${id}'")
    String findNameById(String id);

    @Select("select * from user where name = '${name}'")
    User findByName(String name);

    @Select("select * from user where code = '${code}'")
    User findByCode(String code);

    @Update("update user set status = 'Y' where id = '${id}'")
    void updateStatus(String id);

    @Insert("insert into user values('${id}','${name}','${password}','${email}','${status}','${code}','${bigPic}'" +
            ",'${smallPic}','${qrcode}')")
    void register(User user);

    @Select("select * from user where name = '${name}' and password = '${password}' and status = 'Y' ")
    User login(LUser user);

    @Select("select * from user where id = '${id}' and password = '${password}' ")
    User selectByPrimaryKey(String id, String password);

    @Update("update user set bigPic = '${bigPic}' , smallPic = '${smallPic}' where id = '${id}' and password = '${password}' ")
    void updatePic(User user);

    @Update({"<script>",
            "update user set ",
            "<when test=\" name!=null and name != '' \">",
            "name = '${name}', ",
            "</when>",
            "<when test=\" password!=null and password != '' \">",
            "password = '${password}', ",
            "</when>",
            "<when test=\" email!=null and email != '' \">",
            " email = '${email}', ",
            "</when>",
            " id = '${id}' where id = '${id}' and password = '${oldPassword}' ","</script>"})
    void changeUser(NUser user);
}
