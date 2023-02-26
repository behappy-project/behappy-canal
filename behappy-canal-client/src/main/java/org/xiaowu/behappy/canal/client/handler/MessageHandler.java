package org.xiaowu.behappy.canal.client.handler;

/**
 * protocol message处理
 * <p>
 * Message和FlatMessage什么区别？
 * Message和FlatMessage都有各自对应的Handler。
 * FlatMessage在网络中传播过程中是一个json；
 * 而Message比较接近字节码数据，传输效率高，但需要反序列化；
 *
 * @author xiaowu
 */
public interface MessageHandler<T> {

     void handleMessage(T t);
}
