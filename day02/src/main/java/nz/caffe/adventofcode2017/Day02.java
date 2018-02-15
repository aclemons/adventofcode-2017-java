/**
 * Copyright 2018 Andrew Clemons
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.caffe.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Day02.
 */
public class Day02 {
    private static Stream<List<Integer>> combinations(final List<Integer> l, final int size) {
        if (size == 0) {
            return Stream.of(Collections.emptyList());
        }

        final Function<Integer, Stream<List<Integer>>> lambda = i -> combinations(l.subList(i.intValue() + 1, l.size()),
                size - 1).map(t -> pipe(l.get(i.intValue()), t));

        return IntStream.range(0, l.size()).boxed().flatMap(lambda);
    }

    private static List<Integer> pipe(final Integer head, final List<Integer> tail) {
        final List<Integer> newList = new ArrayList<>(tail);
        newList.add(0, head);
        return newList;
    }

    /**
     * @param args command line args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final Stream<String> lines = reader.lines();

            final Pattern regex = Pattern.compile("\\s+");
            final Function<String, int[]> lambda = x -> {
                try (final IntStream digitStream = regex.splitAsStream(x).mapToInt(y -> Integer.parseInt(y))) {
                    final int[] digits = digitStream.toArray();
                    final IntSummaryStatistics stats = IntStream.of(digits).collect(IntSummaryStatistics::new,
                            IntSummaryStatistics::accept, IntSummaryStatistics::combine);
                    final int diff = stats.getMax() - stats.getMin();

                    final List<Integer> list = IntStream.of(digits).boxed().collect(Collectors.toList());
                    final IntSummaryStatistics match = combinations(list, 2).map(combination -> {
                        return combination.stream().mapToInt(Integer::intValue).collect(IntSummaryStatistics::new,
                                IntSummaryStatistics::accept, IntSummaryStatistics::combine);
                        }).filter(pair -> pair.getMax() % pair.getMin() == 0).findFirst().get();
                    final int division = match.getMax() / match.getMin();

                    return new int[] { diff, division };
                }
            };

            final List<int[]> results = lines.map(lambda).collect(Collectors.toList());
            final int checksum = results.stream().mapToInt(arr -> arr[0]).sum();
            final int sum = results.stream().mapToInt(arr -> arr[1]).sum();

            System.out.println(String.format("The checksum for the spreadsheet is %s", Integer.valueOf(checksum)));
            System.out.println(String.format("The sum of each row's result is %s", Integer.valueOf(sum)));
        }
    }
}
