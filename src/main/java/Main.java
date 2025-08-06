import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        System.out.println(generateRoute("RLRFR", 100));

        Integer maxValue = Collections.max(sizeToFreq.values());
        Integer maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxKey, maxValue);
        System.out.println("Другие размеры:");

        for (Map.Entry<Integer, Integer> i : sizeToFreq.entrySet()) {
            System.out.printf("- %d (%d раз)\n", i.getKey(), i.getValue());
        }
    }

    public static String generateRoute(String letters, int length) {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        Random random = new Random();
        StringBuilder route = new StringBuilder();

        for (int i = 0; i < 1000; i++) {
            AtomicInteger counterKey = new AtomicInteger();
            AtomicInteger counterValue = new AtomicInteger();
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    for (int j = 0; j < length; j++) {
                        char ch = letters.charAt(random.nextInt(letters.length()));
                        route.append(ch);
                        if (ch == 'R') {
                            counterKey.getAndIncrement();
                        }
                    }
                    route.append('\n');
                    countDownLatch.countDown();
                }
                if (!sizeToFreq.containsKey(counterKey.intValue())) {
                    counterValue.set(1);
                    sizeToFreq.put(counterKey.intValue(), counterValue.intValue());
                } else {
                    int value = sizeToFreq.get(counterKey.intValue());
                    value++;
                    sizeToFreq.put(counterKey.intValue(), value);
                }
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return route.toString();
    }
}
