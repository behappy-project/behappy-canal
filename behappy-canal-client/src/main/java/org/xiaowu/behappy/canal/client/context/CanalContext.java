package org.xiaowu.behappy.canal.client.context;

import lombok.experimental.UtilityClass;
import org.xiaowu.behappy.canal.client.model.CanalModel;

/**
 * @author xiaowu
 */
@UtilityClass
public class CanalContext {

    private static final ThreadLocal<CanalModel> CANAL_MODEL_THREAD_LOCAL = new ThreadLocal<>();

    public CanalModel getModel() {
        return CANAL_MODEL_THREAD_LOCAL.get();
    }

    public void setModel(CanalModel canalModel) {
        CANAL_MODEL_THREAD_LOCAL.set(canalModel);
    }

    public void removeModel() {
        CANAL_MODEL_THREAD_LOCAL.remove();
    }
}
