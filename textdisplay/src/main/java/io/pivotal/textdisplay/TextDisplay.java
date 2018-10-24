package io.pivotal.textdisplay;

import java.text.DecimalFormat;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class TextDisplay implements Function<Long, String>{

	public static void main(String[] args) {
		SpringApplication.run(TextDisplay.class, args);
	}

	private static final String[] tensNames = {
			"",
			" ten",
			" twenty",
			" thirty",
			" forty",
			" fifty",
			" sixty",
			" seventy",
			" eighty",
			" ninety"
	};

	private static final String[] numNames = {
			"",
			" one",
			" two",
			" three",
			" four",
			" five",
			" six",
			" seven",
			" eight",
			" nine",
			" ten",
			" eleven",
			" twelve",
			" thirteen",
			" fourteen",
			" fifteen",
			" sixteen",
			" seventeen",
			" eighteen",
			" nineteen"
	};

	@Override
		public String apply(Long number) {
		// 0 to 999 999 999 999
		if (number == 0) { return "zero"; }

		// pad with "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		String snumber = df.format(number);

		// XXXnnnnnnnnn
		int billions = Integer.parseInt(snumber.substring(0,3));
		// nnnXXXnnnnnn
		int millions  = Integer.parseInt(snumber.substring(3,6));
		// nnnnnnXXXnnn
		int hundredThousands = Integer.parseInt(snumber.substring(6,9));
		// nnnnnnnnnXXX
		int thousands = Integer.parseInt(snumber.substring(9,12));

		String tradBillions;
		switch (billions) {
			case 0:
				tradBillions = "";
				break;
			case 1 :
				tradBillions = convertLessThanOneThousand(billions)
						+ " billion ";
				break;
			default :
				tradBillions = convertLessThanOneThousand(billions)
						+ " billion ";
		}

		String result = tradBillions;

		String tradMillions;
		switch (millions) {
			case 0:
				tradMillions = "";
				break;
			case 1 :
				tradMillions = convertLessThanOneThousand(millions)
						+ " million ";
				break;
			default :
				tradMillions = convertLessThanOneThousand(millions)
						+ " million ";
		}
		result += tradMillions;

		String tradHundredThousands;
		switch (hundredThousands) {
			case 0:
				tradHundredThousands = "";
				break;
			case 1 :
				tradHundredThousands = "one thousand ";
				break;
			default :
				tradHundredThousands = convertLessThanOneThousand(hundredThousands)
						+ " thousand ";
		}
		result += tradHundredThousands;

		String tradThousand;
		tradThousand = convertLessThanOneThousand(thousands);
		result += tradThousand;

		// remove extra spaces!
		result = result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
		System.out.println(result);
		return result;
	}

	private String convertLessThanOneThousand(int number) {
		String soFar;

		if (number % 100 < 20){
			soFar = numNames[number % 100];
			number /= 100;
		}
		else {
			soFar = numNames[number % 10];
			number /= 10;

			soFar = tensNames[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) return soFar;
		return numNames[number] + " hundred" + soFar;
	}
}

