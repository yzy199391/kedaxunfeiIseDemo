package yzy.kedaxunfei.research.demo.Util;

import org.springframework.context.ApplicationContext;

import java.util.concurrent.*;

/**
 * @author yanzy
 * @date 2019/2/21 上午10:27
 * @description
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;
    static final private Object lock = new Object();

    public static void setApplicationContext(ApplicationContext applicationContext) throws ExecutionException, InterruptedException {
        synchronized (lock){
            if(applicationContext == null){
                SpringUtil.applicationContext = applicationContext;
            }
        }

        ExecutorService service = Executors.newSingleThreadExecutor();
        FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });
        Future<String> future1 = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });

        service.execute(future);
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}
