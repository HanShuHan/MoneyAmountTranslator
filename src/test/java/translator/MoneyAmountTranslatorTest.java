package translator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class MoneyAmountTranslatorTest {

    @ParameterizedTest
    @CsvSource(value = {
            "Zero : 0",
            "Zero : -0",
            "Zero : 0.001",
            "One Cent : 0.005",
            "Two Cents : 0.015",
            "One Dollar And Two Cents : 1.015",
            "Two Dollars And Two Cents : 2.015",
            "One Dollar : 1.003",
            "Two Dollars : 2.003",
            "Zero : 0",
            "Zero : 0.00",
            "Zero : 0.004",
            "One Cent : 0.005",
            "Two Cents : 0.015",
            "One Dollar And Two Cents : 1.015",
            "Nine Dollars And Two Cents : 9.015",
            "Nine Hundred Dollars And Two Cents : 900.015",
            "One Hundred Dollars And Two Cents : 100.015",
            "Nine Hundred Twenty Dollars And Two Cents : 920.015",
            "One Hundred Ninety Dollars And Two Cents : 190.015",
            "Nine Hundred Twenty-One Dollars And Two Cents : 921.015",
            "One Hundred Ninety-Nine Dollars And Two Cents : 199.015",
            "Nine Hundred Nineteen Dollars And Two Cents : 919.015",
            "One Hundred Ten Dollars And Two Cents : 110.015",
            "Ten Dollars And Two Cents : 10.015",
            "Nineteen Dollars And Two Cents : 19.015",
            "Twenty Dollars And Two Cents : 20.015",
            "Ninety Dollars And Two Cents : 90.015",
            "Twenty-One Dollars And Two Cents : 21.015",
            "Ninety-Nine Dollars And Two Cents : 99.015",
            "One Thousand Ninety-Nine Dollars And Two Cents : 1099.015",
            "Nine Thousand Ninety-Nine Dollars And Two Cents : 9099.015",
            "Ten Thousand Ninety-Nine Dollars And Two Cents : 10099.015",
            "Nineteen Thousand Ninety-Nine Dollars And Two Cents : 19099.015",
            "Twenty Thousand Ninety-Nine Dollars And Two Cents : 20099.015",
            "Ninety Thousand Ninety-Nine Dollars And Two Cents : 90099.015",
            "Twenty-One Thousand Ninety-Nine Dollars And Two Cents : 21099.015",
            "Ninety-Nine Thousand Ninety-Nine Dollars And Two Cents : 99099.015",
            "One Hundred Thousand Ninety-Nine Dollars And Two Cents : 100099.015",
            "Nine Hundred Thousand Ninety-Nine Dollars And Two Cents : 900099.015",
            "One Hundred Ninety-Nine Thousand Ninety-Nine Dollars And Two Cents : 199099.015",
            "Nine Hundred Twenty Thousand Ninety-Nine Dollars And Two Cents : 920099.015",
            "Nine Hundred Twenty-One Thousand Ninety-Nine Dollars And Two Cents : 921099.015",
            "One Hundred Nineteen Thousand Ninety-Nine Dollars And Two Cents : 119099.015",
            "One Hundred Ten Thousand Ninety-Nine Dollars And Two Cents : 110099.015",
            "One Hundred Nine Thousand Ninety-Nine Dollars And Two Cents : 109099.015",
            "One Hundred One Thousand Ninety-Nine Dollars And Two Cents : 101099.015",
            "One Hundred Thousand Ninety-Nine Dollars And Two Cents : 100099.015",
            //
            "One Hundred Thousand Dollars And Two Cents : 100000.015",
            "Nine Hundred Ninety-Nine Million One Hundred Thousand Dollars And Two Cents : 999100000.015",
            "One Million One Hundred Thousand Dollars And Two Cents : 001100000.015",
            "One Hundred Trillion One Million One Hundred Thousand Dollars And Two Cents : 100000001100000.015",
            "One Trillion One Million One Hundred Thousand Dollars And Two Cents : 001000001100000.015",
            "Two Quadrillion One Trillion One Million One Hundred Thousand Dollars And Two Cents : 002001000001100000.015",
            "One Hundred Eleven Quintillion Two Quadrillion One Trillion One Million One Hundred Thousand Dollars And Two Cents : 111002001000001100000.015",
            //
            "Zero : -0.00",
            "Negative One Cent : -0.005",
            "Negative Two Cents : -0.015",
            "Zero : 0.0",
            "One Cent : 0.005",
            "One Hundred Cents : 0.995",
    }, delimiter = ':')
    void toEnglish(String result, String amount) {
        assertEquals(result, MoneyAmountTranslator.toEnglish(new BigDecimal(amount)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "intPart.csv", numLinesToSkip = 1)
    void intPart(BigInteger intPart, String amount) {
        assertEquals(intPart, MoneyAmountTranslator.testIntPart(new BigDecimal(amount)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "fracPart.csv", numLinesToSkip = 1)
    void fracPart(int fracPart, String amount) {
        assertEquals(fracPart, MoneyAmountTranslator.testFracPart(new BigDecimal(amount)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {
            "/translator/from1To9ToEng.csv",
            "/translator/from10To19ToEng.csv",
            "/translator/from20To99ToEng.csv",
            "/translator/threeDigitsToEng.csv"}, numLinesToSkip = 1)
    void threeDigitsToEng(String result, int threeDigits) {
        assertEquals(result, MoneyAmountTranslator.testThreeDigitsToEng(threeDigits));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/translator/hundredsDigit.csv", numLinesToSkip = 1)
    void hundredsToEng(String result, int hundredsDigit) {
        assertEquals(result, MoneyAmountTranslator.testHundredsToEng(hundredsDigit));
    }

    @ParameterizedTest
    @CsvFileSource(resources = {
            "/translator/from1To9ToEng.csv",
            "/translator/from10To19ToEng.csv",
            "/translator/from20To99ToEng.csv"}, numLinesToSkip = 1)
    void tensAndOnesToEng(String result, int tensAndOnes) {
        assertEquals(result, MoneyAmountTranslator.testTensAndOnesToEng(tensAndOnes));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/translator/from20To99ToEng.csv", numLinesToSkip = 1)
    void from20To99ToEng(String result, int tensAndOnes) {
        assertEquals(result, MoneyAmountTranslator.testFrom20To99ToEng(tensAndOnes));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/translator/from10To19ToEng.csv", numLinesToSkip = 1)
    void from10To19ToEng(String result, int num) {
        assertEquals(result, MoneyAmountTranslator.testFrom10To19ToEng(num));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/translator/from1To9ToEng.csv", numLinesToSkip = 1)
    void from1To9ToEng(String result, int num) {
        assertEquals(result, MoneyAmountTranslator.testFrom1To9ToEng(num));
    }

    @Test
    void toChinese() {
    }

}