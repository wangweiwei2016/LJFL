package cn.shang.android.common.util;

/**
 * Singleton helper class for lazily initialization.
 * 
 * @author <a href="http://www.shang.cn/" target="_blank">shang</a>
 * 
 * @param <T>
 */
public abstract class SingletonUtils<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
