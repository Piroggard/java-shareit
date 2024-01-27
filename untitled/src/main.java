import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {

        Solution s = new Solution();
        s.romanToInt("MCMXCIV");
    }
}


class Solution {
    public int romanToInt(String s) {
        Map<Character, Integer> characterIntegerMap = new HashMap<>();
        characterIntegerMap.put('I', 1);
        characterIntegerMap.put('V', 5);
        characterIntegerMap.put('X', 10);
        characterIntegerMap.put('L', 50);
        characterIntegerMap.put('C', 100);
        characterIntegerMap.put('D', 500);
        characterIntegerMap.put('M', 1000);

        char[] chars = s.toCharArray();
        int sum = 0;
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                if (characterIntegerMap.get(chars[i]) < characterIntegerMap.get(chars[i + 1])) {
                    sum = sum + (characterIntegerMap.get(chars[i + 1]) - characterIntegerMap.get(chars[i]));
                    System.out.println(sum);
                    i = i + 1;
                    continue;
                }
            }
            System.out.println(sum);

            sum = sum + characterIntegerMap.get(chars[i]);
        }

        return sum;
    }
}


//
//        I             1
//        V             5
//        X             10
//        L             50
//        C             100
//        D             500
//        M             1000