import java.util.*;

public class Driver {
	public static void main(String[] args) {
		Map<String, Double> vars = new HashMap<>();
		// you can change or add more variables here.
		vars.put("x", 10.0);
		vars.put("y", 27.0);
                vars.put("numberOfPeopleWhoLoveMe", 0.0);
                vars.put("JarrettsClassRatingOutOfTen", 10.0);
                vars.put("quantityOfHotPocketsEatenOnAverageDaily", 6.0);
                vars.put("numberOfMemesViewedPerHour", 22.72523452345);
                vars.put("numberOfSelfDeprecatingJokeVariables", 3.0);

		Expression expr = Expression.quickParse("4*x + y/9 + 12");

		System.out.println("toString:        " + expr);
		System.out.println("toPostfix:       " + expr.toPostfix());
		System.out.println("evaluate:        " + expr.evaluate(vars));
		System.out.println("reciprocal:      " + expr.reciprocal());
		System.out.println("reciprocal(num): " + Expression.Number(7).reciprocal());
		System.out.println("reciprocal(div): " + Expression.quickParse("x / 10").reciprocal());
		System.out.println("getVariables:    " + expr.getVariables());

		Expression mean = Expression.geometricMean(new double[]{4, 9, 3, 7, 6});
		System.out.println("geometricMean:   " + mean);
		System.out.println("it evalutes to:  " + mean.evaluate(vars));
                
                System.out.println();
                
		/*Expression expr2 = Expression.quickParse("4*z + y/9 + 12");

		System.out.println("toString:        " + expr2);
		System.out.println("toPostfix:       " + expr2.toPostfix());
		System.out.println("evaluate:        " + expr2.evaluate(vars));
		System.out.println("reciprocal:      " + expr2.reciprocal());
		System.out.println("reciprocal(num): " + Expression.Number(7).reciprocal());
		System.out.println("reciprocal(div): " + Expression.quickParse("z / 10").reciprocal());
		System.out.println("getVariables:    " + expr2.getVariables());

		Expression mean2 = Expression.geometricMean(new double[]{1});
		System.out.println("geometricMean:   " + mean2);
		System.out.println("it evalutes to:  " + mean2.evaluate(vars)); THIS CORRECTLY THROWS AN ERROR*/ 
                
               Expression expr2 = Expression.quickParse("numberOfPeopleWhoLoveMe-numberOfSelfDeprecatingJokeVariables + quantityOfHotPocketsEatenOnAverageDaily^numberOfSelfDeprecatingJokeVariables");

		System.out.println("toString:        " + expr2);
		System.out.println("toPostfix:       " + expr2.toPostfix());
		System.out.println("evaluate:        " + expr2.evaluate(vars));
		System.out.println("reciprocal:      " + expr2.reciprocal());
		System.out.println("reciprocal(num): " + Expression.Number(144).reciprocal());
		System.out.println("reciprocal(div): " + Expression.quickParse("JarrettsClassRatingOutOfTen / 10").reciprocal());
		System.out.println("getVariables:    " + expr2.getVariables());

		Expression mean2 = Expression.geometricMean(new double[]{1, 6, 5.2, 1231, 55});
		System.out.println("geometricMean:   " + mean2);
		System.out.println("it evalutes to:  " + mean2.evaluate(vars));
                
                System.out.println();
                
                Expression expr3 = Expression.quickParse("8888");

		System.out.println("toString:        " + expr3);
		System.out.println("toPostfix:       " + expr3.toPostfix());
		System.out.println("evaluate:        " + expr3.evaluate(vars));
		System.out.println("reciprocal:      " + expr3.reciprocal());
		System.out.println("reciprocal(num): " + Expression.Number(6).reciprocal());
		System.out.println("reciprocal(div): " + Expression.quickParse("456 / 123").reciprocal());
		System.out.println("getVariables:    " + expr3.getVariables());

		Expression mean3 = Expression.geometricMean(new double[]{1});
		System.out.println("geometricMean:   " + mean3);
		System.out.println("it evalutes to:  " + mean3.evaluate(vars));
                
                System.out.println();


		
	}
}