package com.ras.demo.mapper;


import com.ras.demo.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);
}
