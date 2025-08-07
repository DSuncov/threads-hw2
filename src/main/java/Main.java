import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final int numberThreads = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static List<Thread> listOfThreads = new ArrayList<>(1000);

    public static void main(String[] args) {

        long before = System.currentTimeMillis();

        String[] listOfRoute = new String[numberThreads];

        for (int i = 0; i < listOfRoute.length; i++) {
            listOfRoute[i] = generateRoute("RLRFR", 100);
        }

        for (String route : listOfRoute) {
            Thread thread = new Thread(() -> {
                int counterKey = 0;
                int counterValue;
                for (int i = 0; i < route.length(); i++) {
                    if (route.charAt(i) == 'R') {
                        counterKey++;
                    }
                }

                System.out.println(route);

                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(counterKey)) {
                        counterValue = 1;
                        sizeToFreq.put(counterKey, counterValue);
                    } else {
                        sizeToFreq.put(counterKey, sizeToFreq.get(counterKey) + 1);
                    }
                }
            });
            listOfThreads.add(thread);
            thread.start();
        }

        for (Thread thread : listOfThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Integer maxValue = Collections.max(sizeToFreq.values());
        Integer maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();

        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxKey, maxValue);
        System.out.println("Другие размеры:");

        for (Map.Entry<Integer, Integer> i : sizeToFreq.entrySet()) {
            System.out.printf("- %d (%d раз)\n", i.getKey(), i.getValue());
        }

        long after = System.currentTimeMillis();
        System.out.println(after - before);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}

//    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
//
//    public static void main(String[] args) {
//        long before = System.currentTimeMillis();
//
//        System.out.println(generateRoute("RLRFR", 100));
//
//        Integer maxValue = Collections.max(sizeToFreq.values());
//        Integer maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
//
//        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxKey, maxValue);
//        System.out.println("Другие размеры:");
//
//        for (Map.Entry<Integer, Integer> i : sizeToFreq.entrySet()) {
//            System.out.printf("- %d (%d раз)\n", i.getKey(), i.getValue());
//        }
//        long after = System.currentTimeMillis();
//        System.out.println(after - before);
//    }
//
//    public static String generateRoute(String letters, int length) {
//        CountDownLatch countDownLatch = new CountDownLatch(1000);
//        Random random = new Random();
//        StringBuilder route = new StringBuilder();
//
//        for (int i = 0; i < 1000; i++) {
//            AtomicInteger counterKey = new AtomicInteger();
//            AtomicInteger counterValue = new AtomicInteger();
//            new Thread(() -> {
//                synchronized (sizeToFreq) {
//                    for (int j = 0; j < length; j++) {
//                        char ch = letters.charAt(random.nextInt(letters.length()));
//                        route.append(ch);
//                        if (ch == 'R') {
//                            counterKey.getAndIncrement();
//                        }
//                    }
//                    route.append('\n');
//                    countDownLatch.countDown();
//                }
//                if (!sizeToFreq.containsKey(counterKey.intValue())) {
//                    counterValue.set(1);
//                    sizeToFreq.put(counterKey.intValue(), counterValue.intValue());
//                } else {
//                    int value = sizeToFreq.get(counterKey.intValue());
//                    value++;
//                    sizeToFreq.put(counterKey.intValue(), value);
//                }
//            }).start();
//        }
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return route.toString();
//    }
//}

