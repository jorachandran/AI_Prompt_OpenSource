package com.OpenSource.AI_Prompt_OpenSource.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.OpenSource.AI_Prompt_OpenSource.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
