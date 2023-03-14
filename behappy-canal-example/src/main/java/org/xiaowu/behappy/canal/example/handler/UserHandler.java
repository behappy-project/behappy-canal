package org.xiaowu.behappy.canal.example.handler;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xiaowu.behappy.canal.client.handler.EntryHandler;
import org.xiaowu.behappy.canal.example.model.User;

import java.nio.file.Files;
import java.nio.file.Paths;


@Component
@Slf4j
public class UserHandler implements EntryHandler<User> {

    @Override
    public void insert(User user) {
        log.info("insert message  {}", user);
    }

    @SneakyThrows
    @Override
    public void update(User before, User after) {
        log.info("update before {} ", before);
        log.info("update after {}", after);
        Files.write(Paths.get("D:\\本田.jpg"), after.getLogo());
    }

    @Override
    public void delete(User user) {
        log.info("delete  {}", user);
        throw new RuntimeException("模拟错误");
    }
}
