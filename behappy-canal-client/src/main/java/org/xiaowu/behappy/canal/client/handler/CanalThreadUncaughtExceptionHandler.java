package org.xiaowu.behappy.canal.client.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author xiaowu
 */
@Slf4j
public class CanalThreadUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable e, Method t, Object... params) {
        log.error("method {} have a exception {} , params: {}",t.getName(), e, params);
    }
}
