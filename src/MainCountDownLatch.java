import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class MainCountDownLatch {
    CountDownLatch countDownLatchSecond;
    CountDownLatch countDownLatchThird;

    public MainCountDownLatch() {
        countDownLatchSecond = new CountDownLatch(1);
        countDownLatchThird = new CountDownLatch(1);
    }

    public void first() {
        System.out.print("first");
        countDownLatchSecond.countDown();
    }

    public void second() {
        try {
            countDownLatchSecond.await();
            System.out.print("second");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            countDownLatchThird.countDown();
        }
    }

    public void third() {
        try {
            countDownLatchThird.await();
            System.out.print("third");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MainCountDownLatch mainCountDownLatch = new MainCountDownLatch();

        CompletableFuture<Void> doSecond = CompletableFuture.runAsync(mainCountDownLatch::second);
        CompletableFuture<Void> doThird = CompletableFuture.runAsync(mainCountDownLatch::third);
        CompletableFuture<Void> doFirst = CompletableFuture.runAsync(mainCountDownLatch::first);
        doThird.get();
        doFirst.get();
        doSecond.get();
    }
}
