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
import java.util.function.IntUnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Day01.
 */
public class Day01 {
    /**
     * @param args command line args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        final String input;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            input = reader.readLine();
        }

        final int[] digits = Pattern.compile("(?!^)").splitAsStream(input).mapToInt((x) -> Integer.parseInt(x))
                .toArray();
        final int length = digits.length;

        final IntUnaryOperator lambda = x -> digits[x] == digits[x + 1] ? digits[x] : 0;
        final int count = IntStream.range(0, length - 1).map(lambda).sum()
                + (digits[0] == digits[length - 1] ? digits[0] : 0);

        System.out.println(String.format("The solution to my captcha is: %s", Integer.valueOf(count)));

        final int half = length / 2;
        final IntUnaryOperator secondLambda = x -> {
            final int next = half + x;
            return (digits[x] == (next >= length ? digits[next - length] : digits[next])) ? digits[x] : 0;
        };
        final int secondCount = IntStream.range(0, digits.length).map(secondLambda).sum();

        System.out.println(String.format("The solution to my new captcha is: %s", Integer.valueOf(secondCount)));
    }
}
