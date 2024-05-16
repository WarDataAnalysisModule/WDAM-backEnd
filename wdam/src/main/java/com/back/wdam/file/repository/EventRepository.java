package com.back.wdam.file.repository;

import com.back.wdam.entity.Event;
import com.back.wdam.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUsers(Users user);
}
