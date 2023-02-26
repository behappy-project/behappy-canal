package org.xiaowu.behappy.canal.client.client;

/**
 * @author xiaowu
 */
public interface CanalClient<T> {

    void start();

    void stop();

    void process();
}
