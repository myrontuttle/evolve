package com.myrontuttle.sci.evolve;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.myrontuttle.sci.evolve.util.concurrent.ConfigurableThreadFactory;
import com.myrontuttle.sci.evolve.util.id.IDSource;
import com.myrontuttle.sci.evolve.util.id.IntSequenceIDSource;
import com.myrontuttle.sci.evolve.util.id.StringPrefixIDSource;

/**
 * This is the class that actually runs the expression tasks created by a
 * {@link EvolutionEngine}.  This responsibility is abstracted away from
 * the evolution engine to permit the possibility of creating multiple instances
 * across several machines, all fed by a single shared work queue, using Terracotta
 * (http://www.terracotta.org) or similar.
 * @author Myron Tuttle
 */
public class ExpressionWorker
{
    // Provide each worker instance with a unique name with which to prefix its threads.
    private static final IDSource<String> WORKER_ID_SOURCE = new StringPrefixIDSource("FitnessEvaluationWorker",
                                                                                      new IntSequenceIDSource());

    /**
     * Share this field to use Terracotta to distribute fitness evaluations.
     */
    private final LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();


    /**
     * Thread pool that performs concurrent fitness evaluations.
     */
    private final ThreadPoolExecutor executor;


    /**
     * Creates a FitnessEvaluationWorker that uses daemon threads.
     */
    ExpressionWorker()
    {
        this(true);
    }


    /**
     * @param daemonWorkerThreads If true, any worker threads created will be daemon threads.
     */
    private ExpressionWorker(boolean daemonWorkerThreads)
    {
        ConfigurableThreadFactory threadFactory = new ConfigurableThreadFactory(WORKER_ID_SOURCE.nextID(),
                                                                                Thread.NORM_PRIORITY,
                                                                                daemonWorkerThreads);
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                                               Runtime.getRuntime().availableProcessors(),
                                               60,
                                               TimeUnit.SECONDS,
                                               workQueue,
                                               threadFactory);
        executor.prestartAllCoreThreads();
    }


    public <T> Future<ExpressedCandidate<T>> submit(ExpressionTask<T> task)
    {
        return executor.submit(task);
    }


    /**
     * Entry-point for running this class standalone, as an additional node for expressions.
     * If this method is invoked without using Terracotta (or similar) to share the work queue, the
     * program will do nothing.
     * @param args Program arguments, should be empty.
     */
    public static void main(String[] args)
    {
        // The program will not exit immediately upon completion of the main method because
        // the worker is configured to use non-daemon threads that keep the JVM alive.
        new ExpressionWorker(false);
    }


    /**
     * An ExpressionWorker cannot be garbage-collected if its thread pool has not been shutdown.
     * This method, invoked on garabage collection (or maybe not at all), shuts down the thread
     * pool so that the threads can be released. 
     * @throws Throwable Any exception or error that occurs during finalisation.
     */
    @Override
    protected void finalize() throws Throwable
    {
        executor.shutdown();
        super.finalize();
    }
}
