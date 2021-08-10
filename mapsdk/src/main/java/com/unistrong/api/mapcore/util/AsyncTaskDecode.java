package com.unistrong.api.mapcore.util;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AsyncTaskDecode<Params, Progress, Result> //av
{

  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();//CPU数
  private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
  private static final int MAXIMUM_POOL_SIZE = CPU_COUNT  + 1;
  private static final int KEEP_ALIVE = 1;
	private static final int MESSAGE_POST_RESULT = 0x1;
	private static final int MESSAGE_POST_PROGRESS = 0x2;

   private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};

  private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10); //e
  public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy()); //b
  public static final Executor SERIAL_EXECUTOR = (Util.hasHoneycomb()) ? new SerialExecutor() : Executors.newSingleThreadExecutor(sThreadFactory); //c
  public static final Executor DUAL_THREAD_EXECUTOR = Executors.newFixedThreadPool(4, sThreadFactory); // d
  private static final InternalHandler sHandler = new InternalHandler();
  private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR; //g
  private final WorkerRunnable<Params, Result> mWorker;
  private final FutureTask<Result> mFuture;
  private volatile Status mStatus = Status.PENDING; //j
  private final AtomicBoolean mCancelled = new AtomicBoolean(); //k
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean(); //l

  public AsyncTaskDecode()
  {
    this.mWorker = new WorkerRunnable<Params, Result>() {
      public Result call() throws Exception {
        AsyncTaskDecode.this.mTaskInvoked.set(true);

        Process.setThreadPriority(10);

        //return av.this.changeBearing(av.this.a(av.this.b));
        return postResult(doInBackground(mParams));
      }

    };
    this.mFuture = new FutureTask<Result>(this.mWorker)
    {
      protected void done() {
        try {
          AsyncTaskDecode.this.postResultIfNotInvoked(AsyncTaskDecode.this.mFuture.get());
        } catch (InterruptedException localInterruptedException) {
          Log.w("AsyncTask", localInterruptedException);
        }
        catch (ExecutionException localExecutionException)
        {
          throw new java.lang.RuntimeException("An error occured while executing doInBackground()", localExecutionException
            .getCause());
        } catch (CancellationException localCancellationException) {
          AsyncTaskDecode.this.postResultIfNotInvoked(null);
        }
      }
    };
  }

  private void postResultIfNotInvoked(Result paramResult) {
    boolean wasTaskInvoked = this.mTaskInvoked.get();
    if (!(wasTaskInvoked))
      postResult(paramResult);
  }

  private Result postResult(Result paramResult)
  {
    //Message localMessage = sHandler.obtainMessage(1, new AsyncTaskResult(this, new Object[] { paramResult }));
	  @SuppressWarnings("unchecked")
	Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
				new AsyncTaskResult<Result>(this, paramResult));
	  message.sendToTarget();
    return paramResult;
  }

  public final Status getStatus() // a
  {
    return this.mStatus;
  }

  protected abstract Result doInBackground(Params ... paramArrayOfParams);

  protected void onPreExecute() //b
  {
  }

  protected void onPostExecute(Result paramResult) // a
  {
  }

  protected void onProgressUpdate(Progress[] paramArrayOfProgress) //b
  {
  }

  protected void onCancelled(Result paramResult) //b
  {
    onCancelled();
  }

  protected void onCancelled() //c
  {
  }

  public final boolean isCancelled() // d
  {
    return this.mCancelled.get();
  }

  public final boolean cancel(boolean paramBoolean) // a
  {
    this.mCancelled.set(true);
    return this.mFuture.cancel(paramBoolean);
  }

  public final AsyncTaskDecode<Params, Progress, Result> execute(Params ... paramArrayOfParams) //c
  {
    return executeOnExecutor(sDefaultExecutor, paramArrayOfParams);
  }

  public final AsyncTaskDecode<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params[] paramArrayOfParams) // a
  {
    if (this.mStatus != Status.PENDING) {
      //switch (ax.a[this.j.ordinal()])
      switch (this.mStatus)
      {
      //case 1:
      case RUNNING:
        throw new IllegalStateException("Cannot execute task: the task is already running.");
      //case 2:
      case FINISHED:
        throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
      }

    }

    this.mStatus = Status.RUNNING;

    onPreExecute();

    this.mWorker.mParams = paramArrayOfParams;
    paramExecutor.execute(this.mFuture);

    return this;
  }

  private void finish(Result paramResult) //e
  {
    if (isCancelled())
      onCancelled(paramResult);
    else
      onPostExecute(paramResult);

    this.mStatus = Status.FINISHED;
  }

  private static class AsyncTaskResult<Data>
  {
    final AsyncTaskDecode a; // a
    final Data[] b; //b

    //AsyncTaskResult(av paramav, Data[] paramArrayOfData)
    AsyncTaskResult(AsyncTaskDecode paramav, Data... paramArrayOfData)
    {
      this.a = paramav;
      this.b = paramArrayOfData;
    }
  }

  private static abstract class WorkerRunnable<Params, Result>
    implements Callable<Result>
  {
    Params[] mParams;
  }

  private static class InternalHandler extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      AsyncTaskDecode.AsyncTaskResult locala = (AsyncTaskDecode.AsyncTaskResult)paramMessage.obj;
      switch (paramMessage.what)
      {
      case 1:
        locala.a.finish(locala.b[0]);
        break;
      case 2:
        locala.a.onProgressUpdate(locala.b);
      }
    }
  }

  public static enum Status
  {
    PENDING, RUNNING, FINISHED;
  }

  private static class SerialExecutor //c
    implements Executor
  {
    final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
    Runnable mActive;

	public synchronized void execute(final Runnable r) {
		mTasks.offer(new Runnable() {
			public void run() {
				try {
					r.run();
				} finally {
					scheduleNext();
				}
			}
		});
		if (mActive == null) {
			scheduleNext();
		}
	}

	protected synchronized void scheduleNext() {
		if ((mActive = mTasks.poll()) != null) {
			THREAD_POOL_EXECUTOR.execute(mActive);
		}
	}
  }
}