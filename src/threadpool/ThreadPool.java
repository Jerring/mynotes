package threadpool;

public interface ThreadPool<Job extends Runnable> {
    /**
     * 执行一个 Job。
     *
     * @param job 这个 Job 需要实现 Runnable 接口
     */
    void execute(Job job);

    /**
     * 关闭线程池
     */
    void shutdown();

    /**
     * 增加工作者线程
     *
     * @param num 增加的数量
     */
    void addWorkers(int num);

    /**
     * 减少工作者线程
     *
     * @param num 减少的数量
     */
    void removeWorkers(int num);

    /**
     * 得到正在等待执行的任务数量
     *
     * @return 待执行的 Job 数量
     */
    int getJobSize();
}
