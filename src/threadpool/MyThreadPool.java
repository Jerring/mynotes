package threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MyThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    /**
     * 线程池最大数量
     */
    private static final int MAX_WORKER_NUMBERS = 10;
    /**
     * 线程池默认数量
     */
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    /**
     * 线程池最小数量
     */
    private static final int MIN_WORKER_NUMBERS = 1;
    /**
     * 任务列表
     */
    private final LinkedList<Job> jobs = new LinkedList<>();
    /**
     * 工作者线程列表
     */
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
    /**
     * 工作者线程数量
     */
    private int workNum = DEFAULT_WORKER_NUMBERS;
    /**
     * 线程编号
     */
    private AtomicLong threadId = new AtomicLong();

    public MyThreadPool(int num) {
        workNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initializeWorkers(workNum);
    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            synchronized (jobs) {
                jobs.offer(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs) {
            if (workNum + num > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - workNum;
            }
            initializeWorkers(num);
            workNum += num;
        }
    }

    @Override
    public void removeWorkers(int num) {
        synchronized (jobs) {
            if (num > workNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            int cnt = 0;
            while (cnt < num) {
                Worker worker = workers.get(cnt);
                if (workers.remove(worker)) {
                    worker.shutdown();
                }
            }
            workNum -= cnt;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    private void initializeWorkers(int num) {
        for (int i = 0; i < num; ++i) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadId.incrementAndGet());
            thread.start();
        }
    }

    private class Worker implements Runnable {
        /**
         * 是否继续运行
         */
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 收到中断信号
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    job = jobs.poll();
                }
                if (job != null) {
                    job.run();
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }
}
