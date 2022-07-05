package translator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class MoneyAmountTranslator {

    private final static List<String> eng1To9 =
            Arrays.asList("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine");
    private final static List<String> eng10To19 =
            Arrays.asList("Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen");
    private final static List<String> eng20To90 =
            Arrays.asList("Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety");
    private final static List<String> engAmountSymbols =
            Arrays.asList("", "Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintillion", "Sextillion", "Septillion", "Octillion", "Nonillion", "Decillion");

    /**
     * 將輸入的 BigDecimal 金額，轉換成英文
     * @param amount 金額
     * @return 英文結果字串
     */
    public static String toEnglish(BigDecimal amount) {

        // amount 若為 0，則回傳 Zero
        if(amount.signum() == 0)
            return "Zero";

        // 對 amount 取絕對值
        BigDecimal absAmount = amount.abs();
        // absAmount 的整數部分
        BigInteger intPart = intPart(absAmount);
        // absAmount 的小數部分；最小值為 0，最大值為 100
        int fracPart = fracPart(absAmount);
        // amount 值是否為負
        boolean isNegative = amount.signum() < 0;

        // 將 BigDecimal 做四捨五入後為 0，則回傳 Zero
        if(intPart.signum() == 0 && fracPart == 0)
            return "Zero";

        // 被處理的整數片段
        BigInteger intByParts = intPart;
        // 處理整數部分的次數
        int round = 0;
        // 每一組 3位數，用英文表示的 Map 集合
        Map<String, String> resultMap = new HashMap<>();
        // 當還有沒被轉換成英文的整數部分存在
        while(intByParts.signum() > 0) {
            
            // 處理次數 +1
            round++;
            
            // 最右邊的 3位數字
            int threeDigits = intByParts.remainder(new BigInteger("1000"))
                    .intValue();
            // 從 3位數轉換而成的英文
            StringBuilder engBy3Digits = new StringBuilder()
                    .append(threeDigitsToEng(threeDigits));
            // 3位數值對應的 Amount Symbol
            String amountSymbol = engAmountSymbols.get(round - 1);
            // 若 3位數值的對應的英文不為空字串，且含有 Amount Symbol
            if(engBy3Digits.length() != 0 && round > 1) {
                // 將 3位數值的英文，與它的 Amount Symbol，用空白連接
                engBy3Digits.append(" ")
                        .append(amountSymbol);
            }
            // 將結果，加入 resultMap
            resultMap.put(amountSymbol, engBy3Digits.toString());
            
            // 往左，下一組 3位數字
            intByParts = intByParts.divide(new BigInteger("1000"));
        }

        // 將 amount 完整轉換後的英文字串
        StringBuilder result = new StringBuilder();
        // 若 amount 為負數，則加 Negative 於字串最前面
        if(isNegative) {
            result.append("Negative ");
        }

        // 將蒐集好的 resultMap 裡的字串，組合
        // 從最大的位數開始
        for(int i = round - 1; i >= 0; i--) {
            // Amount Symbol
            String amountSymbol = engAmountSymbols.get(i);
            // 該 Amount Symbol 對應的，英文表示的內容
            String content = resultMap.get(amountSymbol);
            // 若內容不為空白字串
            if(!content.isBlank()) {
                result.append(content)
                        // 結尾留空白字串，以相連
                        .append(" ");
            }
        }

        // 若金額的整數部分大於 0，則加 Dollar Sign
        if(intPart.signum() > 0)
            result.append("Dollar");
        // 若金額的整數部分大於 1，則將 Dollar 變為複數：Dollars
        if(intPart.compareTo(BigInteger.ONE) > 0)
            result.append("s");

        // Dollar(s) 部分和 Cent(s)的部分，之間的連字
        if(intPart.signum() > 0 && fracPart > 0)
            result.append(" And ");

        // Cent(s) 的部分
        // 若金額的整數部分，大於 0
        if(fracPart > 0) {
            // 將金額的小數的部分，轉換成英文
            result.append(threeDigitsToEng(fracPart));
            // 加 Cent Sign
            result.append(" Cent");
            // 若金額的分數部分 大於 1，則將 Cent 變為複數：Cents
            if(fracPart > 1)
                result.append("s");
        }

        // 回傳此輸入金額的英文
        return result.toString();
    }

    /**
     * 取 BigDecimal 的整數部分
     * @param amount A BigDecimal number
     * @return A long number
     */
    private static BigInteger intPart(BigDecimal amount) {
        return amount.toBigInteger();
    }

    /**
     * 取 BigDecimal 的小數部分
     * @param amount A BigDecimal number
     * @return An int number
     */
    private static int fracPart(BigDecimal amount) {
        double d = amount.remainder(BigDecimal.ONE)
                .doubleValue() * 100;
        return (int) Math.round(d);
    }

    /**
     * 將 3位數的數值，轉換成英文
     * @param threeDigits 3位數的數值
     * @return 此 3位數的英文
     */
    private static String threeDigitsToEng(int threeDigits) {

        // 從 3位數轉換而成的英文
        StringBuilder eng = new StringBuilder();

        // 100 至 999
        if(threeDigits >= 100) {
            // 百位數的數字
            int hundredsDigit = threeDigits / 100;
            // 取得 百位數的英文
            eng.append(hundredsToEng(hundredsDigit));

            // 十位數和個位數的數值
            int tensAndOnes = threeDigits % 100;
            // 若剩餘的十位數和個位數的數值，不為 0
            if(tensAndOnes != 0) {
                // 用空白連 Hundred 後面的字
                eng.append(" ");
                // 取得 十位數和個位數的英文
                eng.append(tensAndOnesToEng(tensAndOnes));
            }
        }
        // 1 至 99
        else if(threeDigits >= 1) {
            // 十位數和個位數的數值
            int tensAndOnes = threeDigits % 100;
            // 取得 十位數和個位數的英文
            eng.append(tensAndOnesToEng(tensAndOnes));
        }
        // else，若 3位數的數值為 0，則不做動作。最後回傳空字串

        // 回傳英文結果
        return eng.toString();
    }

    /**
     * 百位數的數值，轉換成英文
     * @param hundredsDigit 百位數的數字
     * @return 英文
     */
    private static String hundredsToEng(int hundredsDigit) {
        return new StringBuilder()
                .append(eng1To9.get(hundredsDigit - 1))
                .append(" Hundred")
                .toString();
    }

    /**
     * 將 1 ~ 99 的數字，轉換成英文
     * @param tensAndOnes 1 ~ 99 的數字
     * @return 英文
     */
    private static String tensAndOnesToEng(int tensAndOnes) {

        // 將 20 ~ 99 的數字，轉換成英文
        if(tensAndOnes >= 20) {
            return from20To99ToEng(tensAndOnes);
        }
        // 將 10 ~ 19 的數字，轉換成英文
        else if(tensAndOnes >= 10) {
            return from10To19ToEng(tensAndOnes);
        }
        // 將 1 ~ 9 的數字，轉換成英文
        else if(tensAndOnes >= 1) {
            return from1To9ToEng(tensAndOnes);
        }
        // 若為 0，則回傳空字串
        else {
            return "";
        }
    }

    /**
     * 將 20 至 99 的數字，轉換成英文
     * @param tensAndOnes 20 至 99 的數字
     * @return 英文
     */
    private static String from20To99ToEng(int tensAndOnes) {

        // 十位數的數字
        int tens = tensAndOnes / 10;
        // 個位數的數字
        int ones = tensAndOnes % 10;

        // 十位數英文
        StringBuilder sb = new StringBuilder()
                .append(eng20To90.get(tens - 2));
        //
        if(ones == 0)
            return sb.toString();
        // 連字
        return sb.append('-')
                .append(eng1To9.get(ones - 1))
                .toString();
    }

    /**
     * 將 10 至 19 的數字，轉換成英文
     * @param num 10 至 19 的數字
     * @return 英文
     */
    private static String from10To19ToEng(int num) {
        return eng10To19.get(num - 10);
    }

    /**
     * 將 1 至 9 的數字，轉換成英文
     * @param num 1 至 9 的數字
     * @return 英文
     */
    private static String from1To9ToEng(int num) {
        return eng1To9.get(num - 1);
    }

    /**
     * 測試 private method - intPart，所使用的 public method
     * @param amount A BigDecimal number
     * @return A long number
     */
    public static BigInteger testIntPart(BigDecimal amount) {
        return intPart(amount);
    }

    /**
     * 測試 private method - fracPart，所使用的 public method
     * @param amount A BigDecimal number
     * @return An int number
     */
    public static int testFracPart(BigDecimal amount) {
        return fracPart(amount);
    }

    /**
     * 測試 private method - threeDigitsToEng，所使用的 public method
     * @param threeDigits 3位數的數值
     * @return 此 3位數的英文
     */
    public static String testThreeDigitsToEng(int threeDigits) {
        return threeDigitsToEng(threeDigits);
    }

    /**
     * 測試 private method - hundredsToEng，所使用的 public method
     * @param hundredsDigit 百位數的數字
     * @return 英文
     */
    public static String testHundredsToEng(int hundredsDigit) {
        return hundredsToEng(hundredsDigit);
    }

    /**
     * 測試 private method - tensAndOnesToEng，所使用的 public method
     * @param tensAndOnes 1 ~ 99 的數字
     * @return 英文
     */
    public static String testTensAndOnesToEng(int tensAndOnes) {
        return tensAndOnesToEng(tensAndOnes);
    }

    /**
     * 測試 private method - from20To99ToEng，所使用的 public method
     * @param tensAndOnes 20 至 99 的數字
     * @return 英文
     */
    public static String testFrom20To99ToEng(int tensAndOnes) {
        return from20To99ToEng(tensAndOnes);
    }

    /**
     * 測試 private method - from10To19ToEng，所使用的 public method
     * @param num 10 至 19 的數字
     * @return 英文
     */
    public static String testFrom10To19ToEng(int num) {
        return from10To19ToEng(num);
    }

    /**
     * 測試 private method - from1To9ToEng，所使用的 public method
     * @param num 1 至 9 的數字
     * @return 英文
     */
    public static String testFrom1To9ToEng(int num) {
        return from1To9ToEng(num);
    }

    public String toChinese(BigDecimal amount) {
        return "";
    }

}
