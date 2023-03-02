package org.xiaowu.behappy.canal.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.xiaowu.behappy.canal.example.model.User;

/**
 *
 * @author xiaowu
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
