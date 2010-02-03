package egp.genealang.util;

import egp.genealang.infra.NamedCaller;

public class ThreadUtil {
    public static void spawnThread(final NamedCaller nc, String threadName,final Runnable r){
        new Thread(threadName){
            public void run(){
                try{
                    r.run();
                }catch(Throwable tr){
                    ExceptionUtil.handleException(nc, tr);
                }
            }
        }.start();
    }
}
