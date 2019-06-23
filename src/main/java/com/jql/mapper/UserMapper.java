package com.jql.mapper;

import com.jql.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from t_user where name=#{value}")
    User getUserByName(String name);
    @Select("select r.`name` from t_role r where r.id in (select r_id from t_ur where u_id = (select u.`id` from t_user u where u.name = #{name}))")
    List<String> getRolesByUserName(String name);
    @Select("select p.name from t_permission p where p.id in (select p_id from t_rp where r_id in (#{roles}))")
    List<String> selectPermissionsByRoleId(Integer roles);
 }
